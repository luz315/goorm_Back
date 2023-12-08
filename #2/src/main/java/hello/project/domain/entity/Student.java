package hello.project.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//implements Comparable 정렬하기
@Getter @Setter
@AllArgsConstructor
public class Student implements Comparable<Student>{ //option+enter - implement
    private String name;
    private int grade;

    //정렬 기준: 성적 오름차 순으로 정렬
    @Override
    public int compareTo(Student o) {
        return this.grade - o.getGrade();
        // 호출한 객체의 grade와 인자로 넘어온 o 객체의 grade를 둘이 비교
        // 둘이 빼서 0 나오면 같다는 얘기고
        // - 나오면 작다는 거, + 나오면 크다는거
    }


    /* @AllArgsConstructor 이랑 같은거야 아래코드가
    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

     */
}

