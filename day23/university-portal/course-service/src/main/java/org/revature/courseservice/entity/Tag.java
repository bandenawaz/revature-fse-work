package org.revature.courseservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tagId;
    @Column(unique = true, nullable = false)
    private String tagName;

    /**
     * ManyToMany (inverse side)
     * mappedby = "tags" means Course.java OWNS the join table
     * This side is read-only in terms of join table management
     */
    @ManyToMany(
            mappedBy = "tags",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();
}
