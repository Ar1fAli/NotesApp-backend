package com.example.notesapp.repository;

import com.example.notesapp.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwnerId(Long ownerId);
}