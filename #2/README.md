## ◇ 백엔드 2번째 과제입니다◇
###   🪄주제: Spring을 이용해 게시판 만들기
<br/> 

###   🪄구현 요구사항

       - 게시글 등록
       
           - 단 건의 게시글 등록
           
       - 게시글 목록 조회
       
           - 등록된 게시글의 목록을 조회
           
           - 응답에는 본문이 포함되지 않는다
           
           - 페이징 기능
           
              - offset 기반 페이징
              - cursor 기반 페이징
              
           - 정렬 순서 : 최신 글이 우선순위 가장 높음
           
       - 게시글 삭제
       
           - 단 건의 게시글 삭제
           
           - soft delete
           
           - 게시글 삭제 시 댓글도 삭제

       - 댓글 등록
       
           - 게시글에 댓글 등록
           
       - 댓글 단건 조회

   <br/>     


### 🪄ERD

- 게시판
    
    | PK | board_no | bigint |
    | --- | --- | --- |
    |  | title | varchar |
    |  | body | text |
    |  | delete_status | enum |

- 댓글
  
    | PK | comment_no | bigint |
    | --- | --- | --- |
    | FK | board_no | bigint |
    |  | body | text |
    |  | delete_status | enum |
  
- soft delete
    - enum deleteStatus : ACTIVE / DELETE

   <br/>     



### 🪄셋팅

- application.yml

```java
Spring:
  datasource:
    url: jdbc:h2:tcp://localhost/~/pbl
    username: sa
    password:
    driver-class-name: org.h2.Driver # 얘는 JPA 활용 강의에서 씀

  jpa:
    database: h2
    database-platform: org.hibernate.dialect.H2Dialect
    show_sql: true

    hibernate:
      ddl-auto: create
      # 애플리케이션 실행시점에 내가 가진 엔터티 보고 테이블 날리고(drop) 다시 생성

    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true

    open-in-view: false
```

  <br/>     


## 1. 엔티티 구현
  
  
### 1) Board

- **@Getter @Setter** 롬복 ****넣기
- **@NoArgsConstructor**
    - 기본 생성자를 만들기
    - JPA를 사용할 때 기본적으로 요구되며, 프레임워크가 프록시를 생성하는 데 사용
    - 생성자 넣는 **이유**
        1. **[ 외부 = 객체 생성 X / 내부 = 객체 생성 O ]** 의 특징을 이용
        생성자를 private으로 정의하면 외부에서 메소드 호출이 안되기 때문에 객체를 생성 X 
        (객체 생성 방법은 클래스 안에서 생성자를 호출하는 방법 뿐)
        2. 싱글톤 성립해서 메모리 낭비 막고 시간 줄이기 위해서 이용
        클래스가 최초 한번만 메모리에 할당되고 그 인스턴스 한개만이 존재하는 것을 보장
        여러 곳에서 객체가 사용될 때 그 인스턴스로 처리하는 디자인 패턴
        
        ( **생성자도** 넣어줘야 돼 = command+N - Constructor )
        
        ( @RequiredArgsConstructor은 **final** 또는 **@NotNull** 붙을 때에만 생성자 생성 )
        
- **@AllArgsConstructor**
    - 작성한 필드에 쓴 모든 생성자만 만들기
    - 테스트 코드나 빌더 패턴을 사용하지 않고 객체를 초기화할 때 유용
- **@SQLDelete~**
    - **`board`** 테이블에서 특정 레코드의 **`DELETE_STATUS`** 컬럼 값을 'DELETE'로 변경
    - 실제로 데이터베이스에서 삭제하는 대신 "삭제됨" 상태로만 표시
- **private ~** 필드 생성
    - 보드 넘버
    - 보드 제목
    - 보드 바디
    - enum
- **@Id @GeneratedValue**
    - 엔티티의 기본 키(primary key)를 자동으로 생성
    - **`GenerationType.IDENTITY`** : 데이터베이스의 자동 증가 기능
- **@Column**
    - 필드 이름을 기반으로 칼럼명을 정할 경우 사용
    - @Column(name=””) 으로 작성하게 되면 필드와 다른 이름으로 칼럼명 사용 가능
- **@Column(columnDefinition = "TEXT")**
    - **`columnDefinition = "TEXT"`** **타입**
        - 긴 텍스트를 저장하기 위한 전용 타입
        - 예) 블로그 포스팅, 기사( 최대 65,535자)
    - **`@Column(length = 1000)` 타입**
        - 예) 짧은 의견, 댓글, 소개 텍스트 저장 (최대 1000자)

- **@Enumerated(EnumType.STRING)**
    - 열거형 enum을 작성할 시, 정수로 데이터베이스에 저장
    - 데이터 크기가 작다는 장점 있지만 enum 순서 변경하면 db 저장값의 의미가 달라져 심각한 버그를 야기

