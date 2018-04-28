package com.sony.spring.controller;

import com.sony.spring.session.DeliverySessionCreationType;
import com.sony.spring.xml.BeanToXml;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/nbi")
public class TestController {
    @RequestMapping(value = "/deliverysession", method = RequestMethod.POST)
    public ResponseEntity<String> test(Long id, HttpServletRequest request)throws IOException, JAXBException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String xml = reader.lines().collect(Collectors.joining());
        DeliverySessionCreationType session = (DeliverySessionCreationType)BeanToXml.convertToObject(xml, DeliverySessionCreationType.class);
        System.out.println("session id: " + id);
        System.out.println(session);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
