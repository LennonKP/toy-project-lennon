package com.example.ticket.controller.dto;

import com.example.ticket.repository.entity.Ticket;
import jakarta.validation.constraints.NotNull;

public record ChangeTicketStatusDTO(
        @NotNull(message = "O novo status é obrigatório") Ticket.STATUS status) {

}