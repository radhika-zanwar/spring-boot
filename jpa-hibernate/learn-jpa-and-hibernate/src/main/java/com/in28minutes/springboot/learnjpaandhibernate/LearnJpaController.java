package com.in28minutes.springboot.learnjpaandhibernate;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LearnJpaController
{
    @GetMapping("/radhika")
    public String test ()
    {
        return "Radhika";
    }
}
