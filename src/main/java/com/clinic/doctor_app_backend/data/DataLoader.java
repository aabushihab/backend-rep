//package com.clinic.doctor_app_backend.data;
//
//import com.clinic.doctor_app_backend.model.HealthInsuranceProvider;
//import com.clinic.doctor_app_backend.repository.HealthInsuranceProviderRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    private final HealthInsuranceProviderRepository repository;
//
//    public DataLoader(HealthInsuranceProviderRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void run(String... args) {
//        // ---------- Jordan Insurance Providers ----------
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Solidarity", "Local Insurer", true, true, true, "solidarity.com.jo"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Newton", "Local Insurer", true, true, true, "newtoninsurance.com"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Euro Arab Insurance Group", "Regional Insurer", true, true, false, "euroarabinsurance.com"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","GIG", "Regional Insurer", true, true, true, "gig.com"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","NatHealth", "TPA", true, true, true, "Health insurance administrator"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","MedNet", "TPA", true, true, true, "Third-party administrator"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Omnicare", "TPA", true, true, true, "Managed care / hospital network"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","GlobeMed - Jordan", "TPA", true, true, true, "Network / Managed care"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Jordan Engineers Association", "Association / Partner", true, true, true, "Insurance for members"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Jordan Medical Association", "Association / Partner", true, true, true, "Insurance for members"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Jordan Dental Association", "Association / Partner", true, true, true, "Insurance for members"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Housing Bank", "Bank / Partner", true, true, true, "Corporate & employee plans"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Arab Bank", "Bank / Partner", true, true, true, "Corporate & employee plans"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Commercial Bank", "Bank / Partner", true, true, true, "Corporate & employee plans"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Indo Jordan Chemical Company", "Company / Corporate", true, true, true, "Employee health insurance"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","UTA Assistance", "International / TPA", true, true, false, "Travel & health assistance"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","Embassies", "Organization", true, true, true, "Diplomatic / staff health insurance"));
//        repository.save(new HealthInsuranceProvider(null, "Jordan","NGOs", "Organization", true, true, true, "Health insurance for staff / programs"));
//
//        // ---------- Saudi Arabia Insurance Providers ----------
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Al Sagr Cooperative Insurance Co.", "Local Insurer", true, true, true, "alsagr.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "The Company for Cooperative Insurance (Tawuniya)", "Local Insurer", true, true, true, "tawuniya.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Bupa Arabia for Cooperative Insurance Co.", "Local Insurer", true, true, true, "bupa.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Al Rajhi Company for Cooperative Insurance", "Local Insurer", true, true, true, "alrajhitakaful.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Malath Cooperative Insurance Co.", "Local Insurer", true, true, true, "malath.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Salama Cooperative Insurance Co.", "Local Insurer", true, true, true, "salama.ae"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Walaa Cooperative Insurance Co.", "Local Insurer", true, true, true, "walaa.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Arabian Shield Cooperative Insurance Co.", "Local Insurer", true, true, true, "arabianshield.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Saudi Arabian Cooperative Insurance Co. (SAICO)", "Local Insurer", true, true, true, "saico.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Allianz Saudi Fransi Cooperative Insurance Co.", "International Insurer", true, true, true, "allianz.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Gulf Insurance Group (GIG)", "Regional Insurer", true, true, true, "gig.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "United Cooperative Assurance (UCA)", "Local Insurer", true, true, true, "uca.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Allied Cooperative Insurance Group (ACIG)", "Local Insurer", true, true, true, "acig.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Arabia Insurance Cooperative Co.", "Local Insurer", true, true, true, "arabia-ins.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Al Etihad Co-operative Insurance Co.", "Local Insurer", true, true, true, "aletihad.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Gulf Union Al Ahlia Cooperative Insurance Co.", "Local Insurer", true, true, true, "gulfunion.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Al Jazira Takaful Taawuni Company", "Local/Takaful Insurer", true, true, true, "aljazira.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Chubb Arabia Cooperative Insurance Co.", "International Insurer", true, true, true, "chubb.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "LIVA Insurance Co.", "Local Insurer", true, true, true, "livainsurance.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Wataniya Insurance Co.", "Local Insurer", true, true, true, "wataniya.com.sa"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Saudi Enaya Cooperative Insurance Co.", "Local Insurer", true, true, true, "saudienaya.com.sa"));
//
//        // ---------- Third‑Party Administrators (TPAs) ----------
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Total Care Saudi Company - TPA", "TPA", true, true, true, "totalcaresaudi.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Saudi NextCare Company - TPA", "TPA", true, true, true, "nextcaresaudi.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Saudi GlobeMed Company - TPA", "TPA", true, true, true, "globemedksa.com"));
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Saudi Arabian Mednet Company - TPA", "TPA", true, true, true, "mednet.com.sa"));
//
//        // ---------- Optional International Players (Licensed in KSA) ----------
//        repository.save(new HealthInsuranceProvider(null, "Saudi Arabia", "Cigna Insurance Saudi Arabia", "International Insurer", true, true, true, "cignahealthcare.com.sa"));
//    }
//
//
//}



