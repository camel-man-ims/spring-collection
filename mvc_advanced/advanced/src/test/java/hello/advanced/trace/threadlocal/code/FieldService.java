package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class FieldService {

    private String nameStore;

    public String logic(String name) {
        log.info("์ ์ฅ name={} -> nameStore={}",name,nameStore);
        nameStore = name;
        sleep();
        log.info("์กฐํ nameStore={}",nameStore);
        return nameStore;
    }

    public void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
