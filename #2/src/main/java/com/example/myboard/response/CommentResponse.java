package com.example.myboard.response;

import com.example.myboard.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private Long commentNo;
    private String body;

    //정적 팩토리 메서드
    //엔티티를 받아 dto를 만들어주는 정적 팩토리 메서ㄷ
    public static CommentResponse from(Comment comment){
        return new CommentResponse(comment.getCommentNo(), comment.getBody());
    }
}
