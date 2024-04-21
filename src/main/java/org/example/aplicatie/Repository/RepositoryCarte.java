package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Carte;

import java.util.List;

public interface RepositoryCarte extends Repository<Carte> {
    List<Carte> findCarteTitluAutor(String titlu, String autor);
}
