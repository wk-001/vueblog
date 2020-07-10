package com.wk.utils;

import com.wk.entity.User;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

    public static User getProfile() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
