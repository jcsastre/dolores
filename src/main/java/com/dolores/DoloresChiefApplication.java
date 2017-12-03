package com.dolores;

import com.dolores.domain.User;
import com.dolores.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DoloresChiefApplication {

	public static void main(String[] args) {

		final ConfigurableApplicationContext ctx = SpringApplication.run(DoloresChiefApplication.class, args);


        final UserRepository userRepository = (UserRepository) ctx.getBean("userRepository");
        User user = new User();
        user.setId(UUID.randomUUID());
        userRepository.saveAndFlush(user);
	}
}
