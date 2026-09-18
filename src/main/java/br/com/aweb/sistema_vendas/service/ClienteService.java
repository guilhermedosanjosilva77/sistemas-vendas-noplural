package br.com.aweb.sistema_vendas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.aweb.sistema_vendas.model.Cliente;
import br.com.aweb.sistema_vendas.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // CREATE
    @Transactional
    public Cliente salvar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteSalvo;
    }

    // READ
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes;
    }

    public Optional<Cliente> buscarPorId(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        return optionalCliente;
    }

    // UPDATE
    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (!optionalCliente.isPresent()) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        Cliente clienteExistente = optionalCliente.get();

        // valida e-mail se alterado
        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail())) {
            if (clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
                throw new IllegalArgumentException("E-mail já cadastrado.");
            }
        }

        // valida CPF se alterado
        if (!clienteExistente.getCpf().equals(clienteAtualizado.getCpf())) {
            if (clienteRepository.existsByCpf(clienteAtualizado.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }
        }

        // atualiza os campos
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setLogradouro(clienteAtualizado.getLogradouro());
        clienteExistente.setNumero(clienteAtualizado.getNumero());
        clienteExistente.setComplemento(clienteAtualizado.getComplemento());
        clienteExistente.setBairro(clienteAtualizado.getBairro());
        clienteExistente.setCidade(clienteAtualizado.getCidade());
        clienteExistente.setUf(clienteAtualizado.getUf());
        clienteExistente.setCep(clienteAtualizado.getCep());

        Cliente clienteSalvo = clienteRepository.save(clienteExistente);
        return clienteSalvo;
    }

    // DELETE
    @Transactional
    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

}
