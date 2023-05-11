package hello.project.article.exception;

import hello.project.comm.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleErrorCode implements ErrorCode {

    UNAUTHORIZED_TO_UPDATE(400, "ARTICLE_003", "게시글을 변경할 권한이 없습니다."),

    TITLE_CANNOT_BE_BLANK(400,"ARTICLE_004","제목은 공백일 수 없습니다."),
    TITLE_CANNOT_BE_OUT_OF_RANGE(400,"ARTICLE_005","제목의 이름은 1자 이상 50자 이하여야 합니다."),

    CONTENTS_CANNOT_BE_OUT_OF_RANGE(400, "ARTICLE_006","내용은 1000자 이하여야 합니다."),

    CAPACITY_CANNOT_BE_OUT_OF_RANGE(400, "ARTICLE_007", "인원의 제한이 1명 이상 5명 이하여야 합니다."),
    CAPACITY_CANNOT_BE_LESS_THAN_PARTICIPANTS_SIZE(400, "ARTICLE_008","참가 인원 제한은 현재 참가자의 인원수보다 적을 수 없습니다."),

    ALREADY_PARTICIPANTS_SIZE_FULL(400, "ARTICLE_008","참가인원이 허용인원을 넘었습니다."),
    ALREADY_CLOSED_EARLY(400, "ARTICLE_012","해당 게시글 조기 마감 되었습니다."),


    MEMBER_IS_PARTICIPANT(400, "ARTICLE_009","해당 인원은 참가자입니다."),
    MEMBER_IS_HOST(400,"ARTICLE_010","해당 인원은 방장입니다."),
    MEMBER_IS_NOT_PARTICIPANT(400, "ARTICLE_011", "해당 인원은 참가자가 아닙니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
