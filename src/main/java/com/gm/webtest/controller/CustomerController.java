package com.gm.webtest.controller;

import com.gm.webtest.annotation.Action;
import com.gm.webtest.annotation.Controller;
import com.gm.webtest.annotation.Inject;
import com.gm.webtest.common.bean.Data;
import com.gm.webtest.common.bean.FileParam;
import com.gm.webtest.common.bean.RequestParam;
import com.gm.webtest.common.bean.View;
import com.gm.webtest.model.Customer;
import com.gm.webtest.service.CustomerSerivce;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Inject
    CustomerSerivce cusServ;

    @Action("get:/customer")
//    public View getIndex(RequestParam param){ 优化后不需要填入参数
    public View getIndex(){
        List<Customer> customerList = cusServ.getCustomers();
        return new View("customer.jsp").addModel("customerList",customerList);
    }

    @Action("post:/customer_create")
    public Data createSubmit(RequestParam param){
        Map<String,Object> fieldMap = param.getFieldMap();
        FileParam fileParam = param.getFile("photo");
        boolean result = cusServ.createCustomer(fieldMap,fileParam);
        return new Data(result);
    }
}
