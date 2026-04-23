package hamburgueria.ui;

import hamburgueria.model.*;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;
import hamburgueria.service.ClienteService;
import hamburgueria.service.PedidoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuConsole {

    private final Scanner scanner = new Scanner(System.in);
    private final CardapioRepository cardapioRepo = new CardapioRepository();
    private final ClienteRepository clienteRepo = new ClienteRepository();
    private final PedidoService pedidoService = new PedidoService(cardapioRepo, clienteRepo);
    private final ClienteService clienteService = new ClienteService(clienteRepo);

    public void iniciar() {
        System.out.println("====================================");
        System.out.println("  🍔 Hamburgueria Sabor na Brasa  ");
        System.out.println("====================================\n");

        boolean rodando = true;
        while (rodando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Escolha: ");
            switch (opcao) {
                case 1 -> menuCardapio();
                case 2 -> menuPedidoAtual();
                case 3 -> menuClientes();
                case 4 -> exibirHistorico();
                case 0 -> rodando = false;
                default -> System.out.println("Opção inválida.\n");
            }
        }
        System.out.println("Obrigado pela preferência! 🍔");
    }

    private void exibirMenuPrincipal() {
        Pedido p = pedidoService.getPedidoAtual();
        System.out.println("--- MENU PRINCIPAL ---");
        System.out.printf("[1] Cardápio%n[2] Pedido atual (%d itens | R$ %.2f)%n[3] Clientes%n[4] Histórico de pedidos%n[0] Sair%n",
                p.getItens().size(), p.calcularTotal());
        System.out.println();
    }

    private void menuCardapio() {
        System.out.println("\n--- CARDÁPIO ---");
        List<Burger> burgers = cardapioRepo.listarBurgers();
        burgers.forEach(b -> System.out.printf("[%d] %s%n", b.getId(), b));

        int burgerId = lerInteiro("\nEscolha o burger (0 = voltar): ");
        if (burgerId == 0) return;

        cardapioRepo.buscarBurgerPorId(burgerId).ifPresentOrElse(burger -> {
            System.out.println("\n--- ACOMPANHAMENTOS ---");
            List<Extra> extras = cardapioRepo.listarExtras();
            extras.forEach(e -> System.out.printf("[%s] %s%n", e.getId(), e));
            System.out.println("Digite os IDs dos extras separados por vírgula (ou Enter para nenhum):");
            String linha = scanner.nextLine().trim();

            List<String> extrasIds = new ArrayList<>();
            if (!linha.isBlank()) {
                for (String s : linha.split(",")) {
                    extrasIds.add(s.trim());
                }
            }
            try {
                pedidoService.adicionarItem(burgerId, extrasIds);
                System.out.println("✅ Item adicionado ao pedido!\n");
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Erro: " + e.getMessage() + "\n");
            }
        }, () -> System.out.println("Burger não encontrado.\n"));
    }

    private void menuPedidoAtual() {
        Pedido pedido = pedidoService.getPedidoAtual();
        System.out.println("\n--- PEDIDO ATUAL ---");

        if (pedido.estaVazio()) {
            System.out.println("Nenhum item no pedido.\n");
            return;
        }

        System.out.println(pedido);
        System.out.println("[1] Remover item  [2] Vincular cliente  [3] Adicionar observação  [4] Confirmar  [5] Cancelar  [0] Voltar");
        int opcao = lerInteiro("Escolha: ");

        switch (opcao) {
            case 1 -> {
                int idx = lerInteiro("Índice do item a remover (começa em 0): ");
                try {
                    pedidoService.removerItem(idx);
                    System.out.println("Item removido.\n");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Índice inválido.\n");
                }
            }
            case 2 -> {
                clienteService.listarTodos().forEach(c ->
                        System.out.printf("[%d] %s - %d pedidos%n", c.getId(), c.getNome(), c.getTotalPedidos()));
                int clienteId = lerInteiro("ID do cliente: ");
                try {
                    pedidoService.vincularCliente(clienteId);
                    System.out.println("Cliente vinculado.\n");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage() + "\n");
                }
            }
            case 3 -> {
                System.out.print("Observação: ");
                pedidoService.definirObservacao(scanner.nextLine());
                System.out.println("Observação salva.\n");
            }
            case 4 -> {
                try {
                    Pedido confirmado = pedidoService.confirmarPedido();
                    System.out.println("✅ Pedido confirmado!\n");
                    System.out.println(confirmado);
                } catch (IllegalStateException e) {
                    System.out.println("❌ " + e.getMessage() + "\n");
                }
            }
            case 5 -> {
                pedidoService.cancelarPedidoAtual();
                System.out.println("Pedido cancelado.\n");
            }
        }
    }

    private void menuClientes() {
        System.out.println("\n--- CLIENTES ---");
        clienteService.listarTodos().forEach(c ->
                System.out.printf("[%d] %s | %s | Pedidos: %d%n",
                        c.getId(), c.getNome(), c.getTelefone(), c.getTotalPedidos()));
        System.out.println("\n[1] Cadastrar novo cliente  [0] Voltar");
        int opcao = lerInteiro("Escolha: ");
        if (opcao == 1) {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
            System.out.print("Endereço (Enter para pular): ");
            String endereco = scanner.nextLine();
            try {
                Cliente novo = clienteService.cadastrar(nome, telefone, endereco);
                System.out.printf("✅ Cliente '%s' cadastrado com ID %d.%n%n", novo.getNome(), novo.getId());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage() + "\n");
            }
        }
    }

    private void exibirHistorico() {
        List<Pedido> historico = pedidoService.getPedidosConfirmados();
        System.out.println("\n--- HISTÓRICO DE PEDIDOS ---");
        if (historico.isEmpty()) {
            System.out.println("Nenhum pedido confirmado ainda.\n");
            return;
        }
        historico.forEach(System.out::println);
    }

    private int lerInteiro(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print(prompt);
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static void main(String[] args) {
        new MenuConsole().iniciar();
    }
}
