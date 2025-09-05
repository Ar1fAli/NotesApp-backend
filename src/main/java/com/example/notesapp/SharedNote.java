package com.example.notesapp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shared_notes")
@Data
public class SharedNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @ManyToOne
    @JoinColumn(name = "shared_with_id", nullable = false)
    private User sharedWith;
}