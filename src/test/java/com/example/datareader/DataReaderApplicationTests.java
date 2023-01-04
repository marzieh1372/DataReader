package com.example.datareader;

import com.example.datareader.model.Student;
import com.example.datareader.repository.StudentRepository;
import com.example.datareader.service.OtherService;
import com.example.datareader.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(classes = DataReaderApplication.class)
class DataReaderApplicationTests {


    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentService studentService;

    @MockBean
    private OtherService mockEmailService;

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }

    @Test
    void givenTwelveRowsMatchingCriteria_whenRetrievingDataSliceBySlice_allDataIsProcessed() {
        saveStudents(12);

        studentService.processStudentsByFirstName("test");

        verify(mockEmailService, times(12)).foreignService(any());
    }

    @Test
    void givenTwelveRowsMatchingCriteria_whenRetrievingDataPageByPage_allDataIsProcessed() {
        saveStudents(12);

        studentService.processStudentsByLastName("test2");

        verify(mockEmailService, times(12)).foreignService(any());
    }

    @Test
    void processStudentsByFirstNameUsingStreams() {
        saveStudents(12);

        studentService.processStudentsByFirstNameUsingStreams("test");

        verify(mockEmailService, times(12)).foreignService(any());
    }

    private void saveStudents(int count) {
        List<Student> students = IntStream.range(0, count)
                .boxed()
                .map(i -> new Student("test", "test2"))
                .collect(Collectors.toList());
        repository.saveAll(students);
    }


}
