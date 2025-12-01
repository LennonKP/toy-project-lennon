package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.IslandService;

@RestController
@RequestMapping("/api/v1/islands")
public class IslandController {

    private IslandService islandService;

    public IslandController(IslandService islandService) {
        this.islandService = islandService;
    }

    @PostMapping("/alocar/{userId}")
    public ResponseEntity<Void> alocarWorkstation(@PathVariable("userId") Integer userId) {
        islandService.alocarWorkstationDisponivel(userId);
        return ResponseEntity.ok().build();
    }

}
