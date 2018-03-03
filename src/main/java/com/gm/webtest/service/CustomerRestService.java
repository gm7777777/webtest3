package com.gm.webtest.service;

import com.gm.webtest.annotation.Inject;
import com.gm.webtest.annotation.Rest;
import com.gm.webtest.annotation.Service;
import com.gm.webtest.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Rest
@Service
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerRestService {

    @Inject
    private CustomerSerivce cusServ;

    @GET
    @Path("/customer/{id}")
    public Customer getCustomer(@PathParam("id") String customerId){
        return cusServ.getCustomerById(customerId);
    }
}
