package io.platformbuilders.teste.repository;

import io.platformbuilders.teste.model.Cliente;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c where c.nome like %?1%")
    List<Cliente> findUserByName(String name);

    @Query("select c from Cliente c where c.cpf like %?1%")
    List<Cliente> findUserByCpf(String cpf);

    default Page<Cliente> findClienteByNamePage(String nome, PageRequest pageRequest) {

        Cliente cliente = new Cliente();
        cliente.setNome(nome);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Cliente> example = Example.of(cliente, exampleMatcher);

        Page<Cliente> retorno = findAll(example, pageRequest);

        return retorno;
    }

    default Page<Cliente> findClienteByCpfPage(String cpf, PageRequest pageRequest) {

        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("cpf", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<Cliente> example = Example.of(cliente, exampleMatcher);

        Page<Cliente> retorno = findAll(example, pageRequest);

        return retorno;
    }
}
