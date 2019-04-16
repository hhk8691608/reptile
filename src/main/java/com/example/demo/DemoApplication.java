package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.utils.SpringTool;

@SpringBootApplication
public class DemoApplication {
	
	public static Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		LOG.info("服务启动中......");
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		SpringTool.setApplicationContext(applicationContext);
	}

}
