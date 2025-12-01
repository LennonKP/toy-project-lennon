package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import com.example.demo.domain.IslandDomainService;
import com.example.demo.repository.IslandRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.exceptions.NotFoundException;
import com.example.demo.service.stereotype.Business;

@Business
public class IslandService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IslandRepository islandRepository;
    @Autowired
    private IslandDomainService islandDomainService;

    public void alocarWorkstationDisponivel(@NonNull Integer userId) {
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException());

        final var islands = islandRepository.findIslandWithAvailableWorkstations();

        var island = islandDomainService.encontrarIlhaEAlocar(islands, user);
        islandRepository.save(island);
    }
}