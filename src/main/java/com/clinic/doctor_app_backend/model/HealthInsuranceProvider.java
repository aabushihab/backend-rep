//package com.clinic.doctor_app_backend.model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "health_insurance_providers")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class HealthInsuranceProvider {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String country;   // New field for country
//
//    private String name;            // Company / Organization name
//
//    private String type;            // Type: Local Insurer / TPA / Bank / Association / NGO
//
//    private boolean classA;         // VIP
//    private boolean classB;         // Standard
//    private boolean classC;         // Basic
//
//    private String website;         // Website or notes
//}
package com.clinic.doctor_app_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "health_insurance_providers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_country_provider_name",
                        columnNames = {"country", "name"}
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthInsuranceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String name;

    private String type;

    private boolean classA;
    private boolean classB;
    private boolean classC;

    private String website;
}
