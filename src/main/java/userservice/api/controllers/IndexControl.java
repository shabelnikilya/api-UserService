package userservice.api.controllers;

import com.google.protobuf.Empty;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.service.grpc.UserCrudServiceGrpc;
import user.service.grpc.UserService;
import userservice.api.models.Role;
import userservice.api.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j
@RestController
@RequestMapping("/index")
public class IndexControl {
    private final UserCrudServiceGrpc.UserCrudServiceBlockingStub stub;
    private final Empty empty;

    @Autowired
    public IndexControl(UserCrudServiceGrpc.UserCrudServiceBlockingStub stub, Empty empty) {
        this.stub = stub;
        this.empty = empty;
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
}
