package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Utilizator;

import java.util.Optional;

public interface RepositoryUtilizator<U extends Utilizator> extends Repository<U>{
    Optional<U> findByUsername(String username);
    Optional<U> findByUsernameAndPassword(String username, String password);
}
