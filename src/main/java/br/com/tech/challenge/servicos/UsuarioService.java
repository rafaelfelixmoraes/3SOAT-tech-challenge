package br.com.tech.challenge.servicos;

import br.com.tech.challenge.api.exception.UsuarioAlreadyExistsException;
import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.tech.challenge.utils.PasswordUtils.encodePassword;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ModelMapper mapper;

    @Transactional
    public Usuario save(UsuarioDTO usuarioDTO) {
        validaUsuario(usuarioDTO.getUsuario());
        usuarioDTO.setSenha(encodePassword(usuarioDTO.getSenha()));
        return usuarioRepository.save(mapper.map(usuarioDTO, Usuario.class));
    }

    private void validaUsuario(String usuario) {
        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new UsuarioAlreadyExistsException("Usuário já cadastrado.");
        }
    }
}
