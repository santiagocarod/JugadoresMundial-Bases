/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "CATEGORIAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categorias.findAll", query = "SELECT c FROM Categorias c")
    , @NamedQuery(name = "Categorias.findByCodcategoria", query = "SELECT c FROM Categorias c WHERE c.codcategoria = :codcategoria")
    , @NamedQuery(name = "Categorias.findByNombre", query = "SELECT c FROM Categorias c WHERE c.nombre = :nombre")})
public class Categorias implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODCATEGORIA")
    private BigInteger codcategoria;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @JoinTable(name = "CAT_EST", joinColumns = {
        @JoinColumn(name = "CODCATEGORIA", referencedColumnName = "CODCATEGORIA")}, inverseJoinColumns = {
        @JoinColumn(name = "CODESTADIO", referencedColumnName = "CODESTADIO")})
    @ManyToMany
    private Collection<Estadio> estadioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categorias")
    private Collection<Precio> precioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codcategoria")
    private Collection<Sillas> sillasCollection;

    public Categorias() {
    }

    public Categorias(BigInteger codcategoria) {
        this.codcategoria = codcategoria;
    }

    public Categorias(BigInteger codcategoria, String nombre) {
        this.codcategoria = codcategoria;
        this.nombre = nombre;
    }

    public BigInteger getCodcategoria() {
        return codcategoria;
    }

    public void setCodcategoria(BigInteger codcategoria) {
        this.codcategoria = codcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Estadio> getEstadioCollection() {
        return estadioCollection;
    }

    public void setEstadioCollection(Collection<Estadio> estadioCollection) {
        this.estadioCollection = estadioCollection;
    }

    @XmlTransient
    public Collection<Precio> getPrecioCollection() {
        return precioCollection;
    }

    public void setPrecioCollection(Collection<Precio> precioCollection) {
        this.precioCollection = precioCollection;
    }

    @XmlTransient
    public Collection<Sillas> getSillasCollection() {
        return sillasCollection;
    }

    public void setSillasCollection(Collection<Sillas> sillasCollection) {
        this.sillasCollection = sillasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codcategoria != null ? codcategoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorias)) {
            return false;
        }
        Categorias other = (Categorias) object;
        if ((this.codcategoria == null && other.codcategoria != null) || (this.codcategoria != null && !this.codcategoria.equals(other.codcategoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Categorias[ codcategoria=" + codcategoria + " ]";
    }
    
}
