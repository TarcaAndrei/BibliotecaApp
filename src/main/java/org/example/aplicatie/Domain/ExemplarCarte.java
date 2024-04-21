package org.example.aplicatie.Domain;


import javax.persistence.*;

@Entity
@Table(name = "ExemplareCarti")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_exemplar"))
}
)
public class ExemplarCarte extends Entitate{

    @ManyToOne
    @JoinColumn(name = "id_carte")
    private Carte carte;
    @Column(name = "cod_exemplar")
    private String codExemplar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public ExemplarCarte(Carte carte, String codExemplar, Status status) {
        this.carte = carte;
        this.codExemplar = codExemplar;
        this.status = status;
    }

    public ExemplarCarte() {
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    public String getCodExemplar() {
        return codExemplar;
    }

    public void setCodExemplar(String codExemplar) {
        this.codExemplar = codExemplar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
