/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mundo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Santiago Caro
 */
@Entity
@Table(name = "PARTIDOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partidos.findAll", query = "SELECT p FROM Partidos p")
    , @NamedQuery(name = "Partidos.findByCodigopartido", query = "SELECT p FROM Partidos p WHERE p.codigopartido = :codigopartido")
    , @NamedQuery(name = "Partidos.findByHorario", query = "SELECT p FROM Partidos p WHERE p.horario = :horario")
    , @NamedQuery(name = "Partidos.findByFase", query = "SELECT p FROM Partidos p WHERE p.fase = :fase")})
public class Partidos implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGOPARTIDO")
    private BigInteger codigopartido;
    @Basic(optional = false)
    @Column(name = "HORARIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidos")
    private Collection<Anotacionxpartido> anotacionxpartidoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidos")
    private Collection<PartidosJueces> partidosJuecesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigopartido")
    private List<Boleteria> boleteriaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidos")
    private Collection<Alineaciones> alineacionesCollection;
    @JoinColumn(name = "LOCALE", referencedColumnName = "NOMBREEQ")
    @ManyToOne
    private Equipo local;
    @JoinColumn(name = "VISITANTE", referencedColumnName = "NOMBREEQ")
    @ManyToOne
    private Equipo visitante;
    @JoinColumn(name = "CODESTADIO", referencedColumnName = "CODESTADIO")
    @ManyToOne
    private Estadio codestadio;
    @JoinColumn(name = "FASE", referencedColumnName = "FASE")
    @ManyToOne
    private Fase fase;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidos")
    private Collection<Tarjetaspartido> tarjetaspartidoCollection;

    public Partidos() {
    }

    public Partidos(BigInteger codigopartido) {
        this.codigopartido = codigopartido;
    }

    public Partidos(BigInteger codigopartido, Date horario) {
        this.codigopartido = codigopartido;
        this.horario = horario;
    }

    public BigInteger getCodigopartido() {
        return codigopartido;
    }

    public void setCodigopartido(BigInteger codigopartido) {
        this.codigopartido = codigopartido;
    }

    public Serializable getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    @XmlTransient
    public Collection<Anotacionxpartido> getAnotacionxpartidoCollection() {
        return anotacionxpartidoCollection;
    }

    public void setAnotacionxpartidoCollection(Collection<Anotacionxpartido> anotacionxpartidoCollection) {
        this.anotacionxpartidoCollection = anotacionxpartidoCollection;
    }

    @XmlTransient
    public Collection<PartidosJueces> getPartidosJuecesCollection() {
        return partidosJuecesCollection;
    }

    public void setPartidosJuecesCollection(Collection<PartidosJueces> partidosJuecesCollection) {
        this.partidosJuecesCollection = partidosJuecesCollection;
    }

    @XmlTransient
    public List<Boleteria> getBoleteriaCollection() {
        return boleteriaCollection;
    }

    public void setBoleteriaCollection(List<Boleteria> boleteriaCollection) {
        this.boleteriaCollection = boleteriaCollection;
    }

    @XmlTransient
    public Collection<Alineaciones> getAlineacionesCollection() {
        return alineacionesCollection;
    }

    public void setAlineacionesCollection(Collection<Alineaciones> alineacionesCollection) {
        this.alineacionesCollection = alineacionesCollection;
    }

    public Equipo getLocal() {
        return local;
    }

    public void setLocal(Equipo local) {
        this.local = local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public void setVisitante(Equipo visitante) {
        this.visitante = visitante;
    }

    public Estadio getCodestadio() {
        return codestadio;
    }

    public void setCodestadio(Estadio codestadio) {
        this.codestadio = codestadio;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    @XmlTransient
    public Collection<Tarjetaspartido> getTarjetaspartidoCollection() {
        return tarjetaspartidoCollection;
    }

    public void setTarjetaspartidoCollection(Collection<Tarjetaspartido> tarjetaspartidoCollection) {
        this.tarjetaspartidoCollection = tarjetaspartidoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigopartido != null ? codigopartido.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partidos)) {
            return false;
        }
        Partidos other = (Partidos) object;
        if ((this.codigopartido == null && other.codigopartido != null) || (this.codigopartido != null && !this.codigopartido.equals(other.codigopartido))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return local + "-" + visitante;
    }

}
