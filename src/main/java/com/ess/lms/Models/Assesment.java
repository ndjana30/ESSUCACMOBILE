package com.ess.lms.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assesment")
@Data
@NoArgsConstructor
public class Assesment
{
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "course_id",insertable = false,updatable = false)
    private Course course;
    private long course_id;
    @ManyToOne
    @JoinColumn(name = "student_id",insertable = false,updatable = false)
    private UserEntity student;
    private long student_id;
    private long mark;

    @JsonBackReference(value = "assesment-course")
    public Course getCourse()
    {
        return this.course;
    }

    @JsonBackReference(value = "assesment-student")
    public UserEntity getStudent()
    {
        return this.student;
    }
}
