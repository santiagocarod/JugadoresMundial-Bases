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
public class SillasPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "NUMFILA")
    private BigInteger numfila;
    @Basic(optional = false)
    @Column(name = "NUMASIENTO")
    private BigInteger numasiento;
    @Basic(optional = false)
    @Column(name = "CODESTADIO")
    private BigInteger codestadio;

    public SillasPK() {
    }

    public SillasPK(BigInteger numfila, BigInteger numasiento, BigInteger codestadio) {
        this.numfila = numfila;
        this.numasiento = numasiento;
        this.codestadio = codestadio;
    }

    public BigInteger getNumfila() {
        return numfila;
    }

    public void setNumfila(BigInteger numfila) {
        this.numfila = numfila;
    }

    public BigInteger getNumasiento() {
        return numasiento;
    }

    public void setNumasiento(BigInteger numasiento) {
        this.numasiento = numasiento;
    }

    public BigInteger getCodestadio() {
        return codestadio;
    }

    public void setCodestadio(BigInteger codestadio) {
        this.codestadio = codestadio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numfila != null ? numfila.hashCode() : 0);
        hash += (numasiento != null ? numasiento.hashCode() : 0);
        hash += (codestadio != null ? codestadio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SillasPK)) {
            return false;
        }
        SillasPK other = (SillasPK) object;
        if ((this.numfila == null && other.numfila != null) || (this.numfila != null && !this.numfila.equals(other.numfila))) {
            return false;
        }
        if ((this.numasiento == null && other.numasiento != null) || (this.numasiento != null && !this.numasiento.equals(other.numasiento))) {
            return false;
        }
        if ((this.codestadio == null && other.codestadio != null) || (this.codestadio != null && !this.codestadio.equals(other.codestadio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.SillasPK[ numfila=" + numfila + ", numasiento=" + numasiento + ", codestadio=" + codestadio + " ]";
    }
    
}
