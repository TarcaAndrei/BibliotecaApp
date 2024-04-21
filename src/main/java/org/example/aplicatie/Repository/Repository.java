package org.example.aplicatie.Repository;


import org.example.aplicatie.Domain.Entitate;

import java.util.List;
import java.util.Optional;

public interface Repository<E extends Entitate> {
    Optional<E> save(E entity);
    Optional<E> update(E entity);
    Optional<E> delete(Integer idEntity);
    Optional<E> findOne(Integer idEntity);
    List<E> findAll();
}
