package sba.sms.dao;

import sba.sms.models.Course;
import sba.sms.models.Student;

import java.util.List;

public interface Studentl {
    List<Student> getAllStudents();
    Student createStudent(Student student);
    Student getStudentByEmail(String email);
    boolean validateStudent(String email, String password);
    void registerStudentToCourse(String email, int courseId);
    List<Course> getStudentCourses(String email);

}
