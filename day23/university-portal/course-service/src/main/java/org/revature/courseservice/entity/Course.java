package org.revature.courseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseid;

    @Column(nullable = false)
    private String courseTitle;

    @Column(length = 1000)
    private String courseDescription;

    private int courseCredits;
    private boolean isCourseActive;

    /**
     * REALTIONSHIP 1: @manyToOne
     * Many Course -> One Instructor
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "instructor_id",
            nullable = false
    )
    private Instructor instructor;

    /**
     * RELATIONSHIP 2: @OneToMany
     * One Course -> many Modules
     */
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("orderIndex ASC")
    private List<Module> modules = new ArrayList<>();

    /**
     * RELATIONSHIP 3: @ManyToMany
     * Many Courses -> Many tags
     */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_tags",
            joinColumns = @JoinColumn(name = "course_id"), //FK pointing to this table
            inverseJoinColumns = @JoinColumn(name = "tag_id") //FK pointing to tags table
    )
    private List<Tag> tags = new ArrayList<>();

    @ElementCollection  //Stores in a seperate course_enrolled_students table
    @CollectionTable(name = "course_enrolled_students",
    joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "student_id")
    private List<Long> enrolledStudentIds = new ArrayList<>();
}
