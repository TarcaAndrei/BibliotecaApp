package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Imprumut;

import java.util.List;
import java.util.Optional;

public interface RepositoryImprumut extends Repository<Imprumut>{
    Optional<Imprumut> returneazaImprumut(Integer idImprumut);
    List<Imprumut> getImprumuturiNereturnate();
    Boolean eImprumutat(Integer idExemplarCarte);
    List<Imprumut> getImprumuturiByExemplar(Integer idExemplarCarte);
}
