package com.example.ticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticket.controller.dto.ChangeTicketStatusDTO;
import com.example.ticket.controller.dto.NewTicketDTO;
import com.example.ticket.repository.entity.Ticket;
import com.example.ticket.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {
    private TicketService ticketBusiness;

    public TicketController(TicketService ticketBusiness) {
        this.ticketBusiness = ticketBusiness;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> getTickets() {
        return ResponseEntity.ok(this.ticketBusiness.getTickets());
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public void newTicket(@RequestBody NewTicketDTO newTicket) {
        ticketBusiness.newTicket(newTicket);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateTicketStatus(@PathVariable int id, @RequestBody @Valid ChangeTicketStatusDTO newStatus) {
        ticketBusiness.updateTicketStatus(id, newStatus.status());
    }

}
