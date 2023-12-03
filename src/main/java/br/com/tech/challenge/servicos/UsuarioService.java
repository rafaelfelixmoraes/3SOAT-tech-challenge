package br.com.tech.challenge.servicos;

import br.com.tech.challenge.bd.repositorios.UsuarioRepository;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.domain.entidades.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ModelMapper mapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, ModelMapper mapper) {
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Usuario save(UsuarioDTO usuarioDTO) {
        usuarioDTO.setSenha(bCryptPasswordEncoder.encode(usuarioDTO.getSenha()));
        return usuarioRepository.save(mapper.map(usuarioDTO, Usuario.class));
    }



}
