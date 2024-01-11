package com.example.myboard.response;

import com.example.myboard.entity.Board;
import com.example.myboard.entity.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long boardNo;
    private String title;
    private String body;
    private DeleteStatus deleteStatus;
    private List<CommentResponse> comments;

    //정적 팩토리 메서드
    public static BoardResponse from(Board board){
        return new BoardResponse(
                board.getBoardNo(),
                board.getTitile(),
                board.getBody(),
                board.getDeleteStatus(),
                //코멘트가 들어오면 리스트라서 하나하나 만들어줘야하므로 stream 생성
                board.getComments().stream().map(CommentResponse::from).collect(Collectors.toList())
        );
    }
}
