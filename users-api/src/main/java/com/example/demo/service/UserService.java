package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;
import com.example.demo.service.stereotype.Business;

import jakarta.validation.Valid;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

@Business
@Service
@Validated
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Set<String> defaultRoles;
    private final RestTemplate restTemplate = new RestTemplate();

    @NonNull
    private final String ticketServiceUrl;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            @Value("${app.user.default.roles}") Set<String> defaultRoles,
            @Value("${app.ticket.service.url}") @NonNull String ticketServiceUrl) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.defaultRoles = defaultRoles;
        this.ticketServiceUrl = ticketServiceUrl;
    }

    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException(
                    "A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }

        userRepository.findByEmail(newUser.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
                });

        userRepository.findByHandle(newUser.handle())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
                });

        User user = new User();

        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));

        Set<Role> roles = new HashSet<>();

        roles.addAll(roleRepository.findByNameIn(defaultRoles));

        Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
        if (additionalRoles.size() != newUser.roles().size()) {
            throw new IllegalArgumentException("Alguns papéis não existem");
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        user.setRoles(roles);

        Profile profile = new Profile();

        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user);

        createOnboardingTicket(user);
        createWorkstationTicket(user);
    }

    private void createOnboardingTicket(User user) {
        try {
            Map<String, Object> ticket = new HashMap<>();
            ticket.put("creatorEmail", user.getEmail());
            ticket.put("assigneeEmail", user.getEmail());
            ticket.put("observerEmails", Collections.emptySet());
            ticket.put("object", "Onboarding");
            ticket.put("action", "Realizar onboard");
            ticket.put("details", "Novo usuário criado: " + user.getProfile().getName());
            ticket.put("locality", "Remoto");

            restTemplate.postForLocation(ticketServiceUrl, ticket);
        } catch (Exception e) {
            System.err.println("Erro ao criar ticket de onboarding: " + e.getMessage());
        }
    }

    private void createWorkstationTicket(User user) {
        try {
            Map<String, Object> ticket = new HashMap<>();
            ticket.put("creatorEmail", user.getEmail());
            ticket.put("assigneeEmail", user.getEmail());
            ticket.put("observerEmails", Collections.emptySet());
            ticket.put("object", "Alocação de Estação de Trabalho");
            ticket.put("action", "Alocar estação");
            ticket.put("details", "Novo usuário precisa de estação: " + user.getProfile().getName());
            ticket.put("locality", "Escritório");

            restTemplate.postForLocation(ticketServiceUrl, ticket);
        } catch (Exception e) {
            System.err.println("Erro ao criar ticket de estação de trabalho: " + e.getMessage());
        }
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }
}