package com.clinic.doctor_app_backend.data;

import com.clinic.doctor_app_backend.model.HealthInsuranceProvider;
import com.clinic.doctor_app_backend.repository.HealthInsuranceProviderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final HealthInsuranceProviderRepository repository;

    public DataLoader(HealthInsuranceProviderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        // ---------- Jordan Insurance Providers ----------
        saveIfNotExists("Jordan", "Solidarity", "Local Insurer", true, true, true, "solidarity.com.jo");
        saveIfNotExists("Jordan", "Newton", "Local Insurer", true, true, true, "newtoninsurance.com");
        saveIfNotExists("Jordan", "Euro Arab Insurance Group", "Regional Insurer", true, true, false, "euroarabinsurance.com");
        saveIfNotExists("Jordan", "GIG", "Regional Insurer", true, true, true, "gig.com");
        saveIfNotExists("Jordan", "NatHealth", "TPA", true, true, true, "Health insurance administrator");
        saveIfNotExists("Jordan", "MedNet", "TPA", true, true, true, "Third-party administrator");
        saveIfNotExists("Jordan", "Omnicare", "TPA", true, true, true, "Managed care / hospital network");
        saveIfNotExists("Jordan", "GlobeMed - Jordan", "TPA", true, true, true, "Network / Managed care");
        saveIfNotExists("Jordan", "Jordan Engineers Association", "Association / Partner", true, true, true, "Insurance for members");
        saveIfNotExists("Jordan", "Jordan Medical Association", "Association / Partner", true, true, true, "Insurance for members");
        saveIfNotExists("Jordan", "Jordan Dental Association", "Association / Partner", true, true, true, "Insurance for members");
        saveIfNotExists("Jordan", "Housing Bank", "Bank / Partner", true, true, true, "Corporate & employee plans");
        saveIfNotExists("Jordan", "Arab Bank", "Bank / Partner", true, true, true, "Corporate & employee plans");
        saveIfNotExists("Jordan", "Commercial Bank", "Bank / Partner", true, true, true, "Corporate & employee plans");
        saveIfNotExists("Jordan", "Indo Jordan Chemical Company", "Company / Corporate", true, true, true, "Employee health insurance");
        saveIfNotExists("Jordan", "UTA Assistance", "International / TPA", true, true, false, "Travel & health assistance");
        saveIfNotExists("Jordan", "Embassies", "Organization", true, true, true, "Diplomatic / staff health insurance");
        saveIfNotExists("Jordan", "NGOs", "Organization", true, true, true, "Health insurance for staff / programs");

        // ---------- Saudi Arabia Insurance Providers ----------
        saveIfNotExists("Saudi Arabia", "Al Sagr Cooperative Insurance Co.", "Local Insurer", true, true, true, "alsagr.com.sa");
        saveIfNotExists("Saudi Arabia", "The Company for Cooperative Insurance (Tawuniya)", "Local Insurer", true, true, true, "tawuniya.com.sa");
        saveIfNotExists("Saudi Arabia", "Bupa Arabia for Cooperative Insurance Co.", "Local Insurer", true, true, true, "bupa.com.sa");
        saveIfNotExists("Saudi Arabia", "Al Rajhi Company for Cooperative Insurance", "Local Insurer", true, true, true, "alrajhitakaful.com");
        saveIfNotExists("Saudi Arabia", "Malath Cooperative Insurance Co.", "Local Insurer", true, true, true, "malath.com.sa");
        saveIfNotExists("Saudi Arabia", "Salama Cooperative Insurance Co.", "Local Insurer", true, true, true, "salama.ae");
        saveIfNotExists("Saudi Arabia", "Walaa Cooperative Insurance Co.", "Local Insurer", true, true, true, "walaa.com.sa");
        saveIfNotExists("Saudi Arabia", "Arabian Shield Cooperative Insurance Co.", "Local Insurer", true, true, true, "arabianshield.com.sa");
        saveIfNotExists("Saudi Arabia", "Saudi Arabian Cooperative Insurance Co. (SAICO)", "Local Insurer", true, true, true, "saico.com.sa");
        saveIfNotExists("Saudi Arabia", "Allianz Saudi Fransi Cooperative Insurance Co.", "International Insurer", true, true, true, "allianz.com.sa");
        saveIfNotExists("Saudi Arabia", "Gulf Insurance Group (GIG)", "Regional Insurer", true, true, true, "gig.com");
        saveIfNotExists("Saudi Arabia", "United Cooperative Assurance (UCA)", "Local Insurer", true, true, true, "uca.com.sa");
        saveIfNotExists("Saudi Arabia", "Allied Cooperative Insurance Group (ACIG)", "Local Insurer", true, true, true, "acig.com.sa");
        saveIfNotExists("Saudi Arabia", "Arabia Insurance Cooperative Co.", "Local Insurer", true, true, true, "arabia-ins.com");
        saveIfNotExists("Saudi Arabia", "Al Etihad Co-operative Insurance Co.", "Local Insurer", true, true, true, "aletihad.com.sa");
        saveIfNotExists("Saudi Arabia", "Gulf Union Al Ahlia Cooperative Insurance Co.", "Local Insurer", true, true, true, "gulfunion.com.sa");
        saveIfNotExists("Saudi Arabia", "Al Jazira Takaful Taawuni Company", "Local/Takaful Insurer", true, true, true, "aljazira.com.sa");
        saveIfNotExists("Saudi Arabia", "Chubb Arabia Cooperative Insurance Co.", "International Insurer", true, true, true, "chubb.com");
        saveIfNotExists("Saudi Arabia", "LIVA Insurance Co.", "Local Insurer", true, true, true, "livainsurance.com");
        saveIfNotExists("Saudi Arabia", "Wataniya Insurance Co.", "Local Insurer", true, true, true, "wataniya.com.sa");
        saveIfNotExists("Saudi Arabia", "Saudi Enaya Cooperative Insurance Co.", "Local Insurer", true, true, true, "saudienaya.com.sa");

        // ---------- Third‑Party Administrators (TPAs) ----------
        saveIfNotExists("Saudi Arabia", "Total Care Saudi Company - TPA", "TPA", true, true, true, "totalcaresaudi.com");
        saveIfNotExists("Saudi Arabia", "Saudi NextCare Company - TPA", "TPA", true, true, true, "nextcaresaudi.com");
        saveIfNotExists("Saudi Arabia", "Saudi GlobeMed Company - TPA", "TPA", true, true, true, "globemedksa.com");
        saveIfNotExists("Saudi Arabia", "Saudi Arabian Mednet Company - TPA", "TPA", true, true, true, "mednet.com.sa");

        // ---------- Optional International Players (Licensed in KSA) ----------
        saveIfNotExists("Saudi Arabia", "Cigna Insurance Saudi Arabia", "International Insurer", true, true, true, "cignahealthcare.com.sa");
    }

    // -------------------- Helper Method --------------------
    private void saveIfNotExists(
            String country,
            String name,
            String type,
            boolean classA,
            boolean classB,
            boolean classC,
            String website) {

        // Check by business key: country + name
        if (!repository.existsByCountryAndName(country, name)) {
            repository.save(new HealthInsuranceProvider(
                    null,
                    country,
                    name,
                    type,
                    classA,
                    classB,
                    classC,
                    website
            ));
        }
    }
}
