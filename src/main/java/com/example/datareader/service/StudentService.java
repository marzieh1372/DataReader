package com.example.datareader.service;

import com.example.datareader.model.Student;

import com.example.datareader.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class StudentService {


    private static final int BATCH_SIZE = 5 ;

    private final StudentRepository studentRepository;
    private final OtherService otherService;
    private final KafkaProducer kafkaProducer;
    @PersistenceContext
    private  EntityManager entityManager;

    public StudentService(StudentRepository studentRepository, OtherService otherService,KafkaProducer kafkaProducer) {
        this.studentRepository = studentRepository;
        this.otherService = otherService;
        this.kafkaProducer = kafkaProducer;
    }

    //using slice for read multipart and send each part to other service
    public void processStudentsByFirstName(String firstName) {
        log.info("ProcessStudentsByFirstName started ....");
        Slice<Student> slice = studentRepository.findAllByFirstName(firstName.toUpperCase(),
                PageRequest.of(0, BATCH_SIZE));
        List<Student> studentsInBatch = slice.getContent();
        studentsInBatch.forEach(otherService::foreignService);

        while(slice.hasNext()) {
            slice = studentRepository.findAllByFirstName(firstName, slice.nextPageable());
            slice.get().forEach(otherService::foreignService);
        }
    }


    //test page
    public void processStudentsByLastName(String lastName) {
        log.info("ProcessStudentsByLastName started ....");
        Page<Student> page = studentRepository.findAllByLastName(lastName, PageRequest.of(0, BATCH_SIZE));
        page.getContent()
                .forEach(otherService::foreignService);

        while (page.hasNext()) {
            page = studentRepository.findAllByLastName(lastName, page.nextPageable());
            page.getContent()
                    .forEach(otherService::foreignService);
        }
    }

    //test
    @Transactional(readOnly = true)
    public void processStudentsByFirstNameUsingStreams(String firstName) {
        log.info("ProcessStudentsByFirstNameUsingStreams started ....");
        try (Stream<Student> students = studentRepository.findAllByFirstName(firstName)) {
            students.peek(entityManager::detach)
                    .forEach(otherService::foreignService);
        }
    }

    //We  dont't have any limitation
    @Transactional(readOnly = true)
    public void processStudentsByFirstNameUsingStreamsToKafka(String firstName) {
        log.info("processStudentsByFirstNameUsingStreamsToKafka started ....");
        try (Stream<Student> students = studentRepository.findAllByFirstName(firstName)) {
            students.peek(entityManager::detach)
                    .forEach(kafkaProducer::sendMessage);
        }
    }
}
