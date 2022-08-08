package hello.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

public class NetworkClientV2 implements InitializingBean, DisposableBean {
    private String url;

    public NetworkClientV2(){
        System.out.println("생성자 호출, url = "+ url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){
        System.out.println("url: " + url + " message = " + message );
    }

    public void disconnect(){
        System.out.println("close: " + url);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 메세지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy");
        disconnect();
    }
}
