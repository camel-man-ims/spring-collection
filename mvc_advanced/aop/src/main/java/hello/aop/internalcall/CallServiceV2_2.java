package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2_2 {

    private final ObjectProvider<CallServiceV2_2> callServiceProvider;

    public void external(){
        log.info("call external");
        CallServiceV2_2 proxy = callServiceProvider.getObject();
        proxy.internal();
    }

    public void internal(){
        log.info("call internal");
    }
}
