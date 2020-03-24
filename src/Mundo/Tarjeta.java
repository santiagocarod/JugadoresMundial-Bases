/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "TARJETA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjeta.findAll", query = "SELECT t FROM Tarjeta t")
    , @NamedQuery(name = "Tarjeta.findByTipot", query = "SELECT t FROM Tarjeta t WHERE t.tipot = :tipot")})
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TIPOT")
    private String tipot;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tarjeta")
    private Collection<Tarjetaspartido> tarjetaspartidoCollection;

    public Tarjeta() {
    }

    public Tarjeta(String tipot) {
        this.tipot = tipot;
    }

    public String getTipot() {
        return tipot;
    }

    public void setTipot(String tipot) {
        this.tipot = tipot;
    }

    @XmlTransient
    public Collection<Tarjetaspartido> getTarjetaspartidoCollection() {
        return tarjetaspartidoCollection;
    }

    public void setTarjetaspartidoCollection(Collection<Tarjetaspartido> tarjetaspartidoCollection) {
        this.tarjetaspartidoCollection = tarjetaspartidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipot != null ? tipot.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjeta)) {
            return false;
        }
        Tarjeta other = (Tarjeta) object;
        if ((this.tipot == null && other.tipot != null) || (this.tipot != null && !this.tipot.equals(other.tipot))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Tarjeta[ tipot=" + tipot + " ]";
    }
    
}
