package org.example.aplicatie.Domain;


import javax.persistence.*;

@Entity
@Table(name = "Administratori")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_administrator")),
        @AttributeOverride(name = "username", column = @Column(name = "username")),
        @AttributeOverride(name = "password", column = @Column(name = "parola"))
})
public class Administrator extends Utilizator{
    @Column(name = "nume")
    private String nume;
    @Column(name = "telefon")
    private String telefon;
    @Column(name = "cnp")
    private String cnp;
    @Column(name = "adresa")
    private String adresa;

    public Administrator() {
    }

    public Administrator(String username, String password, String nume, String telefon, String cnp, String adresa) {
        super(username, password);
        this.nume = nume;
        this.telefon = telefon;
        this.cnp = cnp;
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}
