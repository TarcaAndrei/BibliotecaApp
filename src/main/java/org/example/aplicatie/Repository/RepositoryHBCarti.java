package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Carte;
import org.example.aplicatie.Domain.Carte;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class RepositoryHBCarti implements RepositoryCarte{
    private SessionFactory sessionFactory;

    public RepositoryHBCarti(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Carte> findCarteTitluAutor(String titlu, String autor) {
        try(var session = sessionFactory.openSession()){
            var query = session.createQuery("SELECT C FROM Carte C WHERE C.titlu LIKE :titlu AND C.autor LIKE :autor", Carte.class);
            query.setParameter("titlu", '%' + titlu + '%');
            query.setParameter("autor", '%' + autor + '%');
            return query.list();
        }
        catch (Exception e){
            return List.of();
        }
    }

    @Override
    public Optional<Carte> save(Carte entity) {
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
    public Optional<Carte> update(Carte entity) {
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
    public Optional<Carte> delete(Integer idEntity) {
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
    public Optional<Carte> findOne(Integer idEntity) {
        try(var session = sessionFactory.openSession()){
            var cititor = session.get(Carte.class, idEntity);
            return Optional.ofNullable(cititor);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Carte> findAll() {
        try(var session = sessionFactory.openSession()){
            return session.createQuery("SELECT C FROM Carte C", Carte.class).list();
        }
        catch (Exception e){
            return List.of();
        }
    }
}
