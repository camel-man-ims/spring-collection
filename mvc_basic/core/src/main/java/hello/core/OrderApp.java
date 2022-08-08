package hello.core;

import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.*;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class OrderApp {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl(new MemoryMemberRepository(),new RateDiscountPolicy());
        OrderService orderService = new OrderServiceImpl(new MemoryMemberRepository(),new FixDiscountPolicy());

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order itemA = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("itemA = " + itemA.calculatePrice());
    }
}
