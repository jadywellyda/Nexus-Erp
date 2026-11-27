package com.nexuserp.repository;

import com.nexuserp.domain.Venda;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class VendaRepository extends GenericRepository<Venda> {

    public List<Venda> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Venda v where v.dataVenda between :inicio and :fim order by v.dataVenda desc", Venda.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .list();
        }
    }
}
