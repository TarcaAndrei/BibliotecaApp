package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Cititor;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBCititori implements RepositoryUtilizator<Cititor>{
    private SessionFactory sessionFactory;

    public RepositoryHBCititori(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Cititor> findByUsername(String username) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT C FROM Cititor C WHERE C.username = :username", Cititor.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Cititor> findByUsernameAndPassword(String username, String password) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT C FROM Cititor C WHERE C.username = :username AND C.password = :password", Cititor.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Cititor> save(Cititor entity) {
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
    public Optional<Cititor> update(Cititor entity) {
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
    public Optional<Cititor> delete(Integer idEntity) {
        var cititorOpt = findOne(idEntity);
        if(cititorOpt.isEmpty())
            return Optional.empty();
        try(var session = sessionFactory.openSession()){
            var transaction = session.beginTransaction();
            session.delete(cititorOpt.get());
            transaction.commit();
            return cititorOpt;
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Cititor> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Cititor.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Cititor> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM Cititor C", Cititor.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
