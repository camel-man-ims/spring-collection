package hello.proxy.code.pureproxy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RealSubject implements Subject{
    @Override
    public String opertaion() {
        log.info("실제객체호출");
        sleep(1);
        return "data";
    }

    private void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
