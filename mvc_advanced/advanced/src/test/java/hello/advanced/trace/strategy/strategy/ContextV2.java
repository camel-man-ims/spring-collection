package hello.advanced.trace.strategy.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략을 parameter로 전달
 */

@Slf4j
public class ContextV2 {
    public void execute(Strategy strategy){
        long startTime = System.currentTimeMillis();

        strategy.call(); // 위임
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}",resultTime);
    }
}
