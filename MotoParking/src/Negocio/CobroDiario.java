/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santiago pc
 */
@Entity
@Table(name = "cobro_diario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CobroDiario.findAll", query = "SELECT c FROM CobroDiario c"),
    @NamedQuery(name = "CobroDiario.findByConsecutivo", query = "SELECT c FROM CobroDiario c WHERE c.consecutivo = :consecutivo"),
    @NamedQuery(name = "CobroDiario.findByFecha", query = "SELECT c FROM CobroDiario c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CobroDiario.findByCobro", query = "SELECT c FROM CobroDiario c WHERE c.cobro = :cobro")})
public class CobroDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "consecutivo")
    private Long consecutivo;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "cobro")
    private long cobro;
    @JoinColumns({
        @JoinColumn(name = "cupo_consecutivo", referencedColumnName = "consecutivo"),
        @JoinColumn(name = "cupo_ingreso", referencedColumnName = "ingreso")})
    @ManyToOne(optional = false)
    private Cupo cupo;

    public CobroDiario() {
    }

    public CobroDiario(Long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public CobroDiario(Long consecutivo, Date fecha, long cobro) {
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.cobro = cobro;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getCobro() {
        return cobro;
    }

    public void setCobro(long cobro) {
        this.cobro = cobro;
    }

    public Cupo getCupo() {
        return cupo;
    }

    public void setCupo(Cupo cupo) {
        this.cupo = cupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consecutivo != null ? consecutivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CobroDiario)) {
            return false;
        }
        CobroDiario other = (CobroDiario) object;
        if ((this.consecutivo == null && other.consecutivo != null) || (this.consecutivo != null && !this.consecutivo.equals(other.consecutivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(consecutivo);
    }
    
}
