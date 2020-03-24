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
public class AlineacionesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NOMBREEQ")
    private String nombreeq;
    @Basic(optional = false)
    @Column(name = "CODIGOPARTIDO")
    private BigInteger codigopartido;
    @Basic(optional = false)
    @Column(name = "CODIGOJU")
    private BigInteger codigoju;

    public AlineacionesPK() {
    }

    public AlineacionesPK(String nombreeq, BigInteger codigopartido, BigInteger codigoju) {
        this.nombreeq = nombreeq;
        this.codigopartido = codigopartido;
        this.codigoju = codigoju;
    }

    public String getNombreeq() {
        return nombreeq;
    }

    public void setNombreeq(String nombreeq) {
        this.nombreeq = nombreeq;
    }

    public BigInteger getCodigopartido() {
        return codigopartido;
    }

    public void setCodigopartido(BigInteger codigopartido) {
        this.codigopartido = codigopartido;
    }

    public BigInteger getCodigoju() {
        return codigoju;
    }

    public void setCodigoju(BigInteger codigoju) {
        this.codigoju = codigoju;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreeq != null ? nombreeq.hashCode() : 0);
        hash += (codigopartido != null ? codigopartido.hashCode() : 0);
        hash += (codigoju != null ? codigoju.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlineacionesPK)) {
            return false;
        }
        AlineacionesPK other = (AlineacionesPK) object;
        if ((this.nombreeq == null && other.nombreeq != null) || (this.nombreeq != null && !this.nombreeq.equals(other.nombreeq))) {
            return false;
        }
        if ((this.codigopartido == null && other.codigopartido != null) || (this.codigopartido != null && !this.codigopartido.equals(other.codigopartido))) {
            return false;
        }
        if ((this.codigoju == null && other.codigoju != null) || (this.codigoju != null && !this.codigoju.equals(other.codigoju))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.AlineacionesPK[ nombreeq=" + nombreeq + ", codigopartido=" + codigopartido + ", codigoju=" + codigoju + " ]";
    }
    
}
