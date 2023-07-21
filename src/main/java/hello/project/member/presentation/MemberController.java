package hello.project.member.presentation;


import hello.project.auth.config.AuthenticationPrincipal;
import hello.project.member.service.MemberService;
import hello.project.member.service.dto.request.ChangeNameRequest;
import hello.project.member.service.dto.request.ChangePasswordRequest;
import hello.project.member.service.dto.request.SignUpRequest;
import hello.project.member.service.dto.response.MyInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        memberService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<MyInfoResponse> find(Long id) {
        MyInfoResponse response = memberService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/name")
    public ResponseEntity<Void> updateName(Long id, @RequestBody @Valid ChangeNameRequest request) {
        memberService.updateName(id, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(Long id, @RequestBody @Valid ChangePasswordRequest request) {
        memberService.updatePassword(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long id) {
        memberService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
