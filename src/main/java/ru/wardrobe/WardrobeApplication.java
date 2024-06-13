package ru.wardrobe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.wardrobe.model.Account;
import ru.wardrobe.service.AccountService;

@SpringBootApplication
public class WardrobeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WardrobeApplication.class, args);
	}

}
