/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "EQUIPO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e")
    , @NamedQuery(name = "Equipo.findByNombreeq", query = "SELECT e FROM Equipo e WHERE e.nombreeq = :nombreeq")
    , @NamedQuery(name = "Equipo.findByGrupo", query = "SELECT e FROM Equipo e WHERE e.grupo = :grupo")
    , @NamedQuery(name = "Equipo.findByPuntaje", query = "SELECT e FROM Equipo e WHERE e.puntaje = :puntaje")})
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NOMBREEQ")
    private String nombreeq;
    @Basic(optional = false)
    @Column(name = "GRUPO")
    private Character grupo;
    @Basic(optional = false)
    @Column(name = "PUNTAJE")
    private BigInteger puntaje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nombreeq")
    private Collection<Jugador> jugadorCollection;
    @JoinColumn(name = "ENTRENADOR", referencedColumnName = "CODIGO")
    @OneToOne(optional = false)
    private Staff entrenador;
    @JoinColumn(name = "AUXILIAR", referencedColumnName = "CODIGO")
    @OneToOne(optional = false)
    private Staff auxiliar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipo")
    private Collection<Alineaciones> alineacionesCollection;
    @OneToMany(mappedBy = "local")
    private Collection<Partidos> partidosCollection;
    @OneToMany(mappedBy = "visitante")
    private Collection<Partidos> partidosCollection1;
    @Transient
    private int pg;
    @Transient
    private int pe;
    @Transient
    private int pp;

    public Equipo() {
    }
    
    public int getPg(){
        return pg;
    }
    public int getPe(){
        return pe;
    }
    public int getPp(){
        return pp;
    }
    
    public void setPg(int pg){
        this.pg=pg;
    }
    public void setPe(int pe){
        this.pe=pe;
    }
    public void setPp(int pp){
        this.pp=pp;
    }

    public Equipo(String nombreeq) {
        this.nombreeq = nombreeq;
    }

    public Equipo(String nombreeq, Character grupo, BigInteger puntaje) {
        this.nombreeq = nombreeq;
        this.grupo = grupo;
        this.puntaje = puntaje;
    }

    public String getNombreeq() {
        return nombreeq;
    }

    public void setNombreeq(String nombreeq) {
        this.nombreeq = nombreeq;
    }

    public Character getGrupo() {
        return grupo;
    }

    public void setGrupo(Character grupo) {
        this.grupo = grupo;
    }

    public BigInteger getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(BigInteger puntaje) {
        this.puntaje = puntaje;
    }

    @XmlTransient
    public Collection<Jugador> getJugadorCollection() {
        return jugadorCollection;
    }

    public void setJugadorCollection(Collection<Jugador> jugadorCollection) {
        this.jugadorCollection = jugadorCollection;
    }

    public Staff getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Staff entrenador) {
        this.entrenador = entrenador;
    }

    public Staff getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Staff auxiliar) {
        this.auxiliar = auxiliar;
    }

    @XmlTransient
    public Collection<Alineaciones> getAlineacionesCollection() {
        return alineacionesCollection;
    }

    public void setAlineacionesCollection(Collection<Alineaciones> alineacionesCollection) {
        this.alineacionesCollection = alineacionesCollection;
    }

    @XmlTransient
    public Collection<Partidos> getPartidosCollection() {
        return partidosCollection;
    }

    public void setPartidosCollection(Collection<Partidos> partidosCollection) {
        this.partidosCollection = partidosCollection;
    }

    @XmlTransient
    public Collection<Partidos> getPartidosCollection1() {
        return partidosCollection1;
    }

    public void setPartidosCollection1(Collection<Partidos> partidosCollection1) {
        this.partidosCollection1 = partidosCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreeq != null ? nombreeq.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.nombreeq == null && other.nombreeq != null) || (this.nombreeq != null && !this.nombreeq.equals(other.nombreeq))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreeq;
    }

}
