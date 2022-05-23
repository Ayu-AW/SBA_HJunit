package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.Studentl;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class StudentService implements Studentl {

    @Override
    public List<Student> getAllStudents() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Student> student =s.createQuery("from Student",Student.class).getResultList();
        s.close();
        return student;
    }

    @Override
    public Student createStudent(Student student) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = s.beginTransaction();
            s.persist(student);
            tx.commit();
        } catch (HibernateException exception) {
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return student;
    }

    @Override
    public Student getStudentByEmail(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Student student = s.get(Student.class,email);
            if(student == null)
                throw new HibernateException("Did not find student");
            else
                return student;
        } catch (HibernateException exception) {
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return new Student();
    }
    @Override
    public boolean validateStudent(String email, String password) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Student student = null;
        try {
            student = s.get(Student.class, email);
            if (student == null) {
                System.out.println("Student not found!");
                return true;
            }
            return student.getPassword().equals(password);
        } catch (HibernateException exception) {
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Student student = s.get(Student.class, email);
            Course c = s.get(Course.class, courseId);
            c.addStudents(student);
            s.merge(c);
            tx.commit();
        } catch (HibernateException exception) {
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        } finally {
            s.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Course> courseList = null;
        try {
            tx= s.beginTransaction();
            NativeQuery q = s.createNativeQuery("SELECT c.id, c.name, c.instructor FROM Course as c JOIN student_courses as sc ON sc.courses_id = c.id JOIN Student as s ON s.email = sc.student_email WHERE s.email = :email",Course.class);
            q.setParameter("email", email);
            courseList  = q.getResultList();
            tx.commit();
        } catch (HibernateException exception) {
            if(tx!=null) tx.rollback();
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return courseList;
    }

}
