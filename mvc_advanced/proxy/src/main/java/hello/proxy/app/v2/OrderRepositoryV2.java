package hello.proxy.app.v2;

import java.util.concurrent.TimeUnit;

public class OrderRepositoryV2 {
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
