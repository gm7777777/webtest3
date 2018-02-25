package com.gm.webtest.security.impl;

import com.gm.webtest.security.ISecurity;

import java.util.Set;

public class WebSecurity implements ISecurity{
    @Override
    public String getPassword(String username) {
        String sql = "SELECT password FROm user Where username= ?";
        return null;
    }

    @Override
    public Set<String> getRoleNameSet(String username) {
        return null;
    }

    @Override
    public Set<String> getPermissionNameSet(String roleName) {
        return null;
    }
}
