package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceV2 {
    private final ExamRepositoryV2 examRepository;

    @Trace
    public void request(String itemId){
        examRepository.save(itemId);
    }
}
