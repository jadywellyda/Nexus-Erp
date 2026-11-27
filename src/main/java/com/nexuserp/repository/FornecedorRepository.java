package com.nexuserp.repository;

import com.nexuserp.domain.Fornecedor;
import org.hibernate.Session;

import java.util.List;

public class FornecedorRepository extends GenericRepository<Fornecedor> {

    public boolean existsByCnpj(String cnpj) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            List<Fornecedor> result = session.createQuery(
                    "from Fornecedor where cnpj = :cnpj", Fornecedor.class)
                    .setParameter("cnpj", cnpj)
                    .list();
            return !result.isEmpty();
        }
    }
}
