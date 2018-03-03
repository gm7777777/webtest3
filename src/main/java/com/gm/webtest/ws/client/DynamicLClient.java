package com.gm.webtest.ws.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;

public class DynamicLClient {

    public static void main(String[] args) {
        DynamicClientFactory factory = DynamicClientFactory.newInstance();

        Client client = factory.createClient("http://localhost:8080/ws/soap/hello?wsdl");
        Object[] results = new Object[0];
        try {
            results = client.invoke("sendTest","world");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(results[0]);
    }
}
