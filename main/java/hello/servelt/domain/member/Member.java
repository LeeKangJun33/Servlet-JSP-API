package hello.servelt.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {

    private Long id; //repostory 회원 저장소를 db에 저장하면 그게 아이디 가 발급 됨
    private String username;
    private int age;

    public Member() {

    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }
}
