/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "EFECTIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Efectivo.findAll", query = "SELECT e FROM Efectivo e")
    , @NamedQuery(name = "Efectivo.findByVueltos", query = "SELECT e FROM Efectivo e WHERE e.vueltos = :vueltos")
    , @NamedQuery(name = "Efectivo.findByPagado", query = "SELECT e FROM Efectivo e WHERE e.pagado = :pagado")
    , @NamedQuery(name = "Efectivo.findByIdpago", query = "SELECT e FROM Efectivo e WHERE e.efectivoPK.idpago = :idpago")
    , @NamedQuery(name = "Efectivo.findByTipopago", query = "SELECT e FROM Efectivo e WHERE e.efectivoPK.tipopago = :tipopago")})
public class Efectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EfectivoPK efectivoPK;
    @Basic(optional = false)
    @Column(name = "VUELTOS")
    private BigInteger vueltos;
    @Basic(optional = false)
    @Column(name = "PAGADO")
    private BigInteger pagado;
    @JoinColumns({
        @JoinColumn(name = "IDPAGO", referencedColumnName = "IDPAGO", insertable = false, updatable = false)
        , @JoinColumn(name = "TIPOPAGO", referencedColumnName = "TIPOPAGO", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Pago pago;

    public Efectivo() {
    }

    public Efectivo(EfectivoPK efectivoPK) {
        this.efectivoPK = efectivoPK;
    }

    public Efectivo(EfectivoPK efectivoPK, BigInteger vueltos, BigInteger pagado) {
        this.efectivoPK = efectivoPK;
        this.vueltos = vueltos;
        this.pagado = pagado;
    }

    public Efectivo(BigInteger idpago, String tipopago) {
        this.efectivoPK = new EfectivoPK(idpago, tipopago);
    }

    public EfectivoPK getEfectivoPK() {
        return efectivoPK;
    }

    public void setEfectivoPK(EfectivoPK efectivoPK) {
        this.efectivoPK = efectivoPK;
    }

    public BigInteger getVueltos() {
        return vueltos;
    }

    public void setVueltos(BigInteger vueltos) {
        this.vueltos = vueltos;
    }

    public BigInteger getPagado() {
        return pagado;
    }

    public void setPagado(BigInteger pagado) {
        this.pagado = pagado;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (efectivoPK != null ? efectivoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Efectivo)) {
            return false;
        }
        Efectivo other = (Efectivo) object;
        if ((this.efectivoPK == null && other.efectivoPK != null) || (this.efectivoPK != null && !this.efectivoPK.equals(other.efectivoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Efectivo[ efectivoPK=" + efectivoPK + " ]";
    }
    
}
