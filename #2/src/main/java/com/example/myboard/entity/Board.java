package com.example.myboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE board SET DELETE_STATUS = 'DELETE' WHERE board_no = ?")

public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long boardNo;
    private String titile;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus;

    //cascade = CascadeType.ALL는 보드에 대한 작업이 코멘트에도 자동으로 전파된다
    //영속성컨텍스트
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @Where(clause = "DELETE_STATUS = 'ACTIVE'")
    private List<Comment> comments = new ArrayList<>();

/*  public Board addComment(String commentBody){
        Comment comment = new Comment();
        comment.setBody(commentBody);
        comment.setBoard(this);
        comment.setDeleteStatus(DeleteStatus.ACTIVE);
        this.getComments().add(comment);
        return this;
    }

    해당 방식은 board에서 연관관계를 한번에 관리하는 방식이다
    1.코멘트에서 연관관계를 매핑한 것은
     ->(addboard)에서 Comment 객체가 이미 생성된 상태에서 Board 객체에 연결
    2.보드에서 연관관계 매핑
     -> Board 객체가 직접 Comment 객체를 생성하고 관리

    1번 방식은 비지니스 로직을 추가로 작성해야하므로 유지보수에 용이 그래서 1번 방식 ㄴ
 */

}
