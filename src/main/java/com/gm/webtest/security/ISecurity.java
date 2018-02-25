package com.gm.webtest.security;

import java.util.Set;

public interface ISecurity {

    String getPassword(String username);

    Set<String> getRoleNameSet(String username);

    Set<String> getPermissionNameSet(String roleName);

}
