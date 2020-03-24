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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "JUGADOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugador.findAll", query = "SELECT j FROM Jugador j")
    , @NamedQuery(name = "Jugador.findByNombreju", query = "SELECT j FROM Jugador j WHERE j.nombreju = :nombreju")
    , @NamedQuery(name = "Jugador.findByApellidoju", query = "SELECT j FROM Jugador j WHERE j.apellidoju = :apellidoju")
    , @NamedQuery(name = "Jugador.findByCodigoju", query = "SELECT j FROM Jugador j WHERE j.codigoju = :codigoju")
    , @NamedQuery(name = "Jugador.findByFechan", query = "SELECT j FROM Jugador j WHERE j.fechan = :fechan")
    , @NamedQuery(name = "Jugador.findByEstatura", query = "SELECT j FROM Jugador j WHERE j.estatura = :estatura")
    , @NamedQuery(name = "Jugador.findByPeso", query = "SELECT j FROM Jugador j WHERE j.peso = :peso")
    , @NamedQuery(name = "Jugador.findByNumero", query = "SELECT j FROM Jugador j WHERE j.numero = :numero")
    ,@NamedQuery(name = "Jugador.findByE", query = "SELECT j FROM Jugador j WHERE j.nombreeq.nombreeq = :no")
})
public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "NOMBREJU")
    private String nombreju;
    @Basic(optional = false)
    @Column(name = "APELLIDOJU")
    private String apellidoju;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGOJU")
    private BigInteger codigoju;
    @Basic(optional = false)
    @Column(name = "FECHAN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechan;
    @Basic(optional = false)
    @Column(name = "ESTATURA")
    private BigInteger estatura;
    @Basic(optional = false)
    @Column(name = "PESO")
    private BigInteger peso;
    @Lob
    @Column(name = "FOTO")
    private byte[] foto;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private BigInteger numero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private Collection<Anotacionxpartido> anotacionxpartidoCollection;
    @JoinColumn(name = "NOMBREEQ", referencedColumnName = "NOMBREEQ")
    @ManyToOne(optional = false)
    private Equipo nombreeq;
    @JoinColumn(name = "CODPAIS", referencedColumnName = "CODPAIS")
    @ManyToOne
    private Pais codpais;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private Collection<Alineaciones> alineacionesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jugador")
    private Collection<Tarjetaspartido> tarjetaspartidoCollection;

    public Jugador() {
    }

    public Jugador(BigInteger codigoju) {
        this.codigoju = codigoju;
    }

    public Jugador(BigInteger codigoju, String nombreju, String apellidoju, Date fechan, BigInteger estatura, BigInteger peso, BigInteger numero) {
        this.codigoju = codigoju;
        this.nombreju = nombreju;
        this.apellidoju = apellidoju;
        this.fechan = fechan;
        this.estatura = estatura;
        this.peso = peso;
        this.numero = numero;
    }

    public String getNombreju() {
        return nombreju;
    }

    public void setNombreju(String nombreju) {
        this.nombreju = nombreju;
    }

    public String getApellidoju() {
        return apellidoju;
    }

    public void setApellidoju(String apellidoju) {
        this.apellidoju = apellidoju;
    }

    public BigInteger getCodigoju() {
        return codigoju;
    }

    public void setCodigoju(BigInteger codigoju) {
        this.codigoju = codigoju;
    }

    public Date getFechan() {
        return fechan;
    }

    public void setFechan(Date fechan) {
        this.fechan = fechan;
    }

    public BigInteger getEstatura() {
        return estatura;
    }

    public void setEstatura(BigInteger estatura) {
        this.estatura = estatura;
    }

    public BigInteger getPeso() {
        return peso;
    }

    public void setPeso(BigInteger peso) {
        this.peso = peso;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    @XmlTransient
    public Collection<Anotacionxpartido> getAnotacionxpartidoCollection() {
        return anotacionxpartidoCollection;
    }

    public void setAnotacionxpartidoCollection(Collection<Anotacionxpartido> anotacionxpartidoCollection) {
        this.anotacionxpartidoCollection = anotacionxpartidoCollection;
    }

    public Equipo getNombreeq() {
        return nombreeq;
    }

    public void setNombreeq(Equipo nombreeq) {
        this.nombreeq = nombreeq;
    }

    public Pais getCodpais() {
        return codpais;
    }

    public void setCodpais(Pais codpais) {
        this.codpais = codpais;
    }

    @XmlTransient
    public Collection<Alineaciones> getAlineacionesCollection() {
        return alineacionesCollection;
    }

    public void setAlineacionesCollection(Collection<Alineaciones> alineacionesCollection) {
        this.alineacionesCollection = alineacionesCollection;
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
        hash += (codigoju != null ? codigoju.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jugador)) {
            return false;
        }
        Jugador other = (Jugador) object;
        if ((this.codigoju == null && other.codigoju != null) || (this.codigoju != null && !this.codigoju.equals(other.codigoju))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreju+" "+apellidoju;
    }

}
