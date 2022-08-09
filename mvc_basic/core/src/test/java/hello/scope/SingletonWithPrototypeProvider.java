package hello.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonWithPrototypeProvider {

    @Test
    @DisplayName("Provider를 이용해서 singleton - prototype 문제 해결")
    void singletonClientUsePrototypeV2(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);

        int logic = bean1.logic();

        ClientBean bean2 = ac.getBean(ClientBean.class);
        int logic1 = bean2.logic();

        System.out.println("logic1 = " + logic1);
        System.out.println("logic = " + logic);
    }

     @Scope("singleton")
    static class ClientBean{
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic(){
            PrototypeBean prototype = prototypeBeanProvider.getObject();
            prototype.addCount();
            return prototype.getCount();
        }
    }


    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}

