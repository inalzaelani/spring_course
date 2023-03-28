package com.example.auth.Controller;

import com.example.auth.Service.CourseTypeService;
import com.example.auth.model.Entities.Course;
import com.example.auth.model.Entities.CourseType;
import com.example.auth.model.Request.CourseRequest;
import com.example.auth.model.Request.CourseTypeRequest;
import com.example.auth.model.Response.CommonResponse;
import com.example.auth.model.Response.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/course-type")
public class CourseTypeController {
    @Autowired
    private CourseTypeService courseTypeService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{size}/{page}/{sort}")
    ResponseEntity findAll(@PathVariable("size") int size, @PathVariable("page") int page, @PathVariable String sort){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(size, page, Sort.by("name").descending());
        }

        Page<CourseType> courseTypes = courseTypeService.findAll(pageable);
        CommonResponse commonResponse = new SuccessResponse<>("Success", courseTypes);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping
    ResponseEntity createCourseType(@RequestBody CourseTypeRequest courseTypeRequest){
        CourseType courseType = modelMapper.map(courseTypeRequest, CourseType.class);
        CourseType result = courseTypeService.create(courseType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success", result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> update(@RequestBody CourseTypeRequest courseTypeRequest, @PathVariable Long id){
        CourseType courseType = modelMapper.map(courseTypeRequest, CourseType.class);
        CourseType resault = courseTypeService.update(id,courseType);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success update course", resault));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteEntity(@PathVariable Long id){
        CourseType courseType = courseTypeService.get(id);
        courseTypeService.delete(id);
        SuccessResponse successResponse =new SuccessResponse<>("sukses",courseType);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }
}
