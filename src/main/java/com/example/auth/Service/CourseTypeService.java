package com.example.auth.Service;

import com.example.auth.Exception.NotFoundException;
import com.example.auth.model.Entities.Course;
import com.example.auth.model.Entities.CourseType;
import com.example.auth.model.Request.CourseTypeRequest;
import com.example.auth.repo.CourseTypeRepo;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTypeService {
    @Autowired
    private CourseTypeRepo courseTypeRepo;

    public Page<CourseType> findAll(Pageable pageable){
        try {
            Page<CourseType> courseTypes = courseTypeRepo.findAll(pageable);
            if (courseTypes.isEmpty()){
                throw new NotFoundException("Empty Database");
            }
            return courseTypes;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public CourseType create(CourseType courseType){
        try {
            CourseType newCourseType= courseTypeRepo.save(courseType);
            return newCourseType;
        }catch (DataIntegrityViolationException e){
            throw new EntityExistsException();
        }
    }

    public List<CourseType> createBulk(List<CourseType> courseTypes){
        try {
            return courseTypeRepo.saveAll(courseTypes);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public CourseType get(Long id) {
        try {
            Optional<CourseType> byId = courseTypeRepo.findById(id);
            if (byId.isEmpty()) {
                throw new NotFoundException("Course not found");
            }
            return byId.get();
        }catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public CourseType update(Long id, CourseType courseType){
        try {
            Optional<CourseType> existingCourseType = courseTypeRepo.findById(id);
            if(existingCourseType.isPresent()){
                CourseType updatedCourseType = existingCourseType.get();
                updatedCourseType.setType(courseType.getType());
                return courseTypeRepo.save(updatedCourseType);
            } else {
                throw new NotFoundException("Course type with id " + id + " not found.");
            }
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id){
        try {
            Optional<CourseType> existingCourseType = courseTypeRepo.findById(id);
            if(existingCourseType.isPresent()){
                courseTypeRepo.deleteById(id);
            } else {
                throw new NotFoundException("Course type with id " + id + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
