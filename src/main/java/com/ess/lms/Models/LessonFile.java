package com.ess.lms.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
@Table(name = "lessonFile")
@Entity
@Data
@NoArgsConstructor
public class LessonFile 
{
    @Id
    @GeneratedValue
    private long id;
    @Lob
    private byte[] file;
    private String fileName;
    private String fileExtension;
}
