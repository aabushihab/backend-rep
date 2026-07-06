package com.clinic.doctor_app_backend.dto;

public class DoctorPaymentSummaryDTO {

    private Long doctorId;
    private String doctorName;
    private Double cashTotal;
    private Double posTotal;
    private Double insuranceTotal;
    private Long freeCount;
    private Double grandTotal;
    private Double insuranceDiscount;
    // Must match exactly the JPQL SELECT arguments
//    public DoctorPaymentSummaryDTO(Long doctorId, String doctorName,
//                                   Double cashTotal, Double posTotal,
//                                   Double insuranceTotal, Long freeCount,
//                                   Double grandTotal) {
//        this.doctorId = doctorId;
//        this.doctorName = doctorName;
//        this.cashTotal = cashTotal;
//        this.posTotal = posTotal;
//        this.insuranceTotal = insuranceTotal;
//        this.freeCount = freeCount;
//        this.grandTotal = grandTotal;
//    }


    public DoctorPaymentSummaryDTO(Long doctorId, String doctorName,
                                   Double cashTotal, Double posTotal,
                                   Double insuranceTotal,
                                   Double insuranceDiscount,
                                   Long freeCount,
                                   Double grandTotal) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.cashTotal = cashTotal;
        this.posTotal = posTotal;
        this.insuranceTotal = insuranceTotal;
        this.insuranceDiscount = insuranceDiscount;
        this.freeCount = freeCount;
        this.grandTotal = grandTotal;
    }

    public Double getInsuranceDiscount() { return insuranceDiscount; }
    public void setInsuranceDiscount(Double insuranceDiscount) { this.insuranceDiscount = insuranceDiscount; }
    // GETTERS & SETTERS
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Double getCashTotal() { return cashTotal; }
    public void setCashTotal(Double cashTotal) { this.cashTotal = cashTotal; }

    public Double getPosTotal() { return posTotal; }
    public void setPosTotal(Double posTotal) { this.posTotal = posTotal; }

    public Double getInsuranceTotal() { return insuranceTotal; }
    public void setInsuranceTotal(Double insuranceTotal) { this.insuranceTotal = insuranceTotal; }

    public Long getFreeCount() { return freeCount; }
    public void setFreeCount(Long freeCount) { this.freeCount = freeCount; }

    public Double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Double grandTotal) { this.grandTotal = grandTotal; }
}
