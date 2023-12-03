package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.ObjectNotFoundException;
import br.com.tech.challenge.api.exception.PasswordInvalidException;
import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final JwtService jwtService;

    private final ModelMapper mapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService, ModelMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.mapper = mapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Usuario save(UsuarioDTO usuarioDTO) {
        log.info("Salvando usuario {}", usuarioDTO);
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioRepository.save(mapper.map(usuarioDTO, Usuario.class));
        usuario.setSenha(null);
        return usuario;
    }

    public TokenDTO authenticate(CredencialDTO credencialDTO) {
        log.info("Autenticando o usuario com as seguintes credenciais {}", credencialDTO);
        UserDetails userDetails = loadUserByUsername(credencialDTO.getNomeUsuario());
        boolean doMatchPasswords = bCryptPasswordEncoder.matches(credencialDTO.getSenha(), userDetails.getPassword());

        log.info("Verificando se as senhas correspondem");
        if (doMatchPasswords) {
            log.info("Senhas correspondem, retornando");
            return TokenDTO.builder()
                    .nomeUsuario(userDetails.getUsername())
                    .token(jwtService.generateToken(userDetails))
                    .build();
        }

        log.info("Senha informada inválida");
        throw new PasswordInvalidException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNomeUsuario(username)
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado."));

        return User.builder()
                .username(usuario.getNomeUsuario())
                .password(usuario.getSenha())
                .roles(usuario.getRole())
                .build();
    }

}
