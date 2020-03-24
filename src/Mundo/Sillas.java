/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
 * @author Santiago Caro
 */
@Entity
@Table(name = "SILLAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sillas.findAll", query = "SELECT s FROM Sillas s")
    , @NamedQuery(name = "Sillas.findByNumfila", query = "SELECT s FROM Sillas s WHERE s.sillasPK.numfila = :numfila")
    , @NamedQuery(name = "Sillas.findByNumasiento", query = "SELECT s FROM Sillas s WHERE s.sillasPK.numasiento = :numasiento")
    , @NamedQuery(name = "Sillas.findByCodestadio", query = "SELECT s FROM Sillas s WHERE s.sillasPK.codestadio = :codestadio")
    , @NamedQuery(name = "Sillas.findByEstado", query = "SELECT s FROM Sillas s WHERE s.estado = :estado")})
public class Sillas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SillasPK sillasPK;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private String estado;
    @JoinColumn(name = "CODCATEGORIA", referencedColumnName = "CODCATEGORIA")
    @ManyToOne(optional = false)
    private Categorias codcategoria;
    @JoinColumn(name = "CODESTADIO", referencedColumnName = "CODESTADIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estadio estadio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sillas")
    private Collection<Boleteria> boleteriaCollection;

    public Sillas() {
    }

    public Sillas(SillasPK sillasPK) {
        this.sillasPK = sillasPK;
    }

    public Sillas(SillasPK sillasPK, String estado) {
        this.sillasPK = sillasPK;
        this.estado = estado;
    }

    public Sillas(BigInteger numfila, BigInteger numasiento, BigInteger codestadio) {
        this.sillasPK = new SillasPK(numfila, numasiento, codestadio);
    }

    public SillasPK getSillasPK() {
        return sillasPK;
    }

    public void setSillasPK(SillasPK sillasPK) {
        this.sillasPK = sillasPK;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Categorias getCodcategoria() {
        return codcategoria;
    }

    public void setCodcategoria(Categorias codcategoria) {
        this.codcategoria = codcategoria;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    @XmlTransient
    public Collection<Boleteria> getBoleteriaCollection() {
        return boleteriaCollection;
    }

    public void setBoleteriaCollection(Collection<Boleteria> boleteriaCollection) {
        this.boleteriaCollection = boleteriaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sillasPK != null ? sillasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sillas)) {
            return false;
        }
        Sillas other = (Sillas) object;
        if ((this.sillasPK == null && other.sillasPK != null) || (this.sillasPK != null && !this.sillasPK.equals(other.sillasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Sillas[ sillasPK=" + sillasPK + " ]"+codcategoria;
    }
    
}
