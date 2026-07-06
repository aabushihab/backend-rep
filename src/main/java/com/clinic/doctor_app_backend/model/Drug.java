package com.clinic.doctor_app_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "drugs")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Drug {

    @Id
    private Long drugId;

    private String genericCode;
    private String tradeName;
    private String genericName;
    private String mappingScientificCode;

    private BigDecimal price;
    private BigDecimal tax;

    private Integer granularUnit;

    private String unitType;
    private String dosageForm;

    private String roaCode;
    private String routeOfAdmin;
    private String suggestedRouteOfAdmin;

    private String packageType;
    private String packageSize;

    private String ingredients;
    private String strength;

    private String isControlled;

    private String drugCode;
    private String atcCode;

    private Integer maxRefill;
    private Integer unitTypeId;
    private Integer packageVolume;
    private Integer dosageUnitId;

    private String dosageUnit;
    private String localDrugCode;


}