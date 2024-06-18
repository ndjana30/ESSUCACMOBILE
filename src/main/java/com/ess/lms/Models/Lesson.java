package com.ess.lms.Models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name="lesson")
@Entity
public class Lesson {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false,updatable = false)
    private Course course;
    private long course_id;
    /*
     * @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
    private List<Role> roles =  new ArrayList<>();
     */
    
    @ManyToMany
    @JoinTable(name = "lesson_files", joinColumns = @JoinColumn(name="lesson_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="file_id", referencedColumnName = "id"))
    private List<LessonFile> files = new ArrayList<>();

    @JsonBackReference(value = "course-lesson")
    public Course getCourse()
    {
        return this.course;
    }
    
    public List<LessonFile> getfiles()
    {
        return this.files;
    }
}
