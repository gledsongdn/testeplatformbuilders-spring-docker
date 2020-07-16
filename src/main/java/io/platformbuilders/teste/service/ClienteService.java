package io.platformbuilders.teste.service;

import io.platformbuilders.teste.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class ClienteService {

    public int calculaIdade(Date dataNasc) {

        Calendar dataNascimento = new GregorianCalendar();
        dataNascimento.setTime(dataNasc);
        Calendar today = Calendar.getInstance();

        int dia = dataNascimento.get(Calendar.DAY_OF_MONTH) + 1;

        dataNascimento.add(Calendar.DAY_OF_MONTH, dia);

        int idade = today.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);
        dataNascimento.add(Calendar.YEAR, idade);

        if (today.before(dataNascimento)) {

            idade--;

        }

        return idade;
    }

    public Cliente mapPersistenceModelToRestModel(Cliente cliente) {
        Cliente cliente1 = new Cliente();
        cliente1.setId(cliente.getId());
        cliente1.setNome(cliente.getNome());
        cliente1.setCpf(cliente.getCpf());
        cliente1.setDataNascimento(cliente.getDataNascimento());
        cliente1.setIdade(cliente.getIdade());

        return cliente1;
    }


    public void mapRestModelToPersistenceModel(Cliente clienteRestModel, Cliente cliente) {
        cliente.setNome(clienteRestModel.getNome());
        cliente.setNome(clienteRestModel.getNome());
        cliente.setCpf(clienteRestModel.getCpf());
        cliente.setDataNascimento(clienteRestModel.getDataNascimento());
        cliente.setIdade(clienteRestModel.getIdade());
    }
}
