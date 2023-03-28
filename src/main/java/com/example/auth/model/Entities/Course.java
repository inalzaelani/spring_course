package com.example.auth.model.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 150, unique = true)
    private String title;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Column(name = "link", nullable = false, length = 200)
    private String link;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "course_info_id", referencedColumnName = "id")
    private CourseInfo courseInfo;

    @ManyToOne
    @JoinColumn(name = "course_type_id", referencedColumnName = "id", nullable = false)
    private CourseType courseType;
}
