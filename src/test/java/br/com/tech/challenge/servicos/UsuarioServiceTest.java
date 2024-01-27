package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.UsuarioAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import br.com.tech.challenge.domain.enums.Role;
import br.com.tech.challenge.utils.PasswordUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private final UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper mapper;

    private final String SENHA = "123456";

    private final String NOME_USUARIO = "anamaria";

    private final Role ROLE_TESTE = Role.ADMIN;

    private final String HEADER = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    private final String PAYLOAD = "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";

    private final String SIGNATURE = "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioRepository, jwtService, mapper);
    }

    @DisplayName("Deve criar um usuario com sucesso")
    @Test
    void shouldCreateUsuarioSuccess() {

        var usuarioSalvo = this.setUsuario();

        var usuarioDTO = setUsuarioDto();

        when(mapper.map(usuarioDTO,Usuario.class)).thenReturn(usuarioSalvo);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        var usuarioRetornado = usuarioService.save(usuarioDTO);

        assertNotNull(usuarioRetornado);
        assertEquals(10L, usuarioRetornado.getId());
    }

    @DisplayName("Deve lançar exceção ao tentar criar um usuario com usuario já cadastrado")
    @Test
    void validateExistingUser() {
        try {
            when(usuarioRepository.existsByUsuario(any())).thenReturn(Boolean.TRUE);
            usuarioService.save(setUsuarioDto());
        } catch (Exception e) {
            assertEquals(UsuarioAlreadyExistsException.class, e.getClass());
            assertEquals("Usuário já cadastrado.", e.getMessage());
        }
    }

    @DisplayName("Deve gerar um novo token")
    @Test
    void shouldAuthenticate() {
        var credencialDTO = setCredencialDTO();
        var tokenDTO = setTokenDTO();
        var usuario = setUsuario();
        var token = setToken();
        when(usuarioRepository.findByUsuario(NOME_USUARIO)).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any())).thenReturn(token);
        var tokenDTORetornado = usuarioService.authenticate(credencialDTO);
        assertEquals(tokenDTO.getToken(), tokenDTORetornado.getToken());
        assertEquals(tokenDTO.getNomeUsuario(), tokenDTORetornado.getNomeUsuario());
    }

    @DisplayName("Deve carregar usuário por nome")
    @Test
    void shouldLoadUserByUsername() {
        var usuario = setUsuario();
        when(usuarioRepository.findByUsuario(NOME_USUARIO)).thenReturn(Optional.of(usuario));
        var userDetails = usuarioService.loadUserByUsername(NOME_USUARIO);
        assertEquals(usuario.getUsuario(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
    }

    private String setToken() {
        var token = HEADER.concat(".").concat(PAYLOAD).concat(".").concat(SIGNATURE);
        return token;
    }

    private TokenDTO setTokenDTO() {
        return TokenDTO.builder()
                .nomeUsuario(NOME_USUARIO)
                .token(setToken())
                .build();
    }

    private CredencialDTO setCredencialDTO() {
        return CredencialDTO.builder()
                .nomeUsuario(NOME_USUARIO)
                .senha(SENHA)
                .build();
    }

    private Usuario setUsuario() {
        return Usuario.builder()
                .id(10L)
                .usuario(NOME_USUARIO)
                .senha(PasswordUtils.encodePassword(SENHA))
                .role(ROLE_TESTE)
                .build();
    }

    private UsuarioDTO setUsuarioDto() {
        return UsuarioDTO.builder()
                .id(10L)
                .usuario("anamaria")
                .senha(SENHA)
                .role(ROLE_TESTE)
                .build();
    }

}
