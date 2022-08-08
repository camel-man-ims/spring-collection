package hello.core.member;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{

    @Getter
    private final MemberRepository memberRepository;
    @Getter
    private final DiscountPolicy discountPolicy;

    public MemberServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
