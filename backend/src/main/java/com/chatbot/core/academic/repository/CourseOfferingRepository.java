package com.chatbot.core.academic.repository;

import com.chatbot.core.academic.model.CourseOffering;
import com.chatbot.core.academic.model.Course;
import com.chatbot.core.academic.model.Semester;
import com.chatbot.core.academic.model.Teacher;
import com.chatbot.core.academic.enums.CourseOfferingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Course Offering Repository
 */
@Repository
public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    
    List<CourseOffering> findByCourseId(Long courseId);
    
    List<CourseOffering> findByCourse(Course course);
    
    List<CourseOffering> findBySemesterId(Long semesterId);
    
    List<CourseOffering> findBySemester(Semester semester);
    
    List<CourseOffering> findByTeacherId(Long teacherId);
    
    List<CourseOffering> findByTeacher(Teacher teacher);
    
    List<CourseOffering> findByStatus(CourseOfferingStatus status);
    
    List<CourseOffering> findByCourseIdAndSemesterId(Long courseId, Long semesterId);
    
    List<CourseOffering> findByCourseAndSemester(Course course, Semester semester);
    
    List<CourseOffering> findByTeacherIdAndSemesterId(Long teacherId, Long semesterId);
    
    List<CourseOffering> findByTeacherAndSemester(Teacher teacher, Semester semester);
    
    List<CourseOffering> findByClassroom(String classroom);
    
    List<CourseOffering> findByIsActiveTrue();
    
    List<CourseOffering> findByCourseIdAndSemesterIdAndIsActiveTrue(Long courseId, Long semesterId);
    
    List<CourseOffering> findBySemesterIdAndIsActiveTrue(Long semesterId);
    
    List<CourseOffering> findByTeacherIdAndSemesterIdAndIsActiveTrue(Long teacherId, Long semesterId);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.course.code = :courseCode AND co.semester.code = :semesterCode")
    List<CourseOffering> findByCourseCodeAndSemesterCode(@Param("courseCode") String courseCode, @Param("semesterCode") String semesterCode);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.currentStudents < co.maxStudents AND co.isActive = true")
    List<CourseOffering> findAvailableCourseOfferings();
    
    @Query("SELECT co FROM CourseOffering co WHERE co.status = :status AND co.isActive = true")
    List<CourseOffering> findByStatusAndActive(@Param("status") CourseOfferingStatus status);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.semester.id = :semesterId AND co.status = :status AND co.isActive = true")
    List<CourseOffering> findBySemesterIdAndStatusAndActive(@Param("semesterId") Long semesterId, @Param("status") CourseOfferingStatus status);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.teacher.id = :teacherId AND co.semester.id = :semesterId AND co.isActive = true")
    List<CourseOffering> findByTeacherAndSemesterAndActive(@Param("teacherId") Long teacherId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.classroom = :classroom AND co.semester.id = :semesterId AND co.isActive = true")
    List<CourseOffering> findByClassroomAndSemesterAndActive(@Param("classroom") String classroom, @Param("semesterId") Long semesterId);
    
    @Query("SELECT COUNT(co) FROM CourseOffering co WHERE co.course.id = :courseId AND co.semester.id = :semesterId AND co.isActive = true")
    long countActiveOfferingsByCourseAndSemester(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT COUNT(co) FROM CourseOffering co WHERE co.teacher.id = :teacherId AND co.semester.id = :semesterId AND co.isActive = true")
    long countActiveOfferingsByTeacherAndSemester(@Param("teacherId") Long teacherId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT SUM(co.currentStudents) FROM CourseOffering co WHERE co.course.id = :courseId AND co.semester.id = :semesterId AND co.isActive = true")
    Integer getTotalStudentsByCourseAndSemester(@Param("courseId") Long courseId, @Param("semesterId") Long semesterId);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.semester.id = :semesterId AND co.isActive = true ORDER BY co.course.code")
    List<CourseOffering> findActiveOfferingsBySemesterOrderByCourse(@Param("semesterId") Long semesterId);
    
    @Query("SELECT co FROM CourseOffering co WHERE co.teacher.id = :teacherId AND co.semester.id = :semesterId AND co.isActive = true ORDER BY co.course.code")
    List<CourseOffering> findActiveOfferingsByTeacherAndSemesterOrderByCourse(@Param("teacherId") Long teacherId, @Param("semesterId") Long semesterId);
    
    boolean existsByCourseIdAndSemesterIdAndTeacherId(Long courseId, Long semesterId, Long teacherId);
    
    long countByCourseId(Long courseId);
    
    long countBySemesterId(Long semesterId);
    
    long countByTeacherId(Long teacherId);
    
    long countByStatus(CourseOfferingStatus status);
    
    long countByIsActiveTrue();
}
