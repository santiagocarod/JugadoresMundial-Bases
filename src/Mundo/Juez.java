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
@Table(name = "JUEZ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Juez.findAll", query = "SELECT j FROM Juez j")
    , @NamedQuery(name = "Juez.findByCodj", query = "SELECT j FROM Juez j WHERE j.codj = :codj")
    , @NamedQuery(name = "Juez.findByNombrej", query = "SELECT j FROM Juez j WHERE j.nombrej = :nombrej")
    , @NamedQuery(name = "Juez.findByApellidoj", query = "SELECT j FROM Juez j WHERE j.apellidoj = :apellidoj")})
public class Juez implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODJ")
    private BigInteger codj;
    @Basic(optional = false)
    @Column(name = "NOMBREJ")
    private String nombrej;
    @Basic(optional = false)
    @Column(name = "APELLIDOJ")
    private String apellidoj;
    @JoinColumn(name = "CODPAIS", referencedColumnName = "CODPAIS")
    @ManyToOne
    private Pais codpais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "juez")
    private Collection<PartidosJueces> partidosJuecesCollection;

    public Juez() {
    }

    public Juez(BigInteger codj) {
        this.codj = codj;
    }

    public Juez(BigInteger codj, String nombrej, String apellidoj) {
        this.codj = codj;
        this.nombrej = nombrej;
        this.apellidoj = apellidoj;
    }

    public BigInteger getCodj() {
        return codj;
    }

    public void setCodj(BigInteger codj) {
        this.codj = codj;
    }

    public String getNombrej() {
        return nombrej;
    }

    public void setNombrej(String nombrej) {
        this.nombrej = nombrej;
    }

    public String getApellidoj() {
        return apellidoj;
    }

    public void setApellidoj(String apellidoj) {
        this.apellidoj = apellidoj;
    }

    public Pais getCodpais() {
        return codpais;
    }

    public void setCodpais(Pais codpais) {
        this.codpais = codpais;
    }

    @XmlTransient
    public Collection<PartidosJueces> getPartidosJuecesCollection() {
        return partidosJuecesCollection;
    }

    public void setPartidosJuecesCollection(Collection<PartidosJueces> partidosJuecesCollection) {
        this.partidosJuecesCollection = partidosJuecesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codj != null ? codj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Juez)) {
            return false;
        }
        Juez other = (Juez) object;
        if ((this.codj == null && other.codj != null) || (this.codj != null && !this.codj.equals(other.codj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Juez[ codj=" + codj + " ]";
    }
    
}
