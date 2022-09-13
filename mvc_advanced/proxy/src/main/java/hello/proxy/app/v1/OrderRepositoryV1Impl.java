package hello.proxy.app.v1;

import java.util.concurrent.TimeUnit;

public class OrderRepositoryV1Impl implements OrderRepositoryV1{
    @Override
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
