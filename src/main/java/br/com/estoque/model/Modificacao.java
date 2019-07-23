package br.com.estoque.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Modificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="item", nullable = false)
    private Item item;


    @ManyToOne
    @JoinColumn(name="usuario", nullable = false)
    private Usuario usuario;


    private String descricaoAntiga;
    private String descricaoNova;

    private String especificacaoAntiga;
    private String especificaoNova;

    private String observacaoAntiga;
    private String observacaoNova;

    private int estoqueMinimoAntigo;
    private int estoqueMinimoNovo;

    private String motivo;

    private Date data;



    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricaoAntiga() {
        return descricaoAntiga;
    }

    public void setDescricaoAntiga(String descricaoAntiga) {
        this.descricaoAntiga = descricaoAntiga;
    }

    public String getDescricaoNova() {
        return descricaoNova;
    }

    public void setDescricaoNova(String descricaoNova) {
        this.descricaoNova = descricaoNova;
    }

    public String getEspecificacaoAntiga() {
        return especificacaoAntiga;
    }

    public void setEspecificacaoAntiga(String especificacaoAntiga) {
        this.especificacaoAntiga = especificacaoAntiga;
    }

    public String getEspecificaoNova() {
        return especificaoNova;
    }

    public void setEspecificaoNova(String especificaoNova) {
        this.especificaoNova = especificaoNova;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEstoqueMinimoAntigo(int estoqueMinimoAntigo) {
        this.estoqueMinimoAntigo = estoqueMinimoAntigo;
    }

    public int getEstoqueMinimoAntigo() {
        return estoqueMinimoAntigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getEstoqueMinimoNovo() {
        return estoqueMinimoNovo;
    }

    public void setEstoqueMinimoNovo(int estoqueMinimoNovo) {
        this.estoqueMinimoNovo = estoqueMinimoNovo;
    }

    public String getObservacaoAntiga() {
        return observacaoAntiga;
    }

    public void setObservacaoAntiga(String observacaoAntiga) {
        this.observacaoAntiga = observacaoAntiga;
    }

    public String getObservacaoNova() {
        return observacaoNova;
    }

    public void setObservacaoNova(String observacaoNova) {
        this.observacaoNova = observacaoNova;
    }
}
