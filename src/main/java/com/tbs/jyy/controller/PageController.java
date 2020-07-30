package com.tbs.jyy.controller;

import com.tbs.jyy.service.LogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tbs-jyy
 * @description 页面
 * @date 2020/7/30
 */
@Controller
public class PageController {
    @Autowired
    private LogServiceI logService;

    @GetMapping("/index")
    public String indexPage(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        return "index";
    }
}
