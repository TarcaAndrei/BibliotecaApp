package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Administrator;
import org.example.aplicatie.Domain.Administrator;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBAdministratori implements RepositoryUtilizator<Administrator> {
    private SessionFactory sessionFactory;

    public RepositoryHBAdministratori(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Administrator> findByUsername(String username) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT A FROM Administrator A WHERE A.username = :username", Administrator.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Administrator> findByUsernameAndPassword(String username, String password) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT A FROM Administrator A WHERE A.username = :username AND A.password = :password", Administrator.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Administrator> save(Administrator entity) {
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
    public Optional<Administrator> update(Administrator entity) {
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
    public Optional<Administrator> delete(Integer idEntity) {
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
    public Optional<Administrator> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Administrator.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Administrator> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT A FROM Administrator A", Administrator.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
