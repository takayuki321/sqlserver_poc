package com.example.controller;

import com.example.model.Hello;
import com.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public Hello getHello(@PathVariable("id") int id) {
        return helloService.getHello(id);
    }
}
