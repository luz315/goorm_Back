package hello.project.domain.service;

import hello.project.domain.entity.Student;
import hello.project.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    //Repository를 주입받아야한다
    //1) 생성자 주입
    //2) 빌드 주입
    //3) 세터 주입
    private final StudentRepository studentRepository;
    //final이면 무조건 생성자 해야돼 -> @RequiredArgsConstructor
    //의존성 주입 만족

    //이름 입력받아 저장
    public Student add(String name, int grade){
        Student student = new Student(name, grade);
        studentRepository.add(student);
        return student;
    }

    //전체 성적 조회
    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    //특정 성적 조회
    public List<Student> get(int grade){
        return studentRepository.get(grade);
    }

}
