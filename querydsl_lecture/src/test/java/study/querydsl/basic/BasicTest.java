package study.querydsl.basic;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class BasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    void setup(){
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);

        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){
        // member1을 찾기
        Member findMember = em.createQuery("select m from Member m where m.username = :username",Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
        QMember m = new QMember("m");

        Member member = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();
        assertThat(member.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydslV2(){
        Member findMember = queryFactory
                // QMember.member -> static import -> member
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("같은 테이블을 join 해야할 때 이름이 같으면 안되니까(alias)")
    public void startQuerydslV3(){

        QMember m1 = new QMember("m1");

        Member findMember = queryFactory
                .select(m1)
                .from(m1)
                .where(m1.username.eq("member1"))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
