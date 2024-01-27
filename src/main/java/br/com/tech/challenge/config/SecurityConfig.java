package br.com.tech.challenge.config;

import br.com.tech.challenge.api.filter.JwtAuthFilter;
import br.com.tech.challenge.servicos.JwtService;
import br.com.tech.challenge.servicos.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;

    private final UsuarioService usuarioService;

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(antMatcher("/clientes/**")).permitAll()
                        .requestMatchers(antMatcher("/produtos/**")).permitAll()
                        .requestMatchers(antMatcher("/pedidos/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/usuarios/**")).hasRole("ADMIN")
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .anyRequest().permitAll())
                .userDetailsService(usuarioService)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Profile("test")
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(antMatcher("/**"));
    }

}


