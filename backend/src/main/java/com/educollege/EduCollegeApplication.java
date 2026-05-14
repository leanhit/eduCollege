package com.educollege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EduCollegeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduCollegeApplication.class, args);
    }
}
