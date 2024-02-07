package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {

    private NotesMapper notesMapper;

    public  NoteService(NotesMapper notesMapper){
        this.notesMapper=notesMapper;
    }

    @PostConstruct
    public void postConstruct(){
        System.out.println("Creating NoteService bean");
    }

    public void addOrUpdateNote(Notes note) {
        if (note.getNotesId() == null) {
            notesMapper.addNote(note);
        }
        else {
            notesMapper.editNote(note);
        }
    }

    public List<Notes> getNotesByUserId(Integer userId) {
        return notesMapper.getAllNotes(userId);
    }

    public Notes getNoteById(Integer noteId) {
        return notesMapper.getNoteById(noteId);
    }


    public int editNote(Notes note){
        return notesMapper.editNote(note);
    }

    public int deleteNote(int noteId) {
        return notesMapper.deleteNote(noteId);
    }
}
