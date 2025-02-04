package ru.itmentor.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;
    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }
    //конфиг для самого спринг секьюрити(какая стр отвечает за вход и тд)
//конфиг авторизации
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()//настройка авторизации
                .antMatchers("/auth/login", "/error").permitAll()//на указанные страницы всех пускаем
                .anyRequest().authenticated()//все любые другие запросы только для аутентифицированных
                .and() //переход к стр логина
                .formLogin()
                .loginPage("/auth/login")//для входа в приложение
                .loginProcessingUrl("/process_login")//название адреса, куда отправляем данные с формы
                .defaultSuccessUrl("/user", true) //после аутент куда переправлять
                .failureUrl("/auth/login?error") //если не успешная аутент
                .successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
    //настраиваем аутентификацию
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }



    // аутентификация inMemory
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
    //шифорование пароля
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}