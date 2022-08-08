package hello.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClientV3  {
    private String url;

    public NetworkClientV3(){
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

    public void init() {
        connect();
        call("초기화 메세지");
    }

    public void close() {
        System.out.println("destroy");
        disconnect();
    }
}
