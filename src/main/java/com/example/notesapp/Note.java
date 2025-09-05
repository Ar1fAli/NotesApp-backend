package com.example.notesapp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notes")
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Version
    private Long version;
}