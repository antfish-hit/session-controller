package com.sony.httpclient;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * httpClient连接工具
 *
 * @author wyj
 * @date 4/27/2018
 */
public class HttpClientTool {
    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(HttpClientTool.class);

    private static CloseableHttpClient httpclient = null;

    // ms毫秒,从池中获取链接超时时间
    static final int connectionRequestTimeout = 5000;
    // ms毫秒,建立链接超时时间
    static final int connectTimeout = 5000;
    // ms毫秒,读取超时时间
    static final int socketTimeout = 30000;

    // 最大总并发
    static final int maxTotal = 500;
    // 每路并发
    static final int maxPerRoute = 100;

    /**
     * 获取httpClient连接
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        if (null == httpclient) {
            synchronized (HttpClientTool.class) {
                if (null == httpclient) {
                    httpclient = init();
                }
            }
        }
        return httpclient;
    }

    /**
     * 链接池初始化
     *
     * @return
     */
    private static CloseableHttpClient init() {
        CloseableHttpClient newHttpclient = null;

        // 设置连接池
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf).register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // 如果已经重试了2次，就放弃
                if (executionCount >= 2) {
                    logger.error("connect date: " + new Date());
                    logger.error("try connect for 2 times but fail.");
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    logger.error("connect date: " + new Date());
                    logger.error("there is no response from server");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    logger.error("connect date: " + new Date());
                    logger.error("SSL connection error");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    logger.error("connect date: " + new Date());
                    logger.error("connection timeout");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    logger.error("connect date: " + new Date());
                    logger.error("unknown host");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    logger.error("connect date: " + new Date());
                    logger.error("connection is rejected");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return false;
                }
                // SSL握手异常
                if (exception instanceof SSLException) {
                    logger.error("connect date: " + new Date());
                    logger.error("SSL connection error");
                    logger.error(exception.toString() + " : " + exception.getCause().getMessage());
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
        newHttpclient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setRetryHandler(httpRequestRetryHandler).build();
        return newHttpclient;
    }
}
