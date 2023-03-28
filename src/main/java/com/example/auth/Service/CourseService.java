package com.example.auth.Service;

import com.example.auth.Exception.NotFoundException;
import com.example.auth.model.Entities.Course;
import com.example.auth.model.Entities.CourseInfo;
import com.example.auth.model.Entities.CourseType;
import com.example.auth.model.Request.CourseRequest;
import com.example.auth.repo.CourseInfoRepo;
import com.example.auth.repo.CourseRepo;
import com.example.auth.repo.CourseTypeRepo;
import com.example.auth.utils.specification.SearchCriteria;
import com.example.auth.utils.specification.Spec;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseInfoRepo courseInfoRepo;
    @Autowired
    private CourseTypeRepo courseTypeRepo;
    @Autowired
    private FileService fileService;


    public Page<Course> list(Pageable pageable) {
        try {
            List<Course> courseList = courseRepo.findAll();
            if (courseList.isEmpty()){
                throw new NotFoundException("Database Empty");
            }
            Page<Course> all = courseRepo.findAll(pageable);
            if (all.isEmpty()){
                throw new NotFoundException("Wrong page size");
            }
            return all;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public Course create(CourseRequest courseRequest) {
        try {
            String filePath = "";
            Optional<CourseType> courseType = courseTypeRepo.findById(courseRequest.getCourseType());
            if (courseType.isEmpty()){
                throw new NotFoundException("Course Type Not Found");
            }

            if (!courseRequest.getFile().isEmpty()){
                filePath = fileService.uploadFile(courseRequest.getFile());
            }
            CourseInfo courseInfo = modelMapper.map(courseRequest, CourseInfo.class);
            CourseInfo courseInfoRequest = courseInfoRepo.save(courseInfo);
            Course course = modelMapper.map(courseRequest, Course.class);
            course.setLink(filePath);
            course.setCourseInfo(courseInfoRequest);
            course.setCourseType(courseType.get());
            return courseRepo.save(course);
        }catch (DataIntegrityViolationException e){
            throw new EntityExistsException();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Resource download(Long id){
        try {
            Course course = courseRepo.findById(id).orElseThrow(() -> new NotFoundException("Course Not Found"));
            String fileName = course.getLink();
            return fileService.download(fileName);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Course> createBulk(List<CourseRequest> courseRequests) {
        try {
            List<Course> courses = new ArrayList<>();
            for (CourseRequest courseRequest : courseRequests) {
                CourseInfo courseInfo = modelMapper.map(courseRequest, CourseInfo.class);
                CourseInfo courseInfoRequest = courseInfoRepo.save(courseInfo);

                Course course = modelMapper.map(courseRequest, Course.class);
                course.setCourseInfo(courseInfoRequest);
                courses.add(course);
            }
            return courseRepo.saveAll(courses);
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistsException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Course get(Long id) {
        try {
            Optional<Course> course = courseRepo.findById(id);
            if (course.isEmpty()) {
                throw new NotFoundException("Course not found");
            }
            return course.get();
        }catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Course> findByTitle(String value){
        try {
            List<Course> courseList = courseRepo.findByTitleContainsIgnoreCase(value);
            if (courseList.isEmpty()){
                throw new NotFoundException("Course with " + value + " title not found");
            }
            return courseList;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Page<Course> SearchByLevel(String value, Pageable pageable){
        try {
            Page<Course> courseList = courseRepo.findByCourseInfo_LevelContainsIgnoreCase(value, pageable);
            if (courseList.isEmpty()){
                throw new NotFoundException("Course with " + value + " level not found");
            }
            return courseList;
        }catch (NotFoundException e){
            throw e;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Course> listby(SearchCriteria searchCriteria){
        Specification specification= new Spec<Course>().findBy(searchCriteria);
        List<Course> courseList = courseRepo.findAll(specification);
        return courseList;
    }

    public void update(CourseRequest courseRequest, Long id) {
        try {
            Course existingCourse = get(id);
            existingCourse.setTitle(courseRequest.getTitle());
            existingCourse.setDescription(courseRequest.getDescription());

            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setDuration(courseRequest.getDuration());
            courseInfo.setLevel(courseRequest.getLevel());
            existingCourse.setCourseInfo(courseInfo);

            courseRepo.save(existingCourse);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            Optional<Course> course1 = courseRepo.findById(id);
            if (course1.isEmpty()){
                throw new NotFoundException("Tidak ada course dengan id" + id);
            }
            courseRepo.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
