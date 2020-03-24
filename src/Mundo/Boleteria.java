/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Mundo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "BOLETERIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Boleteria.findAll", query = "SELECT b FROM Boleteria b")
    , @NamedQuery(name = "Boleteria.findByCodigoboleta", query = "SELECT b FROM Boleteria b WHERE b.codigoboleta = :codigoboleta")
    , @NamedQuery(name = "Boleteria.canti", query = "SELECT count(b) FROM Boleteria b WHERE b.codigopartido.codigopartido =:codigopartido")
    ,@NamedQuery(name = "Boleteria.catpat", query = "SELECT b FROM Boleteria b WHERE b.codigopartido =:part AND b.sillas.codcategoria.codcategoria =:cate")
    ,@NamedQuery(name = "Boleteria.catDis", query = "SELECT count(b) FROM Boleteria b WHERE b.codigopartido.codigopartido =:part AND b.sillas.codcategoria.codcategoria =:cate AND b.pago IS NULL")
    ,@NamedQuery(name = "Boleteria.catDisT", query = "SELECT count(b) FROM Boleteria b WHERE b.codigopartido =:part AND b.pago IS NULL")
    ,@NamedQuery(name = "Boleteria.catDisT1", query = "SELECT b FROM Boleteria b WHERE b.codigopartido.codigopartido =:part AND b.sillas.codcategoria.codcategoria =:cate AND b.pago IS NULL")
    ,@NamedQuery(name = "Boleteria.spk", query = "SELECT b FROM Boleteria b WHERE b.codigopartido =:part AND b.sillas.sillasPK =:spk")
})
@SequenceGenerator(name = "boletaseq", sequenceName = "BOLETAS", allocationSize = 1)
public class Boleteria implements Serializable {
    @JoinColumns({
        @JoinColumn(name = "CODCATEGORIA", referencedColumnName = "CODCATEGORIA"),
        @JoinColumn(name = "FASE", referencedColumnName = "FASE")})
    @ManyToOne
    private Precio precio;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGOBOLETA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boletaseq")
    private BigDecimal codigoboleta;
    @JoinColumns({
        @JoinColumn(name = "IDPAGO", referencedColumnName = "IDPAGO")
        , @JoinColumn(name = "TIPOPAGO", referencedColumnName = "TIPOPAGO")})
    @ManyToOne(optional = false)
    private Pago pago;
    @JoinColumn(name = "CODIGOPARTIDO", referencedColumnName = "CODIGOPARTIDO")
    @ManyToOne(optional = false)
    private Partidos codigopartido;
    @JoinColumns({
        @JoinColumn(name = "NUMFILA", referencedColumnName = "NUMFILA")
        , @JoinColumn(name = "CODESTADIO", referencedColumnName = "CODESTADIO")
        , @JoinColumn(name = "NUMASIENTO", referencedColumnName = "NUMASIENTO")})
    @ManyToOne(optional = false)
    private Sillas sillas;

    public Boleteria() {
    }

    public Boleteria(BigDecimal codigoboleta) {
        this.codigoboleta = codigoboleta;
    }

    public BigDecimal getCodigoboleta() {
        return codigoboleta;
    }

    public void setCodigoboleta(BigDecimal codigoboleta) {
        this.codigoboleta = codigoboleta;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Partidos getCodigopartido() {
        return codigopartido;
    }

    public void setCodigopartido(Partidos codigopartido) {
        this.codigopartido = codigopartido;
    }

    public Sillas getSillas() {
        return sillas;
    }

    public void setSillas(Sillas sillas) {
        this.sillas = sillas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoboleta != null ? codigoboleta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Boleteria)) {
            return false;
        }
        Boleteria other = (Boleteria) object;
        if ((this.codigoboleta == null && other.codigoboleta != null) || (this.codigoboleta != null && !this.codigoboleta.equals(other.codigoboleta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mundo.Boleteria[ codigoboleta=" + codigoboleta + " ]";
    }

    public Precio getPrecio() {
        return precio;
    }

    public void setPrecio(Precio precio) {
        this.precio = precio;
    }
    
}
