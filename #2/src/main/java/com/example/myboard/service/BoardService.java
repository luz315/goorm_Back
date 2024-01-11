package com.example.myboard.service;

import com.example.myboard.entity.Board;
import com.example.myboard.entity.DeleteStatus;
import com.example.myboard.repository.BoardRepository;
import com.example.myboard.request.BoardDeleteRequest;
import com.example.myboard.request.BoardPostRequest;
import com.example.myboard.response.BoardListResponse;
import com.example.myboard.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 등록
    @Transactional // 얘는 습관이야
    public BoardResponse writeBoard(BoardPostRequest request){
        Board board = new Board();
        board.setTitile(request.getTitle());
        board.setBody(request.getBody());
        board.setDeleteStatus(DeleteStatus.ACTIVE);
        return BoardResponse.from(boardRepository.save(board));
    }

    // 게시글 목록 조회
    @Transactional
    public List<BoardListResponse> searchBoardList(int page, int pageSize) {
        // jpa의 자동으로 만들어진 crud 기능 사용
        return boardRepository.findAllByDeleteStatus(
                DeleteStatus.ACTIVE,
                PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "boardNo"))
        ).map(BoardListResponse::from)
                .toList();

    }

    //게시글 단건 조회(게시글 + 댓글)
    public BoardResponse searchBoard(Long boardNo){
        return boardRepository.findBoardWithCommentByBoardNo(boardNo)
                .map(BoardResponse::from)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 게시글입니다"));

    }

    //게시글 삭제
    @Transactional
    public String deleteBoard(BoardDeleteRequest request) {
        Optional<Board> boardOptional = boardRepository.findById(request.getBoardNo());
        Board board = boardOptional
                .orElseThrow(()-> new RuntimeException("존재하지 않는 게시글입니다"));

        boardRepository.delete(board);

        return "OK";

    }


}
