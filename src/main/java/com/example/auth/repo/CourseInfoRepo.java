package com.example.auth.repo;

import com.example.auth.model.Entities.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInfoRepo extends JpaRepository<CourseInfo, Long> {
}
