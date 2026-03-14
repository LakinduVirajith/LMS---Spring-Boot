package com.vpcodelabs.lms.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vpcodelabs.lms.entities.Mentor;
import com.vpcodelabs.lms.entities.Session;
import com.vpcodelabs.lms.entities.Student;
import com.vpcodelabs.lms.entities.Subject;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByStudentEmail(String studentEmail);

    boolean existsByStudentAndMentorAndSessionAtBetween(Student student, Mentor mentor, Date start, Date end);

    boolean existsByStudentAndSubjectAndSessionAtBetween(Student student, Subject subject, Date start, Date end);

    boolean existsByMentorAndSessionAtBetween( Mentor mentor, Date start, Date end);
}
