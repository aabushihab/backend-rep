package com.clinic.doctor_app_backend.controller;

import com.clinic.doctor_app_backend.dto.CreateFolderRequest;
import com.clinic.doctor_app_backend.model.Folder;
import com.clinic.doctor_app_backend.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
@CrossOrigin("*")

public class FolderController {

    private final FolderService folderService;
    @GetMapping("/{id}/items")
    public List<Long> getItems(
            @PathVariable Long id) {

        return folderService.getItemIds(id);
    }
    @PostMapping
    public Folder create(@RequestBody CreateFolderRequest request) {
        return folderService.create(request.getName(), request.getDescription());
    }

    @GetMapping("/{id}")
    public Folder getById(@PathVariable Long id) {
        return folderService.getById(id);
    }

    @GetMapping
    public List<Folder> getAll() {
        return folderService.getAll();
    }

    @GetMapping("/search")
    public List<Folder> search(@RequestParam ("keyword") String keyword) {
        return folderService.search(keyword);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        folderService.delete(id);
    }

    @PostMapping("/{id}/items")
    public Folder addItems(
            @PathVariable Long id,
            @RequestBody List<Long> itemIds) {

        return folderService.addItems(id, itemIds);
    }

    @DeleteMapping("/{id}/items")
    public Folder removeItems(
            @PathVariable Long id,
            @RequestBody List<Long> itemIds) {

        return folderService.removeItems(id, itemIds);
    }
}
