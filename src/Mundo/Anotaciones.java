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
@Table(name = "ANOTACIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anotaciones.findAll", query = "SELECT a FROM Anotaciones a")
    , @NamedQuery(name = "Anotaciones.findByTipoan", query = "SELECT a FROM Anotaciones a WHERE a.tipoan = :tipoan")})
public class Anotaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TIPOAN")
    private String tipoan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anotaciones")
    private List<Anotacionxpartido> anotacionxpartidoCollection;

    public Anotaciones() {
    }

    public Anotaciones(String tipoan) {
        this.tipoan = tipoan;
    }

    public String getTipoan() {
        return tipoan;
    }

    public void setTipoan(String tipoan) {
        this.tipoan = tipoan;
    }

    @XmlTransient
    public List<Anotacionxpartido> getAnotacionxpartidoCollection() {
        return anotacionxpartidoCollection;
    }

    public void setAnotacionxpartidoCollection(List<Anotacionxpartido> anotacionxpartidoCollection) {
        this.anotacionxpartidoCollection = anotacionxpartidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoan != null ? tipoan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anotaciones)) {
            return false;
        }
        Anotaciones other = (Anotaciones) object;
        if ((this.tipoan == null && other.tipoan != null) || (this.tipoan != null && !this.tipoan.equals(other.tipoan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Anotaciones[ tipoan=" + tipoan + " ]";
    }

    public List<Anotacionxpartido> getAnotacionxpartidoList() {
        return anotacionxpartidoCollection;
    }

    public void setAnotacionxpartidoList(List<Anotacionxpartido> la) {
         anotacionxpartidoCollection=la;
    }
    
}
