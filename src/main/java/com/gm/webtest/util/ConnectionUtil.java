package com.gm.webtest.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class ConnectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtil.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;


    private static final QueryRunner QUERY_RUNNER;
    //增加链接池
    private static final BasicDataSource DATA_POOL;

    static {

        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        DATA_POOL = new BasicDataSource();
        DATA_POOL.setDriverClassName(DRIVER);
        DATA_POOL.setUrl(URL);
        DATA_POOL.setUsername(USERNAME);
        DATA_POOL.setPassword(PASSWORD);


//        try{
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("cant not load jdbc driver ",e);
//        }
    }



    /**
     * 得到数据库链接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
//                conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                conn = DATA_POOL.getConnection();
            } catch (SQLException e) {
                LOGGER.error("open Connection failure", e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }


    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close Connection failure", e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static void beginTranscation(){
        Connection conn = getConnection();
        if(conn!=null){
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("begin transcation failure",e);
                throw new RuntimeException(e);
            }finally{
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    public static void commitTranscation(){
        Connection conn = getConnection();
        if(conn!=null){

            try {
                conn.commit();
            } catch (SQLException e) {
                LOGGER.error(" commit transcation failure",e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void commitTranscationWithClose(){
        Connection conn = getConnection();
        if(conn!=null){

            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error(" commit close transcation failure",e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void rollbackTranscation(){
        Connection conn = getConnection();
        if(conn!=null){

            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("rollback transcation failure",e);
                throw new RuntimeException(e);
            }

        }
    }
}
