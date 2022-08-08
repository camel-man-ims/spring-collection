package hello.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonTest {

    @Test
    @DisplayName("싱글톤일때")
    void singletonBeanFind(){
        // 직접 넣어주면 자동으로 componentScan 의 대상이 된다.
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        System.out.println("생성위치1");
        SingletonBean bean1 = ac.getBean(SingletonBean.class);
        System.out.println("생성위치2");
        SingletonBean bean2 = ac.getBean(SingletonBean.class);
        System.out.println(bean1);
        System.out.println(bean2);
        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean{
        @PostConstruct
        public void init(){
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean.destroy");
        }
    }

}
