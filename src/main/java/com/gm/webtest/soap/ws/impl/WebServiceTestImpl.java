package com.gm.webtest.soap.ws.impl;

import com.gm.webtest.soap.ws.WebServiceTest;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

@WebService(
serviceName = "WebServiceTest",
        portName = "WebServiceTestPort",
        endpointInterface = "com.gm.webtest.soap.ws.WebServiceTest"
)
@Component
//http://localhost:8080/WebTest3/ws/soap/hello
public class WebServiceTestImpl implements WebServiceTest{
    @Override
    public String sendTest(String name) {
        return "hello"+name;
    }
}
