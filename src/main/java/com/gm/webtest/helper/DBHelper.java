package com.gm.webtest.helper;

import com.gm.webtest.util.ConnectionUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class DBHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBHelper.class);

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        Connection conn = ConnectionUtil.getConnection();
        LOGGER.info("start query data");
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query list failure", e);
        }
//        finally{
//            closeConnection(conn);
//        }
        return entityList;
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;
        Connection conn = ConnectionUtil.getConnection();
        try {
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("exeucte map query failure", e);
        }
        return null;
    }

    public static int exeucteUpdate(String sql, Object... params) {
        int rows = 0;
        Connection conn = ConnectionUtil.getConnection();
        try {

            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure", e);
        }
//        finally{
//            closeConnection(conn);
//        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (fieldMap == null || fieldMap.isEmpty()) {
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + entityClass.getSimpleName();
        StringBuilder colums = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String fieldNames : fieldMap.keySet()) {
            colums.append(fieldNames).append(",");
            values.append("?,");
        }
        colums.replace(colums.lastIndexOf(","), colums.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += colums + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return exeucteUpdate(sql, params) == 1;
    }
}
