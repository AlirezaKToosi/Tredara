package com.novare.tredara.repositories;

import com.novare.tredara.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
