/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "TARJETASPARTIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarjetaspartido.findAll", query = "SELECT t FROM Tarjetaspartido t")
    , @NamedQuery(name = "Tarjetaspartido.findByMinuto", query = "SELECT t FROM Tarjetaspartido t WHERE t.tarjetaspartidoPK.minuto = :minuto")
    , @NamedQuery(name = "Tarjetaspartido.findByTiempo", query = "SELECT t FROM Tarjetaspartido t WHERE t.tarjetaspartidoPK.tiempo = :tiempo")
    , @NamedQuery(name = "Tarjetaspartido.findByTipot", query = "SELECT t FROM Tarjetaspartido t WHERE t.tarjetaspartidoPK.tipot = :tipot")
    , @NamedQuery(name = "Tarjetaspartido.findByCodigoju", query = "SELECT t FROM Tarjetaspartido t WHERE t.tarjetaspartidoPK.codigoju = :codigoju")
    , @NamedQuery(name = "Tarjetaspartido.findByCodigopartido", query = "SELECT t FROM Tarjetaspartido t WHERE t.tarjetaspartidoPK.codigopartido = :codigopartido")})
public class Tarjetaspartido implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarjetaspartidoPK tarjetaspartidoPK;
    @JoinColumn(name = "CODIGOJU", referencedColumnName = "CODIGOJU", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Jugador jugador;
    @JoinColumn(name = "CODIGOPARTIDO", referencedColumnName = "CODIGOPARTIDO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partidos partidos;
    @JoinColumn(name = "TIPOT", referencedColumnName = "TIPOT", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tarjeta tarjeta;

    public Tarjetaspartido() {
    }

    public Tarjetaspartido(TarjetaspartidoPK tarjetaspartidoPK) {
        this.tarjetaspartidoPK = tarjetaspartidoPK;
    }

    public Tarjetaspartido(BigInteger minuto, BigInteger tiempo, String tipot, BigInteger codigoju, BigInteger codigopartido) {
        this.tarjetaspartidoPK = new TarjetaspartidoPK(minuto, tiempo, tipot, codigoju, codigopartido);
    }

    public TarjetaspartidoPK getTarjetaspartidoPK() {
        return tarjetaspartidoPK;
    }

    public void setTarjetaspartidoPK(TarjetaspartidoPK tarjetaspartidoPK) {
        this.tarjetaspartidoPK = tarjetaspartidoPK;
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

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarjetaspartidoPK != null ? tarjetaspartidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarjetaspartido)) {
            return false;
        }
        Tarjetaspartido other = (Tarjetaspartido) object;
        if ((this.tarjetaspartidoPK == null && other.tarjetaspartidoPK != null) || (this.tarjetaspartidoPK != null && !this.tarjetaspartidoPK.equals(other.tarjetaspartidoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Tarjetaspartido[ tarjetaspartidoPK=" + tarjetaspartidoPK + " ]";
    }
    
}
