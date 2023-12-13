## ◇ 백엔드 1번째 과제입니다◇
###   🪄주제: Spring을 이용해 응답/에러 모델 만들기
<br/> 

###   🪄API 요구사항
       - 이름과 성적을 입력받아 저장하는 api (성적이 6 이상이면 에러 응답 처리)
       
       - 입력된 성적을 조회 하는 api (성적 오름차순으로 조회)
       
       - 특정 성적을 입력받아 해당 성적의 학생만 조회하는 api
   <br/>     

###   🪄구현 요구사항
       - Controller 에서 응답 모델 만들기

          * ApiResponse<T>: 여러가지 데이터 타입(클래스)를 result 로 넣을 수 있도록 사용
          * makeResponse(T result), makeResponse(List<T> results): 단건, 복수건 결과 응답 객체로 만들도록 구현
          
       - 에러응답을 만들기 위해서 @ExceptionHander를 사용하여 exception의 데이터 이용
       
       - exceptionHander에서 응답모델을 만들 때 필요한 데이터가 포함시킬 수 있는 customException 구현
       
          *ErrorCode 는 enum 으로 정의
   <br/>     






## 웹 애플리케이션 계층 구조 

- 엔터티

- 리포지토리

- 서비스

- 컨트롤러
<br>

**Controller <- Service <- Repository**

<br>

----
<br>

## 1) entity_Student
<br>

1. @Getter @Setter **롬복** 넣기 (@Data의 장단점 꼭 알아두기)
<br>    
    
2. 과제 조건 (성적 오름차순 정렬) 실행 → **implements comparable<T>** 사용
<br>

3. **implements** 사용하면 반드시 **부모 메소드 오버라이딩(재정의)** 필요<br>
( option+enter → implement methods → 모두 선택 )
<br>

4. private로 필드 생성
( **생성자도** 넣어줘야 돼 = command+N - Constructor )

 - `생성자 넣는 이유`<br>
    
   - **[ 외부 = 객체 생성 X   /  내부 = 객체 생성 O ]** 의 특징을 이용
생성자를 private으로 정의하면 외부에서 메소드 호출이 안되기 때문에 객체를 생성 X<br> 
(객체 생성 방법은 클래스 안에서 생성자를 호출하는 방법 뿐)<br><br>
   - 싱글톤 성립해서 메모리 낭비 막고 시간 줄이기 위해서 이용 <br>
클래스가 최초 한번만 메모리에 할당되고 그 인스턴스 한개만이 존재하는 것을 보장      
여러 곳에서 객체가 사용될 때 그 인스턴스로 처리하는 디자인 패턴
<br><br>

5. **@AllArgsConstructor** 넣으면 모든 필드에 생성자 자동으로 만들어준다<br>
( @RequiredArgsConstructor은 **final** 또는 **@NotNull** 붙을 때에만 생성자 생성 )
<br>

6. 만들어진 **부모 메서드** (compareTo) 에서 **정렬 조건** 만들기<br>
호출한 객체와 인자로 넘어온 객체와 비교<br>
( 둘이 뺐을 때 0이면 같고, 음수면 호출한 객체가 더 작고, 양수면 호출한 객체가 더 크다 )
<br>

## 2) repository_**StudentRepository**
<br>

1. **@Repository** 어노테이션 넣기
<br>

2. **Set** 사용<br>
mvc에서는 map 사용 ( 하지만 실무에서는 hashmap 사용안함 )
<br>

3. **기능 구현** _이름과 성적 **추가** <br>
public void **add**(Student student)<br>
비지니스 로직으로 서비스 단에서 미리 만들어서 넘겨주기
<br>

4. **기능 구현** _전체 성적 오름차순으로 **조회** <br>
- public **List** < Student > **findAll** (option + enter_java.util) <br>
- set에다 .**stream**하고 **.collect** 사용해서 원하는 타입으로 리턴 가능
- **.sorted**로 오름차순 구현 <br>
  ( Student 클래스의 compareTo 이미 구현해서 얘만 넣어줘도 OK )
