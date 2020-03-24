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
public class PartidosJuecesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "CODJ")
    private BigInteger codj;
    @Basic(optional = false)
    @Column(name = "CODIGOPARTIDO")
    private BigInteger codigopartido;

    public PartidosJuecesPK() {
    }

    public PartidosJuecesPK(BigInteger codj, BigInteger codigopartido) {
        this.codj = codj;
        this.codigopartido = codigopartido;
    }

    public BigInteger getCodj() {
        return codj;
    }

    public void setCodj(BigInteger codj) {
        this.codj = codj;
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
        hash += (codj != null ? codj.hashCode() : 0);
        hash += (codigopartido != null ? codigopartido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartidosJuecesPK)) {
            return false;
        }
        PartidosJuecesPK other = (PartidosJuecesPK) object;
        if ((this.codj == null && other.codj != null) || (this.codj != null && !this.codj.equals(other.codj))) {
            return false;
        }
        if ((this.codigopartido == null && other.codigopartido != null) || (this.codigopartido != null && !this.codigopartido.equals(other.codigopartido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.PartidosJuecesPK[ codj=" + codj + ", codigopartido=" + codigopartido + " ]";
    }
    
}
