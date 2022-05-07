package userservice.api.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {
    private int id;
    private String firstName;
    private String secondName;
    private String password;
    private int age;
    private Role role;

    public User(int id, String firstName, String secondName, int age, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.role = role;
    }
}
