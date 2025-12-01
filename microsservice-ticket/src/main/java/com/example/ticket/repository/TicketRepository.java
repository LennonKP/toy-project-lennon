package com.example.ticket.repository;

import org.springframework.data.repository.ListCrudRepository;
import com.example.ticket.repository.entity.Ticket;

public interface TicketRepository extends ListCrudRepository<Ticket, Integer> {
}
