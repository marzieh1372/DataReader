package com.example.datareader.repository;

import com.example.datareader.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * The Slice object allows us to process the first batch of Student entities
     * @param firstName
     * @param page
     * @return
     */
    Slice<Student> findAllByFirstName(String firstName, Pageable page);
    Page<Student> findAllByLastName(String firstName, Pageable page);
    Stream<Student> findAllByFirstName(String firstName);

}
