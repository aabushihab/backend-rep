//package com.clinic.doctor_app_backend.service;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class VisitNativeService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public List<Map<String, Object>> getVisitsByPatientId(Long patientId) {
//
//        String sql = """
//            SELECT *
//            FROM visits
//            WHERE patient_id = ?
//            ORDER BY id ASC
//        """;
//
//        return jdbcTemplate.queryForList(sql, patientId);
//    }
//}
//
package com.clinic.doctor_app_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VisitNativeService {

    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getVisitsByPatientId(Long patientId) {

        String sql =
                "SELECT * " +
                        "FROM visits " +
                        "WHERE patient_id = ? " +
                        "ORDER BY id ASC";

        return jdbcTemplate.queryForList(sql, patientId);
    }
}
