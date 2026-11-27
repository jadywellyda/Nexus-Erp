package com.nexuserp.repository;

import com.nexuserp.domain.BaseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public abstract class GenericRepository<T extends BaseEntity> {

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepository() {
        this.entityClass = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session openSession() {
        return JpaUtil.getSessionFactory().openSession();
    }

    public T salvar(T entity) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        }
    }

    public void atualizar(T entity) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        }
    }

    public void remover(Long id) {
        try (Session session = openSession()) {
            Transaction tx = session.beginTransaction();
            T entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        }
    }

    public Optional<T> buscarPorId(Long id) {
        try (Session session = openSession()) {
            T entity = session.find(entityClass, id);
            return Optional.ofNullable(entity);
        }
    }

    public List<T> listarTodos() {
        try (Session session = openSession()) {
            return session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
        }
    }
}
