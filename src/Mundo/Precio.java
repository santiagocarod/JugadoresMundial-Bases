/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mafeg
 */
@Entity
@Table(name = "PRECIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Precio.findAll", query = "SELECT p FROM Precio p"),
    @NamedQuery(name = "Precio.findByValor", query = "SELECT p FROM Precio p WHERE p.valor = :valor"),
    @NamedQuery(name = "Precio.findByCodcategoria", query = "SELECT p FROM Precio p WHERE p.precioPK.codcategoria = :codcategoria"),
    @NamedQuery(name = "Precio.findByFase", query = "SELECT p FROM Precio p WHERE p.precioPK.fase = :fase")})
public class Precio implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PrecioPK precioPK;
    @Basic(optional = false)
    @Column(name = "VALOR")
    private BigInteger valor;
    @JoinColumn(name = "CODCATEGORIA", referencedColumnName = "CODCATEGORIA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Categorias categorias;
    @JoinColumn(name = "FASE", referencedColumnName = "FASE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Fase fase1;
    @OneToMany(mappedBy = "precio")
    private List<Boleteria> boleteriaList;

    public Precio() {
    }

    public Precio(PrecioPK precioPK) {
        this.precioPK = precioPK;
    }

    public Precio(PrecioPK precioPK, BigInteger valor) {
        this.precioPK = precioPK;
        this.valor = valor;
    }

    public Precio(BigInteger codcategoria, String fase) {
        this.precioPK = new PrecioPK(codcategoria, fase);
    }

    public PrecioPK getPrecioPK() {
        return precioPK;
    }

    public void setPrecioPK(PrecioPK precioPK) {
        this.precioPK = precioPK;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public Categorias getCategorias() {
        return categorias;
    }

    public void setCategorias(Categorias categorias) {
        this.categorias = categorias;
    }

    public Fase getFase1() {
        return fase1;
    }

    public void setFase1(Fase fase1) {
        this.fase1 = fase1;
    }

    @XmlTransient
    public List<Boleteria> getBoleteriaList() {
        return boleteriaList;
    }

    public void setBoleteriaList(List<Boleteria> boleteriaList) {
        this.boleteriaList = boleteriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (precioPK != null ? precioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Precio)) {
            return false;
        }
        Precio other = (Precio) object;
        if ((this.precioPK == null && other.precioPK != null) || (this.precioPK != null && !this.precioPK.equals(other.precioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
    
}
