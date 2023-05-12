package hello.project.article.exception;

import hello.project.comm.exception.CustomException;

public class ArticleException extends CustomException {

    public ArticleException(ArticleErrorCode message) {
        super(String.valueOf(message));
    }
}
