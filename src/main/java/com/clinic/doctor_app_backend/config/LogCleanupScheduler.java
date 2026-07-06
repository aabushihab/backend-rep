package com.clinic.doctor_app_backend.config;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class LogCleanupScheduler {

    private final JdbcTemplate jdbcTemplate;

    public LogCleanupScheduler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Transactional
    @Scheduled(cron = "0 59 23 * * ?")
    public void deleteOldLogs() {
        String sql = "DELETE FROM public.user_action_log WHERE timestamp < NOW() - INTERVAL '30 days'";
        int deletedRows = jdbcTemplate.update(sql);
        System.out.println("Deleted " + deletedRows + " old logs.");
    }
}
