package hello.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClientV4 {
    private String url;

    public NetworkClientV4(){
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

    @PostConstruct
    public void init() {
        connect();
        call("초기화 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("destroy");
        disconnect();
    }
}
