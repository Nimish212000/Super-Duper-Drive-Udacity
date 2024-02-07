package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {

        @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
        List<Notes> getAllNotes(int userid);

        @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
        Notes getNoteById(int noteId);

        @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
        @Options(useGeneratedKeys = true, keyProperty = "noteId")
        int addNote(Notes notes);

        @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
        int editNote(Notes notes);

        @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
        int deleteNote(int noteId);
}
