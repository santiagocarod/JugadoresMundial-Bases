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
@Table(name = "ALINEACIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alineaciones.findAll", query = "SELECT a FROM Alineaciones a")
    , @NamedQuery(name = "Alineaciones.findByTipoa", query = "SELECT a FROM Alineaciones a WHERE a.tipoa = :tipoa")
    , @NamedQuery(name = "Alineaciones.findByPosicion", query = "SELECT a FROM Alineaciones a WHERE a.posicion = :posicion")
    , @NamedQuery(name = "Alineaciones.findByNombreeq", query = "SELECT a FROM Alineaciones a WHERE a.alineacionesPK.nombreeq = :nombreeq")
    , @NamedQuery(name = "Alineaciones.findByCodigopartido", query = "SELECT a FROM Alineaciones a WHERE a.alineacionesPK.codigopartido = :codigopartido")
    , @NamedQuery(name = "Alineaciones.findByCodigoju", query = "SELECT a FROM Alineaciones a WHERE a.alineacionesPK.codigoju = :codigoju")
    , @NamedQuery(name = "Alineaciones.findByPE", query = "SELECT a FROM Alineaciones a WHERE a.alineacionesPK.nombreeq = :nombreeq AND  a.alineacionesPK.codigopartido = :codigopartido")})
public class Alineaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlineacionesPK alineacionesPK;
    @Basic(optional = false)
    @Column(name = "TIPOA")
    private String tipoa;
    @Basic(optional = false)
    @Column(name = "POSICION")
    private String posicion;
    @JoinColumn(name = "NOMBREEQ", referencedColumnName = "NOMBREEQ", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Equipo equipo;
    @JoinColumn(name = "CODIGOJU", referencedColumnName = "CODIGOJU", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "CODIGOPARTIDO", referencedColumnName = "CODIGOPARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partidos partidos;

    public Alineaciones() {
    }

    public Alineaciones(AlineacionesPK alineacionesPK) {
        this.alineacionesPK = alineacionesPK;
    }

    public Alineaciones(AlineacionesPK alineacionesPK, String tipoa, String posicion) {
        this.alineacionesPK = alineacionesPK;
        this.tipoa = tipoa;
        this.posicion = posicion;
    }

    public Alineaciones(String nombreeq, BigInteger codigopartido, BigInteger codigoju) {
        this.alineacionesPK = new AlineacionesPK(nombreeq, codigopartido, codigoju);
    }

    public AlineacionesPK getAlineacionesPK() {
        return alineacionesPK;
    }

    public void setAlineacionesPK(AlineacionesPK alineacionesPK) {
        this.alineacionesPK = alineacionesPK;
    }

    public String getTipoa() {
        return tipoa;
    }

    public void setTipoa(String tipoa) {
        this.tipoa = tipoa;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
        hash += (alineacionesPK != null ? alineacionesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alineaciones)) {
            return false;
        }
        Alineaciones other = (Alineaciones) object;
        if ((this.alineacionesPK == null && other.alineacionesPK != null) || (this.alineacionesPK != null && !this.alineacionesPK.equals(other.alineacionesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Alineaciones[ alineacionesPK=" + alineacionesPK + " ]";
    }

}
