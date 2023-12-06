package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.CredencialDTO;
import br.com.tech.challenge.domain.dto.TokenDTO;
import br.com.tech.challenge.domain.dto.UsuarioDTO;
import br.com.tech.challenge.servicos.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para controle de Usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final ModelMapper mapper;

    @Operation(description = "Endpoint para criar um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Usuário inválido."),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro no servidor.")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> save(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.map(usuarioService.save(usuarioDTO), UsuarioDTO.class));
    }

    @Operation(description = "Endpoint para gerar token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token criado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor")
    })
    @PostMapping("/auth")
    public ResponseEntity<TokenDTO> authenticate(@RequestBody CredencialDTO credencialDTO) {
        return ResponseEntity.ok(usuarioService.authenticate(credencialDTO));
    }

}