- **연관관계 매핑**
    - **CascadeType.ALL**
        - 부모 엔티티에 대한 모든 변경(생성, 업데이트, 삭제 등)이 자식 엔티티에도 적용
        (= 보드에 대한 작업이 코멘트에도 자동으로 전파)
    - **@Where ~** soft delete
    - **private ~** 필드 생성
    - 추가 매핑 (이미 comment 클래스에서 써서 안써도 괜찮다)
        - public Board addComment(String commentBody){
                Comment comment = new Comment();
                comment.setBody(commentBody);
                comment.setBoard(this);
                comment.setDeleteStatus(DeleteStatus.ACTIVE);
                this.getComments().add(comment);
                return this;
        }
        - 해당 방식은 board에서 연관관계를 한번에 관리하는 방식
         → Board 객체가 직접 Comment 객체를 생성하고 관리
  <br/>     



### 2) Comment

- **@Getter** **@Setter** 롬복넣기
- **@SQLDelete~**
    - **`board`** 테이블에서 특정 레코드의 **`DELETE_STATUS`** 컬럼 값을 'DELETE'로 변경
    - 실제로 데이터베이스에서 삭제하는 대신 "삭제됨" 상태로만 표시
- **private ~** 필드 생성
    - 코멘트 넘버
    - 코멘트 바디
    - enum
- **@Id @GeneratedValue**
    - 엔티티의 기본 키(primary key)를 자동으로 생성
    - **`GenerationType.IDENTITY`** : 데이터베이스의 자동 증가 기능
- **@Column(columnDefinition = "TEXT")**
    - **`columnDefinition = "TEXT"`** **타입** ( 최대 65,535자)
    - **`@Column(length = 1000)` 타입** (최대 1000자)

- **@Enumerated(EnumType.STRING)**
    - 열거형 enum을 작성할 시, 정수로 데이터베이스에 저장
    - 데이터 크기가 작다는 장점 있지만 enum 순서 변경하면 db 저장값의 의미가 달라져 심각한 버그를 야기

- **연관관계 매핑**
    - **fetch = FetchType.LAZY**
        - @XToOne(OneToOne, ManyToOne)같이 1로 가는 관계는 LAZY로 변경
    - **public void addboard(Board board) ~**
        - **`Comment`** 객체가 이미 생성된 상태에서 **`Board`** 객체에 연결
        - 비지니스 로직을 추가로 작성해야하므로 유지보수에 용이
  <br/>     



### 3) DeleteStatus

- *`ACTIVE*, *DELETE*`
    <br/>     



## 2. 리포지토리 구현




### 1) Board**Repository**

- **extends JpaRepository<Board, Long>**
- **Page<Board> findAllByDeleteStatus ~**
    - 특정 기능 만들때에는 따로 작성 (findAll은 이미 Jpa가 제공)
    - Spring Data JPA에서 페이징 처리된 결과
    → 여기서 할 일은 보드넘버에 해당하는 보드 객체를 조회
    - **`Page<Board>`** = **`Board`** 엔티티의 데이터가 포함된 페이지
    - **`Pageable`** = 파라미터는 페이징 처리에 필요한 정보 (페이지 번호, 페이지당 데이터 수, 정렬 방식, 등)
    1. 요청받는다
    2. 요청받은 보드를 찾는다
    3. 업데이트 해준다
    
- **@Query ~**
    - 특정 번호(**`boardNo`**)를 가진 게시글을 찾기
    - **`LEFT JOIN FETCH`** = **`Board`** 와 연관된 모든 댓글 함께 가져온다 (게시글+댓글)
    - **`WHERE b.boardNo = :boardNo`** = 검색하려는 게시글의 번호 지정
- **Optional<Board> findBoardWithCommentByBoardNo**
    - Optional = null 처리에 활용하기 위해 사용
    - @Param = boardNo
  <br/>     


## 3. 서비스 구현




### 1) BoardService

- **@Transactional** 얘는 습관
- **@RequiredArgsConstructor**
    - final 또는 @NotNull 붙을 때에만 생성자 생성
- **private final BoardRepository ~** repository를 주입
    - Repository 주입 
    1) 생성자 주입(추천),   2) 빌드 주입,   3) 세터 주입
    ( final이면 생성자 무조건 해야 돼 option + enter )

- **public BoardResponse writeBoard(BoardPostRequest request) ~**
    - 게시글 등록
        - 보드 객체 생성
        - 보드 객체에 입력받은 request의 제목, 본문, 삭제 상태(=ACTIVE) 설정
        - **보드 객체**를 **보드리포지토리**의 **save** 메서드 이용해서 **응답**으로 반환

- **public List<BoardListResponse> searchBoardList(int page, int pageSize) ~**
    - 게시글 목록 조회
        - **`findAllByDeleteStatus`** = **`DeleteStatus`**가 **`ACTIVE`**인 **`Board`** 엔티티들 조회
        - 삭제되지 않은 게시글들을 페이지네이션(페이지 번호, 한 페이지에 표시할 게시글의 수)과 
        정렬(내림차순) 조건에 맞추어 조회
        - **`BoardListResponse`** 를 리스트로 반환

