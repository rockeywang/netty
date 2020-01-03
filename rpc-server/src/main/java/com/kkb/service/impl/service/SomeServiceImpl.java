package com.kkb.service.impl.service;

import com.kkb.service.SomeService;

/**
 * @author wangchao
 */
public class SomeServiceImpl implements SomeService {
    @Override
    public String hello(String name) {
        return name + "欢迎你";
    }
}
