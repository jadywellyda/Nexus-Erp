package com.nexuserp.repository;

import com.nexuserp.domain.Usuario;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends GenericRepository<Usuario> {

    public Optional<Usuario> buscarPorEmail(String email) {
        try (Session session = JpaUtil.getSessionFactory().openSession()) {
            List<Usuario> result = session.createQuery(
                    "from Usuario where email = :email", Usuario.class)
                    .setParameter("email", email)
                    .list();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        }
    }

    public boolean existsByEmail(String email) {
        return buscarPorEmail(email).isPresent();
    }
}