- **public BoardResponse searchBoard(Long boardNo)**
    - 게시글 단건 조회(게시글 + 댓글)
        - **`findBoardWithCommentByBoardNo`** = **`boardNo`**를 가진 게시글, 댓글 조회
        - 결과를 **`BoardResponse`** 형태로 변환
        - **`Optional`** 객체의 **`map`** 메서드 사용 
         → **`Optional`** 객체가 비어있는 경우 **`RuntimeException`** 발생

- **public String deleteBoard(BoardDeleteRequest request)**
    - 게시글 삭제
        - **`findById`** = 요청된 게시글 번호에 해당하는 **`Board`** 엔티티 조회
            - **`Optional`** = 해당 엔티티가 존재할 수도, 존재하지 않을 수도 있음을 나타낸다
        - **`orElseThrow`** 메서드 = **`boardOptional`**에 게시글 존재 여부 확인 및 가져오기
        - 조회한 **`Board`** 객체를 **`boardRepository`**의 **`delete`** 메서드를 사용하여 삭제
        - 삭제가 성공적으로 완료된 경우 문자열 "OK"를 반환
  <br/>     



### 2) CommentService

- **@Transactional** 얘는 습관
- **@RequiredArgsConstructor**
    - final 또는 @NotNull 붙을 때에만 생성자 생성
- **private final BoardRepository ~** repository를 주입
    - Repository 주입 
    1) 생성자 주입(추천),   2) 빌드 주입,   3) 세터 주입
    ( final이면 생성자 무조건 해야 돼 option + enter )

- **`findBoardWithCommentByBoardNo`** = 요청된 게시글 번호에 해당하는 **`Board`** 엔티티 조회
- **`Optional`**에서 게시글 존재 여부 확인 및 가져오기

- **public BoardResponse writeComment(CommentPostRequest request)**
    - 댓글 등록
        - 코멘트 객체 생성
        - 코멘트 객체에 입력받은 request의 코멘트 바디, 삭제 상태(=ACTIVE) 설정
        - 댓글과 게시글 연결
        - **보드 객체**를 **보드리포지토리**의 **save** 메서드 사용해 댓글 저장
        - 게시글 정보 반환
  <br/>     



## 4. DTO 구현




### 1) BoardPostRequest

- pk는 보낼 필요 없다
- `title`
- `body`

### 2) BoardDeleteRequest

- `boardNo`

### 3) CommentPostRequest

- `boardNo`
- `commentBody`

<br/>     


### 4) BoardListResponse

- `boardNo`
- `title`
- 정적 팩토리 메서드

### 5) BoardResponse

- `boardNo`
- `title`
- `body`
- `deleteStatus`
- `CommentResponse`
- 정적 팩토리 메서드

### 6) CommentResponse

- `commentNo`
- `Body`
- 정적 팩토리 메서드

<br/>     



## 5. 컨트롤러 구현




### 1) BoardController

- **private final BoardService ~** service를 주입

- 게시글 등록
    - @PostMapping("board")
    - BoardResponse writeBoard
    - @RequestBody BoardPostRequest
    - boardService반환

- 게시글 목록 조회(페이징)
    - @GetMapping("boards")
    - List<BoardListResponse> searchBoardList
    - @RequestParam int page / int pageSize
    - boardService반환

- 게시글 단건 조회 (게시글+댓글)
    - @GetMapping("board")
    - BoardResponse searchBoard
    - @RequestParam boardNo
    - boardService반환

- 게시글 삭제
    - @DeleteMapping("board")
    - String deleteBoard
    - @RequestBody BoardDeleteRequest
    - boardService반환
  <br/>     


### 2) CommentController

- **private final CommentService ~** comment를 주입

- 댓글 등록
    - @PostMapping("comment”)
    - BoardResponse writeComment
    - @RequestBody CommentPostRequest
    - commentService반환
  <br/>     
<br/>  

## 결과  
   

**1) 게시글 저장**

<img width="375" alt="스크린샷 2024-01-11 오전 3 47 53" src="https://github.com/luz315/goorm_Back/assets/125282732/ec9fb252-2ab6-45bb-bcd6-ec444d0b1b31">
  <br/>    
  <br/>     


**2) 게시글 목록 조회 (페이징)**

<img width="375" alt="스크린샷 2024-01-11 오전 3 49 48" src="https://github.com/luz315/goorm_Back/assets/125282732/2917e8c8-1080-4c1c-bb5c-c50ff6c28968">
  <br/>    
  <br/>     


**3) 댓글 등록**

<img width="375" alt="스크린샷 2024-01-12 오전 1 59 48" src="https://github.com/luz315/goorm_Back/assets/125282732/118dbf44-3ddc-4950-85c2-882f2ac43d0a">
  <br/>    
  <br/>     


**4) 게시글 + 댓글 조회**

<img width="375" alt="스크린샷 2024-01-12 오전 2 01 34" src="https://github.com/luz315/goorm_Back/assets/125282732/afcb04fb-3117-47d5-af8d-511c1a83234e">
  <br/>    
  <br/>     


**5) 게시글 삭제**

<img width="375" alt="스크린샷 2024-01-12 오전 3 01 56" src="https://github.com/luz315/goorm_Back/assets/125282732/3787159c-c051-446c-9dbc-b99cdadf81c5">
