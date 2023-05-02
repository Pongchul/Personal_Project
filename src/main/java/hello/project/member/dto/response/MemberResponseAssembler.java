package hello.project.member.dto.response;

import hello.project.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponseAssembler {

    public static MyInfoResponse myInfoResponse(Member member) {
        return new MyInfoResponse(member.getId(), member.getUserId(), member.getUserName());
    }

    public static MemberResponse memberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getUserName());
    }

    public static List<MemberResponse> memberResponses(List<Member> members) {
        return members.stream()
                .map(MemberResponseAssembler::memberResponse)
                .collect(Collectors.toList());
    }
}
