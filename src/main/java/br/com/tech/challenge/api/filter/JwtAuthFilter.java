package br.com.tech.challenge.api.filter;

import br.com.tech.challenge.servicos.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    //private final UsuarioServiceImpl usuarioService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer")) {
            String token = authorization.split(" ")[1];
            boolean isValid = jwtService.isTokenValid(token);

            if (isValid) {
                String loginUsuario = jwtService.getLoginUsuario(token);
                // TODO: implementar classe service
//                UserDetails usuario = usuarioService.loadUserByUsername(loginUsuario);
//                UsernamePasswordAuthenticationToken user =
//                        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
//                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }

        filterChain.doFilter(request, response);
    }

}
