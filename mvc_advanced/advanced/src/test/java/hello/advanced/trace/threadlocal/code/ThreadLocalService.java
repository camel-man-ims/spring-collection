package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadLocalService {

    private final ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name={} -> nameStore={}",name,nameStore.get());
        nameStore.set(name);
        sleep();
        log.info("조회 nameStore={}",nameStore.get());
        return nameStore.get();
    }

    public void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
