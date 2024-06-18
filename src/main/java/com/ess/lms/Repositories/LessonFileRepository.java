package com.ess.lms.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ess.lms.Models.LessonFile;

public interface LessonFileRepository extends JpaRepository<LessonFile,Long>{
    
}
