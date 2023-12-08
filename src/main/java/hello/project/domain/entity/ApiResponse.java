package hello.project.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponse<T> {

    //필드생성

    private final Status status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> results;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Metadata metadata;
    //result들어온거 result에 담아주고, Status 응답해주고
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;


    //정상응답
    public ApiResponse(List<T> results) { //생성자
        this.status = new Status(2000, "OK");
        this.results = results;
        this.metadata = new Metadata(results.size());
    }
    //예외응답
    public ApiResponse(int code, String message, Object data) { //생성자
        this.status = new Status(code, message);
        this.data = data;

    }

    @Getter
    @AllArgsConstructor
    private static class Status{
        private int code;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    private  static class Metadata{
        private int resultcount=0;

    }



}
