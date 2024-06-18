package com.ess.lms.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ess.lms.Models.Lesson;

@Repository
public interface LessonRepositories extends JpaRepository<Lesson,Long>
{

}
