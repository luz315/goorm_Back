package com.example.myboard.request;

import lombok.Data;

@Data
public class BoardPostRequest {
    //pk라 보드 넘버는 당연히 보낼 필요 없다
    private String title;
    private String body;

}
