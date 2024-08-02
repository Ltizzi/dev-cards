package com.ltizzi.dev_cards;

import com.ltizzi.dev_cards.security.utils.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@ComponentScan(basePackages = {"com.ltizzi.dev_cards"})
public class DevCardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevCardsApplication.class, args);
	}

}
