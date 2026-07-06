package com.clinic.doctor_app_backend.service;


import com.clinic.doctor_app_backend.model.UserActionLog;
import com.clinic.doctor_app_backend.repository.UserActionLogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final UserActionLogRepository logRepository;

    public LogService(UserActionLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void log(String username, String action, String details) {
        UserActionLog log = new UserActionLog();
        log.setUsername(username);
        log.setAction(action);
        log.setDetails(details);
        logRepository.save(log);
    }
}
