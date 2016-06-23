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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author santiago pc
 */
@Embeddable
public class CupoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "consecutivo")
    private long consecutivo;
    @Basic(optional = false)
    @Column(name = "ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ingreso;

    public CupoPK() {
    }

    public CupoPK(long consecutivo, Date ingreso) {
        this.consecutivo = consecutivo;
        this.ingreso = ingreso;
    }

    public long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) consecutivo;
        hash += (ingreso != null ? ingreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CupoPK)) {
            return false;
        }
        CupoPK other = (CupoPK) object;
        if (this.consecutivo != other.consecutivo) {
            return false;
        }
        if ((this.ingreso == null && other.ingreso != null) || (this.ingreso != null && !this.ingreso.equals(other.ingreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Negocio.CupoPK[ consecutivo=" + consecutivo + ", ingreso=" + ingreso + " ]";
    }
    
}
