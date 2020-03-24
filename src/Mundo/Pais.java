/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "PAIS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p")
    , @NamedQuery(name = "Pais.findByCodpais", query = "SELECT p FROM Pais p WHERE p.codpais = :codpais")
    , @NamedQuery(name = "Pais.findByNombrep", query = "SELECT p FROM Pais p WHERE p.nombrep = :nombrep")})
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODPAIS")
    private String codpais;
    @Basic(optional = false)
    @Column(name = "NOMBREP")
    private String nombrep;
    @OneToMany(mappedBy = "codpais")
    private Collection<Juez> juezCollection;
    @OneToMany(mappedBy = "codpais")
    private Collection<Jugador> jugadorCollection;
    @OneToMany(mappedBy = "codpais")
    private Collection<Usuarios> usuariosCollection;

    public Pais() {
    }

    public Pais(String codpais) {
        this.codpais = codpais;
    }

    public Pais(String codpais, String nombrep) {
        this.codpais = codpais;
        this.nombrep = nombrep;
    }

    public String getCodpais() {
        return codpais;
    }

    public void setCodpais(String codpais) {
        this.codpais = codpais;
    }

    public String getNombrep() {
        return nombrep;
    }

    public void setNombrep(String nombrep) {
        this.nombrep = nombrep;
    }

    @XmlTransient
    public Collection<Juez> getJuezCollection() {
        return juezCollection;
    }

    public void setJuezCollection(Collection<Juez> juezCollection) {
        this.juezCollection = juezCollection;
    }

    @XmlTransient
    public Collection<Jugador> getJugadorCollection() {
        return jugadorCollection;
    }

    public void setJugadorCollection(Collection<Jugador> jugadorCollection) {
        this.jugadorCollection = jugadorCollection;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpais != null ? codpais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.codpais == null && other.codpais != null) || (this.codpais != null && !this.codpais.equals(other.codpais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Pais[ codpais=" + codpais + " ]";
    }
    
}
