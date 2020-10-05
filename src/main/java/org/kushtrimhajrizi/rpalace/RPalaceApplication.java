package org.kushtrimhajrizi.rpalace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RPalaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RPalaceApplication.class, args);
	}

}
