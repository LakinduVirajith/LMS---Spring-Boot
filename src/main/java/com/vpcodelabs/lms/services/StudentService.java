package com.vpcodelabs.lms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vpcodelabs.lms.entities.Student;

public interface StudentService {
    Student createNewStudent(Student student);

    Page<Student> getAllStudents(Pageable pageable);
    
    Student getStudentById(Long id);
    
    Student updateStudentById(Long id, Student updatedStudent);
    
    void deleteStudent(Long id);
}
