package com.changgou.search.service;

import org.springframework.stereotype.Service;

import java.util.Map;


public interface SearchService {
    Map search(Map<String, String> searchMap);

    /**
     * 导入数据
     */
    void importDate();

}
