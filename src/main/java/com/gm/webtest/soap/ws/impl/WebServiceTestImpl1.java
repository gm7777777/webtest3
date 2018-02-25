package com.gm.webtest.soap.ws.impl;

import com.gm.webtest.soap.ws.WebServiceTest;
import com.gm.webtest.soap.ws.WebServiceTest1;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService
@Component
//http://localhost:8080/WebTest3/ws/soap/hello
public class WebServiceTestImpl1 implements WebServiceTest1{
    @Override
    public String sendTest(String name) {
        return "hello"+name;
    }
}
