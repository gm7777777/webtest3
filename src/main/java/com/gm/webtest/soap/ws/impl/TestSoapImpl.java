package com.gm.webtest.soap.ws.impl;

import com.gm.webtest.annotation.Inject;
import com.gm.webtest.annotation.Service;
import com.gm.webtest.annotation.Soap;
import com.gm.webtest.model.Customer;
import com.gm.webtest.service.CustomerSerivce;
import com.gm.webtest.soap.ws.TestSoapService;

@Soap
@Service
public class TestSoapImpl implements TestSoapService{

    @Inject
    CustomerSerivce cusServ;
    @Override
    public Customer getCustomer(String id) {

        return cusServ.getCustomerById(id);
    }
}
