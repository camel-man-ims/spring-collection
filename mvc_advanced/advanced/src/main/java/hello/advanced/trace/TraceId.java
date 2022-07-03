package hello.advanced.trace;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TraceId {
    private String id;
    private int level;

    public TraceId() {
        this.id = createdId();
        this.level = 0;
    }

    private TraceId(String id, int level){
        this.id = id;
        this.level = level;
    }

    private String createdId() {
        return UUID.randomUUID().toString().substring(0,8); // 앞에 몇개만 짤라서 사용
    }

    public TraceId createNextId(){
        return new TraceId(id,level+1);
    }

    public TraceId createPreviousId(){
        return new TraceId(id,level-1);
    }

    public boolean isFirstLevel(){
        return level == 0;
    }
}
