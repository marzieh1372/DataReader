package com.example.datareader.controller;

import com.example.datareader.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/send-data")
    public void sendData(){
      log.info("SendData ......");
      studentService.processStudentsByFirstName("Ali");
    }

    @PostMapping("/send-data-stream")
    public void sendDataStream(){
        log.info("SendData ......");
        studentService.processStudentsByFirstNameUsingStreamsToKafka("Ali");
    }
}
