package hello.autowired;

import hello.core.AutoAppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class QualifierTest {

    ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

    @Test
    void qualifierTest(){
        MemberServiceImpl bean = ac.getBean(MemberServiceImpl.class);
        System.out.println(bean.getDiscountPolicy().getClass());
    }
}
