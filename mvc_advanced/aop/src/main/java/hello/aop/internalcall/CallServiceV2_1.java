package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2_1 {

    private final ApplicationContext applicationContext;

    public void external(){
        log.info("call external");
        CallServiceV2_1 proxy = applicationContext.getBean(CallServiceV2_1.class);
        proxy.internal();
    }

    public void internal(){
        log.info("call internal");
    }
}
