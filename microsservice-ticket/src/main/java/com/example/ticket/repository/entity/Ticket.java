package com.example.ticket.repository.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
@Table(name = "tickets")
public class Ticket {

    public enum STATUS {
        CRIADO,
        ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }

    public Ticket() {
    }

    public Ticket(String creatorEmail, String assigneeEmail, STATUS status, Set<String> observerEmails, String object,
            String action,
            String details, String locality) {
        this.creatorEmail = creatorEmail;
        this.assigneeEmail = assigneeEmail;
        this.status = status;
        this.observerEmails = observerEmails;
        this.object = object;
        this.action = action;
        this.details = details;
        this.locality = locality;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String creatorEmail;

    @Column(nullable = false)
    private String assigneeEmail;

    @ElementCollection
    @CollectionTable(name = "ticket_observers", joinColumns = { @JoinColumn(name = "ticket_id") })
    @Column(name = "email")
    private Set<String> observerEmails;

    @Column(nullable = false, length = 255)
    private String object;

    @Column(nullable = false, length = 255)
    private String action;

    @Column(nullable = false, length = 255)
    private String details;

    @Column(nullable = false, length = 255)
    private String locality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATUS status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public Set<String> getObserverEmails() {
        return observerEmails;
    }

    public String getObject() {
        return object;
    }

    public String getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public String getLocality() {
        return locality;
    }

    public STATUS getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

}
