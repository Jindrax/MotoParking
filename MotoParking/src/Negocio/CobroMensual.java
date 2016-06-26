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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "cobro_mensual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CobroMensual.findAll", query = "SELECT c FROM CobroMensual c"),
    @NamedQuery(name = "CobroMensual.findByPlaca", query = "SELECT c FROM CobroMensual c WHERE c.cobroMensualPK.placa = :placa"),
    @NamedQuery(name = "CobroMensual.findByFecha", query = "SELECT c FROM CobroMensual c WHERE c.cobroMensualPK.fecha = :fecha"),
    @NamedQuery(name = "CobroMensual.findByDesde", query = "SELECT c FROM CobroMensual c WHERE c.desde = :desde"),
    @NamedQuery(name = "CobroMensual.findByHasta", query = "SELECT c FROM CobroMensual c WHERE c.hasta = :hasta"),
    @NamedQuery(name = "CobroMensual.findByCobro", query = "SELECT c FROM CobroMensual c WHERE c.cobro = :cobro")})
public class CobroMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CobroMensualPK cobroMensualPK;
    @Basic(optional = false)
    @Column(name = "desde")
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Basic(optional = false)
    @Column(name = "hasta")
    @Temporal(TemporalType.DATE)
    private Date hasta;
    @Basic(optional = false)
    @Column(name = "cobro")
    private long cobro;
    @JoinColumn(name = "placa", referencedColumnName = "placa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UsuarioMensual usuarioMensual;

    public CobroMensual() {
    }

    public CobroMensual(CobroMensualPK cobroMensualPK) {
        this.cobroMensualPK = cobroMensualPK;
    }

    public CobroMensual(CobroMensualPK cobroMensualPK, Date desde, Date hasta, long cobro) {
        this.cobroMensualPK = cobroMensualPK;
        this.desde = desde;
        this.hasta = hasta;
        this.cobro = cobro;
    }

    public CobroMensual(String placa, Date fecha) {
        this.cobroMensualPK = new CobroMensualPK(placa, fecha);
    }

    public CobroMensualPK getCobroMensualPK() {
        return cobroMensualPK;
    }

    public void setCobroMensualPK(CobroMensualPK cobroMensualPK) {
        this.cobroMensualPK = cobroMensualPK;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public long getCobro() {
        return cobro;
    }

    public void setCobro(long cobro) {
        this.cobro = cobro;
    }

    public UsuarioMensual getUsuarioMensual() {
        return usuarioMensual;
    }

    public void setUsuarioMensual(UsuarioMensual usuarioMensual) {
        this.usuarioMensual = usuarioMensual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cobroMensualPK != null ? cobroMensualPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CobroMensual)) {
            return false;
        }
        CobroMensual other = (CobroMensual) object;
        if ((this.cobroMensualPK == null && other.cobroMensualPK != null) || (this.cobroMensualPK != null && !this.cobroMensualPK.equals(other.cobroMensualPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return cobroMensualPK.getPlaca();
    }
    
}
