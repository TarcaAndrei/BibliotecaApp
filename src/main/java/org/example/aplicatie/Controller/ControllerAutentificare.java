package org.example.aplicatie.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.example.aplicatie.Service.ServiceApp;

import java.io.IOException;

public class ControllerAutentificare {
    public GridPane gridLogin;
    public GridPane gridRegister;
    public TextField usernameLogin;
    public PasswordField passwordLogin;
    public RadioButton checkCititor;
    public RadioButton checkBiblio;
    public RadioButton checkAdmin;
    public TextField usernameRegister;
    public TextField numeTextField;
    public PasswordField passwordField;
    public TextField cnpTextField;
    public TextField adresaTextField;
    public TextField telefonTextField;
    public RadioButton regCititor;
    public RadioButton regBiblio;
    public RadioButton regAdmin;
    public RowConstraints randLogin;
    public RowConstraints randRegister;
    private ServiceApp service;

    public void setService(ServiceApp service) {
        this.service = service;
        gridRegister.setVisible(false);
        gridLogin.setVisible(true);
        initialize();
    }

    private void initialize() {
        var toogleGroup = new ToggleGroup();
        checkCititor.setSelected(true);
        checkCititor.setToggleGroup(toogleGroup);
        checkBiblio.setToggleGroup(toogleGroup);
        checkAdmin.setToggleGroup(toogleGroup);

        var toogleGroup2 = new ToggleGroup();
        regCititor.setSelected(true);
        regCititor.setToggleGroup(toogleGroup2);
        regBiblio.setToggleGroup(toogleGroup2);
        regAdmin.setToggleGroup(toogleGroup2);
    }

    public void handleLogin(ActionEvent actionEvent) throws IOException {
        var username = usernameLogin.getText();
        var password = passwordLogin.getText();
        if (checkCititor.isSelected()) {
            loginCititor(username, password);
        } else if (checkBiblio.isSelected()) {
            loginBibliotecar(username, password);
        } else if (checkAdmin.isSelected()) {
            loginAdministrator(username, password);
        }
        clearFields();
    }

    private void loginCititor(String username, String password) throws IOException {
        try {
            Stage userStage = new Stage();
            var cititor = service.loginCititor(username, password);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/cititor-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            ControllerCititor controller = fxmlLoader.getController();
            controller.setServiceApp(service, cititor);
            userStage.setTitle("Biblioteca");
            userStage.setScene(scene);
            userStage.setOnCloseRequest(event -> {
                service.logout(username);
                service.removeObserver(controller);
            });
            userStage.show();
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    private void loginBibliotecar(String username, String password) throws IOException {
        try {
            Stage userStage = new Stage();
            var bibliotecar = service.loginBibliotecar(username, password);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/bibliotecar-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            ControllerBibliotecar controller = fxmlLoader.getController();
            controller.setServiceApp(service, bibliotecar);
            userStage.setTitle("Biblioteca");
            userStage.setScene(scene);
            userStage.setOnCloseRequest(event -> {
                service.logoutBibliotecar();
                service.removeObserver(controller);
            });
            userStage.show();
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    private void loginAdministrator(String username, String password) throws IOException {
        try {
            Stage userStage = new Stage();
            var admin = service.loginAdministrator(username, password);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/admin-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            ControllerAdministrator controller = fxmlLoader.getController();
            controller.setServiceApp(service, admin);
            userStage.setTitle("Biblioteca");
            userStage.setScene(scene);
            userStage.setOnCloseRequest(event -> {
                service.logoutAdministrator();
                service.removeObserver(controller);
            });
            userStage.show();
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    public void handlerRegisterRequest(ActionEvent actionEvent) {
        gridLogin.setVisible(false);
        gridRegister.setVisible(true);
    }

    public void handlerCancel(ActionEvent actionEvent) {
        gridRegister.setVisible(false);
        gridLogin.setVisible(true);
        clearFields();
    }


    private void clearFields() {
        usernameLogin.clear();
        passwordLogin.clear();
        usernameRegister.clear();
        numeTextField.clear();
        passwordField.clear();
        cnpTextField.clear();
        adresaTextField.clear();
        telefonTextField.clear();
    }

    public void handlerSave(ActionEvent actionEvent) {
        var username = usernameRegister.getText();
        var nume = numeTextField.getText();
        var password = passwordField.getText();
        var cnp = cnpTextField.getText();
        var adresa = adresaTextField.getText();
        var telefon = telefonTextField.getText();
        if(username.isEmpty() || nume.isEmpty() || password.isEmpty() || cnp.isEmpty() || adresa.isEmpty() || telefon.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", "Toate campurile sunt obligatorii");
            return;
//            handlerCancel(actionEvent);
        }
        if (regCititor.isSelected()) {
            registerCititor(username, nume, password, cnp, adresa, telefon);
        } else if (regBiblio.isSelected()) {
            registerBibliotecar(username, nume, password, cnp, adresa, telefon);
        } else if (regAdmin.isSelected()) {
            registerAdministrator(username, nume, password, cnp, adresa, telefon);
        }
        handlerCancel(actionEvent);
    }

    private void registerCititor(String username, String nume, String password, String cnp, String adresa, String telefon) {
        try {
            service.registerCititor(username, nume, password, cnp, adresa, telefon);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "Contul a fost creat cu succes");
            handlerCancel(null);
        } catch (RuntimeException ex) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    private void registerBibliotecar(String username, String nume, String password, String cnp, String adresa, String telefon) {
        try {
            service.registerBibliotecar(username, nume, password, cnp, adresa, telefon);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "Contul a fost creat cu succes");
            handlerCancel(null);
        } catch (RuntimeException ex) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }

    private void registerAdministrator(String username, String nume, String password, String cnp, String adresa, String telefon) {
        try {
            service.registerAdministrator(username, nume, password, cnp, adresa, telefon);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "Contul a fost creat cu succes");
            handlerCancel(null);
        } catch (RuntimeException ex) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", ex.getMessage());
        }
    }
}
