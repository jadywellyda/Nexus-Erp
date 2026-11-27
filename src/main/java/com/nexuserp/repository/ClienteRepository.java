package com.nexuserp.repository;

import com.nexuserp.domain.Cliente;
import org.hibernate.Session;

import java.util.List;

public class ClienteRepository extends GenericRepository<Cliente> {

    public boolean existsByCpfCnpj(String cpfCnpj) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            List<Cliente> result = session.createQuery(
                    "from Cliente where cpfCnpj = :cpfCnpj", Cliente.class)
                    .setParameter("cpfCnpj", cpfCnpj)
                    .list();
            return !result.isEmpty();
        }
    }
}
