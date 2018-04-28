package com.sony;

import com.sony.httpclient.HttpClientTool;
import com.sony.session.ActionType;
import com.sony.session.DeliverySessionCreationType;
import com.sony.threadPool.SessionThreadPool;
import com.sony.xml.BeanToXml;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 发送session工具类
 *
 * @author wyj
 * @date 4/27/2018
 */
public class SendSessionUtil {

    private static final Logger logger = LoggerFactory.getLogger(SendSessionUtil.class);

    /**
     * 定时任务线程池
     */
    private static final ScheduledExecutorService timer = SessionThreadPool.instance.getSendStopSessionThreadPool();

    /**
     * 普通任务线程池
     */
    private static final ThreadPoolExecutor sessionPool = SessionThreadPool.instance.getSendStartSessionThreadPool();

    private SendSessionUtil(){}

    /**
     * 批量发送session bean转xml字符串至服务器
     *
     * @param sessions
     * @param clazz
     * @param url
     */
    public static void sendSession(List<DeliverySessionCreationType> sessions, Class<?> clazz, String url){
        sessions.stream()
                .forEach(session -> {
                    sessionPool.execute(() -> {
                        try{
                            divideSession(session, clazz, url);
                        }catch (JAXBException e){
                            logger.error("date: " + new Date());
                            logger.error("transfer bean to xml error.");
                            logger.error(e.toString() + " : " + e.getCause().getMessage());
                        }catch (IOException e){
                            logger.error("date: " + new Date());
                            logger.error("send data error.");
                            logger.error(e.toString() + " : " + e.getCause().getMessage());
                        }
                    });
                }
        );
    }

    /**
     * 根据action type将session分开执行
     *
     * @param session
     * @param clazz
     * @param url
     * @throws JAXBException
     * @throws IOException
     */
    private static void divideSession(DeliverySessionCreationType session, Class<?> clazz, String url)throws JAXBException, IOException {
        switch (session.getAction()){
            case START:
                sendSession(session,clazz, url);
                //发送完毕后将action type转为stop
                session.setAction(ActionType.STOP);
                //并加入定时任务
                doSchedule(session,clazz, url);
                break;
            case STOP:
                doSchedule(session,clazz, url);
                break;
            default:
                logger.error("date: " + new Date());
                logger.error("action type is illegal");
                break;
        }
    }

    /**
     * 发送action type为stop的定时任务
     *
     * @param session
     * @param clazz
     * @param url
     */
    private static void doSchedule(DeliverySessionCreationType session, Class<?> clazz, String url){
        timer.schedule(() -> {
            try {
                sendSession(session,clazz, url);
            } catch (JAXBException e) {
                logger.error("date: " + new Date());
                logger.error("transfer bean to xml error.");
                logger.error(e.toString() + " : " + e.getCause().getMessage());
            } catch (IOException e) {
                logger.error("date: " + new Date());
                logger.error("send data error.");
                logger.error(e.toString() + " : " + e.getCause().getMessage());
            }
        },(session.getStopTime() - session.getStartTime()), TimeUnit.MILLISECONDS);
    }

    /**
     * 将session bean转为xml字符串并发送至服务器
     *
     * @param session
     * @param clazz
     * @param url
     * @throws JAXBException
     * @throws IOException
     */
    private static void sendSession(DeliverySessionCreationType session, Class<?> clazz, String url)throws JAXBException, IOException{
        String data = BeanToXml.convertToXml(session, clazz);
        httpClientSend(data, url, "text/xml;charset=utf-8");
    }

    /**
     * 使用httpClient发送数据值服务
     *
     * @param data
     * @param url
     * @param contentType
     * @throws IOException
     */
    private static synchronized void httpClientSend(String data, String url, String contentType)throws IOException{
        //获得httpclient
        HttpClient client = HttpClientTool.getHttpClient();
        //创建post请求
        HttpPost post = new HttpPost(url);
        //设置发送的消息体类型
        post.setHeader("content-type",contentType);
        //将需要发送的数据加入消息体
        StringEntity entity = new StringEntity(data, Charset.forName("UTF-8"));
        post.setEntity(entity);

        Map<String, String> map = new HashMap<>(5);
        //将信息发送情况通过日志打印到控制台
        map.put("send date",new Date().toString());
        map.put("url", url);
        map.put("body",data);
        recordLog(map);
        map.clear();

        HttpResponse response = client.execute(post);

        //将服务器返回情况通过日志打印到控制台
        map.put("receive date",new Date().toString());
        map.put("code", String.valueOf(response.getStatusLine().getStatusCode()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String responseEntity = reader.lines().collect(Collectors.joining());
        map.put("responseEntity",responseEntity);

        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            map.put("result","success");
        }else{
            map.put("result","fail");
        }
        recordLog(map);
        map.clear();
        map.put("===","==============================================================================");
        recordLog(map);
    }

    /**
     * 打印日志
     *
     * @param map
     */
    private static void recordLog(Map<String, String> map){
        map.entrySet()
                .stream()
                .forEach( entry -> logger.info(entry.getKey() + ":" + entry.getValue()));
    }
}
