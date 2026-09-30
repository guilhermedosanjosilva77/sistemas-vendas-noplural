package br.com.aweb.sistema_vendas.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import br.com.aweb.sistema_vendas.model.Pedido;
import br.com.aweb.sistema_vendas.model.StatusPedido;
import br.com.aweb.sistema_vendas.service.ClienteService;
import br.com.aweb.sistema_vendas.service.PedidoService;
import br.com.aweb.sistema_vendas.service.ProdutoService;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService,
            ClienteService clienteService,
            ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    // LISTAR TODOS OS PEDIDOS
    @GetMapping
    public ModelAndView listarPedidos() {
        return new ModelAndView("pedido/list",
                Map.of("pedidos", pedidoService.listarTodos()));
    }

    // FORMULÁRIO DE NOVO PEDIDO
    @GetMapping("/novo")
    public ModelAndView novoPedidoForm() {
        return new ModelAndView("pedido/form",
                Map.of(
                        "pedido", new Pedido(),
                        "clientes", clienteService.listarTodos(),
                        "produtos", produtoService.listarTodos()));
    }

    // CRIAR NOVO PEDIDO
    @PostMapping("/novo")
    public String criarPedido(@RequestParam Long clienteId) {
        var optionalCliente = clienteService.buscarPorId(clienteId);
        if (!optionalCliente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }

        Pedido pedido = pedidoService.criarPedido(optionalCliente.get());
        return "redirect:/pedidos/edit/" + pedido.getId();
    }

    // FORMULÁRIO DE EDIÇÃO DE PEDIDO
    @GetMapping("/edit/{id}")
    public ModelAndView editarPedidoForm(@PathVariable Long id) {
        var optionalPedido = pedidoService.buscarPorId(id);
        if (!optionalPedido.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Pedido pedido = optionalPedido.get();

        // Não permite editar pedidos cancelados
        if (pedido.getStatus() == StatusPedido.DESATIVADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido cancelado não pode ser editado");
        }

        return new ModelAndView("pedido/edit",
                Map.of(
                        "pedido", pedido,
                        "produtos", produtoService.listarTodos()));
    }

    // ADICIONAR ITEM AO PEDIDO
    @PostMapping("/{pedidoId}/adicionar-item")
    public String adicionarItem(@PathVariable Long pedidoId,
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade) {
        try {
            pedidoService.adicionarItem(pedidoId, produtoId, quantidade);
            return "redirect:/pedidos/edit/" + pedidoId;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // REMOVER ITEM DO PEDIDO
    @PostMapping("/{pedidoId}/remover-item/{itemId}")
    public String removerItem(@PathVariable Long pedidoId,
            @PathVariable Long itemId) {
        try {
            pedidoService.removerItem(pedidoId, itemId);
            return "redirect:/pedidos/edit/" + pedidoId;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // FINALIZAR PEDIDO
    @PostMapping("/{id}/finalizar")
    public String finalizarPedido(@PathVariable Long id) {
        return "redirect:/pedidos";
    }

    // CANCELAR PEDIDO - FORMULÁRIO DE CONFIRMAÇÃO
    @GetMapping("/cancelar/{id}")
    public ModelAndView cancelarPedidoForm(@PathVariable Long id) {
        var optionalPedido = pedidoService.buscarPorId(id);
        if (!optionalPedido.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ModelAndView("pedido/cancelar",
                Map.of("pedido", optionalPedido.get()));
    }

    // CANCELAR PEDIDO - AÇÃO
    @PostMapping("/cancelar/{id}")
    public String cancelarPedido(@PathVariable Long id) {
        try {
            pedidoService.cancelarPedido(id);
            return "redirect:/pedidos";
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // DETALHES DO PEDIDO
    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhesPedido(@PathVariable Long id) {
        var optionalPedido = pedidoService.buscarPorId(id);
        if (!optionalPedido.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ModelAndView("pedido/detalhes",
                Map.of("pedido", optionalPedido.get()));
    }
}