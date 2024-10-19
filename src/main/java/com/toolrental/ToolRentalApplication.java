package com.toolrental;

import com.toolrental.domain.entity.RentalAgreement;
import com.toolrental.domain.entity.ToolType;
import com.toolrental.domain.entity.Tool;
import com.toolrental.domain.repository.InMemoryRepository;
import com.toolrental.domain.repository.Repository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class ToolRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolRentalApplication.class, args);
	}

	// TODO in real system these Repository beans would be backed by the persistence store via spring-data
	@Bean
	public Repository<Tool, String> toolRepository(){
        return new InMemoryRepository<>();
	}

	@Bean
	public Repository<ToolType, String> toolTypeRepository(){
        return new InMemoryRepository<>();
	}

	@Bean
	public Repository<RentalAgreement, UUID> rentalAgreementRepository(){
		return new InMemoryRepository<>();
	}
}
