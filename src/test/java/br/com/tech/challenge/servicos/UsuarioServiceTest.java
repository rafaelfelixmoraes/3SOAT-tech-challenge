package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.UsuarioAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import br.com.tech.challenge.domain.enums.Role;
import br.com.tech.challenge.utils.PasswordUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

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

    private final Role ROLE_TESTE = Role.ADMIN;

    UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(usuarioRepository, jwtService, mapper);
    }

    @DisplayName("Deve criar um usuario com sucesso")
    @Test
    void shouldCreateUsuarioSuccess() {

        Usuario usuarioSalvo = this.setUsuario();

        UsuarioDTO usuarioDTO = setUsuarioDto();

        when(mapper.map(usuarioDTO,Usuario.class)).thenReturn(usuarioSalvo);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        Usuario usuarioRetornado = usuarioService.save(usuarioDTO);

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

    private Usuario setUsuario() {
        return Usuario.builder()
                .id(10L)
                .usuario("anamaria")
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
