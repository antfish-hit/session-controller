package com.sony;

import com.sony.session.ActionType;
import com.sony.session.DeliverySessionCreationType;
import com.sony.threadPool.SessionThreadPool;
import com.sony.xml.BeanToXml;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class Test {
    public static void main(String[] args)throws JAXBException, IOException{
//        String xml = BeanToXml.convertToXml(
//                new DeliverySessionCreationType("test-version",1L, ActionType.STOP,0,0), DeliverySessionCreationType.class);
//        try(BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\Download\\test\\test1.xml"));){
//            writer.write(xml);
//            writer.flush();
//        }catch (IOException e){
//
//        }
        List<DeliverySessionCreationType> list = new ArrayList<>(100);
        for(int i = 0; i < 10; i++){
            list.add(new DeliverySessionCreationType(("test-version" + i), 1L, i % 2 == 0 ? ActionType.START : ActionType.STOP,0,i*100));
        }
//        List<DeliverySessionCreationType> list = new ArrayList(){{
//            add(new DeliverySessionCreationType("test-version1",1L, ActionType.START,0,1000));
//            add(new DeliverySessionCreationType("test-version2",1L, ActionType.STOP,0,3000));
//            add(new DeliverySessionCreationType("test-version3",1L, ActionType.START,0,2000));
//            add(new DeliverySessionCreationType("test-version4",1L, ActionType.START,0,800));
//            add(new DeliverySessionCreationType("test-version5",1L, ActionType.STOP,0,1500));
////            add(new DeliverySessionCreationType("test-version6",1L, ActionType.START,0,1000));
////            add(new DeliverySessionCreationType("test-version7",1L, ActionType.STOP,0,3000));
////            add(new DeliverySessionCreationType("test-version8",1L, ActionType.START,0,2000));
////            add(new DeliverySessionCreationType("test-version9",1L, ActionType.STOP,0,800));
////            add(new DeliverySessionCreationType("test-version10",1L, ActionType.START,0,1500));
//        }};

        SendSessionUtil.sendSession(list, DeliverySessionCreationType.class,"http://localhost:8080/test");
//        SendSessionUtil.sendSession(new DeliverySessionCreationType("test-version",1L, ActionType.STOP,0,0), DeliverySessionCreationType.class,"http://localhost:8081/notices/test");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");

//        ThreadPoolExecutor executor = SessionThreadPool.instance.getSendStartSessionThreadPool(list.size());
    }
}
