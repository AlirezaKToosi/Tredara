package com.novare.tredara.repositories;

import com.novare.tredara.models.Item;
import com.novare.tredara.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
