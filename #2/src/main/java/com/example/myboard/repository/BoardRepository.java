package com.example.myboard.repository;

import com.example.myboard.entity.Board;
import com.example.myboard.entity.DeleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
// findAll은 이미 있어서 안붙여줘도 되는데 특정 기능 만들때에는 여기다 작성하기
    Page<Board> findAllByDeleteStatus(DeleteStatus deleteStatus, Pageable pageable);


/* 1.요청받는다
   2.요청받은 보드를 찾는다
   3.업데이트 해준다

   -> 여기서 할 일은 보드넘버에 해당하는 보드 객체를 조회한다
 */

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.comments c WHERE b.boardNo = :boardNo")
    Optional<Board> findBoardWithCommentByBoardNo(@Param("boardNo")Long boardNo);

    //Optional = null 처리에 활용하기 위해 사용
}
