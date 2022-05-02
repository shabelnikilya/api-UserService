package userservice.api;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import user.service.grpc.UserCrudServiceGrpc;

@SpringBootApplication
public class UserApiApplication {

	@Bean
	public UserCrudServiceGrpc.UserCrudServiceBlockingStub getStub(@Value("${grpc.server}") String server) {
		ManagedChannel channel = ManagedChannelBuilder.forTarget(server).usePlaintext().build();
		return UserCrudServiceGrpc.newBlockingStub(channel);
	}

	@Bean
	public Empty getEmpty() {
		return Empty.newBuilder().build();
	}

	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

}
