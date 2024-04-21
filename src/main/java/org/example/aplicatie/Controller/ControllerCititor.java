package org.example.aplicatie.Controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.aplicatie.Domain.Carte;
import org.example.aplicatie.Domain.Cititor;
import org.example.aplicatie.Domain.ExemplarCarte;
import org.example.aplicatie.Observer.EventType;
import org.example.aplicatie.Observer.Observer;
import org.example.aplicatie.Service.ServiceApp;

public class ControllerCititor implements Observer {
    public TableView<Carte> tabelCarti;
    public TextField titluSearch;
    public TextField autorSearch;
    public Label titluAutorExemplare;
    public TableView<ExemplarCarte> tabelExemplare;
    public TableColumn<Carte, String> coloanaTitluMain;
    public TableColumn<Carte, String> coloanaAutorMain;
    public TableColumn<ExemplarCarte, String> coloanaExemplar;
    private ServiceApp serviceApp;
    private Carte carte;
    private Cititor cititor;
    private String titluu;
    private String autorr;
    ObservableList<Carte> modelTabelMain = FXCollections.observableArrayList();
    ObservableList<ExemplarCarte> modelTabelSecundar = FXCollections.observableArrayList();

    public void setServiceApp(ServiceApp serviceApp, Cititor cititor){
        this.serviceApp = serviceApp;
        this.cititor = cititor;
        this.serviceApp.addObserver(this);
        titluu = "";
        autorr = "";
        this.loadTables();
    }

    private void loadTables(){
        coloanaTitluMain.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        coloanaAutorMain.setCellValueFactory(new PropertyValueFactory<>("autor"));
//        coloanaAutorMain.setCellValueFactory(cellData -> cellData.getValue().getAutor());
        tabelCarti.setItems(modelTabelMain);
        tabelExemplare.setItems(modelTabelSecundar);
        coloanaExemplar.setCellValueFactory(new PropertyValueFactory<>("codExemplar"));
        tabelCarti.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, newValue) ->{
            if(newValue != null){
                titluAutorExemplare.setText(newValue.getTitlu() + " - " + newValue.getAutor());
                carte = newValue;
                reloadTableSecundar();
            }
            else{
                modelTabelSecundar.clear();
            }
        });
        this.reloadTabelMain();
    }

    private void reloadTableSecundar(){
        if(carte != null)
            modelTabelSecundar.setAll(serviceApp.getExemplareCarteDisponibile(carte));
    }

    private void reloadTabelMain(){
        modelTabelMain.setAll(serviceApp.getCartiFiltrate(titluu, autorr));
        reloadTableSecundar();
    }

//    private void reloadTabel(){
//        modelTabel.setAll(serviceApp.ge());
//    }

    public void handlerCancel(ActionEvent actionEvent) {
        autorr = "";
        titluu = "";
        this.reloadTabelMain();
        this.clearFields();
    }

    public void handlerCauta(ActionEvent actionEvent) {
        titluu = titluSearch.getText();
        autorr = autorSearch.getText();
        this.reloadTabelMain();
        this.clearFields();

    }

    public void hanlderImprumuta(ActionEvent actionEvent) {
        var exemplar = tabelExemplare.getSelectionModel().getSelectedItem();
        if(exemplar == null){
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,"Eroare","Nu ati selectat niciun exemplar");
            return;
        }
        try{
            serviceApp.adaugaImprumut(cititor, exemplar.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Succes","Cartea a fost imprumutata");
//            reloadTableSecundar();
        }
        catch (RuntimeException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }
    }

    private void clearFields(){
        titluSearch.clear();
        autorSearch.clear();
    }

    @Override
    public void update(EventType e) {
        switch (e){
            case MODIFICARE_CARTE:
                reloadTabelMain();
            case MODIFICARE_EXEMPLAR_CARTE, IMPRUMUT, RETURNARE:
                reloadTableSecundar();
                break;
        }
    }
}
