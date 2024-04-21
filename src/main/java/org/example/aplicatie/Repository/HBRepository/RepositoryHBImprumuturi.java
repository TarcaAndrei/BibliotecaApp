package org.example.aplicatie.Repository.HBRepository;

import org.example.aplicatie.Domain.Imprumut;
import org.example.aplicatie.Domain.Imprumut;
import org.example.aplicatie.Repository.RepositoryImprumut;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBImprumuturi implements RepositoryImprumut {
    private SessionFactory sessionFactory;
    
    public RepositoryHBImprumuturi(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Optional<Imprumut> returneazaImprumut(Integer idImprumut) {
        var imprumutOpt = findOne(idImprumut);
        if(imprumutOpt.isEmpty()){
            return Optional.empty();
        }
        var imprumut = imprumutOpt.get();
        imprumut.setReturnat(true);
        return update(imprumut);
    }

    @Override
    public List<Imprumut> getImprumuturiNereturnate() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT I FROM Imprumut I WHERE I.returnat = false", Imprumut.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Boolean eImprumutat(Integer idImprumut) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT COUNT(I) FROM Imprumut I WHERE I.id = :idImprumut AND I.returnat = false", Long.class);
            query.setParameter("idImprumut", idImprumut);
            return query.uniqueResult() > 0;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public List<Imprumut> getImprumuturiByExemplar(Integer idExemplarCarte) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT I FROM Imprumut I WHERE I.exemplarCarte.id = :idExemplarCarte", Imprumut.class);
            query.setParameter("idExemplarCarte", idExemplarCarte);
            return query.list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<Imprumut> save(Imprumut entity) {
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Imprumut> update(Imprumut entity) {
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return Optional.of(entity);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Imprumut> delete(Integer idEntity) {
        var biblioOpt = findOne(idEntity);
        if(biblioOpt.isEmpty()){
            return Optional.empty();
        }
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.delete(biblioOpt.get());
            transaction.commit();
            return biblioOpt;
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Imprumut> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Imprumut.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Imprumut> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM Imprumut C", Imprumut.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
