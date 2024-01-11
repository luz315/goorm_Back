package com.example.myboard.service;

import com.example.myboard.entity.Board;
import com.example.myboard.entity.Comment;
import com.example.myboard.entity.DeleteStatus;
import com.example.myboard.repository.BoardRepository;
import com.example.myboard.repository.CommentRepository;
import com.example.myboard.request.CommentPostRequest;
import com.example.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;

    //댓글 등록
    @Transactional
    public BoardResponse writeComment(CommentPostRequest request){
        Optional<Board> boardOptional = boardRepository.findBoardWithCommentByBoardNo(request.getBoardNo());

        Board board = boardOptional.orElseThrow(()-> new RuntimeException("존재하지 않는 게시글입니다"));

        // 새로운 댓글을 생성하고 데이터를 설정한다
        Comment comment = new Comment();
        comment.setBody(request.getCommentBody());
        comment.setDeleteStatus(DeleteStatus.ACTIVE);

        // 댓글과 게시글을 연결한다
        comment.addboard(board);

        // 댓글을 저장한다
       boardRepository.save(board);

        // 게시글 정보를 반환한다 (이 부분은 프로젝트의 구조에 따라 다를 수 있음)
        return BoardResponse.from(board);

    }

}
