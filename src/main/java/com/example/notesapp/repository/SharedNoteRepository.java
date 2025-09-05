package com.example.notesapp.repository;

import com.example.notesapp.SharedNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SharedNoteRepository extends JpaRepository<SharedNote, Long> {
    List<SharedNote> findBySharedWithId(Long sharedWithId);
    SharedNote findByNoteIdAndSharedWithId(Long noteId, Long sharedWithId);
}