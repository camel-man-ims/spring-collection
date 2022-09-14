package hello.proxy.app.v3;

import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class OrderRepositoryV3 {
    public void save(String itemId) {
        if(itemId.equals("ex")){
            throw new IllegalArgumentException("예외 발생");
        }
        sleep(1);
    }

    private void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
