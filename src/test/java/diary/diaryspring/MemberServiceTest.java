package diary.diaryspring;

import diary.diaryspring.domain.Member;
import diary.diaryspring.repository.MemberRepository;
import diary.diaryspring.service.MemberService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberServiceTest {

    @Autowired MemberRepository mr;
    @Autowired MemberService ms;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setId("id");

        //when
        String resId = ms.join(member);

        //then
        Member result = mr.findById(resId).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void 중복회원() throws Exception {
        //given
        Member member1 = new Member();
        member1.setId("id");

        Member member2 = new Member();
        member2.setId("id");

        //when
        ms.join(member1);

        //then
        String result = ms.join(member2);
        assertThat(result).isEqualTo("");
        /**
        assertThatThrownBy(() -> { ms.join(member2); })
                .isInstanceOf(Exception.class)
                .hasMessageMatching("이미 존재하는 회원");
         **/
    }

    @Test
    public void 정상로그인() throws Exception {
        //given
        Member member = new Member();
        member.setId("id");
        member.setPw("pw");

        //when
        ms.join(member);
        String result = ms.login(member.getId(), member.getPw());

        //then
        assertThat(result).contains("님 환영합니다.");
    }

    @Test
    public void 틀린비번로그인() throws Exception {
        //given
        Member member = new Member();
        member.setId("id");
        member.setPw("pw");

        //when
        ms.join(member);
        String result = ms.login(member.getId(), "other");

        //then
        assertThat(result).isEqualTo("비밀번호 체크");
    }

    @Test
    public void 없는계정로그인() throws Exception {
        //given
        Member member = new Member();
        member.setId("id");
        member.setPw("pw");

        //when
        String result = ms.login(member.getId(), member.getPw());

        //then
        assertThat(result).isEqualTo("없는 계정");
    }
}

