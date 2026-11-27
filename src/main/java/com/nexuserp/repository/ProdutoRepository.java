package com.nexuserp.repository;

import com.nexuserp.domain.Produto;
import org.hibernate.Session;

import java.util.List;

public class ProdutoRepository extends GenericRepository<Produto> {

    public boolean existsBySku(String sku) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            List<Produto> result = session.createQuery(
                    "from Produto where sku = :sku", Produto.class)
                    .setParameter("sku", sku)
                    .list();
            return !result.isEmpty();
        }
    }

    public List<Produto> findByEstoqueBaixo() {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Produto p where p.estoqueAtual <= p.estoqueMinimo and p.ativo = true", Produto.class)
                    .list();
        }
    }
}
