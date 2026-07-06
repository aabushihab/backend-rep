package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {


}
