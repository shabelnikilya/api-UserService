package userservice.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import user.service.grpc.UserCrudServiceGrpc;
import user.service.grpc.UserService;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConf extends WebSecurityConfigurerAdapter {
    UserCrudServiceGrpc.UserCrudServiceBlockingStub stub;

    @Autowired
    public WebSecurityConf(UserCrudServiceGrpc.UserCrudServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/index/find-all").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .and()
                    .logout()
                    .permitAll();
    }

    @Bean
    public UserDetailsService users() {
        return username -> {
            UserService.FindUserByNameRequest userRequest = UserService.FindUserByNameRequest.newBuilder()
                    .setName(username)
                    .build();
            UserService.FindUserResponse response = stub.findUserByName(userRequest);
            List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(response.getUser().getRole()));
            return new User(response.getUser().getSecondName(), response.getUser().getPassword(), authorityList);
        };
    }
}
