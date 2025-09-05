//package com.example.notesapp.controller;
//
//import com.example.notesapp.Note;
//import com.example.notesapp.User;
//import com.example.notesapp.service.NoteService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/notes")
//public class NoteController {
//
//    @Autowired
//    private NoteService noteService;
//
//    private User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = new User();
//        user.setEmail(authentication.getName());
//        return user;
//    }
//
//    @GetMapping
//    public List<Note> getNotes() {
//        return noteService.getUserNotes(getCurrentUser());
//    }
//
//    @PostMapping
//    public Note createNote(@RequestBody Note note) {
//        return noteService.createNote(note, getCurrentUser());
//    }
//
//    @PutMapping("/{id}")
//    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
//        return noteService.updateNote(id, note, getCurrentUser());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
//        noteService.deleteNote(id, getCurrentUser());
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{id}/share")
//    public ResponseEntity<?> shareNote(@PathVariable Long id, @RequestBody Map<String, String> request) {
//        String sharedWithEmail = request.get("sharedWithEmail");
//        noteService.shareNote(id, sharedWithEmail, getCurrentUser());
//        return ResponseEntity.ok(Map.of("message", "Note shared"));
//    }
//}









package com.example.notesapp.controller;

import com.example.notesapp.Note;
import com.example.notesapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> getNotes() {
        return noteService.getUserNotes();
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<?> shareNote(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String sharedWithEmail = request.get("sharedWithEmail");
        noteService.shareNote(id, sharedWithEmail);
        return ResponseEntity.ok(Map.of("message", "Note shared"));
    }
}
