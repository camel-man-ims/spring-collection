package hello.core;

import hello.core.member.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        memberService.join(new Member(1L,"memberA",Grade.BASIC));
        Member member = memberService.findMember(1L);
        System.out.println(member);
    }
}


