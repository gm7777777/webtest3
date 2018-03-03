package com.gm.webtest.soap.ws.impl;

import com.gm.webtest.entity.TestInfo;
import com.gm.webtest.soap.ws.rest.TestService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RestTestImpl implements TestService{
    @Override
    public List<TestInfo> retrieveAllTestInfo() {
        List<TestInfo> list = new ArrayList<TestInfo>();
        list.add(new TestInfo("1","1"));
        list.add(new TestInfo("2","2"));
        list.add(new TestInfo("3","3"));

        return list;
    }

    @Override
    public TestInfo getTestInfoByID(long id) {
        return null;
    }

    @Override
    public List<TestInfo> getTestsByName(String name) {
        return null;
    }

    @Override
    public List<TestInfo> createTest(TestInfo test) {
        return null;
    }

    @Override
    public TestInfo updateTest(long id, Map<String, Object> map) {
        return null;
    }

    @Override
    public TestInfo deleteTest(long id) {
        return null;
    }
}
