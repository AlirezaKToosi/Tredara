package com.novare.tredara.services;

import com.novare.tredara.models.EActionType;
import com.novare.tredara.models.Item;
import com.novare.tredara.models.Log;
import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.LogRepository;
import com.novare.tredara.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRepo itemRepo;

    public void saveLog(Log log) {
        logRepository.save(log);
    }

    public List<Log> getLogHistory() {
        return logRepository.findAll();
    }

    public void logSuccessfulLogin(String userName) {
        Log log = new Log();
        log.setActionType(EActionType.SUCCESSFUL_LOGIN);
        log.setActionDetails("User logged in successfully.");
        log.setUser(userService.findByEmail(userName).orElse(null));
        log.setTimestamp(new Date());
        saveLog(log);
    }

    public void logFailedLogin(String userName) {
        Log log = new Log();
        log.setActionType(EActionType.FAILED_LOGIN);
        log.setActionDetails("User login failed for email: " + userName);
        log.setUser(userService.findByEmail(userName).orElse(null));
        log.setTimestamp(new Date());
        saveLog(log);
    }

    public void logResourceAccess(EActionType action, String userName, Long item) {
        Log log = new Log();
        log.setActionType(action);
        log.setItem(itemRepo.findById(item).orElse(null));
        if(action.getActionTypeId()==3)
        log.setActionDetails("User Created new item with email: " + userName + " and for item:" + item);
        else if(action.getActionTypeId()==4)
        log.setActionDetails("User Added new bid with email: " + userName + " and for item:" + item);
        log.setUser(userService.findByEmail(userName).orElse(null));
        log.setTimestamp(new Date());
        saveLog(log);
    }

    public LogDTO convertToLogDTO(Log log) {
        LogDTO logDTO = new LogDTO();
        logDTO.setId(log.getId());
        logDTO.setActionType(log.getActionType().name());
        logDTO.setActionDetails(log.getActionDetails());
        logDTO.setTimestamp(log.getTimestamp());
        if (log.getUser() != null) {
            logDTO.setUserId(log.getUser().getId());
        }

        return logDTO;
    }
}
