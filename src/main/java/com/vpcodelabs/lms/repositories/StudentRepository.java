package com.vpcodelabs.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpcodelabs.lms.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
}
