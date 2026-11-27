# NexusERP

Sistema Integrado de Gestão Empresarial (ERP) de nível avançado, desenvolvido em Java com interface gráfica JavaFX e banco de dados PostgreSQL integrado via Hibernate/JPA.

## Tecnologias

- **Java 17+**
- **JavaFX 21** (interface gráfica, FXML, CSS)
- **Hibernate 6** (JPA / ORM)
- **PostgreSQL** (banco de dados relacional)
- **Maven** (gerenciamento de dependências e build)
- **jBCrypt** (criptografia de senhas)
- **Logback** (logs)
- **Lombok** (redução de código boilerplate)

## Módulos implementados

1. **Login e segurança** — autenticação, controle de sessão e RBAC (perfis: Administrador, Gerente, Vendedor, Estoque)
2. **Cadastros** — clientes, fornecedores, produtos, categorias e usuários
3. **Vendas** — finalização de venda com baixa automática de estoque
4. **Compras** — registro de compras e entrada de estoque
5. **Estoque** — consulta de estoque, alerta de produto abaixo do mínimo
6. **Financeiro** — contas a pagar e a receber
7. **Relatórios** — vendas por período com geração de PDF (Apache PDFBox)
8. **Auditoria** — tabela de log de alterações

## Estrutura de pastas

```
nexus-erp/
├── pom.xml
├── README.md
├── scripts/
│   └── banco.sql                 # Script de criação do banco PostgreSQL
└── src/
    ├── main/
    │   ├── java/com/nexuserp/
    │   │   ├── domain/           # Entidades JPA (Produto, Venda, Usuario, etc.)
    │   │   ├── repository/       # Camada de persistência (Hibernate)
    │   │   ├── service/          # Regras de negócio
    │   │   ├── presentation/
    │   │   │   ├── controller/   # Controllers JavaFX
    │   │   │   └── view/         # FXML e CSS (copiados para resources)
    │   │   ├── util/             # Utilitários (BCrypt, sessão, navegação, alertas)
    │   │   └── exception/        # Exceções de negócio
    │   └── resources/
    │       ├── hibernate.cfg.xml # Configuração do Hibernate
    │       ├── logback.xml       # Configuração de logs
    │       └── com/nexuserp/presentation/view/
    │           ├── *.fxml        # Telas da interface
    │           └── style.css     # Estilização
    └── test/
```

## Como executar

### 1. Banco de dados

Instale o PostgreSQL e execute o script de criação:

```bash
psql -U postgres -f scripts/banco.sql
```

O script cria o banco `nexuserp`, todas as tabelas e um usuário padrão:
- **E-mail:** `admin@nexuserp.com`
- **Senha:** `admin123`

### 2. Configuração de conexão

Edite o arquivo `src/main/resources/hibernate.cfg.xml` e ajuste usuário/senha do PostgreSQL, se necessário:

```xml
<property name="hibernate.connection.username">seu_usuario</property>
<property name="hibernate.connection.password":"sua_senha</property>
```

### 3. Rodar o projeto

Com o Maven instalado:

```bash
cd nexus-erp
mvn clean javafx:run
```

Ou empacote e execute:

```bash
mvn clean package
java -jar target/nexus-erp-1.0.0.jar
```

> **Nota:** o projeto usa JavaFX com dependências de runtime. O comando `mvn javafx:run` é o mais simples para desenvolvimento.

## Principais padrões de projeto

- **Camadas:** Presentation → Service → Repository → Domain
- **DAO / Repository:** `GenericRepository` abstrato com operações CRUD genéricas
- **Service:** transações e regras de negócio (ex: baixa de estoque ao finalizar venda)
- **Singleton:** `JpaUtil` para gerenciar a `SessionFactory` do Hibernate
- **MVC:** Controllers JavaFX + FXML + CSS

## Funcionalidades destacadas

### 1. Tela de Vendas com Carrinho
- Seleção de cliente e produto via ComboBox
- Adição de itens ao carrinho com controle de estoque em tempo real
- Agrupamento automático de itens repetidos (soma quantidade)
- Cálculo automático de subtotal e total
- Finalização da venda com baixa automática no estoque e geração de `ItemVenda`

### 2. Tela de Compras Completa
- Seleção de fornecedor e produto via ComboBox
- Carrinho de compras com quantidade e preço de custo
- Agrupamento automático de itens repetidos
- Finalização da compra com entrada automática no estoque

### 3. Controle de Estoque (Entrada/Saída)
- Lista completa de produtos com estoque atual vs mínimo
- Registro manual de entrada de estoque (ex: nota fiscal)
- Registro manual de saída de estoque (ex: avaria, doação)
- Histórico de movimentações com usuário, data e referência
- Filtro de produtos com estoque abaixo do mínimo

### 4. Financeiro (Contas a Pagar e Receber)
- Cadastro de contas a receber (cliente, descrição, valor, vencimento)
- Cadastro de contas a pagar (fornecedor, descrição, valor, vencimento)
- Resumo de totais pendentes (a receber e a pagar)
- Listagem de todas as contas com status
- Baixa de conta (marcar como paga/recebida)

### 5. Relatório PDF de Vendas
- Filtro por período (data inicial e final)
- Geração de PDF utilizando Apache PDFBox (licença Apache 2.0)
- Relatório contendo: número da venda, cliente, data, usuário e valor total
- Exibição do faturamento total do período
- Escolha do local para salvar o arquivo PDF

## Observações

- A senha do usuário administrador no script SQL já vem com hash BCrypt (`admin123`).
- O gerador de PDF utiliza fontes Arial do Windows (`C:\Windows\Fonts\arial.ttf`). Se executar em outro sistema operacional, ajuste os caminhos das fontes em `RelatorioService.java`.
