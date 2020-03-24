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
@Table(name = "ANOTACIONXPARTIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anotacionxpartido.findAll", query = "SELECT a FROM Anotacionxpartido a")
    , @NamedQuery(name = "Anotacionxpartido.findByTiempo", query = "SELECT a FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.tiempo = :tiempo")
    , @NamedQuery(name = "Anotacionxpartido.findByMinuto", query = "SELECT a FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.minuto = :minuto")
    , @NamedQuery(name = "Anotacionxpartido.findByVar", query = "SELECT a FROM Anotacionxpartido a WHERE a.var = :var")
    , @NamedQuery(name = "Anotacionxpartido.findByCodigoju", query = "SELECT a FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.codigoju = :codigoju")
    , @NamedQuery(name = "Anotacionxpartido.findByCodigopartido", query = "SELECT a FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.codigopartido = :codigopartido")
    , @NamedQuery(name = "Anotacionxpartido.findByTipoan", query = "SELECT a FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.tipoan = :tipoan")
    ,@NamedQuery(name = "Anotacionxpartido.marcador", query = "SELECT count(a) FROM Anotacionxpartido a WHERE a.anotacionxpartidoPK.codigopartido = :cod AND a.jugador.nombreeq = :equi")})
public class Anotacionxpartido implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnotacionxpartidoPK anotacionxpartidoPK;
    @Basic(optional = false)
    @Column(name = "VAR")
    private String var;
    @JoinColumn(name = "TIPOAN", referencedColumnName = "TIPOAN", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Anotaciones anotaciones;
    @JoinColumn(name = "CODIGOJU", referencedColumnName = "CODIGOJU", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "CODIGOPARTIDO", referencedColumnName = "CODIGOPARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partidos partidos;

    public Anotacionxpartido() {
    }

    public Anotacionxpartido(AnotacionxpartidoPK anotacionxpartidoPK) {
        this.anotacionxpartidoPK = anotacionxpartidoPK;
    }

    public Anotacionxpartido(AnotacionxpartidoPK anotacionxpartidoPK, String var) {
        this.anotacionxpartidoPK = anotacionxpartidoPK;
        this.var = var;
    }

    public Anotacionxpartido(BigInteger tiempo, BigInteger minuto, BigInteger codigoju, BigInteger codigopartido, String tipoan) {
        this.anotacionxpartidoPK = new AnotacionxpartidoPK(tiempo, minuto, codigoju, codigopartido, tipoan);
    }

    public AnotacionxpartidoPK getAnotacionxpartidoPK() {
        return anotacionxpartidoPK;
    }

    public void setAnotacionxpartidoPK(AnotacionxpartidoPK anotacionxpartidoPK) {
        this.anotacionxpartidoPK = anotacionxpartidoPK;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Anotaciones getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Anotaciones anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
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
        hash += (anotacionxpartidoPK != null ? anotacionxpartidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anotacionxpartido)) {
            return false;
        }
        Anotacionxpartido other = (Anotacionxpartido) object;
        if ((this.anotacionxpartidoPK == null && other.anotacionxpartidoPK != null) || (this.anotacionxpartidoPK != null && !this.anotacionxpartidoPK.equals(other.anotacionxpartidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Anotacionxpartido[ anotacionxpartidoPK=" + anotacionxpartidoPK + " ]";
    }

}
