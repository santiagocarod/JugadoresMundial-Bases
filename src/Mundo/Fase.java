/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "FASE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fase.findAll", query = "SELECT f FROM Fase f")
    , @NamedQuery(name = "Fase.findByFase", query = "SELECT f FROM Fase f WHERE f.fase = :fase")})
public class Fase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "FASE")
    private String fase;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fase1")
    private Collection<Precio> precioCollection;
    @OneToMany(mappedBy = "fase")
    private List<Partidos> partidosCollection;

    public Fase() {
    }

    public Fase(String fase) {
        this.fase = fase;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    @XmlTransient
    public Collection<Precio> getPrecioCollection() {
        return precioCollection;
    }

    public void setPrecioCollection(Collection<Precio> precioCollection) {
        this.precioCollection = precioCollection;
    }

    @XmlTransient
    public List<Partidos> getPartidosCollection() {
        return partidosCollection;
    }

    public void setPartidosCollection(List<Partidos> partidosCollection) {
        this.partidosCollection = partidosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fase != null ? fase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Fase)) {
            return false;
        }
        Fase other = (Fase) object;
        if ((this.fase == null && other.fase != null) || (this.fase != null && !this.fase.equals(other.fase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return fase;
    }
    
}
