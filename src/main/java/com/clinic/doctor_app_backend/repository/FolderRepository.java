package com.clinic.doctor_app_backend.repository;


import com.clinic.doctor_app_backend.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByNameContainingIgnoreCase(String keyword);
    boolean existsByNameIgnoreCase(String name);

}