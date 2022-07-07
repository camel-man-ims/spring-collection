package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void execute(){
        long startTime = System.currentTimeMillis();
        log.info("비지니스 로직1 실행");

        call();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}",resultTime);
    }

    protected abstract void call();
}
