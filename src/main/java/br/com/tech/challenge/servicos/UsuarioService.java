package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.UserOrPasswordInvalidException;
import br.com.tech.challenge.api.exception.UsuarioAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import br.com.tech.challenge.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final JwtService jwtService;

    private final ModelMapper mapper;

    @Transactional
    public Usuario save(UsuarioDTO usuarioDTO) {
        validateUsuario(usuarioDTO.getUsuario());
        usuarioDTO.setSenha(PasswordUtils.encodePassword(usuarioDTO.getSenha()));
        return usuarioRepository.save(mapper.map(usuarioDTO, Usuario.class));
    }

    private void validateUsuario(String usuario) {
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new UsuarioAlreadyExistsException("Usuário já cadastrado.");
        }
    }

    public TokenDTO authenticate(CredencialDTO credencialDTO) {
        log.info("Autenticando o usuario com as seguintes credenciais {}", credencialDTO);
        UserDetails userDetails = loadUserByUsername(credencialDTO.getNomeUsuario());
        boolean doMatchPasswords = PasswordUtils.passwordsMatch(credencialDTO.getSenha(), userDetails.getPassword());

        log.info("Verificando se as senhas correspondem");
        if (doMatchPasswords) {
            log.info("Senhas correspondem, retornando");
            return TokenDTO.builder()
                    .nomeUsuario(userDetails.getUsername())
                    .token(jwtService.generateToken(userDetails))
                    .build();
        }

        log.info("Senha informada inválida");
        throw new UserOrPasswordInvalidException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(UserOrPasswordInvalidException::new);

        return User.builder()
                .username(usuario.getUsuario())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }

}
