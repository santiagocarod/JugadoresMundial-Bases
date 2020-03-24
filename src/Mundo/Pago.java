/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import DAL.PagoJpaController;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "PAGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findByTotal", query = "SELECT p FROM Pago p WHERE p.total = :total")
    , @NamedQuery(name = "Pago.findByIdpago", query = "SELECT p FROM Pago p WHERE p.pagoPK.idpago = :idpago")
    , @NamedQuery(name = "Pago.findByTipopago", query = "SELECT p FROM Pago p WHERE p.pagoPK.tipopago = :tipopago")
    , @NamedQuery(name = "Pago.act", query = "SELECT count(p) FROM Pago p")})

public class Pago implements Serializable {

    @Transient
   public static long NUMEROPAGO =0;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PagoPK pagoPK;
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigInteger total;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pago")
    private Collection<Boleteria> boleteriaCollection;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pago")
    private Efectivo efectivo;
    @JoinColumn(name = "USUARIOS_ID", referencedColumnName = "ID")
    @ManyToOne
    private Usuarios usuariosId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pago")
    private Tarjetacredito tarjetacredito;

    public Pago() {
    }

    public Pago(PagoPK pagoPK) {
        this.pagoPK = pagoPK;
    }

    public Pago(PagoPK pagoPK, BigInteger total) {
        this.pagoPK = pagoPK;
        this.total = total;
    }

    public Pago(String tipopago) {
        PagoJpaController us = new PagoJpaController(Persistence.createEntityManagerFactory("BasesFinalPU"));
         NUMEROPAGO=us.actualizarPago()+1;
        System.out.println(NUMEROPAGO);
        this.pagoPK = new PagoPK(BigInteger.valueOf(NUMEROPAGO), tipopago);
        
       
        
    }
    public PagoPK getPagoPK() {
        return pagoPK;
    }

    public void setPagoPK(PagoPK pagoPK) {
        this.pagoPK = pagoPK;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    @XmlTransient
    public Collection<Boleteria> getBoleteriaCollection() {
        return boleteriaCollection;
    }

    public void setBoleteriaCollection(Collection<Boleteria> boleteriaCollection) {
        this.boleteriaCollection = boleteriaCollection;
    }

    public Efectivo getEfectivo() {
        return efectivo;
    }

    public void setEfectivo(Efectivo efectivo) {
        this.efectivo = efectivo;
    }

    public Usuarios getUsuariosId() {
        return usuariosId;
    }

    public void setUsuariosId(Usuarios usuariosId) {
        this.usuariosId = usuariosId;
    }

    public Tarjetacredito getTarjetacredito() {
        return tarjetacredito;
    }

    public void setTarjetacredito(Tarjetacredito tarjetacredito) {
        this.tarjetacredito = tarjetacredito;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoPK != null ? pagoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.pagoPK == null && other.pagoPK != null) || (this.pagoPK != null && !this.pagoPK.equals(other.pagoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Pago[ pagoPK=" + pagoPK + " ]";
    }
    
}
