package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Carte;
import org.example.aplicatie.Domain.ExemplarCarte;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBExemplareCarti implements RepositoryExemplarCarte{
    private SessionFactory sessionFactory;

    public RepositoryHBExemplareCarti(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<ExemplarCarte> findExemplarByCarte(Integer idCarte) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT E FROM ExemplarCarte E WHERE E.carte.id = :idCarte", ExemplarCarte.class);
            query.setParameter("idCarte", idCarte);
            return query.list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public List<ExemplarCarte> findExemplareDisponibile(Integer idCarte) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT E FROM ExemplarCarte E WHERE E.carte.id = :idCarte AND E.status = 'DISPONIBIL'", ExemplarCarte.class);
            query.setParameter("idCarte", idCarte);
            return query.list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public List<ExemplarCarte> findExemplareImprumutate(Integer idCarte) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT E FROM ExemplarCarte E WHERE E.carte.id = :idCarte AND E.status = 'IMPRUMUTAT'", ExemplarCarte.class);
            query.setParameter("idCarte", idCarte);
            return query.list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<ExemplarCarte> save(ExemplarCarte entity) {
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
    public Optional<ExemplarCarte> update(ExemplarCarte entity) {
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
    public Optional<ExemplarCarte> delete(Integer idEntity) {
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
    public Optional<ExemplarCarte> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(ExemplarCarte.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<ExemplarCarte> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM ExemplarCarte C", ExemplarCarte.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
