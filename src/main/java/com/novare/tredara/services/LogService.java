package com.novare.tredara.services;

import com.novare.tredara.models.EActionType;
import com.novare.tredara.models.Log;
import com.novare.tredara.payloads.LogDTO;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.LogRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final UserService userService;
    private final ItemRepo itemRepo;

    public LogService(LogRepository logRepository, UserService userService, ItemRepo itemRepo) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.itemRepo = itemRepo;
    }

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
        if (action.getActionTypeId() == 3)
            log.setActionDetails("User Created new item with email: " + userName + " and for item:" + item);
        else if (action.getActionTypeId() == 4)
            log.setActionDetails("User Added new bid with email: " + userName + " and for item:" + item);
        log.setUser(userService.findByEmail(userName).orElse(null));
        log.setTimestamp(new Date());
        saveLog(log);
    }

    public List<LogDTO> getFilteredLogHistory(LogDTO request) {
        return this.getLogHistory()
                .stream()
                .map(this::convertToLogDTO)
                .filter(dto -> {
                    if (request.getFilterStartInterval() != null && request.getFilterEndInterval() != null) {
                        return dto.getTimestamp().after(request.getFilterStartInterval())
                                && dto.getTimestamp().before(request.getFilterEndInterval());
                    } else if (request.getFilterStartInterval() != null) {
                        return dto.getTimestamp().after(request.getFilterStartInterval());
                    } else if (request.getFilterEndInterval() != null) {
                        return dto.getTimestamp().before(request.getFilterEndInterval());
                    }
                    return true;
                })
                .filter(dto -> {
                    if (request.getActionType() != null) {
                        return dto.getActionType().equalsIgnoreCase(request.getActionType());
                    }
                    return true;
                })
                .toList();
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
