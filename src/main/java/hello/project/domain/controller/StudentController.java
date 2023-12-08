package hello.project.domain.controller;

import hello.project.domain.entity.ApiResponse;
import hello.project.domain.entity.ErrorCode;
import hello.project.domain.entity.Student;
import hello.project.domain.exception.CustomException;
import hello.project.domain.exception.InputRestriction;
import hello.project.domain.service.StudentService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    //final이면 생성자 조회

    @PostMapping("/student")
    public ApiResponse add(
            @RequestParam("name") String name,
            @RequestParam("grade") int grade
    ) {
        //6이상으로 요청하면 예외처리
        if (grade>=6){
            throw new CustomException(ErrorCode.BAD_REQUEST,"grade는 6 이상 입력할 수 없습니다.", new InputRestriction(6));
        }
        return makeResponse(studentService.add(name, grade));

    }

    // 전체성적조회
    @GetMapping ("/students")
    public ApiResponse findAll() {
        return makeResponse(studentService.findAll());
        //TODO
    }

    // 특정성적조회
    @GetMapping("/students/{grade}")
    public ApiResponse get(@PathVariable("grade") int grade){
        return  makeResponse(studentService.get(grade));
    }


    public <T> ApiResponse<T> makeResponse(List<T> result) {
        return new ApiResponse<>(result);
    }

    public <T> ApiResponse<T> makeResponse(T result) {
        return makeResponse(Collections.singletonList(result));
    }

    //예외처리
    @ExceptionHandler(CustomException.class)
    public ApiResponse customExceptionHandler(HttpServletResponse response, CustomException customException){
       // response.setStatus(customException.getErrorCode().getCode());
        return new ApiResponse(customException.getErrorCode().getCode(),
                customException.getErrorCode().getMessage(),
                customException.getData());
    }


}
