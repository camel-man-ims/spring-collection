package hello.core.member;

import hello.core.discount.RateDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl(new MemoryMemberRepository(),new RateDiscountPolicy());

    @Test
    void join(){
        Member member = new Member(1L,"memberA",Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