- **collect(Collectors.toList())** 리스트 형식
<br><br><br>

## 3) service_StudentService
<br>

1. **@Service** 어노테이션 넣기
<br>

2. **repository를 주입** 받아야 한다<br>
private **final** Student**Repository** ~ <br><br>
   - Repository 주입: 생성자 주입(추천), 빌드 주입, 세터 주입<br>
( final이면 생성자 무조건 해야 돼 option + enter ) 
<br>

3. **생성자 주입** 만족<br>
**@RequiredArgsConstructor** (**final** 붙은 애들 생성자 만들어준다)
<br><br>

4. repository에서 서비스 단에서 미리 만들어서 넘겨주는 거 이어서 구현<br>
- **[저장]** public **Student** (option + enter) **addStudent**
- student 객체 생성에서 이름 성적 저장
- 리포지토리에 해당 student 객체 추가하기<br>
- **[조회]** public **List**< Student > **findAll** <br>
- 리포지토리에 findAll하기
<br><br><br>
 

## 4) entity_ApiResponse (응답 모델 만들기)
<br>

1. **@Getter** 넣기 
<br>
        
2. API 요구사항 **클래스** 생성_**Status**<br>
- **@Getter**<br>
- private static class **Status**
- private인데 final은 없으니 **@AllArgsConstructor** 사용
<br>

3. API 요구사항 **클래스** 생성_**Metadata**<br>
- **@Getter**<br>
- private static class **Status**
- private인데 final은 없으니 **@AllArgsConstructor** 사용
<br>

4. API 요구사항 **필드** 생성 
- 각 필드마다 **@JsonInclude** 입력 (Null표시 없애려고)
- private **final** Status **status** (정상응답, 에러응답에 사용)<br>
- private **List**< T > **results** (정상응답에 사용)<br>
- private Metadata **metadata** (정상응답에 사용)<br>
- private Object **data** (에러응답에 사용)<br>
<br>

5. private로 필드 생성했으니 **생성자 주입** (= **정상** 응답)<br>
(command+N_**List** 클릭) or (option+enter해서 추가)<br>
**public ApiResponse** <br>

- **results**(status, metadata,results) **출력**
- new **Status**()
- **results**
- new **Metadata**(results.size())
<br>

6. private로 필드 생성했으니 **생성자 주입** (= **에러** 응답)<br>
(command+N_**data** 클릭) or (option+enter해서 추가) <br>
**public ApiResponse** <br>
- **status**(code, message), **data 출력**
- new **Status**(code, message)<br>
  (조건= code 나중에 enum 처리 해줘야 해)
- **data**
<br><br><br>

## 5) Controller_StudentController
<br>

1. **@RestController**넣기 <br>
(반환값 **문자**로 연결하기 위해 사용 / @Controller는 뷰로 연결)
<br>

2. **service를 주입** 받아야 한다<br>
private **final** Student**Service** ~
<br>

3. **생성자 주입** 만족 ( **final**이면 생성자 무조건 해야 돼 )<br>
**@RequiredArgsConstructor** (final 붙은 애들 생성자 만들어준다)
<br>

4. 기능 구현_**add** <br>
**public ApiResponse add** <br><br>
- **PostMapping**
- public **ApiResponse add** 
- **@RequestParam** 사용
- **makeResponse** 리턴 <br>
   ( **서비스의 add**에 name, grade 넣고 응답받기 )
<br>

5. 기능 구현_**findAll**<br>
**public ApiResponse findAll** <br>
- **GetMapping**
- public **ApiResponse findAll** 
- **makeResponse** 리턴 <br>
   ( **서비스의 findAll** 리턴 )
<br>

6. **makeResponse** 실행 (**단수** 데이터)<br>
**public < T > ApiResponse< T > makeResponse(T result)** <br>
- **Collections.singletonList(result)** 로 하나 짜리 리스트 만들기
<br>

