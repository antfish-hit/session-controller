package com.sony.spring.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * bean转换为xml字符串工具类
 *
 * @author wyj
 * @date 4/27/2018
 */
public class BeanToXml {
    /**
     * 将带有注解的bean转换为xml字符串
     *
     * @param object
     * @param className
     * @return
     * @throws JAXBException
     */
    public static String convertToXml(Object object, Class<?> className)throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(className);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public static Object convertToObject(String xml, Class<?> className)throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(className);
        Unmarshaller marshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return marshaller.unmarshal(reader);
    }
}
