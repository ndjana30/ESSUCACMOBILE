package com.ess.lms.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ess.lms.Models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long>
{
    
}
