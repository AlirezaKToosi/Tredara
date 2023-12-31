package com.novare.tredara;

import com.novare.tredara.models.User;
import com.novare.tredara.repositories.UserRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Tradera By Novare",
                version = "1.0",
                description = "Tradera Clone application Backend Preject"
        )
)
public class TredaraApplication implements CommandLineRunner{

    @Autowired
    private UserRepo userRepo;
    public static void main(String[] args) {
        SpringApplication.run(TredaraApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



    @Override
    public void run(String... args) throws Exception {

        try {
            User user1 = new User();
            user1.setEmail("abc@gmail.com");
            user1.setFullName("ABC DER");
            user1.setPassword("abc");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
