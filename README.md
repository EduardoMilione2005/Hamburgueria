# 🍔 Hamburgueria Sabor na Brasa

Sistema de gerenciamento de pedidos para hamburgueria, desenvolvido em Java 17 com Maven. O projeto aplica **6 padrões de projeto** do catálogo GoF em cenários reais de uma hamburgueria.

---

## 📋 Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Como Executar](#como-executar)
- [Padrões de Projeto](#padrões-de-projeto)
  - [Singleton](#1-singleton)
  - [Factory Method](#2-factory-method)
  - [Abstract Factory](#3-abstract-factory)
  - [Decorator](#4-decorator)
  - [Bridge](#5-bridge)
  - [Chain of Responsibility](#6-chain-of-responsibility)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Testes](#testes)

---

## Visão Geral

O sistema permite ao atendente de uma hamburgueria:

- Navegar pelo cardápio e montar pedidos personalizados
- Adicionar extras e ingredientes adicionais a cada burger
- Vincular clientes cadastrados a pedidos
- Confirmar ou cancelar pedidos com validação automática
- Receber notificações via WhatsApp ou e-mail
- Montar combos prontos (Executivo, Premium, Veggie)
- Consultar histórico de pedidos confirmados

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Maven | 3.x |
| JUnit Jupiter | 5.10.0 |

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.6+

### Compilar e rodar

```bash

git clone https://github.com/seu-usuario/hamburgueria.

cd hamburgueria

mvn clean compile

mvn exec:java -Dexec.mainClass="hamburgueria.ui.MenuConsole"
```

### Gerar o JAR executável

```bash
mvn clean package
java -jar target/sabor-na-brasa-1.0.0.jar
```

---

## Padrões de Projeto

### 1. Singleton

**Localização:** `hamburgueria/singleton/GerenciadorDePedidos.java`

**Contexto:** A hamburgueria precisa de um único ponto de controle do caixa — registrar todos os pedidos confirmados e acumular o faturamento total sem duplicações.

**Implementação:** `GerenciadorDePedidos` usa o idioma *double-checked locking* com `volatile` para garantir uma única instância thread-safe.

```java
public static GerenciadorDePedidos getInstance() {
    if (instancia == null) {
        synchronized (GerenciadorDePedidos.class) {
            if (instancia == null) instancia = new GerenciadorDePedidos();
        }
    }
    return instancia;
}
```

**Benefício:** Garante consistência do faturamento mesmo em ambientes concorrentes, sem necessidade de passar a instância entre classes.

---

### 2. Factory Method

**Localização:** `hamburgueria/factorymethod/`

**Contexto:** Um pedido pode ser de balcão, delivery ou agendado — cada tipo tem regras diferentes (taxa de entrega, horário obrigatório, etc.).

**Implementação:** `PedidoCreator` é a classe abstrata com o método fábrica `criarPedido()`. As subclasses concretas (`PedidoBalcaoCreator`, `PedidoDeliveryCreator`, `PedidoAgendadoCreator`) decidem qual subtipo de `Pedido` instanciar.

```
PedidoCreator (abstract)
├── PedidoBalcaoCreator   → cria PedidoBalcao
├── PedidoDeliveryCreator → cria PedidoDelivery (+R$6,90)
└── PedidoAgendadoCreator → cria PedidoAgendado (com horário)
```

**Benefício:** Adicionar um novo tipo de pedido (ex.: drive-thru) requer apenas uma nova subclasse, sem alterar código existente.

---

### 3. Abstract Factory

**Localização:** `hamburgueria/abstractfactory/`

**Contexto:** A hamburgueria oferece combos prontos (Executivo, Premium, Veggie). Cada combo agrupa um burger + acompanhamento + bebida compatíveis entre si.

**Implementação:** `ComboFactory` define a interface da fábrica abstrata. Cada fábrica concreta garante que os produtos criados são consistentes entre si.

```
ComboFactory (interface)
├── ComboExecutivoFactory → Smash Classic + Batata Frita + Refrigerante
├── ComboPremiumFactory   → Truffle & Shroom + Onion Rings + Milk Shake
└── ComboVeggieFactory    → Veggie Verde + Onion Rings + Refrigerante
```

**Benefício:** O cliente do código não precisa saber quais produtos compõem cada combo — a fábrica garante coerência.

---

### 4. Decorator

**Localização:** `hamburgueria/decorator/`

**Contexto:** Cada burger pode ser personalizado com ingredientes extras (bacon, queijo, molho especial, ovo frito). A combinação deve ser flexível e cada ingrediente acumula preço e descrição.

**Implementação:** `Ingrediente` é a interface base. `BurgerBase` é o componente concreto. `IngredienteDecorator` é o decorador abstrato. Cada ingrediente extra é um decorador concreto que envolve o objeto anterior.

```
Ingrediente (interface)
└── BurgerBase (componente)
└── IngredienteDecorator (abstract)
    ├── BaconExtra    (+R$5,00)
    ├── QueijoExtra   (+R$3,50)
    ├── MolhoEspecial (+R$2,00)
    └── OvoFrito      (+R$4,00)
```

Um `BurgerPersonalizadoBuilder` oferece uma API fluente sobre os decoradores.

**Benefício:** Qualquer combinação de ingredientes é possível sem explosão de subclasses.

---

### 5. Bridge

**Localização:** `hamburgueria/bridge/`

**Contexto:** A hamburgueria notifica cliente e cozinha via diferentes canais (WhatsApp, e-mail). O *conteúdo* da notificação (confirmação, cancelamento) e o *canal* de envio variam de forma independente.

**Implementação:** `Notificador` (abstração) e `CanalNotificacao` (implementação) são hierarquias separadas conectadas por composição.

```
Notificador (abstração)          CanalNotificacao (implementação)
├── NotificadorCliente     ──────├── CanalWhatsApp
└── NotificadorCozinha     ──────└── CanalEmail
```

**Benefício:** Adicionar um canal (ex.: SMS) não exige alterar nenhum `Notificador`, e vice-versa.

---

### 6. Chain of Responsibility

**Localização:** `hamburgueria/chainofresponsibility/`

**Contexto:** Antes de confirmar um pedido, o sistema precisa passar por diversas regras de validação. Essas regras devem ser aplicadas em sequência, e qualquer falha interrompe o processo com uma mensagem clara.

**Implementação:** `ValidacaoPedidoHandler` é o handler abstrato. Cada handler concreto verifica uma regra e, se aprovada, delega ao próximo elo da cadeia. `ValidacaoCadeia` monta e expõe a cadeia completa.

```
ValidacaoPedidoHandler (abstract)
├── ValidarStatusPedidoHandler    → pedido deve estar ABERTO
├── ValidarItensPedidoHandler     → ao menos 1 item
├── ValidarValorMinimoHandler     → total ≥ R$20,00
├── ValidarLimiteItensHandler     → no máximo 10 itens
└── ValidarClienteDeliveryHandler → avisa se sem cliente (balcão)
```

**Uso no `PedidoService`:**

```java
ValidacaoCadeia.obter().validar(pedidoAtual);
pedidoAtual.confirmar();
```

**Saída no console ao confirmar:**
```
--- Validando pedido (Chain of Responsibility) ---
✔ [Validação] Status: ABERTO — pedido elegível para confirmação.
✔ [Validação] Itens: OK (2 item(s))
✔ [Validação] Valor mínimo: OK (R$ 63.80)
✔ [Validação] Limite de itens: OK
✔ [Validação] Cliente vinculado: Ana Souza
--- Validação concluída com sucesso ---
```

**Benefício:** Novas regras de validação são adicionadas criando um novo handler e inserindo-o na cadeia — sem alterar o `PedidoService` nem os handlers existentes. Cada handler tem responsabilidade única e bem definida.

---

## Estrutura do Projeto

```
src/
└── main/java/hamburgueria/
    ├── chainofresponsibility/       # Padrão Chain of Responsibility (validação)
    │   ├── ValidacaoPedidoHandler.java   ← handler abstrato
    │   ├── ValidacaoHandlers.java        ← handlers concretos + ValidacaoCadeia
    │   └── ValidacaoPedidoException.java ← exceção customizada
    ├── abstractfactory/             # Padrão Abstract Factory (combos)
    │   ├── ComboFactory.java
    │   └── ComboFactoryProvider.java
    ├── bridge/                      # Padrão Bridge (notificações)
    │   ├── CanalNotificacao.java
    │   ├── CanalWhatsApp.java
    │   ├── CanalEmail.java
    │   ├── Notificador.java
    │   └── NotificadorFactory.java
    ├── decorator/                   # Padrão Decorator (ingredientes)
    │   ├── Ingrediente.java
    │   ├── BurgerBase.java
    │   ├── IngredienteDecorator.java
    │   └── BurgerPersonalizadoBuilder.java
    ├── factorymethod/               # Padrão Factory Method (tipos de pedido)
    │   ├── PedidoCreator.java
    │   └── PedidoBalcaoCreator.java
    ├── singleton/                   # Padrão Singleton (caixa)
    │   └── GerenciadorDePedidos.java
    ├── model/                       # Entidades de domínio
    │   ├── Burger.java
    │   ├── Cliente.java
    │   ├── Extra.java
    │   ├── ItemPedido.java
    │   └── Pedido.java
    ├── repository/                  # Repositórios em memória
    │   ├── CardapioRepository.java
    │   └── ClienteRepository.java
    ├── service/                     # Lógica de negócio
    │   ├── PedidoService.java
    │   └── ClienteService.java
    └── ui/
        └── MenuConsole.java         # Interface de linha de comando

src/test/java/hamburgueria/
    ├── ValidacaoCadeiaTest.java     # Testes do Chain of Responsibility
    ├── BurgerTest.java
    ├── ClienteTest.java
    ├── PedidoServiceTest.java
    ├── PedidoTest.java
    └── PedroesTest.java
```

---

## Testes

```bash
mvn test

mvn test -Dtest=ValidacaoCadeiaTest
```

Os testes cobrem os cenários principais do Chain of Responsibility:

| Teste | Cenário |
|---|---|
| `pedidoVazioDeveLancarExcecao` | Pedido sem itens é rejeitado |
| `pedidoAbaixoDoMinimoDeveLancarExcecao` | Total abaixo de R$20,00 é rejeitado |
| `pedidoBalcaoValidoDevePassar` | Pedido de balcão válido passa em toda a cadeia |
| `pedidoComClienteValidoDevePassar` | Pedido com cliente vinculado passa em toda a cadeia |
| `pedidoComItensAcimaDoLimiteDeveLancarExcecao` | Mais de 10 itens é rejeitado |
| `pedidoJaConfirmadoDeveLancarExcecao` | Pedido já confirmado é rejeitado pelo status |

---

## Resumo dos Padrões

| Padrão | Categoria | Problema resolvido |
|---|---|---|
| Singleton | Criacional | Único ponto de controle do caixa |
| Factory Method | Criacional | Criação de tipos diferentes de pedido |
| Abstract Factory | Criacional | Montagem de combos coerentes |
| Decorator | Estrutural | Personalização dinâmica de burgers |
| Bridge | Estrutural | Separação entre notificação e canal |
| Chain of Responsibility | Comportamental | Validação sequencial de pedidos |