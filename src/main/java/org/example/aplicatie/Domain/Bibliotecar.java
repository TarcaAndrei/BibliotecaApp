package org.example.aplicatie.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Bibliotecari")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_bibliotecar")),
        @AttributeOverride(name = "username", column = @Column(name = "username")),
        @AttributeOverride(name = "password", column = @Column(name = "parola"))
}
)
public class Bibliotecar extends Utilizator{

    @Column(name = "nume")
    private String nume;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "adresa")
    private String adresa;

    public Bibliotecar() {
    }

    public Bibliotecar(String username, String password, String nume, String telefon, String cnp, String adresa) {
        super(username, password);
        this.nume = nume;
        this.telefon = telefon;
        this.cnp = cnp;
        this.adresa = adresa;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getCnp() {
        return cnp;
    }

    public String getAdresa() {
        return adresa;
    }
}
