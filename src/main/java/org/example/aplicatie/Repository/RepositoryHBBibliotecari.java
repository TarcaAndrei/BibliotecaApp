package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Bibliotecar;
import org.example.aplicatie.Domain.Cititor;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBBibliotecari implements RepositoryUtilizator<Bibliotecar>{
    private SessionFactory sessionFactory;
    @Override
    public Optional<Bibliotecar> findByUsername(String username) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT B FROM Bibliotecar B WHERE B.username = :username", Bibliotecar.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bibliotecar> findByUsernameAndPassword(String username, String password) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT B FROM Bibliotecar B WHERE B.username = :username AND B.password = :password", Bibliotecar.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bibliotecar> save(Bibliotecar entity) {
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
    public Optional<Bibliotecar> update(Bibliotecar entity) {
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
    public Optional<Bibliotecar> delete(Integer idEntity) {
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
    public Optional<Bibliotecar> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Bibliotecar.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Bibliotecar> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT B FROM Bibliotecar B", Bibliotecar.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
