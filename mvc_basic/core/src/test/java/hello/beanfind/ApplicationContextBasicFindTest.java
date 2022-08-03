package hello.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 bean 출력")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);
    }

    @Test
    @DisplayName("타입으로만 조회")
    void findBeanType(){
        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService);
    }

    @Test
    @DisplayName("구현체 타입으로 조회")
    void findBeanTypeImpl(){
        MemberService memberService = ac.getBean(MemberServiceImpl.class);
        System.out.println("memberService = " + memberService);
    }

    @Test
    @DisplayName("조회 실패")
    void findBeanFail(){
        MemberService memberService = ac.getBean("xxadq",MemberServiceImpl.class);
        System.out.println("memberService = " + memberService);
    }

    @Test
    @DisplayName("조회 실패 assertThrows")
    void findBeanFailLambda(){
        assertThrows(NoSuchBeanDefinitionException.class,()->ac.getBean("xca",MemberService.class));
    }
}
