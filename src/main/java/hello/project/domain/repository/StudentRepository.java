package hello.project.domain.repository;

import hello.project.domain.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class StudentRepository {
    Set<Student> students = new HashSet<>();

    public void add(Student student){
        students.add(student);
    }

    //전체 성적 오름차순조회

    public List<Student> findAll() {
        return students.stream()
                .sorted() //이미 Student 클래스에서 구현해서 얘만 넣어줌 된다
                .collect(Collectors.toList());
    }

    //특정 성적을 입력 받아 해당 학생 조회
    public List<Student> get(int grade){
        return students.stream()
                .filter(student -> student.getGrade()==grade)
                .collect(Collectors.toList());
    }

}
