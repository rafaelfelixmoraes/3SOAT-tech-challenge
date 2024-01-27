package br.com.tech.challenge.api;

import br.com.tech.challenge.domain.dto.ClienteDTO;
import br.com.tech.challenge.domain.dto.RequestClienteCpfDTO;
import br.com.tech.challenge.domain.entidades.Cliente;
import br.com.tech.challenge.servicos.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private ClienteService clienteService;

    private static final String ROTA_CLIENTES = "/clientes";

    @AfterAll
    static void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @DisplayName("Deve salvar um cliente com sucesso")
    @Test
    void shouldSaveClienteSuccess() throws Exception {
        ClienteDTO clienteDTO = setClienteDto();

        mockMvc.perform(post(ROTA_CLIENTES)
                        .content(mapper.writeValueAsString(clienteDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value(clienteDTO.getNome()));

    }

    @DisplayName("Deve retornar erro ao criar um cliente inválido")
    @Test
    void shouldReturnErrorForInvalidCliente() throws Exception {

        ClienteDTO clienteDTO = setClienteDto();

        clienteDTO.setCpf("");
        clienteDTO.setNome("");

        mockMvc.perform(post(ROTA_CLIENTES)
                        .content(mapper.writeValueAsString(clienteDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", Matchers.containsInAnyOrder(
                        "CPF deve ser válido e no formato ###.###.###-##.",
                        "O campo nome é obrigatório.",
                        "Nome deve ter entre 3 e 200 caracteres."
                )));
    }

    @DisplayName("Deve salvar um cliente com sucesso apenas com cpf")
    @Test
    void shouldSaveClienteWithValidCpf() throws Exception {


        RequestClienteCpfDTO clienteCpfDTO = new RequestClienteCpfDTO("136.171.130-28");

        mockMvc.perform(post(ROTA_CLIENTES + "/cpf")
                        .content(mapper.writeValueAsString(clienteCpfDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Deve lançar uma exceção ao salvar um cliente com CPF inválido")
    @Test
    void shouldThrowExceptionWhenSavingClientWithInvalidCPF() throws Exception {
        RequestClienteCpfDTO clienteCpfDTO = new RequestClienteCpfDTO("cpf_invalido");

        mockMvc.perform(post(ROTA_CLIENTES + "/cpf")
                        .content(mapper.writeValueAsString(clienteCpfDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exceptionMessage").value("O CPF deve conter 11 dígitos numéricos."));
    }

    @DisplayName("Deve lançar uma exceção ao salvar um cliente com CPF já existente")
    @Test
    void shouldThrowExceptionWhenSavingClientWithExistingCPF() throws Exception {
        RequestClienteCpfDTO clienteCpfDTO = new RequestClienteCpfDTO(setCliente().getCpf());
        clienteCpfDTO.setCpf("667.743.160-69");
        mockMvc.perform(post(ROTA_CLIENTES + "/cpf")
                        .content(mapper.writeValueAsString(clienteCpfDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("Deve listar os clientes com sucesso")
    @Test
    void shouldListClientSuccess() throws Exception {
        mockMvc.perform(get(ROTA_CLIENTES)
                        .content(mapper.writeValueAsString(setListClientes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(4)));
    }

    @DisplayName("Deve listar os clientes vazios com sucesso")
    @Test
    void shouldListEmptyClientSuccess() throws Exception {
        final var queryParam = String.valueOf(200L);

        mockMvc.perform(get(ROTA_CLIENTES)
                        .param("id", queryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    private Cliente setCliente() {
        return Cliente.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

    private ClienteDTO setClienteDto() {
        return ClienteDTO.builder()
                .id(1L)
                .nome("Anthony Samuel Joaquim Teixeira")
                .email("anthony.samuel.teixeira@said.adv.br")
                .cpf("143.025.400-95")
                .build();
    }

    private List<Cliente> setListClientes() {
        var cliente = Cliente.builder()
                .id(10L)
                .nome("Ana Maria")
                .email("ana.maria@gmail.com")
                .cpf("603.072.360-05")
                .build();
        return Collections.singletonList(cliente);
    }
    private Page<Cliente> createMockPage() {
        List<Cliente> cliente = List.of(setCliente());
        return new PageImpl<>(cliente, PageRequest.of(0, 10), cliente.size());
    }

}
