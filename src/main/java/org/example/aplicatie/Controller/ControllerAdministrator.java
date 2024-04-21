package org.example.aplicatie.Controller;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.aplicatie.Domain.Administrator;
import org.example.aplicatie.Domain.Carte;
import org.example.aplicatie.Domain.ExemplarCarte;
import org.example.aplicatie.Domain.Status;
import org.example.aplicatie.Observer.EventType;
import org.example.aplicatie.Observer.Observer;
import org.example.aplicatie.Service.ServiceApp;

public class ControllerAdministrator implements Observer {
    public TextField titluCauta;
    public TextField autorCauta;
    public TableView<Carte> tabelMain;
    public TableColumn<Carte, String> coloanaTitlu;
    public TableColumn<Carte, String> coloanaAutor;
    public TextField titluField;
    public TextField autorField;
    public TableView<ExemplarCarte> tabelExemplare;
    public TableColumn<ExemplarCarte, String> coloanaExemplar;
    public TableColumn<ExemplarCarte, Status> coloanaStatus;
    public TextField exemplarField;
    private ServiceApp serviceApp;
    private Carte carteSelectata;
    private Administrator administrator;
    private String titluu;
    private String autorr;

    ObservableList<Carte> modelTabel = FXCollections.observableArrayList();
    ObservableList<ExemplarCarte> modelTabelExemplare = FXCollections.observableArrayList();

    public void setServiceApp(ServiceApp serviceApp, Administrator administrator){
        this.serviceApp = serviceApp;
        this.administrator = administrator;
        titluu = "";
        autorr = "";
        this.loadTables();
        this.serviceApp.addObserver(this);
        carteSelectata = null;
    }

    private void loadTables(){
        coloanaTitlu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitlu()));
        coloanaAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        coloanaExemplar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodExemplar()));
        coloanaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tabelMain.setItems(modelTabel);
        tabelExemplare.setItems(modelTabelExemplare);
        this.reloadTableMain();
        tabelMain.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) ->{
            if(newValue != null){
                carteSelectata = newValue;
                this.reloadTableExemplare();
                titluField.setText(newValue.getTitlu());
                autorField.setText(newValue.getAutor());
            }
            else{
                modelTabelExemplare.clear();
                titluField.clear();
                autorField.clear();
            }
        });

        tabelExemplare.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) ->{
            if(newValue != null){
                exemplarField.setText(newValue.getCodExemplar());
            }
            else{
                exemplarField.clear();
            }
        });
    }

    private void reloadTableMain(){
        modelTabel.setAll(serviceApp.getCartiFiltrate(titluu, autorr));
    }

    private void reloadTableExemplare(){
        if(carteSelectata != null) {
            modelTabelExemplare.setAll(serviceApp.getExemplareCarte(carteSelectata));
        }
        else{
            modelTabelExemplare.clear();
        }
    }


    public void handlerFiltreaza(ActionEvent actionEvent) {
        titluu = titluCauta.getText();
        autorr = autorCauta.getText();
        this.reloadTableMain();
        this.titluCauta.clear();
        this.autorCauta.clear();
    }

    private void clearFields(){
        titluField.clear();
        autorField.clear();
        exemplarField.clear();
    }


    public void handlerAdaugaCarte(ActionEvent actionEvent) {
        var titlu = titluField.getText();
        var autor = autorField.getText();
        if(titlu.isEmpty() || autor.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Completati toate campurile");
            return;
        }
        try{
            serviceApp.adaugaCarte(titlu, autor);
            clearFields();
            reloadTableMain();

//            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "Carte adaugata cu succes");
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void handlerModificaCarte(ActionEvent actionEvent) {
        var titlu = titluField.getText();
        var autor = autorField.getText();
        var carte = tabelMain.getSelectionModel().getSelectedItem();
        if(carte == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Selectati o carte");
            return;
        }
        if(titlu.isEmpty() || autor.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Completati toate campurile");
            return;
        }
        try {
            serviceApp.modificaCarte(carte.getId(), titlu, autor);
            clearFields();
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void handlerStergeCarte(ActionEvent actionEvent) {
        var carte = tabelMain.getSelectionModel().getSelectedItem();
        if(carte == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Selectati o carte");
            return;
        }
        try {
            serviceApp.stergeCarte(carte.getId());
            clearFields();
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void hanlderAdaugaExemplar(ActionEvent actionEvent) {
        var codExemplar = exemplarField.getText();
        var carte = tabelMain.getSelectionModel().getSelectedItem();
        if(carte == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Selectati o carte");
            return;
        }
        if(codExemplar.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Completati toate campurile");
            return;
        }
        try {
            serviceApp.adaugaExemplar(carte.getId(), codExemplar);
            clearFields();
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void handlerModificaExemplar(ActionEvent actionEvent) {
        var exemplarCarte = tabelExemplare.getSelectionModel().getSelectedItem();
        if(exemplarCarte == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Selectati un exemplar");
            return;
        }
        var codExemplar = exemplarField.getText();
        if(codExemplar.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Completati toate campurile");
            return;
        }
        try{
            serviceApp.modificaExemplar(exemplarCarte.getId(), codExemplar);
            clearFields();
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void handlerStergeExemplar(ActionEvent actionEvent) {
        var exemplarCarte = tabelExemplare.getSelectionModel().getSelectedItem();
        if(exemplarCarte == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING, "Atentie", "Selectati un exemplar");
            return;
        }
        try{
            serviceApp.stergeExemplar(exemplarCarte.getId());
            clearFields();
        }
        catch (Exception ex){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    @Override
    public void update(EventType e) {
        switch (e){
            case IMPRUMUT, RETURNARE, MODIFICARE_EXEMPLAR_CARTE:
                reloadTableExemplare();
                break;
            case MODIFICARE_CARTE:
                reloadTableMain();
                break;
        }
    }
}
