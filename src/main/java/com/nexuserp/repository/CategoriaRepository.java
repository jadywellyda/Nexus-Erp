package com.nexuserp.repository;

import com.nexuserp.domain.Categoria;
import org.hibernate.Session;

import java.util.List;

public class CategoriaRepository extends GenericRepository<Categoria> {

    public boolean existsByNome(String nome) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            List<Categoria> result = session.createQuery(
                    "from Categoria where nome = :nome", Categoria.class)
                    .setParameter("nome", nome)
                    .list();
            return !result.isEmpty();
        }
    }
}
