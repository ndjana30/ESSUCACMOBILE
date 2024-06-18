package com.ess.lms.Models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name="course")
@Entity
@NoArgsConstructor
public class Course 
{
    @Id
    @GeneratedValue
    private long id;

    private String name;
    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;
    
    @ManyToMany
    @JoinTable(name = "course_students", joinColumns = @JoinColumn(name="course_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="student_id", referencedColumnName = "id"))
    private List<UserEntity> students = new ArrayList<>();
    
    // @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @ManyToMany
    @JoinTable(name = "course_lecturers", joinColumns = @JoinColumn(name="course_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="lecturer_id", referencedColumnName = "id"))
    private List<UserEntity> lecturers = new ArrayList<>();
    
    @JsonManagedReference(value = "course-lesson")
    public List<Lesson> getLessons()
    {
        return this.lessons;
    }

    @OneToMany(mappedBy = "course")
    public List<Assesment> assesments = new ArrayList<>();

    @JsonManagedReference(value = "assesment-course")
    public List<Assesment> getAssesments()
    {
        return this.assesments;
    }
    
}
