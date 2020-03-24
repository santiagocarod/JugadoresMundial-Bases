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
@Table(name = "TARJETACREDITO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjetacredito.findAll", query = "SELECT t FROM Tarjetacredito t")
    , @NamedQuery(name = "Tarjetacredito.findByNumero", query = "SELECT t FROM Tarjetacredito t WHERE t.numero = :numero")
    , @NamedQuery(name = "Tarjetacredito.findByBanco", query = "SELECT t FROM Tarjetacredito t WHERE t.banco = :banco")
    , @NamedQuery(name = "Tarjetacredito.findByIdpago", query = "SELECT t FROM Tarjetacredito t WHERE t.tarjetacreditoPK.idpago = :idpago")
    , @NamedQuery(name = "Tarjetacredito.findByTipopago", query = "SELECT t FROM Tarjetacredito t WHERE t.tarjetacreditoPK.tipopago = :tipopago")})
public class Tarjetacredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarjetacreditoPK tarjetacreditoPK;
    @Basic(optional = false)
    @Column(name = "NUMERO")
    private BigInteger numero;
    @Basic(optional = false)
    @Column(name = "BANCO")
    private String banco;
    @JoinColumns({
        @JoinColumn(name = "IDPAGO", referencedColumnName = "IDPAGO", insertable = false, updatable = false)
        , @JoinColumn(name = "TIPOPAGO", referencedColumnName = "TIPOPAGO", insertable = false, updatable = false)})
    @OneToOne(optional = false)
    private Pago pago;

    public Tarjetacredito() {
    }

    public Tarjetacredito(TarjetacreditoPK tarjetacreditoPK) {
        this.tarjetacreditoPK = tarjetacreditoPK;
    }

    public Tarjetacredito(TarjetacreditoPK tarjetacreditoPK, BigInteger numero, String banco) {
        this.tarjetacreditoPK = tarjetacreditoPK;
        this.numero = numero;
        this.banco = banco;
    }

    public Tarjetacredito(BigInteger idpago, String tipopago) {
        this.tarjetacreditoPK = new TarjetacreditoPK(idpago, tipopago);
    }

    public TarjetacreditoPK getTarjetacreditoPK() {
        return tarjetacreditoPK;
    }

    public void setTarjetacreditoPK(TarjetacreditoPK tarjetacreditoPK) {
        this.tarjetacreditoPK = tarjetacreditoPK;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
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
        hash += (tarjetacreditoPK != null ? tarjetacreditoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjetacredito)) {
            return false;
        }
        Tarjetacredito other = (Tarjetacredito) object;
        if ((this.tarjetacreditoPK == null && other.tarjetacreditoPK != null) || (this.tarjetacreditoPK != null && !this.tarjetacreditoPK.equals(other.tarjetacreditoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Tarjetacredito[ tarjetacreditoPK=" + tarjetacreditoPK + " ]";
    }
    
}
