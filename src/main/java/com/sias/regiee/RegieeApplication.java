package com.sias.regiee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Edgar
 * @create 2022-05-27 14:46
 * @faction:
 */
@SpringBootApplication
@ServletComponentScan("com.sias.regiee.filter")
@EnableTransactionManagement
public class RegieeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegieeApplication.class,args);
    }
}
