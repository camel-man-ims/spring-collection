package hello.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class SingletonWithPrototype {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        bean1.addCount();

        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        bean2.addCount();

        System.out.println("bean1.getCount() = " + bean1.getCount());
        System.out.println("bean2.getCount() = " + bean2.getCount());
    }

    @Test
    @DisplayName("싱글톤내의 프로토타입 빈의 값을 1씩 증가했다고 생각했지만, 실제로는 2가 출력")
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class,PrototypeBean.class);

        ClientBean cBean1 = ac.getBean(ClientBean.class);
        int logic1 = cBean1.logic();

        ClientBean cBean2 = ac.getBean(ClientBean.class);
        int logic2 = cBean2.logic();

        System.out.println("logic1 = " + logic1);
        System.out.println("logic2 = " + logic2);
    }

    @Test
    @DisplayName("해결방법 1. 내부에 ApplicationContext를 생성해서 호출될때마다 주입")
    void singletonClientUsePrototypeV2(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBeanV2.class,PrototypeBean.class);

        ClientBeanV2 cBean1 = ac.getBean(ClientBeanV2.class);
        int logic1 = cBean1.logic();

        ClientBeanV2 cBean2 = ac.getBean(ClientBeanV2.class);
        int logic2 = cBean2.logic();

        System.out.println("logic1 = " + logic1);
        System.out.println("logic2 = " + logic2);
    }

    // @Scope("singleton")
    static class ClientBean{
        private final PrototypeBean prototypeBean;

        ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    static class ClientBeanV2{
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        public int logic(){
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            return prototypeBean.getCount();
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

