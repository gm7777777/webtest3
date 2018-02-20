package com.gm.webtest.service;

import com.gm.webtest.annotation.Service;
import com.gm.webtest.annotation.Transcation;
import com.gm.webtest.helper.DBHelper;
import com.gm.webtest.model.Customer;
import com.gm.webtest.util.ConnectionUtil;
import com.gm.webtest.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class CustomerSerivce {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerSerivce.class);


    @Transcation
    public List<Customer> getCustomers(){
        Connection conn = null;
        try{
            List<Customer> customerList = new ArrayList<Customer>();
            String sql = "SELECT * FROM customer";
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContract(rs.getString("contract"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setDes(rs.getString("desc"));
                customerList.add(customer);
            }
            return customerList;
        } catch (SQLException e) {
            LOGGER.error(" exception sql in search ",e);
        }finally{
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(" exception in close conncection ",e);
                }
            }
        }
        return null;
    }

    @Transcation
    public List<Customer> getCustomers1(){
        String sql = "SELECT * FROM customer";
        return DBHelper.queryEntityList(Customer.class,sql,null);
    }


    @Transcation
    public boolean addCustomer(Map<String,Object> fieldMap){
        return DBHelper.insertEntity(Customer.class,fieldMap);
    }
}
