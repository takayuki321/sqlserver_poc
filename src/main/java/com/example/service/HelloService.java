package com.example.service;

import com.example.mapper.HelloMapper;
import com.example.model.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Autowired
    private HelloMapper helloMapper;

    public Hello getHello(int id) {
        return helloMapper.selectById(id);
    }
}