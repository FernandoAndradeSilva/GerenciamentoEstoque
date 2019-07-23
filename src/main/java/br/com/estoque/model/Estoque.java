package br.com.estoque.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Estoque implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int entradas;
    private int saidas;
    private int saldo;
    private float custo;

    @ManyToOne
    @JoinColumn(name="unidade",  nullable= true)
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name="item",  nullable= true)
    private Item item;

    public int getEntradas() {
        return entradas;
    }

    public void setEntradas(int entradas) {
        this.entradas = entradas;
    }

    public int getSaidas() {
        return saidas;
    }

    public void setSaidas(int saidas) {
        this.saidas = saidas;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estoque estoque = (Estoque) o;
        return id == estoque.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
