package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.ExemplarCarte;

import java.util.List;

public interface RepositoryExemplarCarte extends Repository<ExemplarCarte>{
    List<ExemplarCarte> findExemplarByCarte(Integer idCarte);
    List<ExemplarCarte> findExemplareDisponibile(Integer idCarte);
    List<ExemplarCarte> findExemplareImprumutate(Integer idCarte);
}
