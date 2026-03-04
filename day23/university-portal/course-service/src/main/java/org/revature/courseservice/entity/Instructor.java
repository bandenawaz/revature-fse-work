package org.revature.courseservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorId;

    @Column(nullable = false)
    private String instructorName;

    @Column(nullable = false, unique = true)
    private String instructorEmail;

    @Column(nullable = false)
    private String instructorSpecialization;

    /**
     * @OneToMany Relationship
     * One Instructor --> Many Courses
     * mappedBy = "instructor" means: go look at the instructor
     * field inside Course.java - that field owns the FK in DB
     */
    @OneToMany(
            mappedBy = "instructor",
            cascade = CascadeType.ALL, // Cascade save/delete courses
            fetch = FetchType.LAZY    //Don't load courses unless asked
    )
    @JsonIgnore  //This prevents infinite recursion in JSON(Instructor->Course->Instructor)
    private List<Course> courses = new ArrayList<>();
}
