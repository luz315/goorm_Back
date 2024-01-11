package com.example.myboard.repository;

import com.example.myboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

/*  설계는 구현하기 나름이지만
    댓글 등록요청 할 때 보드넘버랑 댓글 내용을 받으니까 보드넘버에 해당하는 보드 객체를 확인하고
    코멘트랑 연관관계 걸어놨으니까 양방향 업데이트를 해준다
    즉 보드리포지토리에서만 작성하고 코멘트 리포지토리는 비워둔다*/

}
