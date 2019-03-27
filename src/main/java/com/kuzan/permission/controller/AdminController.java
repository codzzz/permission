package com.kuzan.permission.controller;

import com.kuzan.permission.commons.RequestHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sui on 2019/3/22.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index")
    public String index(ModelMap map){
        map.put("user", RequestHolder.getCurrentUser());
        return "admin";
    }
}