7. **makeResponse** 실행 (**복수** 데이터)<br>
**public < T > ApiResponse< T > makeResponse(List< T > result)** <br>
- new **ApiResponse**<>(result)<br>
  리턴 값은 ApiResponse에서 만든 results와 같은 List 형태이므로 ApiResponse 그냥 그대로 가져와 result에 넣는다
<br>

## 6) entity_ErrorCode (enum 설정)
<br>

1. **필드** 생성 (enum은 final로 다 작성할 것)
- 필드마다 @Getter 붙여주기
- private final int code
- private final String message
- private final HttpStatus httpStatus
<br>

2. **생성자** 생성 ( **final**이면 생성자 무조건! ) <br>
option + enter
<br>

3. **enum** 생성
- **OK** 정의
- **BAD_REQUEST** 정의
<br><br><br>

## 7-1) exception_CustomException (예외 설정)
<br>

어떠한 오류가 아닌 자체적으로 예외 조건을 만드는 것이기 때문에 예외 설정이 필요하다
<br>

1. **필드** 생성 
- 필드마다 @Getter 붙여주기
- private final Errorcode **errorcode**
- private String **message**
- private Map.Entry<String, Object> **data**
<br>

2. **생성자** 생성 ( command + N_모두 선택 )<br>
**public CustomException~**
- errorcode
- message
- data = new AbstractMap.SimpleEntry<><br>
( **.getClass().getSimpleName() =** 클래스 이름 뽑아주는 메서드 / **inputRestriction** 출력 때문에 넣었음 )
<br>

3. **메세지**만 **생성자** 한 번 더 생성 ( command + N_message 선택 )<br>
**public String getMessage()** <br>
- if(**StringUtils.hasLength**(this.message)) 문자열이 Null인지 확인하고 사용자가 정의한 메세지 보내기
<br><br>
<br>

## 7-2) exception_InputRestriction
<br>

1. **@Getter** <br>
2. **@AllArgsConstructor 생성자 자동화** <br>
3. **필드** 생성 ( private int **maxGrade** )<br>
<br><br>

## 8) Controller_StudentController (예외 조건 추가)
<br>

1. **@ExceoptionHandler(CustomException.class)** <br>
( CustomException 클래스와 연동 ) <br>
**public ApiResponse customExceptionHandler~** <br>
- 서블릿의 response, customException 받기<br>
- new **ApiResponse 리턴**<br>
(CustomException 클래스에서 - ErrorCode 클래스에서 - **code / message / data** 받기)
<br>

2. 기능 구현_**add**에서 **예외처리 조건 추가**<br>
**public ApiResponse add {if문}** <br>
- grade가 6이상이면 예외<br>
- new **CustomException** <br>
(ErrorCode 클래스의 **BAD_REQUEST**, **메세지**, **InputRestriction**)
<br><br><br>

## 9-1) repository_**StudentRepository (특정 조회 추가)**
<br>

1. **기능 구현**_전체 성적 오름차순으로 **조회** <br>
- public **List**<Student>**get**(int grade)<br>
- set에다 .**stream**하고 **.collect** 사용해서 원하는 타입으로 리턴 가능<br>
- **.filter**로 student의 grade 중에 입력 받은 grade만 리턴 <br>
- **collect(Collectors.toList())** 리스트 형식
<br><br><br>

## 9-2) service_StudentService **(특정 조회 추가)**

1. repository에서 서비스 단에서 미리 만들어서 넘겨주는 거 이어서 구현
- **[특정 조회]** public List<Student> get(int grade)
- 리포지토리에 get하기
<br><br><br>

## 9-3) service_StudentService **(특정 조회 추가)**

1. 기능 구현_**get** <br>
**public ApiResponse get~**
- **GetMapping** ({grade}=pathvariable 사용)
- public **ApiResponse get (@PathVariable ~ )** 
- **makeResponse** 리턴 <br>
   ( **서비스의 findAll** 리턴 )
