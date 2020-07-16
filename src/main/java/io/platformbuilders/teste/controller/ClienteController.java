package io.platformbuilders.teste.controller;

import io.platformbuilders.teste.model.Cliente;
import io.platformbuilders.teste.repository.ClienteRepository;
import io.platformbuilders.teste.service.ClienteService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> clientes() {

        PageRequest page = PageRequest.of(0, 5, Sort.by("id"));
        Page<Cliente> list = clienteRepository.findAll(page);

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @GetMapping("/page/{pagina}")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> usuarioPagina(@PathVariable("pagina") int pagina) {

        PageRequest page = PageRequest.of(pagina, 5, Sort.by("id"));
        Page<Cliente> list = clienteRepository.findAll(page);

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> detalhar(@PathVariable Long id) {

        Optional<Cliente> cliente = clienteRepository.findById(id);

        return new ResponseEntity<Cliente>(cliente.get(), HttpStatus.OK);
    }

    @GetMapping("/clientePorNome/{nome}")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> clientePorNome(@PathVariable String nome) {
        PageRequest pageRequest = null;
        Page<Cliente> list = null;

        if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
            list = clienteRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
            list = clienteRepository.findClienteByNamePage(nome, pageRequest);
        }

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @GetMapping("/clientePorNome/{nome}/page/{page}")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> clientePorNomePage(@PathVariable("nome") String nome,
                                                            @PathVariable("page") int page) {

        PageRequest pageRequest = null;
        Page<Cliente> list = null;

        if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
            list = clienteRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
            list = clienteRepository.findClienteByNamePage(nome, pageRequest);
        }

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @GetMapping("/clientePorCpf/{cpf}")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> clientePorCpf(@PathVariable String cpf) {
        PageRequest pageRequest = null;
        Page<Cliente> list = null;

        if (cpf == null || (cpf != null && cpf.trim().isEmpty()) || cpf.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(0, 5, Sort.by("cpf"));
            list = clienteRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(0, 5, Sort.by("cpf"));
            list = clienteRepository.findClienteByCpfPage(cpf, pageRequest);
        }

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @GetMapping("/clientePorCpf/{cpf}/page/{page}")
    @CachePut("listaDeClientes")
    public ResponseEntity<Page<Cliente>> clientePorCpfPage(@PathVariable("cpf") String cpf,
                                                           @PathVariable("page") int page) {

        PageRequest pageRequest = null;
        Page<Cliente> list = null;

        if (cpf == null || (cpf != null && cpf.trim().isEmpty()) || cpf.equalsIgnoreCase("undefined")) {
            pageRequest = PageRequest.of(page, 5, Sort.by("cpf"));
            list = clienteRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(page, 5, Sort.by("cpf"));
            list = clienteRepository.findClienteByCpfPage(cpf, pageRequest);
        }

        return new ResponseEntity<Page<Cliente>>(list, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {

        cliente.setIdade(clienteService.calculaIdade(cliente.getDataNascimento()));

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return new ResponseEntity<Cliente>(clienteSalvo, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Cliente> atualizar(@RequestBody Cliente cliente) {

        cliente.setIdade(clienteService.calculaIdade(cliente.getDataNascimento()));

        Cliente save = clienteRepository.save(cliente);

        return new ResponseEntity<Cliente>(save, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizarParte(@PathVariable("id") Long id, @RequestBody Map<Object, Object> campos) {

        Cliente cliente = clienteRepository.findById(id).get();

        Cliente clienteRestModel = clienteService.mapPersistenceModelToRestModel(cliente);

        campos.forEach((campo, value) -> {
            if ("nome".equals(campo)) {
                clienteRestModel.setNome((String) value);
            } else if ("cpf".equals(campo)) {
                clienteRestModel.setCpf((String) value);
            } else if ("dataNascimento".equals(campo)) {

                Date dataNova = null;
                try {
                    dataNova = DateUtils.parseDate((String) value, "yyyy-MM-dd", "dd/MM/yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                clienteRestModel.setDataNascimento(dataNova);
            } else if ("idade".equals(campo)) {
                clienteRestModel.setIdade((Integer) value);
            }
        });

        clienteService.mapRestModelToPersistenceModel(clienteRestModel, cliente);
        cliente.setIdade(clienteService.calculaIdade(cliente.getDataNascimento()));
        Cliente save = clienteRepository.save(cliente);

        return new ResponseEntity<Cliente>(save, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cliente> remover(@PathVariable Long id) {

        clienteRepository.deleteById(id);

        return new ResponseEntity<Cliente>(HttpStatus.OK);
    }

}
