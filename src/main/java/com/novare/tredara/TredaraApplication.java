package com.novare.tredara;

import com.novare.tredara.models.User;
import com.novare.tredara.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
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
           // userRepo.save(user1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
