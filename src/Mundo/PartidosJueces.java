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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "PARTIDOS_JUECES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PartidosJueces.findAll", query = "SELECT p FROM PartidosJueces p")
    , @NamedQuery(name = "PartidosJueces.findByCodj", query = "SELECT p FROM PartidosJueces p WHERE p.partidosJuecesPK.codj = :codj")
    , @NamedQuery(name = "PartidosJueces.findByCodigopartido", query = "SELECT p FROM PartidosJueces p WHERE p.partidosJuecesPK.codigopartido = :codigopartido")
    , @NamedQuery(name = "PartidosJueces.findByRol", query = "SELECT p FROM PartidosJueces p WHERE p.rol = :rol")})
public class PartidosJueces implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PartidosJuecesPK partidosJuecesPK;
    @Basic(optional = false)
    @Column(name = "ROL")
    private String rol;
    @JoinColumn(name = "CODJ", referencedColumnName = "CODJ", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Juez juez;
    @JoinColumn(name = "CODIGOPARTIDO", referencedColumnName = "CODIGOPARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partidos partidos;

    public PartidosJueces() {
    }

    public PartidosJueces(PartidosJuecesPK partidosJuecesPK) {
        this.partidosJuecesPK = partidosJuecesPK;
    }

    public PartidosJueces(PartidosJuecesPK partidosJuecesPK, String rol) {
        this.partidosJuecesPK = partidosJuecesPK;
        this.rol = rol;
    }

    public PartidosJueces(BigInteger codj, BigInteger codigopartido) {
        this.partidosJuecesPK = new PartidosJuecesPK(codj, codigopartido);
    }

    public PartidosJuecesPK getPartidosJuecesPK() {
        return partidosJuecesPK;
    }

    public void setPartidosJuecesPK(PartidosJuecesPK partidosJuecesPK) {
        this.partidosJuecesPK = partidosJuecesPK;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public Partidos getPartidos() {
        return partidos;
    }

    public void setPartidos(Partidos partidos) {
        this.partidos = partidos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partidosJuecesPK != null ? partidosJuecesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PartidosJueces)) {
            return false;
        }
        PartidosJueces other = (PartidosJueces) object;
        if ((this.partidosJuecesPK == null && other.partidosJuecesPK != null) || (this.partidosJuecesPK != null && !this.partidosJuecesPK.equals(other.partidosJuecesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.PartidosJueces[ partidosJuecesPK=" + partidosJuecesPK + " ]";
    }
    
}
