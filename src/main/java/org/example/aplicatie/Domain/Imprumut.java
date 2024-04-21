package org.example.aplicatie.Domain;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "Imprumuturi")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_imprumut"))
})
public class Imprumut extends Entitate{

    @ManyToOne
    @JoinColumn(name = "id_cititor")
    private Cititor cititor;
    @ManyToOne
    @JoinColumn(name = "id_exemplar")
    private ExemplarCarte exemplarCarte;
    @Column(name = "data_imprumut")
    private LocalDate dataImprumut;
    @Column(name = "returnata")
    private Boolean returnat;

    public Imprumut(Cititor cititor, ExemplarCarte exemplarCarte, LocalDate dataImprumut, Boolean returnat) {
        this.cititor = cititor;
        this.exemplarCarte = exemplarCarte;
        this.dataImprumut = dataImprumut;
        this.returnat = returnat;
    }

    public Imprumut() {
    }

    public Cititor getCititor() {
        return cititor;
    }

    public void setCititor(Cititor cititor) {
        this.cititor = cititor;
    }

    public ExemplarCarte getExemplarCarte() {
        return exemplarCarte;
    }

    public void setExemplarCarte(ExemplarCarte exemplarCarte) {
        this.exemplarCarte = exemplarCarte;
    }

    public LocalDate getDataImprumut() {
        return dataImprumut;
    }

    public void setDataImprumut(LocalDate dataImprumut) {
        this.dataImprumut = dataImprumut;
    }

    public Boolean getReturnat() {
        return returnat;
    }

    public void setReturnat(Boolean returnat) {
        this.returnat = returnat;
    }
}
