package com.ess.lms.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.ess.lms.Models.Course;
import com.ess.lms.Models.Role;
import com.ess.lms.Models.UserEntity;
import com.ess.lms.Repositories.CourseRepository;
import com.ess.lms.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("api/v1/courses")
@CrossOrigin("*")
public class CourseController
{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("add")
    public Object courseCreate(@RequestParam("name") String name)
    {
        Course course = new Course();
        course.setName(name);
        courseRepository.save(course);
        return new ResponseEntity<>("Course with name "+course.getName()+" Created",HttpStatus.OK);
    }
    
    @GetMapping("all")
    public Object getAllCourses() 
    {
        return new ResponseEntity<>(courseRepository.findAll().parallelStream().collect(Collectors.toList()),HttpStatus.OK);
    }
    
    @PostMapping("{course_id}/lecturer/assign")
    public Object assignLecturers(@PathVariable long course_id,@RequestParam String name)
    {
        Optional<Course> course = courseRepository.findById(course_id);
        if(course.isPresent())
        {
            System.out.println("Course Present");
            Optional<UserEntity> lecturer = userRepository.findByUsername(name);
            if(lecturer.isPresent())
            {
                System.out.println("Lecturer Present");
                
             List<Role> lecturer_roles = lecturer.get().getRoles().stream().filter(obj->obj.getName().equals("LECTURER")).collect(Collectors.toList());
              System.out.println("roles found are " +lecturer_roles);

              if(lecturer_roles.isEmpty())
              {
                System.out.println("NO LECTURER ROLE FOUND SORRY");
                return new ResponseEntity<>(("YOU DO NOT HAVE PERMISSION TO BE A LECTURER"),HttpStatus.BAD_REQUEST);
              }
              else
              {
                List<UserEntity> smth = course.get().getLecturers();
                smth.add(lecturer.get());
                System.out.println("Finally trying to set the lecturer");

                course.get().setLecturers(smth);
                System.out.println("Lecturer set");

                courseRepository.save(course.get());
                return new ResponseEntity<>(("LECTURER ADDED TO COURSE"),HttpStatus.OK);
              }
            }
            else
            {
                return new ResponseEntity<>(("LECTURER DOES NOT EXIST"),HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(("Course Does Not Exist"),HttpStatus.BAD_REQUEST);
    }


    @PostMapping("{course_id}/student/assign")
    public Object assignStudent(@PathVariable long course_id,@RequestParam String name)
    {
        Optional<Course> course = courseRepository.findById(course_id);
        if(course.isPresent())
        {
            System.out.println("Course Present");
            Optional<UserEntity> student = userRepository.findByUsername(name);
            if(student.isPresent())
            {
                System.out.println("LStudent Present");
                
             List<Role> student_roles = student.get().getRoles().stream().filter(obj->obj.getName().equals("STUDENT")).collect(Collectors.toList());
              System.out.println("roles found are " +student_roles);

              if(student_roles.isEmpty())
              {
                System.out.println("NO Student ROLE FOUND SORRY");
                return new ResponseEntity<>(("YOU DO NOT HAVE PERMISSION TO BE A Student"),HttpStatus.BAD_REQUEST);
              }
              else
              {
                List<UserEntity> smth = course.get().getStudents();
                smth.add(student.get());
                System.out.println("students are: "+ smth);
                System.out.println("Finally trying to set the student");

                course.get().setStudents(smth);
                System.out.println("Student set");

                courseRepository.save(course.get());
                return new ResponseEntity<>(("Student ADDED TO COURSE"),HttpStatus.OK);
              }
            }
            else
            {
                return new ResponseEntity<>(("Student DOES NOT EXIST"),HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(("Course Does Not Exist"),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("{course_id}/lecturers/all")
    public Object getCourseLecturers(@PathVariable long course_id)
    {
        Optional<Course> course = courseRepository.findById(course_id);
        if(course.isPresent())
        {
            return new ResponseEntity<>((course.get().getLecturers().stream().collect(Collectors.toList())),HttpStatus.OK);
        }
        return new ResponseEntity<>(("Course Does Not Exist"),HttpStatus.NOT_FOUND);
    }

    @GetMapping("{course_id}/lessons/all")
    public Object getCourseLessons(@PathVariable long course_id)
    {
        Optional<Course> course = courseRepository.findById(course_id);
        if(course.isPresent())
        {
            return new ResponseEntity<>((course.get().getLessons()),HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("{course_id}/students/all")
    public Object getCourseStudents(@PathVariable long course_id)
    {
        Optional<Course> course = courseRepository.findById(course_id);
        if(course.isPresent())
        {
            return new ResponseEntity<>((course.get().getStudents()),HttpStatus.OK);
        }
        return null;
    }

}