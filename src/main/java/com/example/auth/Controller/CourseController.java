package com.example.auth.Controller;

import com.example.auth.Service.CourseService;
import com.example.auth.model.Entities.Course;
import com.example.auth.model.Request.CourseRequest;
import com.example.auth.model.Request.SearchRequest;
import com.example.auth.model.Response.CommonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import com.example.auth.model.Response.SuccessResponse;
import com.example.auth.utils.JwtUtil;
import com.example.auth.utils.constants.Operator;
import com.example.auth.utils.specification.SearchCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{size}/{page}/{sort}")
    public ResponseEntity getAllCourse(@PathVariable int size, @PathVariable int page, @PathVariable String sort){
        Pageable pageable = PageRequest.of(size, page, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page, Sort.by("id").descending());
        }
        Page<Course> courseList = courseService.list(pageable);
        CommonResponse successResponse = new SuccessResponse<>("sukses",courseList);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @GetMapping(params = {"key","value","operator"})
    public ResponseEntity getAllBy(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("operator") String operator) throws Exception{
        SearchCriteria searchCriteria= new SearchCriteria(key, Operator.valueOf(operator), value);
        List<Course> courseList = courseService.listby(searchCriteria);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponse<>("Success "+ key, courseList));
    }

//    @PostMapping(value = "/upload",
//    produces = "application/json")
//    public ResponseEntity<CommonResponse> createCourse(@Valid @RequestParam("course") String course, @RequestParam("file") MultipartFile file) throws Exception{
//        ObjectMapper objectMapper = new ObjectMapper();
//        CourseRequest courseRequest = objectMapper.readValue(course, CourseRequest.class);
//        Course result = courseService.create(courseRequest, file);
//        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success", result));
//    }

    @PostMapping(value = "/upload")
    public ResponseEntity createCourse(@Valid CourseRequest course) throws Exception{
        Course result = courseService.create(course);
        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse<>("success", result));
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        Resource file = courseService.download(id);
        byte[] fileShow = StreamUtils.copyToByteArray(file.getInputStream());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(fileShow);
    }

    @PostMapping("/addbulk")
    public ResponseEntity createBulk(@RequestBody List< @Valid CourseRequest> courseRequests){
        List<Course> creatBulk = courseService.createBulk(courseRequests);
        CommonResponse commonResponse = new SuccessResponse<>("Success", creatBulk);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/search/{page}/{size}/{sort}")
    public ResponseEntity searchByLevel(@RequestBody SearchRequest searchRequest, @PathVariable int size, @PathVariable int page, @PathVariable String sort){
        Pageable pageable = PageRequest.of(size, page, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page, Sort.by("id").descending());
        }
        Page<Course> coursePage = courseService.SearchByLevel(searchRequest.getLevel(),pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", coursePage);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        Course course = courseService.get(id);
        SuccessResponse<Course> successResponse = new SuccessResponse<>("sukses",course);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> update(@RequestBody CourseRequest course, @PathVariable Long id){
        courseService.update(course,id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success update course", course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEntity(@PathVariable Long id){
        Course course = courseService.get(id);
        courseService.delete(id);
        SuccessResponse successResponse =new SuccessResponse<>("sukses",course);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }


}
