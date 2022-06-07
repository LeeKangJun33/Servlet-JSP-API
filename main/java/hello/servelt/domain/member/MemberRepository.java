package hello.servelt.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 동시성 문제가 고려되어있지 않음,실무에서는 ConcurrentHashMap,AtomicLong 사용 고려
* */

public class MemberRepository {

    private static Map<Long,Member> store = new HashMap<>();
    private static long sequence = 0L; //아이디가 하나씩 증가하는 시퀀스 로 쓸거임

    private static final MemberRepository instance = new MemberRepository(); //싱글톤으로 생성

    public static MemberRepository getInstance(){
        return instance;
    }

    private MemberRepository(){

    } //싱글톤 만들때는 private 로 생성자를 막아줘야함 그래서 아무나 생성하지마 못하게 해줘야한다.

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    } // store에 있는 모든 값들을 꺼내서 새로운 ArrayList 에 담아서 넘겨줌

    public void clearStore(){
        store.clear();
    } // 이거는 그냥 TEST에서 쓰는건데 store 를 다 날려보냄

}
