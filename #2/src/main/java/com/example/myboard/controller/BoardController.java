package com.example.myboard.controller;

import com.example.myboard.request.BoardDeleteRequest;
import com.example.myboard.request.BoardPostRequest;
import com.example.myboard.response.BoardListResponse;
import com.example.myboard.response.BoardResponse;
import com.example.myboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //게시글 등록
    @PostMapping("board")
    public BoardResponse writeBoard(
            @RequestBody BoardPostRequest boardPostRequest
            ){
        return boardService.writeBoard(boardPostRequest);
    }

    //게시글 목록 조회(페이징)
    @GetMapping("boards")
    public List<BoardListResponse> searchBoardList(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ){
        return boardService.searchBoardList(page, pageSize);
    }

    //게시글 단건 조회 (게시글+댓글)
    @GetMapping("board")
    public BoardResponse searchBoard(
            @RequestParam("boardNo") Long boardNo
    ){
        return boardService.searchBoard(boardNo);

    }

    //게시글 삭제
    @DeleteMapping("board")
    public String deleteBoard(
            @RequestBody BoardDeleteRequest boardDeleteRequest
    ){
        return boardService.deleteBoard(boardDeleteRequest);
    }
}
