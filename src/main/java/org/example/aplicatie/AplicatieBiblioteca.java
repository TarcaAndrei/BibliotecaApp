package org.example.aplicatie;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.aplicatie.Controller.ControllerAutentificare;
import org.example.aplicatie.Domain.Administrator;
import org.example.aplicatie.Domain.Bibliotecar;
import org.example.aplicatie.Domain.Cititor;
import org.example.aplicatie.Repository.*;
import org.example.aplicatie.Repository.DBRepository.RepositoryDBImprumuturi;
import org.example.aplicatie.Repository.HBRepository.*;
import org.example.aplicatie.Service.ServiceApp;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AplicatieBiblioteca extends Application {
    private static SessionFactory sessionFactory;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AplicatieBiblioteca.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Hello!");
        ControllerAutentificare controller = fxmlLoader.getController();
        Properties propertiesDB = new Properties();
        try {
            propertiesDB.load(new FileReader("db.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find db.properties " + e);
        }
//        RepositoryUtilizator<Cititor> cititorRepositoryUtilizator = new RepositoryDBCititori(propertiesDB);
        RepositoryUtilizator<Cititor> cititorRepositoryUtilizator = new RepositoryHBCititori(sessionFactory);

        ///TODO: IN continuare cu celelate entitai
        ///TODO: Eventual un server aside?
        ///Sau de fiecare data cand se deschide aplicatia sa se faca o conexiune la baza de date
        RepositoryUtilizator<Bibliotecar> bibliotecarRepositoryUtilizator = new RepositoryHBBibliotecari(sessionFactory);
//        RepositoryUtilizator<Bibliotecar> bibliotecarRepositoryUtilizator = new RepositoryDBBibliotecari(propertiesDB);
        RepositoryUtilizator<Administrator> administratorRepositoryUtilizator = new RepositoryHBAdministratori(sessionFactory);
//        RepositoryUtilizator<Administrator> administratorRepositoryUtilizator = new RepositoryDBAdministratori(propertiesDB);
        RepositoryCarte repositoryCarte = new RepositoryHBCarti(sessionFactory);
//        RepositoryCarte repositoryCarte = new RepositoryDBCarti(propertiesDB);
//        RepositoryExemplarCarte repositoryExemplareCarti = new RepositoryDBExemplareCarti(propertiesDB, repositoryCarte);
        RepositoryExemplarCarte repositoryExemplareCarti = new RepositoryHBExemplareCarti(sessionFactory);


//        RepositoryImprumut repositoryImprumut = new RepositoryDBImprumuturi(propertiesDB, cititorRepositoryUtilizator, repositoryExemplareCarti);
        RepositoryImprumut repositoryImprumut = new RepositoryHBImprumuturi(sessionFactory);
        ServiceApp serviceApp = new ServiceApp(cititorRepositoryUtilizator, bibliotecarRepositoryUtilizator, administratorRepositoryUtilizator, repositoryCarte, repositoryExemplareCarti, repositoryImprumut);
        controller.setService(serviceApp);
        stage.setScene(scene);
        stage.show();
    }

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
        launch();
    }
}