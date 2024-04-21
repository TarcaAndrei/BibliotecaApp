package org.example.aplicatie.Domain;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "Carti")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id_carte"))
})
public class Carte extends Entitate{
    @Column(name="titlu")
    private String titlu;
    @Column(name="autor")
    private String autor;


    public Carte() {

    }

    public Carte(String titlu, String autor) {
        this.titlu = titlu;
        this.autor = autor;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Carte carte = (Carte) o;
//        return Objects.equals(titlu, carte.titlu) && Objects.equals(autor, carte.autor);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(titlu, autor);
//    }

    @Override
    public String toString() {
        return "Carte{" +
                "titlu='" + titlu + '\'' +
                ", autor='" + autor + '\'' +
                '}';
    }
}
