package com.gm.webtest.soap.ws.rest;

import com.gm.webtest.entity.TestInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

public interface TestService {

    @GET
    @Path("/tests")
    @Produces(MediaType.APPLICATION_JSON)
    List<TestInfo> retrieveAllTestInfo();

    @GET
    @Path("/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestInfo getTestInfoByID(@PathParam("id") long id);

    @POST
    @Path("/tests")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    List<TestInfo> getTestsByName(@FormParam("name") String name);

    @POST
    @Path("/test")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    List<TestInfo> createTest(TestInfo test);

    @PUT
    @Path("/test/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    TestInfo updateTest(@PathParam("id") long id , Map<String,Object> map);

    @DELETE
    @Path("/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    TestInfo deleteTest(@PathParam("id") long id );


}
