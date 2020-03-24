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
public class TarjetaspartidoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MINUTO")
    private BigInteger minuto;
    @Basic(optional = false)
    @Column(name = "TIEMPO")
    private BigInteger tiempo;
    @Basic(optional = false)
    @Column(name = "TIPOT")
    private String tipot;
    @Basic(optional = false)
    @Column(name = "CODIGOJU")
    private BigInteger codigoju;
    @Basic(optional = false)
    @Column(name = "CODIGOPARTIDO")
    private BigInteger codigopartido;

    public TarjetaspartidoPK() {
    }

    public TarjetaspartidoPK(BigInteger minuto, BigInteger tiempo, String tipot, BigInteger codigoju, BigInteger codigopartido) {
        this.minuto = minuto;
        this.tiempo = tiempo;
        this.tipot = tipot;
        this.codigoju = codigoju;
        this.codigopartido = codigopartido;
    }

    public BigInteger getMinuto() {
        return minuto;
    }

    public void setMinuto(BigInteger minuto) {
        this.minuto = minuto;
    }

    public BigInteger getTiempo() {
        return tiempo;
    }

    public void setTiempo(BigInteger tiempo) {
        this.tiempo = tiempo;
    }

    public String getTipot() {
        return tipot;
    }

    public void setTipot(String tipot) {
        this.tipot = tipot;
    }

    public BigInteger getCodigoju() {
        return codigoju;
    }

    public void setCodigoju(BigInteger codigoju) {
        this.codigoju = codigoju;
    }

    public BigInteger getCodigopartido() {
        return codigopartido;
    }

    public void setCodigopartido(BigInteger codigopartido) {
        this.codigopartido = codigopartido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (minuto != null ? minuto.hashCode() : 0);
        hash += (tiempo != null ? tiempo.hashCode() : 0);
        hash += (tipot != null ? tipot.hashCode() : 0);
        hash += (codigoju != null ? codigoju.hashCode() : 0);
        hash += (codigopartido != null ? codigopartido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarjetaspartidoPK)) {
            return false;
        }
        TarjetaspartidoPK other = (TarjetaspartidoPK) object;
        if ((this.minuto == null && other.minuto != null) || (this.minuto != null && !this.minuto.equals(other.minuto))) {
            return false;
        }
        if ((this.tiempo == null && other.tiempo != null) || (this.tiempo != null && !this.tiempo.equals(other.tiempo))) {
            return false;
        }
        if ((this.tipot == null && other.tipot != null) || (this.tipot != null && !this.tipot.equals(other.tipot))) {
            return false;
        }
        if ((this.codigoju == null && other.codigoju != null) || (this.codigoju != null && !this.codigoju.equals(other.codigoju))) {
            return false;
        }
        if ((this.codigopartido == null && other.codigopartido != null) || (this.codigopartido != null && !this.codigopartido.equals(other.codigopartido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.TarjetaspartidoPK[ minuto=" + minuto + ", tiempo=" + tiempo + ", tipot=" + tipot + ", codigoju=" + codigoju + ", codigopartido=" + codigopartido + " ]";
    }
    
}
