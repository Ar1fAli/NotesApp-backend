//package com.example.notesapp.service;
//
//import com.example.notesapp.Note;
//import com.example.notesapp.SharedNote;
//import com.example.notesapp.User;
//import com.example.notesapp.repository.NoteRepository;
//import com.example.notesapp.repository.SharedNoteRepository;
//import com.example.notesapp.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class NoteService {
//    @Autowired
//    private NoteRepository noteRepository;
//
//    @Autowired
//    private SharedNoteRepository sharedNoteRepository;
//
//    @Autowired
//    private UserService userService;
//
////    public Note createNote(Note note, User owner) {
////        note.setOwner(owner);
////        return noteRepository.save(note);
////    }
//
//
//        @Autowired
//        private final UserRepository userRepository;
//
//        public Note createNote(Note note) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String email = auth.getName();
//
//            User owner = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found: " + email));
//
//            note.setOwner(owner);
//            return noteRepository.save(note);
//        }
////
////        public List<Note> getUserNotes() {
////            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////            String email = auth.getName();
////
////            User owner = userRepository.findByEmail(email)
////                    .orElseThrow(() -> new RuntimeException("User not found: " + email));
////
////            return noteRepository.findByOwner(owner);
////        }
//
//
//
//    public Note updateNote(Long id, Note updatedNote, User user) {
//        Note note = noteRepository.findById(id).orElseThrow();
//        if (!note.getOwner().getId().equals(user.getId())) {
//            throw new RuntimeException("Not owner");
//        }
//        note.setTitle(updatedNote.getTitle());
//        note.setContent(updatedNote.getContent());
//        return noteRepository.save(note);
//    }
//
//    public void deleteNote(Long id, User user) {
//        Note note = noteRepository.findById(id).orElseThrow();
//        if (!note.getOwner().getId().equals(user.getId())) {
//            throw new RuntimeException("Not owner");
//        }
//        noteRepository.delete(note);
//    }
//
//    public List<Note> getUserNotes(User user) {
//        List<Note> ownedNotes = noteRepository.findByOwnerId(user.getId());
//        List<SharedNote> shared = sharedNoteRepository.findBySharedWithId(user.getId());
//        List<Note> sharedNotes = new ArrayList<>();
//        for (SharedNote s : shared) {
//            sharedNotes.add(s.getNote());
//        }
//        ownedNotes.addAll(sharedNotes);
//        return ownedNotes;
//    }
//
//    public void shareNote(Long noteId, String sharedWithEmail, User owner) {
//        Note note = noteRepository.findById(noteId).orElseThrow();
//        if (!note.getOwner().getId().equals(owner.getId())) {
//            throw new RuntimeException("Not owner");
//        }
//        User sharedWith = userService.findByEmail(sharedWithEmail);
//        if (sharedWith == null) {
//            throw new RuntimeException("User not found");
//        }
//        if (sharedNoteRepository.findByNoteIdAndSharedWithId(noteId, sharedWith.getId()) != null) {
//            throw new RuntimeException("Already shared");
//        }
//        SharedNote sharedNote = new SharedNote();
//        sharedNote.setNote(note);
//        sharedNote.setSharedWith(sharedWith);
//        sharedNoteRepository.save(sharedNote);
//    }
//}











package com.example.notesapp.service;

import com.example.notesapp.Note;
import com.example.notesapp.SharedNote;
import com.example.notesapp.User;
import com.example.notesapp.repository.NoteRepository;
import com.example.notesapp.repository.SharedNoteRepository;
import com.example.notesapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private SharedNoteRepository sharedNoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public Note createNote(Note note) {
        User owner = getCurrentUser();
        note.setOwner(owner);
        return noteRepository.save(note);
    }

    public List<Note> getUserNotes() {
        User user = getCurrentUser();
        List<Note> ownedNotes = noteRepository.findByOwnerId(user.getId());

        List<SharedNote> shared = sharedNoteRepository.findBySharedWithId(user.getId());
        List<Note> sharedNotes = new ArrayList<>();
        for (SharedNote s : shared) {
            sharedNotes.add(s.getNote());
        }

        ownedNotes.addAll(sharedNotes);
        return ownedNotes;
    }

    public Note updateNote(Long id, Note updatedNote) {
        User user = getCurrentUser();
        Note note = noteRepository.findById(id).orElseThrow();

        if (!note.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not owner");
        }

        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        User user = getCurrentUser();
        Note note = noteRepository.findById(id).orElseThrow();

        if (!note.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not owner");
        }

        noteRepository.delete(note);
    }

    public void shareNote(Long noteId, String sharedWithEmail) {
        User owner = getCurrentUser();
        Note note = noteRepository.findById(noteId).orElseThrow();

        if (!note.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Not owner");
        }

        User sharedWith = userService.findByEmail(sharedWithEmail);
        if (sharedWith == null) {
            throw new RuntimeException("User not found");
        }

        if (sharedNoteRepository.findByNoteIdAndSharedWithId(noteId, sharedWith.getId()) != null) {
            throw new RuntimeException("Already shared");
        }

        SharedNote sharedNote = new SharedNote();
        sharedNote.setNote(note);
        sharedNote.setSharedWith(sharedWith);
        sharedNoteRepository.save(sharedNote);
    }
}
