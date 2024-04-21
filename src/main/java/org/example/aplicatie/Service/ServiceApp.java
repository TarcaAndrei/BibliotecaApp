package org.example.aplicatie.Service;

import org.example.aplicatie.Domain.*;
import org.example.aplicatie.Observer.EventType;
import org.example.aplicatie.Observer.Observable;
import org.example.aplicatie.Observer.Observer;
import org.example.aplicatie.Repository.RepositoryCarte;
import org.example.aplicatie.Repository.RepositoryExemplarCarte;
import org.example.aplicatie.Repository.RepositoryImprumut;
import org.example.aplicatie.Repository.RepositoryUtilizator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.StreamSupport;

public class ServiceApp implements Observable {
    private RepositoryUtilizator<Cititor> repositoryCititori;
    private RepositoryUtilizator<Bibliotecar> repositoryBibliotecari;
    private RepositoryUtilizator<Administrator> repositoryAdministratori;
    private RepositoryCarte repositoryCarte;
    private RepositoryExemplarCarte repositoryExemplareCarti;
    private RepositoryImprumut repositoryImprumut;
    private List<String> loggedUsers;
    private Bibliotecar bibliotecarLogat;
    private Administrator administratorLogat;
//    private Map<String, Observer>

    public ServiceApp(RepositoryUtilizator<Cititor> repositoryCititori, RepositoryUtilizator<Bibliotecar> repositoryBibliotecari, RepositoryUtilizator<Administrator> repositoryAdministratori, RepositoryCarte repositoryCarte, RepositoryExemplarCarte repositoryExemplareCarti, RepositoryImprumut repositoryImprumut) {
        this.repositoryCititori = repositoryCititori;
        this.repositoryBibliotecari = repositoryBibliotecari;
        this.repositoryAdministratori = repositoryAdministratori;
        this.repositoryCarte = repositoryCarte;
        this.repositoryExemplareCarti = repositoryExemplareCarti;
        this.repositoryImprumut = repositoryImprumut;
        this.loggedUsers = new ArrayList<>();
        bibliotecarLogat = null;
        administratorLogat = null;
    }

    public void adaugaImprumut(Cititor cititor, Integer idExemplarCarte){
        var exemplarCarte = repositoryExemplareCarti.findOne(idExemplarCarte).get();
        exemplarCarte.setStatus(Status.IMPRUMUTAT);
        var imprumut = new Imprumut(cititor, exemplarCarte, LocalDate.now(), false);
        var opt1 = repositoryExemplareCarti.update(exemplarCarte);
        var opt2 = repositoryImprumut.save(imprumut);
        if(opt1.isEmpty() || opt2.isEmpty()){
            throw new RuntimeException("Avem o problema");
        }
        this.notifyAllObservers(EventType.IMPRUMUT);
    }

    public void returneazaImprumut(Integer idImprumut){
        var imprumut = repositoryImprumut.returneazaImprumut(idImprumut).get();
        var exemplarCarte = repositoryExemplareCarti.findOne(imprumut.getExemplarCarte().getId()).get();
        exemplarCarte.setStatus(Status.DISPONIBIL);
        var opt1 = repositoryImprumut.returneazaImprumut(idImprumut);
        var opt2 =repositoryExemplareCarti.update(exemplarCarte);
        if(opt1.isEmpty() || opt2.isEmpty()){
            throw new RuntimeException("Avem o problema");
        }
        this.notifyAllObservers(EventType.RETURNARE);
    }

    public Cititor loginCititor(String username, String password){
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        var cititorOpt = repositoryCititori.findByUsernameAndPassword(username, parolaCriptata);
        if(cititorOpt.isEmpty()){
            throw new RuntimeException("Utilizator sau parola incorecte");
        }
        if(loggedUsers.contains(username)){
            throw new RuntimeException("Utilizatorul este deja logat");
        }
        loggedUsers.add(username);
        return cititorOpt.get();
        //Sa ii pun in login users
    }

    public Bibliotecar loginBibliotecar(String username, String password){
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        var bibliotecarOpt = repositoryBibliotecari.findByUsernameAndPassword(username, parolaCriptata);
        if(bibliotecarOpt.isEmpty()){
            throw new RuntimeException("Bibliotecar sau parola incorecte");
        }
        if(bibliotecarLogat != null){
            throw new RuntimeException("Bibliotecarul este deja logat");
        }
        bibliotecarLogat = bibliotecarOpt.get();
        return bibliotecarOpt.get();
        //Sa ii pun in login users
    }

    public Administrator loginAdministrator(String username, String password){
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        var administratorOpt = repositoryAdministratori.findByUsernameAndPassword(username, parolaCriptata);
        if(administratorOpt.isEmpty()){
            throw new RuntimeException("Administrator sau parola incorecte");
        }
        if(administratorLogat != null){
            throw new RuntimeException("Administratorul este deja logat");
        }
        administratorLogat = administratorOpt.get();
        return administratorOpt.get();
        //Sa ii pun in login users
    }

    public void logout(String username){
        loggedUsers.remove(username);
    }

    public void logoutBibliotecar(){
        bibliotecarLogat = null;
    }

    public void logoutAdministrator(){
        administratorLogat = null;
    }

    public List<Carte> getAllCarti(){
        return repositoryCarte.findAll();
    }

    public List<Carte> getCartiFiltrate(String titlu, String autor){
        return repositoryCarte.findCarteTitluAutor(titlu, autor);
    }