<br>

---
<br>

## 10) 결과
<br>

### 1) 이름과 성적 저장
<br>

[**Post** 방식] http://localhost:8080/**student** 실행해서 아래 **Body** 작성 <br>

<img width="524" alt="스크린샷 2023-12-07 오후 4 11 31" src="https://github.com/luz315/goorm_Back/assets/125282732/5cb0dff0-ddce-4bc7-b0d2-4455d30914d6"> <br>
<img width="381" alt="스크린샷 2023-12-07 오후 3 20 50" src="https://github.com/luz315/goorm_Back/assets/125282732/ade1bb68-2c49-4ad8-9e09-593522b7fedf">

<br>

### 2) 입력된 성적 전체 조회
<br>

[**Get** 방식] http://localhost:8080/**students** 실행 <br>

<img width="469" alt="스크린샷 2023-12-07 오후 3 35 02" src="https://github.com/luz315/goorm_Back/assets/125282732/b11f7f96-73a7-4100-810a-0e37ccb0ddf3">

<br><br>

### 3) 특정 성적으로 조회
<br>

[**Get** 방식] http://localhost:8080/**students/1** 실행 <br>

<img width="474" alt="스크린샷 2023-12-07 오후 3 32 07" src="https://github.com/luz315/goorm_Back/assets/125282732/5986a9a9-5e7a-44d4-bb54-18dc5cb33293">

<br><br>


### 4) 에러 처리
<br>

[**Post** 방식] http://localhost:8080/**student** 실행하고 성적을 6이상으로 넣었을 경우 <br>

<img width="375" alt="스크린샷 2023-12-07 오후 3 21 32" src="https://github.com/luz315/goorm_Back/assets/125282732/b6da041d-09bd-4b09-b89b-403f1230eb43">
<br>

---
<br>

## < 참고 >

#### @RequiredArgsConstructor / private final~ 코드 조합 설명

우리가 생성한 필드에 final이 들어가면 무조건 생성자를 만들어줘야 한다

그리고 그 생성자를 자동으로 만들어주는 어노테이션이 @RequiredArgsConstructor이다
</br>
</br>

#### 그렇다면 왜 생성자를 만들어야 하는가

```java
@Autowired

private ~
```

* @Autowired는 스프링 빈에 등록되어 있는 것을 Injection 해준다
</br>

위와 같이 설정하는 것을 Field Injection 필드 인젝션이라고 한다 (기본 방식)

기본 필드 바로 위에 @Autowired를 사용하는 필드 인젝션 단점은 변경하고 싶을 때 변경할 수가 없다는 것이다 (ex. 테스트 할 때)

</br>


#### 다른 방법 1) Setter Injection ( = command + N _ Setter 선택 )
</br>

Setter하고 위에 @Autowired를 붙여주는 셋터 인젝션 방식이다
</br>
</br>

- **장점**: 테스트코드 작성할 때 Mock을 직접 주입 가능
</br>

- **단점**: 애플리케이션 돌아가는 도중에 누군가 바꿀 수 있다는 것 (하지만 그럴 일 거의 없음)

</br>
  

#### 다른 방법 2) Constructor Injection ( = command + N _ Constructor 선택 )
</br>

Constructor 하고 위에 @Autowired를 붙여주는 생성자 인젝션 방식이다
</br>
</br>

- **장점**: 생성할 때 이미 다 끝나 버리기 때문에 중간에 바꿀 수가 없기 때문에 셋터 인젝션의 단점을 보완할 수 있다
</br>
</br>

그리고 요즘 스프링은 @Autowired를 자동으로 Injection하므로 **생략이 가능**하다

나머지 필드 생성 코드에 final을 붙여주고 @RequiredArgsConstructor을 사용하면 생성자 또한 **생략이 가능**하다

</br>

#### 그렇게 해서 아래코드로 사용되는 것이다

```java
@RequiredArgsConstructor

private final~
```
