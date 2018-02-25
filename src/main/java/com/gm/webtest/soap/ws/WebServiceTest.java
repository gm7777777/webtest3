package com.gm.webtest.soap.ws;

import javax.jws.WebService;

@WebService
public interface WebServiceTest {

    String sendTest(String name);
}
