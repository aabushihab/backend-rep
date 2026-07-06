package com.clinic.doctor_app_backend.service;

import com.clinic.doctor_app_backend.model.Folder;
import com.clinic.doctor_app_backend.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    public List<Long> getItemIds(Long folderId) {

        Folder folder = getById(folderId);

        return folder.getItemIds() == null
                ? List.of()
                : folder.getItemIds();
    }
    public Folder create(String name, String description) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder name cannot be empty");
        }

        boolean exists = folderRepository.existsByNameIgnoreCase(name);

        if (exists) {
            throw new IllegalStateException("Folder name already exists");
        }

        Folder folder = new Folder();
        folder.setName(name.trim());
        folder.setDescription(description);
        folder.setItemIds(new ArrayList<>());

        return folderRepository.save(folder);
    }

    public Folder getById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Folder not found"));
    }

    public List<Folder> getAll() {
        return folderRepository.findAll();
    }

    public void delete(Long id) {
        folderRepository.deleteById(id);
    }

    public List<Folder> search(String keyword) {
        return folderRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Folder addItems(Long folderId, List<Long> itemIds) {

        Folder folder = getById(folderId);

        if (folder.getItemIds() == null) {
            folder.setItemIds(new ArrayList<>());
        }

        for (Long id : itemIds) {
            if (!folder.getItemIds().contains(id)) {
                folder.getItemIds().add(id);
            }
        }

        return folderRepository.save(folder);
    }

    public Folder removeItems(Long folderId, List<Long> itemIds) {

        Folder folder = getById(folderId);

        if (folder.getItemIds() != null) {
            folder.getItemIds().removeAll(itemIds);
        }

        return folderRepository.save(folder);
    }
}
