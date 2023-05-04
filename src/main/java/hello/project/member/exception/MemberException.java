package hello.project.member.exception;

import hello.project.comm.exception.CustomException;

public class MemberException extends CustomException {

    public MemberException(MemberErrorCode message) {
        super(String.valueOf(message));
    }
}
