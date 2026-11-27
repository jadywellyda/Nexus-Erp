-- Banco de dados: nexuserp
-- Execute no PostgreSQL antes de rodar o sistema

CREATE DATABASE IF NOT EXISTS nexuserp;
\c nexuserp

CREATE TABLE perfis (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(200)
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    perfil_id INTEGER REFERENCES perfis(id),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    tipo_pessoa VARCHAR(20) NOT NULL CHECK (tipo_pessoa IN ('FISICA','JURIDICA')),
    nome_razao_social VARCHAR(200) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(150),
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fornecedores (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(200) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    contato VARCHAR(100),
    email VARCHAR(150),
    telefone VARCHAR(20),
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(200) NOT NULL,
    descricao TEXT,
    categoria_id INTEGER REFERENCES categorias(id),
    preco_custo NUMERIC(12,2) NOT NULL DEFAULT 0,
    preco_venda NUMERIC(12,2) NOT NULL DEFAULT 0,
    estoque_minimo INTEGER NOT NULL DEFAULT 0,
    estoque_atual INTEGER NOT NULL DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(30) NOT NULL UNIQUE,
    cliente_id INTEGER REFERENCES clientes(id),
    usuario_id INTEGER REFERENCES usuarios(id),
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) DEFAULT 'FINALIZADA'
);

CREATE TABLE itens_venda (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES vendas(id) ON DELETE CASCADE,
    produto_id INTEGER REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL
);

CREATE TABLE compras (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(30) NOT NULL UNIQUE,
    fornecedor_id INTEGER REFERENCES fornecedores(id),
    usuario_id INTEGER REFERENCES usuarios(id),
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) DEFAULT 'FINALIZADA'
);

CREATE TABLE itens_compra (
    id SERIAL PRIMARY KEY,
    compra_id INTEGER REFERENCES compras(id) ON DELETE CASCADE,
    produto_id INTEGER REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL
);

CREATE TABLE contas_receber (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES clientes(id),
    venda_id INTEGER REFERENCES vendas(id),
    descricao VARCHAR(200),
    valor NUMERIC(12,2) NOT NULL,
    vencimento DATE NOT NULL,
    pagamento DATE,
    status VARCHAR(30) DEFAULT 'PENDENTE'
);

CREATE TABLE contas_pagar (
    id SERIAL PRIMARY KEY,
    fornecedor_id INTEGER REFERENCES fornecedores(id),
    compra_id INTEGER REFERENCES compras(id),
    descricao VARCHAR(200),
    valor NUMERIC(12,2) NOT NULL,
    vencimento DATE NOT NULL,
    pagamento DATE,
    status VARCHAR(30) DEFAULT 'PENDENTE'
);

CREATE TABLE movimentacoes_estoque (
    id SERIAL PRIMARY KEY,
    produto_id INTEGER REFERENCES produtos(id),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA','SAIDA')),
    quantidade INTEGER NOT NULL,
    data_movimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INTEGER REFERENCES usuarios(id),
    referencia VARCHAR(100)
);

CREATE TABLE auditoria_log (
    id SERIAL PRIMARY KEY,
    tabela VARCHAR(100),
    acao VARCHAR(20),
    registro_id INTEGER,
    usuario_id INTEGER REFERENCES usuarios(id),
    data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valores_antigos TEXT,
    valores_novos TEXT
);

-- Dados iniciais
INSERT INTO perfis (nome, descricao) VALUES ('ADMINISTRADOR', 'Acesso total ao sistema');
INSERT INTO perfis (nome, descricao) VALUES ('GERENTE', 'Gerenciamento de vendas e financeiro');
INSERT INTO perfis (nome, descricao) VALUES ('VENDEDOR', 'Vendas e cadastro de clientes');
INSERT INTO perfis (nome, descricao) VALUES ('ESTOQUE', 'Controle de estoque e compras');

-- Senha padrão: admin123
INSERT INTO usuarios (nome, email, senha_hash, ativo, perfil_id)
VALUES ('Administrador', 'admin@nexuserp.com',
        '\$2a\$10\$N.zmdr9k7xOC8tGc9qN0S.iY6u8y7R6t5Y4u3I2o1P0a9S8d7F6g5', TRUE, 1);
