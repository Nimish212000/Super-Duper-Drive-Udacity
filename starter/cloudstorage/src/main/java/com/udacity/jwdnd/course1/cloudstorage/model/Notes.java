package com.udacity.jwdnd.course1.cloudstorage.model;

public class Notes {

    private Integer userId;
    private Integer noteId;
    private String noteTitle;
    private String noteDescription;

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Integer getNotesId() {return noteId;}

    public void setNoteId(Integer noteId) {this.noteId = noteId;}


    public String getNoteTitle(){return noteTitle;}

    public void setNoteTitle(String noteTitle) {this.noteTitle = noteTitle;}

    public String getNoteDescription() {return noteDescription;}
    public void setNoteDescription(String noteDescription) {this.noteDescription = noteDescription;}
}
