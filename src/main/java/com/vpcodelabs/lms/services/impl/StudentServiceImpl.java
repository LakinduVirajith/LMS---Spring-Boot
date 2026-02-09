package com.vpcodelabs.lms.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vpcodelabs.lms.entities.Student;
import com.vpcodelabs.lms.exceptions.CustomException;
import com.vpcodelabs.lms.repositories.StudentRepository;
import com.vpcodelabs.lms.services.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public Student createNewStudent(Student student) {
        try {
            return studentRepository.save(student);
        } catch (Exception exception) {
            log.error("Error creating student: {}", exception.getMessage());
            throw new CustomException("Failed to create new student", HttpStatus.CONFLICT);
        }
        
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        try {
            log.debug("getting students");
            return studentRepository.findAll(pageable);
        } catch (Exception exception) {
            log.error("Failed to get all students", exception);
            throw new CustomException("Failed to get all students", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Student getStudentById(Long id) {
        try {
            Student student = studentRepository.findById(id).orElseThrow(
                () -> new CustomException("Student not found", HttpStatus.NOT_FOUND));
            log.info("Successfully fetched student {}", id);
            return student;
        } catch (CustomException exception) {
            log.warn("Student not found with id: {} to fetch", id, exception);
            throw new CustomException("Student Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error getting student", exception);
            throw new CustomException("Failed to get student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Student updateStudentById(Long id, Student updatedStudent) {
        try {
            Student student = studentRepository.findById(id).orElseThrow(
                () -> new CustomException("Student not found", HttpStatus.NOT_FOUND));
            modelMapper.map(updatedStudent, student);
            return studentRepository.save(student);
        } catch (CustomException exception) {
            log.warn("Student not found with id: {} to update", id, exception);
            throw new CustomException("Student Not found", HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error updating student", exception);
            throw new CustomException("Failed to update student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteStudent(Long id) {
         try {
            studentRepository.deleteById(id);
        } catch (Exception exception) {
            log.error("Failed to delete student with id {}", id, exception);
            throw new CustomException("Failed to delete student", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
