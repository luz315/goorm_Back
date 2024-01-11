package com.example.myboard.request;

import lombok.Data;

@Data
public class CommentPostRequest {
    private Long BoardNo;
    private String CommentBody;
}
