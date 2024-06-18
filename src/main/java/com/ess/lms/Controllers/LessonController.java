package com.ess.lms.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ess.lms.Models.Lesson;
import com.ess.lms.Models.LessonFile;
import com.ess.lms.Repositories.LessonFileRepository;
import com.ess.lms.Repositories.LessonRepositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/v1/lessons")
@CrossOrigin("*")
public class LessonController {

    @Autowired
    LessonRepositories lessonRepository;
    @Autowired
    LessonFileRepository lessonFileRepository;
    @PostMapping("add")
    public Object addLesosn(
        @RequestParam("name") String name,
        @RequestParam("course_id") long course_id,
        @RequestParam("files") List<MultipartFile> file)
        {

        Lesson lesson = new Lesson();
        lesson.setCourse_id(course_id);
        lesson.setName(name);
        List<LessonFile> lessonfiles = 
         file.stream().map(obj->
         {
            LessonFile lessonFile = new LessonFile();
            lessonFile.setFileName(obj.getOriginalFilename());
            try {
                lessonFile.setFile(obj.getBytes());
            } catch (IOException e) 
            {
                e.printStackTrace();
            }
            lessonFile.setFileExtension(obj.getContentType());

            lessonFileRepository.save(lessonFile);    
            lesson.setFiles(List.of(lessonFile));
            // lessonfiles.add(lessonFile);
            
            return lessonFile;
            // lessonFiles.add(lessonFile);
        }).collect(Collectors.toList());
            
        lessonRepository.save(lesson);
        
       return new ResponseEntity<>("Lesson Created",HttpStatus.OK);
        }

        @GetMapping("{id}/files")
        public Object getLessonFiles(@PathVariable long id) {
            Optional<Lesson> lesson = lessonRepository.findById(id);
            if(lesson.isPresent())
            {
                return new ResponseEntity<>(lesson.get().getfiles(),HttpStatus.OK);
            }
            return new ResponseEntity<>("LESSON NOT FOUND",HttpStatus.NOT_FOUND);
        }
        
    
}
