package com.gm.webtest.proxy.aop;

import com.gm.webtest.annotation.Transcation;
import com.gm.webtest.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy{

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAGS = new ThreadLocal<Boolean>(){

        @Override
        protected Boolean initialValue(){
            return false;
        }
    };



    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAGS.get();
        Method method = proxyChain.getTargetMethod();

        if(!flag&&method.isAnnotationPresent(Transcation.class)){
            FLAGS.set(true);
            try {
                ConnectionUtil.beginTranscation();
                LOGGER.info(" begin proxy transcation");
                result = proxyChain.doProxyChain();
                ConnectionUtil.commitTranscation();
                LOGGER.info("commit proxy transcation");
            } catch (Exception e) {
                ConnectionUtil.rollbackTranscation();
                LOGGER.debug("rollback transcation");
                throw e;
            }finally{
                FLAGS.remove();
            }
        }else{
            result = proxyChain.doProxyChain();
        }

        return result;
    }
}
