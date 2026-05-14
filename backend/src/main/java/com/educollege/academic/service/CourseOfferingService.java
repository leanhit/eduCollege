package com.educollege.academic.service;

import com.educollege.academic.model.Course;
import com.educollege.academic.model.CourseOffering;
import com.educollege.academic.model.Semester;
import com.educollege.academic.repository.CourseOfferingRepository;
import com.educollege.academic.repository.CourseRepository;
import com.educollege.academic.repository.SemesterRepository;
import com.educollege.user.model.Teacher;
import com.educollege.user.repository.TeacherRepository;
import com.educollege.core.enums.CourseOfferingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Course Offering Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseOfferingService {
    
    private final CourseOfferingRepository courseOfferingRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final TeacherRepository teacherRepository;
    
    public CourseOffering createCourseOffering(CourseOffering courseOffering) {
        System.out.println("Creating course offering");
        
        Course course = courseRepository.findById(courseOffering.getCourse().getId())
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseOffering.getCourse().getId()));
        
        Semester semester = semesterRepository.findById(courseOffering.getSemester().getId())
            .orElseThrow(() -> new RuntimeException("Semester not found with id: " + courseOffering.getSemester().getId()));
        
        if (courseOffering.getTeacher() != null) {
            Teacher teacher = teacherRepository.findById(courseOffering.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + courseOffering.getTeacher().getId()));
            courseOffering.setTeacher(teacher);
        }
        
        courseOffering.setCourse(course);
        courseOffering.setSemester(semester);
        courseOffering.setStatus(CourseOfferingStatus.SCHEDULED);
        
        CourseOffering savedOffering = courseOfferingRepository.save(courseOffering);
        System.out.println("Course offering created successfully");
        return savedOffering;
    }
    
    public CourseOffering updateCourseOffering(Long id, CourseOffering courseOffering) {
        System.out.println("Updating course offering with id: " + id);
        
        CourseOffering existingOffering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        if (courseOffering.getCourse() != null && 
            !existingOffering.getCourse().getId().equals(courseOffering.getCourse().getId())) {
            Course course = courseRepository.findById(courseOffering.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseOffering.getCourse().getId()));
            existingOffering.setCourse(course);
        }
        
        if (courseOffering.getSemester() != null && 
            !existingOffering.getSemester().getId().equals(courseOffering.getSemester().getId())) {
            Semester semester = semesterRepository.findById(courseOffering.getSemester().getId())
                .orElseThrow(() -> new RuntimeException("Semester not found with id: " + courseOffering.getSemester().getId()));
            existingOffering.setSemester(semester);
        }
        
        if (courseOffering.getTeacher() != null && 
            (existingOffering.getTeacher() == null || 
             !existingOffering.getTeacher().getId().equals(courseOffering.getTeacher().getId()))) {
            Teacher teacher = teacherRepository.findById(courseOffering.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + courseOffering.getTeacher().getId()));
            existingOffering.setTeacher(teacher);
        }
        
        existingOffering.setMaxStudents(courseOffering.getMaxStudents());
        existingOffering.setCurrentStudents(courseOffering.getCurrentStudents());
        existingOffering.setSchedule(courseOffering.getSchedule());
        existingOffering.setClassroom(courseOffering.getClassroom());
        existingOffering.setStartDate(courseOffering.getStartDate());
        existingOffering.setEndDate(courseOffering.getEndDate());
        existingOffering.setExamDate(courseOffering.getExamDate());
        existingOffering.setExamRoom(courseOffering.getExamRoom());
        existingOffering.setStatus(courseOffering.getStatus());
        existingOffering.setNotes(courseOffering.getNotes());
        
        CourseOffering updatedOffering = courseOfferingRepository.save(existingOffering);
        System.out.println("Course offering updated successfully");
        return updatedOffering;
    }
    
    public void deleteCourseOffering(Long id) {
        System.out.println("Deleting course offering with id: " + id);
        
        CourseOffering offering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        courseOfferingRepository.delete(offering);
        System.out.println("Course offering deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Optional<CourseOffering> getCourseOfferingById(Long id) {
        return courseOfferingRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getAllCourseOfferings() {
        return courseOfferingRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsByCourseId(Long courseId) {
        return courseOfferingRepository.findByCourseId(courseId);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsBySemesterId(Long semesterId) {
        return courseOfferingRepository.findBySemesterId(semesterId);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsByTeacherId(Long teacherId) {
        return courseOfferingRepository.findByTeacherId(teacherId);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsByCourseIdAndSemesterId(Long courseId, Long semesterId) {
        return courseOfferingRepository.findByCourseIdAndSemesterId(courseId, semesterId);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsByStatus(CourseOfferingStatus status) {
        return courseOfferingRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getOpenCourseOfferings() {
        return courseOfferingRepository.findByStatus(CourseOfferingStatus.OPEN);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsBySemesterIdAndStatus(Long semesterId, CourseOfferingStatus status) {
        return courseOfferingRepository.findBySemesterIdAndStatusAndActive(semesterId, status);
    }
    
    @Transactional(readOnly = true)
    public List<CourseOffering> getCourseOfferingsByTeacherIdAndSemesterId(Long teacherId, Long semesterId) {
        return courseOfferingRepository.findByTeacherIdAndSemesterId(teacherId, semesterId);
    }
    
    public CourseOffering openCourseOffering(Long id) {
        System.out.println("Opening course offering with id: " + id);
        
        CourseOffering offering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        offering.setStatus(CourseOfferingStatus.OPEN);
        CourseOffering openedOffering = courseOfferingRepository.save(offering);
        System.out.println("Course offering opened successfully");
        return openedOffering;
    }
    
    public CourseOffering closeCourseOffering(Long id) {
        System.out.println("Closing course offering with id: " + id);
        
        CourseOffering offering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        offering.setStatus(CourseOfferingStatus.CLOSED);
        CourseOffering closedOffering = courseOfferingRepository.save(offering);
        System.out.println("Course offering closed successfully");
        return closedOffering;
    }
    
    public CourseOffering cancelCourseOffering(Long id) {
        System.out.println("Cancelling course offering with id: " + id);
        
        CourseOffering offering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        offering.setStatus(CourseOfferingStatus.CANCELLED);
        CourseOffering cancelledOffering = courseOfferingRepository.save(offering);
        System.out.println("Course offering cancelled successfully");
        return cancelledOffering;
    }
    
    public CourseOffering completeCourseOffering(Long id) {
        System.out.println("Completing course offering with id: " + id);
        
        CourseOffering offering = courseOfferingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course offering not found with id: " + id));
        
        offering.setStatus(CourseOfferingStatus.COMPLETED);
        CourseOffering completedOffering = courseOfferingRepository.save(offering);
        System.out.println("Course offering completed successfully");
        return completedOffering;
    }
    
    @Transactional(readOnly = true)
    public long countCourseOfferingsBySemesterId(Long semesterId) {
        return courseOfferingRepository.countBySemesterId(semesterId);
    }
    
    @Transactional(readOnly = true)
    public long countCourseOfferingsByTeacherId(Long teacherId) {
        return courseOfferingRepository.countByTeacherId(teacherId);
    }
    
    @Transactional(readOnly = true)
    public long countByStatus(CourseOfferingStatus status) {
        return courseOfferingRepository.countByStatus(status);
    }
}
