package com.example.auth.repo;

import com.example.auth.model.Entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    Page<Course> findByCourseInfo_LevelContainsIgnoreCase(String level, Pageable pageable);
    List<Course> findByTitleContainsIgnoreCase(String title);

    List<Course> findAll(Specification specification);

}
