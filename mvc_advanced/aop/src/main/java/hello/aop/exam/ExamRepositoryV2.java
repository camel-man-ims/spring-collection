package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepositoryV2 {
    private static int seq = 0;

    /**
     * 5번에 1번 실패하는 요청
     */
    @Retry(value = 5) // 수정가능 @Retry(5) 이렇게 써도 된다.
    public String save(String itemId){
        seq++;
        if(seq%5 == 0){
            throw new IllegalArgumentException("예외 발생");
        }
        return "Ok";
    }
}
