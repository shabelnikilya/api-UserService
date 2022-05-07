package userservice.api;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {
    /**
     * Для паролей в БД.
     */
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("100"));
        System.out.println(encoder.encode("101"));
        System.out.println(encoder.encode("110"));

        System.out.println(encoder.encode("111"));
    }
}
