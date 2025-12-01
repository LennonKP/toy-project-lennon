package com.example.ticket.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.ticket.controller.dto.NewTicketDTO;
import com.example.ticket.repository.TicketRepository;
import com.example.ticket.repository.entity.Ticket;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Validated
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(
            TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getTickets() {
        return this.ticketRepository.findAll();
    }

    @Transactional
    public void newTicket(@Valid NewTicketDTO newTicket) {
        // In a real microservice, we might want to validate if emails exist by calling
        // User Service,
        // but for now we just trust the input or assume loose coupling.

        Set<String> observers = new HashSet<>();
        if (newTicket.observerEmails() != null) {
            observers.addAll(newTicket.observerEmails());
        }

        Ticket ticket = new Ticket(newTicket.creatorEmail(), newTicket.assigneeEmail(), Ticket.STATUS.CRIADO, observers,
                newTicket.object(),
                newTicket.action(), newTicket.details(), newTicket.locality());

        ticketRepository.save(ticket);
    }

    public void updateTicketStatus(int ticketID, Ticket.STATUS status) {
        Ticket ticket = this.ticketRepository.findById(ticketID)
                .orElseThrow(() -> new IllegalArgumentException("Ticket não encontrado"));

        ticket.setStatus(status);

        this.ticketRepository.save(ticket);
    }
}
