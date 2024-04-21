package org.example.aplicatie;

import org.example.aplicatie.Domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Optional;

public class MainHibernate {
    private static SessionFactory sessionFactory;

    private static void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
        setUp();
//        Carte carte = new Carte("Titlu", "Autor");
//        carte.setId(15);
//        ExemplarCarte exemplarCarte = new ExemplarCarte(carte, "123", Status.DISPONIBIL);
//        Cititor cititor = new Cititor("utizator", "yG3/R+==", "Nume ", "", "", " Cititor");
//        cititor.setId(5);
//        Bibliotecar bibliotecar = new Bibliotecar("biblio2", "yG3/R+IKZydijPmL8fXvXA==", "Nume Cititooor", "0722222333", "1234567893", "Adresa Cititor");
        try(var session = sessionFactory.openSession()){
            var exemplarCarte = session.get(ExemplarCarte.class, 4);
            System.out.println(exemplarCarte);
//            return Optional.ofNullable(query.uniqueResult());
        }
        catch (Exception e){
//            return Optional.empty();
        }

        tearDown();
    }
}
