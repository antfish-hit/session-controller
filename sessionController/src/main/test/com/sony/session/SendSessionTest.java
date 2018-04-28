package com.sony.session;

import com.sony.SendSessionUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wyj
 * @date 4/28/2018
 */
public class SendSessionTest {
    private List<DeliverySessionCreationType> sessions = null;
    private static final int SIZE = 10;
    private String url = null;
    private static final Logger logger = LoggerFactory.getLogger(SendSessionTest.class);

    @Before
    public void init(){
        url = "http://localhost:8080/nbi/deliverysession";

        sessions = new ArrayList<>(SIZE);
        for(int i = 0; i < SIZE; i++){
            sessions.add(new DeliverySessionCreationType(
                    ("test-version" + i),
                    (long)Math.random() * 1000000,
                    (i % 2 == 0 ? ActionType.START : ActionType.STOP),
                    0,
                    (long)Math.random() * 10000
                    ));
        }
    }

    @Test
    public void sendSessionTest()throws Exception{
        SendSessionUtil.sendSession(sessions, DeliverySessionCreationType.class, url);
    }
}
