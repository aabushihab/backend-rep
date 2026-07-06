package com.clinic.doctor_app_backend.schedual;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PostgresBackupService {

    @Value("${backup.db-name}")
    private String dbName;

    @Value("${backup.db-user}")
    private String dbUser;

    @Value("${backup.db-password}")
    private String dbPassword;

    @Value("${backup.dir}")
    private String backupDir;

    @Value("${backup.cron}")
    private String cronExpression;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    @Scheduled(cron = "${backup.cron}")
    public void backupDatabase() {
        try {
            // Ensure backup directory exists
            File dir = new File(backupDir);
            if (!dir.exists()) dir.mkdirs();

            String timestamp = LocalDateTime.now().format(FORMATTER);
            String backupFile = backupDir + "\\clinicdb_" + timestamp + ".sql";

            // Full path to pg_dump.exe
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe";

            // Each argument is a separate string
            ProcessBuilder pb = new ProcessBuilder(
                    pgDumpPath,
                    "-U", dbUser,
                    "-h", "localhost",
                    "-p", "5432",
                    "-f", backupFile,
                    dbName
            );

            // Set password
            pb.environment().put("PGPASSWORD", dbPassword);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Print output for debugging
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup successful: " + backupFile);
            } else {
                System.err.println("Backup failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
