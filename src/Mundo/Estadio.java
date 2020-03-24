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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "ESTADIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estadio.findAll", query = "SELECT e FROM Estadio e")
    , @NamedQuery(name = "Estadio.findByCodestadio", query = "SELECT e FROM Estadio e WHERE e.codestadio = :codestadio")
    , @NamedQuery(name = "Estadio.findByNombree", query = "SELECT e FROM Estadio e WHERE e.nombree = :nombree")
    , @NamedQuery(name = "Estadio.findByCapacidad", query = "SELECT e FROM Estadio e WHERE e.capacidad = :capacidad")
    , @NamedQuery(name = "Estadio.findByCiudad", query = "SELECT e FROM Estadio e WHERE e.ciudad = :ciudad")})
public class Estadio implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODESTADIO")
    private BigInteger codestadio;
    @Basic(optional = false)
    @Column(name = "NOMBREE")
    private String nombree;
    @Basic(optional = false)
    @Column(name = "CAPACIDAD")
    private BigInteger capacidad;
    @Basic(optional = false)
    @Column(name = "CIUDAD")
    private String ciudad;
    @ManyToMany(mappedBy = "estadioCollection")
    private List<Categorias> categoriasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadio")
    private Collection<Sillas> sillasCollection;
    @OneToMany(mappedBy = "codestadio")
    private Collection<Partidos> partidosCollection;

    public Estadio() {
    }

    public Estadio(BigInteger codestadio) {
        this.codestadio = codestadio;
    }

    public Estadio(BigInteger codestadio, String nombree, BigInteger capacidad, String ciudad) {
        this.codestadio = codestadio;
        this.nombree = nombree;
        this.capacidad = capacidad;
        this.ciudad = ciudad;
    }

    public BigInteger getCodestadio() {
        return codestadio;
    }

    public void setCodestadio(BigInteger codestadio) {
        this.codestadio = codestadio;
    }

    public String getNombree() {
        return nombree;
    }

    public void setNombree(String nombree) {
        this.nombree = nombree;
    }

    public BigInteger getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(BigInteger capacidad) {
        this.capacidad = capacidad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @XmlTransient
    public List<Categorias> getCategoriasCollection() {
        return categoriasCollection;
    }

    public void setCategoriasCollection(List<Categorias> categoriasCollection) {
        this.categoriasCollection = categoriasCollection;
    }

    @XmlTransient
    public Collection<Sillas> getSillasCollection() {
        return sillasCollection;
    }

    public void setSillasCollection(Collection<Sillas> sillasCollection) {
        this.sillasCollection = sillasCollection;
    }

    @XmlTransient
    public Collection<Partidos> getPartidosCollection() {
        return partidosCollection;
    }

    public void setPartidosCollection(Collection<Partidos> partidosCollection) {
        this.partidosCollection = partidosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codestadio != null ? codestadio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estadio)) {
            return false;
        }
        Estadio other = (Estadio) object;
        if ((this.codestadio == null && other.codestadio != null) || (this.codestadio != null && !this.codestadio.equals(other.codestadio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Estadio[ codestadio=" + codestadio + " ]";
    }
    
}
