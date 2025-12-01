package com.example.ticket.controller.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewTicketDTO(

        @NotNull @Email(message = "Deve ser um e-mail válido") String creatorEmail,
        @NotNull @Email(message = "Deve ser um e-mail válido") String assigneeEmail,
        Set<String> observerEmails,
        @NotNull(message = "O objeto é obrigatório") @NotBlank(message = "Não pode ser composta apenas de espaços") String object,
        @NotNull(message = "A ação é obrigatória") @NotBlank(message = "Não pode ser composta apenas de espaços") String action,
        @NotNull(message = "Os detalhes é obrigatório") @NotBlank(message = "Não pode ser composta apenas de espaços") String details,
        @NotNull(message = "O local é obrigatório") @NotBlank(message = "Não pode ser composta apenas de espaços") String locality) {

}

// @NotNull(message = "O nome é obrigatório")
// @NotBlank(message = "Não pode ser composta apenas de espaços")
// @Length(min = 3, message = "O nome de ter no mínimo 3 caracteres")
// String name,
// String handle,
// @NotNull(message = "O e-mail é obrigatório")
// @Email(message = "Deve ser um e-mail válido")
