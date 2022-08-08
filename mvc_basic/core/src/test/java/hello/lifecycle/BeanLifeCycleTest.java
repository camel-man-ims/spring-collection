package hello.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    @DisplayName("v1, url이 null로 찍힘")
    public void lifeCycleTestV1(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfigV1.class);
        NetworkClientV1 client = ac.getBean(NetworkClientV1.class);
        ac.close();
    }

    @Test
    @DisplayName("v2 스프링 객체들 이용")
    public void lifeCycleTestV2(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfigV2.class);
        NetworkClientV2 client = ac.getBean(NetworkClientV2.class);
        ac.close();
    }

    @Test
    @DisplayName("v3 bean 에 명시")
    public void lifeCycleTestV3(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfigV3.class);
        NetworkClientV3 client = ac.getBean(NetworkClientV3.class);
        ac.close();
    }

    @Test
    @DisplayName("v4 annotation 이용")
    public void lifeCycleTestV4(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfigV4.class);
        NetworkClientV4 client = ac.getBean(NetworkClientV4.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfigV1 {
        @Bean
        public NetworkClientV1 networkClient(){
            NetworkClientV1 networkClient = new NetworkClientV1();
            networkClient.setUrl("http://naver.com");
            return networkClient;
        }
    }

    @Configuration
    static class LifeCycleConfigV2 {
        @Bean
        public NetworkClientV2 networkClient(){
            NetworkClientV2 networkClient = new NetworkClientV2();
            networkClient.setUrl("http://naver.com");
            return networkClient;
        }
    }

    @Configuration
    static class LifeCycleConfigV3 {

        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClientV3 networkClient(){
            NetworkClientV3 networkClient = new NetworkClientV3();
            networkClient.setUrl("http://naver.com");
            return networkClient;
        }
    }

    @Configuration
    static class LifeCycleConfigV4 {
        @Bean
        public NetworkClientV4 networkClient(){
            NetworkClientV4 networkClient = new NetworkClientV4();
            networkClient.setUrl("http://naver.com");
            return networkClient;
        }
    }
}
