package org.example.aplicatie.Domain;


import javax.persistence.*;

@Entity
@Table(name = "Cititori")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_cititor")),
        @AttributeOverride(name = "username", column = @Column(name = "username")),
        @AttributeOverride(name = "password", column = @Column(name = "parola"))
}
)
public class Cititor extends Utilizator{
    @Column(name = "nume")
    private String nume;
    @Column(name = "telefon")
    private String telefon;
    @Column(name = "cnp")
    private String cnp;
    @Column(name = "adresa")
    private String adresa;

    public Cititor(String username, String password, String nume, String telefon, String cnp, String adresa) {
        super(username, password);
        this.nume = nume;
        this.telefon = telefon;
        this.cnp = cnp;
        this.adresa = adresa;
    }

    public Cititor() {

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

    @Override
    public String toString() {
        return "Cititor{" +
                "nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", cnp='" + cnp + '\'' +
                ", adresa='" + adresa + '\'' +
                "} " + super.toString();
    }
}
