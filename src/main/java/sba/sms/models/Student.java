package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "student")

public class Student {
    @Id
    @NonNull
    @Column(length =50, nullable = false)
    String email;

    @NonNull
    @Column(length = 50, nullable = false)
    String name;

    @NonNull
    @Column(length = 50, nullable = false)
    String password;

    @NonNull
    @ToString.Exclude // Not sure if I need this one
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.ALL) //add orphanRemoval = true;
    @JoinTable(name ="student_courses", joinColumns = {@JoinColumn(name = "student_email")},inverseJoinColumns = {@JoinColumn(name = "courses_id")})
    List<Course> sCourses = new ArrayList<>();

    public void addsCourses(Course course) {
        sCourses.add(course);
        course.getStudents().add(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }
//    @Override
//    public int hashCode() {
//        return Objects.hash(email, name, password, sCourses);
//    }

    @Override
    public int hashCode() {

        return Objects.hash(email, name, password);
    }

    @Override
    public String toString() {
        return "Student{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
