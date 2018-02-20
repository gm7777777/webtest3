package com.gm.webtest.proxy.aop;

public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
