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
import javax.persistence.Embeddable;

/**
 *
 * @author Santiago Caro
 */
@Embeddable
public class EfectivoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "IDPAGO")
    private BigInteger idpago;
    @Basic(optional = false)
    @Column(name = "TIPOPAGO")
    private String tipopago;

    public EfectivoPK() {
    }

    public EfectivoPK(BigInteger idpago, String tipopago) {
        this.idpago = idpago;
        this.tipopago = tipopago;
    }

    public BigInteger getIdpago() {
        return idpago;
    }

    public void setIdpago(BigInteger idpago) {
        this.idpago = idpago;
    }

    public String getTipopago() {
        return tipopago;
    }

    public void setTipopago(String tipopago) {
        this.tipopago = tipopago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpago != null ? idpago.hashCode() : 0);
        hash += (tipopago != null ? tipopago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EfectivoPK)) {
            return false;
        }
        EfectivoPK other = (EfectivoPK) object;
        if ((this.idpago == null && other.idpago != null) || (this.idpago != null && !this.idpago.equals(other.idpago))) {
            return false;
        }
        if ((this.tipopago == null && other.tipopago != null) || (this.tipopago != null && !this.tipopago.equals(other.tipopago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.EfectivoPK[ idpago=" + idpago + ", tipopago=" + tipopago + " ]";
    }
    
}
