package br.com.aweb.sistema_vendas.model;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Nome é obrigatório.")
    @Column(nullable = false)
    private String nome;
    @NotBlank(message = "E-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank(message = "CPF é obrigatório.")
    @CPF(message = "CPF inválido")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos.")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    @NotBlank(message = "Telefone é obrigatório.")
    @Column(nullable = false)
    private String telefone;
    @NotBlank(message = "Logradouro é obrigatório.")
    @Column(nullable = false)
    private String logradouro;
    private String numero;
    private String complemento;
    @NotBlank(message = "Bairro é obrigatório.")
    @Column(nullable = false)
    private String bairro;
    @NotBlank(message = "Cidade é obrigatório.")
    @Column(nullable = false)
    private String cidade;
    @NotBlank(message = "UF é obrigatório")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres.")
    @Column(nullable = false, length = 2)
    private String uf;
    @NotBlank(message = "CEP é obrigatório")
    @Column(nullable = false)
    private String cep;
}