    public List<ExemplarCarte> getExemplareCarteDisponibile(Carte carte){
        return repositoryExemplareCarti.findExemplareDisponibile(carte.getId());
    }

    public List<ExemplarCarte> getExemplareCarte(Carte carte){
        return repositoryExemplareCarti.findExemplarByCarte(carte.getId());
    }

    public void adaugaCarte(String titlu, String autor){
        var cartea = new Carte(titlu, autor);
        try{
            repositoryCarte.save(cartea);
        }
        catch (Exception e){
            throw new RuntimeException("Cartea exista deja");
        }
        this.notifyAllObservers(EventType.MODIFICARE_CARTE);
    }

    public void modificaCarte(Integer idCarte, String titluNou, String autorNou){
        var cartea = new Carte(titluNou, autorNou);
        cartea.setId(idCarte);
        try{
            repositoryCarte.update(cartea);
        }
        catch (Exception e){
            throw new RuntimeException("Cartea nu exista");
        }
        this.notifyAllObservers(EventType.MODIFICARE_CARTE);
    }


    public void stergeCarte(Integer idCarte){
        var cartea = repositoryCarte.findOne(idCarte);
        if(cartea.isEmpty()){
            throw new RuntimeException("Cartea nu exista");
        }
        var exemplare = repositoryExemplareCarti.findExemplarByCarte(idCarte);
        for(var exemplar : exemplare){
            stergeExemplar(exemplar.getId());
        }
        try{
            repositoryCarte.delete(idCarte);
        }
        catch (Exception e){
            throw new RuntimeException("Cartea nu exista");
        }
        this.notifyAllObservers(EventType.MODIFICARE_CARTE);
    }

    private String encrypt(String strToEncrypt, String secret) {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception e){
            throw new RuntimeException("Eroare la criptare");
        }
    }

    public void registerCititor(String username, String nume, String password, String cnp, String adresa, String telefon) {
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        Cititor cititor = new Cititor(username, parolaCriptata, nume, telefon, cnp, adresa);
        var cititorOptional = repositoryCititori.save(cititor);
        if(cititorOptional.isEmpty()){
            throw new RuntimeException("Utilizatorul exista deja");
        }
    }

    public void registerBibliotecar(String username, String nume, String password, String cnp, String adresa, String telefon) {
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        Bibliotecar bibliotecar = new Bibliotecar(username, parolaCriptata, nume, telefon, cnp, adresa);
        var bibliotecarOptional = repositoryBibliotecari.save(bibliotecar);
        if(bibliotecarOptional.isEmpty()){
            throw new RuntimeException("Utilizatorul exista deja");
        }
    }

    public void registerAdministrator(String username, String nume, String password, String cnp, String adresa, String telefon) {
        var parolaCriptata = encrypt(password, System.getenv("SECRETKEY"));
        Administrator administrator = new Administrator(username, parolaCriptata, nume, telefon, cnp, adresa);
        var administratorOptional = repositoryAdministratori.save(administrator);
        if(administratorOptional.isEmpty()){
            throw new RuntimeException("Utilizatorul exista deja");
        }
    }

    public void adaugaExemplar(Integer id, String codExemplar) {
        var carte = repositoryCarte.findOne(id).get();
        var exemplar = new ExemplarCarte(carte, codExemplar, Status.DISPONIBIL);
        try{
            repositoryExemplareCarti.save(exemplar);
        }
        catch (Exception e){
            throw new RuntimeException("Exemplarul exista deja");
        }
        this.notifyAllObservers(EventType.MODIFICARE_EXEMPLAR_CARTE);
    }

    public void modificaExemplar(Integer id, String codExemplar) {
        var exemplar = repositoryExemplareCarti.findOne(id).get();
        exemplar.setCodExemplar(codExemplar);
        try{
            repositoryExemplareCarti.update(exemplar);
        }
        catch (Exception e){
            throw new RuntimeException("Exemplarul nu exista");
        }
        this.notifyAllObservers(EventType.MODIFICARE_EXEMPLAR_CARTE);
    }

    public void stergeExemplar(Integer id) {
        var exemplar = repositoryExemplareCarti.findOne(id);
        if(exemplar.isEmpty()){
            throw new RuntimeException("Exemplarul nu exista");
        }
        for(var imprumut : repositoryImprumut.getImprumuturiByExemplar(id)){
            repositoryImprumut.delete(imprumut.getId());
        }
        try{
            repositoryExemplareCarti.delete(id);
        }
        catch (Exception e){
            throw new RuntimeException("Exemplarul nu exista");
        }
        this.notifyAllObservers(EventType.MODIFICARE_EXEMPLAR_CARTE);
    }

    public List<Imprumut> getExemplareCarteImprumutate(String titlu, String autor, String codExemplar) {
        var listaImprumuturi = repositoryImprumut.getImprumuturiNereturnate();
        if(titlu.isEmpty() && autor.isEmpty() && codExemplar.isEmpty()){
            return listaImprumuturi;
        }
        else{
            return StreamSupport.stream(listaImprumuturi.spliterator(), false)
                    .filter(imprumut -> imprumut.getExemplarCarte().getCarte().getTitlu().toLowerCase().contains(titlu.toLowerCase()))
                    .filter(imprumut -> imprumut.getExemplarCarte().getCarte().getAutor().toLowerCase().contains(autor.toLowerCase()))
                    .filter(imprumut -> imprumut.getExemplarCarte().getCodExemplar().toLowerCase().contains(codExemplar.toLowerCase()))
                    .toList();
        }
    }
}
