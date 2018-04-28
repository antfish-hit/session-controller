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
        List<DeliverySessionCreationType> list = new ArrayList<>(100);
        for(int i = 0; i < 10; i++){
            list.add(new DeliverySessionCreationType(("test-version" + i), 1L, i % 2 == 0 ? ActionType.START : ActionType.STOP,0,i*100));
        }

        SendSessionUtil.sendSession(list, DeliverySessionCreationType.class,"http://localhost:8080/test");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");
        System.out.println("=============================end====================================");

    }
}
