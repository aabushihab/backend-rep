package com.clinic.doctor_app_backend.dto;

import com.clinic.doctor_app_backend.model.Visit;

public class VisitPageResponse {
    private int page;
    private Visit visit;

    public VisitPageResponse(int page, Visit visit) {
        this.page = page;
        this.visit = visit;
    }

    public int getPage() {
        return page;
    }

    public Visit getVisit() {
        return visit;
    }
}