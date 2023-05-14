package com.zhou.gulimall.search.controller;


import com.zhou.gulimall.search.service.MallSearchService;
import com.zhou.gulimall.search.vo.SearchParam;
import com.zhou.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;

    /**
     * springmvc自动将页面提交过来的所有请求参数
     * @param param
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model){
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
