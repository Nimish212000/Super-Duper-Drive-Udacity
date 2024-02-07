package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.*;

import java.io.File;
import java.util.List;

@Mapper
public interface FileMapper {



        @Select("SELECT * FROM FILES WHERE userid = #{userid}")
        List<com.udacity.jwdnd.course1.cloudstorage.model.File> getAllFiles(int userId);

        @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
        com.udacity.jwdnd.course1.cloudstorage.model.File getFileById(int fileId);

        @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
        com.udacity.jwdnd.course1.cloudstorage.model.File getFileByNameAndUser(String filename, int userid);

        @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
                "VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
        @Options(useGeneratedKeys = true, keyProperty = "fileId")
        int insertFile(com.udacity.jwdnd.course1.cloudstorage.model.File file);

        @Update("UPDATE FILES SET filename = #{filename}, contenttype = #{contenttype}, " +
                "filesize = #{filesize}, filedata = #{filedata} WHERE fileId = #{fileId}")
        int updateFile(File file);

        @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
        int deleteFile(int fileId);
    }


