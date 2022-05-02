package userservice.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class User {
    private int id;
    private String firstName;
    private String secondName;
    private int age;
    private Role role;

    public User(String firstName, String secondName, int age, Role role) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.role = role;
    }
}
