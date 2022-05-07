package userservice.api.controllers;

import com.google.protobuf.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import user.service.grpc.UserCrudServiceGrpc;
import user.service.grpc.UserService;
import userservice.api.models.Role;
import userservice.api.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexControl {
    private final UserCrudServiceGrpc.UserCrudServiceBlockingStub stub;
    private final Empty empty;
    private final PasswordEncoder encoder;

    @Autowired
    public IndexControl(UserCrudServiceGrpc.UserCrudServiceBlockingStub stub, Empty empty,
                        PasswordEncoder encoder) {
        this.stub = stub;
        this.empty = empty;
        this.encoder = encoder;
    }

    @GetMapping("/find-all")
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Iterator<UserService.AllUserResponse> responseIterator = stub.allUser(empty);
        while (responseIterator.hasNext()) {
            UserService.AllUserResponse response = responseIterator.next();
            UserService.User userGrpc = response.getUser();
            User user = new User(
                    userGrpc.getId(),
                    userGrpc.getFirstName(),
                    userGrpc.getSecondName(),
                    userGrpc.getAge(),
                    Role.valueOf(userGrpc.getRole())
            );
            users.add(user);
        }
        return users;
    }

    @PostMapping("/save")
    public User saveUser(@RequestBody User user) {
        UserService.User userGrpc = UserService.User
                .newBuilder()
                .setFirstName(user.getFirstName())
                .setSecondName(user.getSecondName())
                .setPassword(encoder.encode(user.getPassword()))
                .setAge(user.getAge())
                .setRole(user.getRole().toString())
                .build();
        UserService.CreateUserRequest request = UserService.CreateUserRequest.newBuilder()
                .setUser(userGrpc)
                        .build();
        UserService.CreateUserResponse response = stub.createUser(request);
        user.setId(response.getUser().getId());
        return user;
    }

    @GetMapping("/findById/{id}")
    public User findUserById(@PathVariable int id) {
        UserService.FindUserByIdRequest request = UserService
                .FindUserByIdRequest
                .newBuilder()
                .setId(id)
                .build();
        UserService.FindUserResponse response = stub.findUserById(request);
        UserService.User userGrpc = response.getUser();
        return new User(
                userGrpc.getId(),
                userGrpc.getFirstName(),
                userGrpc.getSecondName(),
                userGrpc.getAge(),
                Role.valueOf(userGrpc.getRole())
        );
    }

    @GetMapping("/findBySecondName/{secondName}")
    public User findUserBySecondName(@PathVariable String secondName) {
        UserService.FindUserByNameRequest request = UserService.FindUserByNameRequest
                .newBuilder()
                .setName(secondName)
                .build();
        UserService.FindUserResponse response = stub.findUserByName(request);
        UserService.User userGrpc = response.getUser();
        return new User(
                userGrpc.getId(),
                userGrpc.getFirstName(),
                userGrpc.getSecondName(),
                userGrpc.getAge(),
                Role.valueOf(userGrpc.getRole())
        );
    }
}
