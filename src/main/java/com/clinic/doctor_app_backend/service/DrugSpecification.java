package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.dto.DrugFilter;
import com.clinic.doctor_app_backend.model.Drug;
import org.springframework.data.jpa.domain.Specification;

public class DrugSpecification {

    public static Specification<Drug> filter(DrugFilter filter) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (filter.getTradeName() != null)
                predicate.getExpressions().add(
                        cb.like(cb.lower(root.get("tradeName")),
                                "%" + filter.getTradeName().toLowerCase() + "%")
                );

            if (filter.getGenericName() != null)
                predicate.getExpressions().add(
                        cb.like(cb.lower(root.get("genericName")),
                                "%" + filter.getGenericName().toLowerCase() + "%")
                );

            if (filter.getAtcCode() != null)
                predicate.getExpressions().add(
                        cb.equal(root.get("atcCode"), filter.getAtcCode())
                );

            if (filter.getIngredients() != null)
                predicate.getExpressions().add(
                        cb.like(cb.lower(root.get("ingredients")),
                                "%" + filter.getIngredients().toLowerCase() + "%")
                );

            if (filter.getDosageForm() != null)
                predicate.getExpressions().add(
                        cb.equal(root.get("dosageForm"), filter.getDosageForm())
                );

            return predicate;
        };
    }
}