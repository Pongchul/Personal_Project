package hello.project.auth.support;


public interface PasswordEncoder {

    String encrypt(String password);
}
