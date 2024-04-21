package org.example.aplicatie.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.aplicatie.Domain.Bibliotecar;
import org.example.aplicatie.Domain.Cititor;
import org.example.aplicatie.Domain.ExemplarCarte;
import org.example.aplicatie.Domain.Imprumut;
import org.example.aplicatie.Observer.EventType;
import org.example.aplicatie.Observer.Observer;
import org.example.aplicatie.Service.ServiceApp;

public class ControllerBibliotecar implements Observer {
    public TableColumn<Imprumut, String> coloanaExemplar;
    public TableColumn<Imprumut, String> coloanaTitlu;
    public TableColumn<Imprumut, String> coloanaAutor;

    public TextField exemplField;
    public TextField titluFielld;
    public TextField autorField;
    public TableView<Imprumut> tabelMain;
    public TableColumn<Imprumut, String> coloanaCititor;
    private ServiceApp serviceApp;
    private Bibliotecar bibliotecar;
    private String titluu;
    private String autorr;
    private String codExemplarr;
    ObservableList<Imprumut> modelTabel = FXCollections.observableArrayList();

    public void setServiceApp(ServiceApp serviceApp, Bibliotecar bibliotecar){
        this.serviceApp = serviceApp;
        this.bibliotecar = bibliotecar;
        this.serviceApp.addObserver(this);
        this.loadTables();
    }

    private void loadTables(){
        coloanaExemplar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExemplarCarte().getCodExemplar()));
        coloanaTitlu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExemplarCarte().getCarte().getTitlu()));
        coloanaAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExemplarCarte().getCarte().getAutor()));
        coloanaCititor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCititor().getNume()));
        tabelMain.setItems(modelTabel);
        titluu = "";
        autorr = "";
        codExemplarr = "";
        this.reloadTable();
    }

    private void reloadTable(){
        modelTabel.setAll(serviceApp.getExemplareCarteImprumutate(titluu, autorr, codExemplarr));
    }

    public void handlerImprumuturi(ActionEvent actionEvent) {
        titluu = titluFielld.getText();
        autorr = autorField.getText();
        codExemplarr = exemplField.getText();
        this.reloadTable();
        clearFields();
    }

    private void clearFields(){
        exemplField.clear();
        titluFielld.clear();
        autorField.clear();
    }

    @Override
    public void update(EventType e) {
        reloadTable();
    }

    public void handlerReturneaza(ActionEvent actionEvent) {
        var imprumut = tabelMain.getSelectionModel().getSelectedItem();
        if(imprumut != null){
            try {
                serviceApp.returneazaImprumut(imprumut.getId());
            }
            catch (Exception e){
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", e.getMessage());
            }
        }
        else{
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Eroare", "Nu ati selectat niciun imprumut");
        }
    }
}
