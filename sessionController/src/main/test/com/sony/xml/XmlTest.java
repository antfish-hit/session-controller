package com.sony.xml;

import com.sony.session.ActionType;
import com.sony.session.DeliverySessionCreationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author wyj
 * @date 4/28/2018
 */
public class XmlTest {

    @Test
    public void convertToXml()throws Exception{
        DeliverySessionCreationType sessionCreationType = new DeliverySessionCreationType(
                "test-version", 1L, ActionType.START, 0, 0
        );
        String xml = BeanToXml.convertToXml(sessionCreationType, DeliverySessionCreationType.class);
        System.out.println(xml);
        Assert.assertNotNull(xml);
    }

    @Test
    public void convertToBean()throws Exception{
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<DeliverySession Version=\"test-version\">\n" +
                "    <DeliverySessionId>1</DeliverySessionId>\n" +
                "    <Action>Start</Action>\n" +
                "    <StartTime>0</StartTime>\n" +
                "    <StopTime>0</StopTime>\n" +
                "</DeliverySession>";
        Object sessionCreationType = BeanToXml.convertToObject(xml, DeliverySessionCreationType.class);
        Assert.assertNotNull(sessionCreationType);
        Assert.assertTrue(sessionCreationType instanceof DeliverySessionCreationType);
    }
}
