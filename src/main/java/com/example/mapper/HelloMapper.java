package com.example.mapper;

import com.example.model.Hello;

public interface HelloMapper {
    Hello selectById(int id);
}