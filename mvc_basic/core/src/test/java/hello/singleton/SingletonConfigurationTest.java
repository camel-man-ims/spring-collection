package hello.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonConfigurationTest {

    @Test
    @DisplayName("싱글톤 보장 테스트")
    void singleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl ms = ac.getBean(MemberServiceImpl.class);
        OrderServiceImpl os = ac.getBean(OrderServiceImpl.class);
        MemberRepository mr3 = ac.getBean(MemberRepository.class);

        MemberRepository mr1 = ms.getMemberRepository();
        MemberRepository mr2 = os.getMemberRepository();

        System.out.println("mr1 = " + mr1);
        System.out.println("mr2 = " + mr2);
        System.out.println("mr3 = " + mr3);
    }

    @Test
    @DisplayName("AppConfig CGLIB")
    void cglib(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean.getClass() = " + bean.getClass());
    }
}
