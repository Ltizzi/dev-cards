package com.ltizzi.dev_cards;

import com.ltizzi.dev_cards.security.utils.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class DevCardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevCardsApplication.class, args);
	}

}
