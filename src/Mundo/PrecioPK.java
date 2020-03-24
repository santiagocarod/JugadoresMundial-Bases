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
 * @author mafeg
 */
@Embeddable
public class PrecioPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "CODCATEGORIA")
    private BigInteger codcategoria;
    @Basic(optional = false)
    @Column(name = "FASE")
    private String fase;

    public PrecioPK() {
    }

    public PrecioPK(BigInteger codcategoria, String fase) {
        this.codcategoria = codcategoria;
        this.fase = fase;
    }

    public BigInteger getCodcategoria() {
        return codcategoria;
    }

    public void setCodcategoria(BigInteger codcategoria) {
        this.codcategoria = codcategoria;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codcategoria != null ? codcategoria.hashCode() : 0);
        hash += (fase != null ? fase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrecioPK)) {
            return false;
        }
        PrecioPK other = (PrecioPK) object;
        if ((this.codcategoria == null && other.codcategoria != null) || (this.codcategoria != null && !this.codcategoria.equals(other.codcategoria))) {
            return false;
        }
        if ((this.fase == null && other.fase != null) || (this.fase != null && !this.fase.equals(other.fase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.PrecioPK[ codcategoria=" + codcategoria + ", fase=" + fase + " ]";
    }
    
}
