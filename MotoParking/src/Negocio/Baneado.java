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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jairo
 */
@Entity
@Table(name = "baneado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Baneado.findAll", query = "SELECT b FROM Baneado b"),
    @NamedQuery(name = "Baneado.findByPlaca", query = "SELECT b FROM Baneado b WHERE b.placa = :placa"),
    @NamedQuery(name = "Baneado.findByRazon", query = "SELECT b FROM Baneado b WHERE b.razon = :razon"),
    @NamedQuery(name = "Baneado.findByFecha", query = "SELECT b FROM Baneado b WHERE b.fecha = :fecha")})
public class Baneado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "razon")
    private String razon;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "placa", referencedColumnName = "placa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;

    public Baneado() {
    }

    public Baneado(String placa) {
        this.placa = placa;
    }

    public Baneado(String placa, String razon, Date fecha) {
        this.placa = placa;
        this.razon = razon;
        this.fecha = fecha;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (placa != null ? placa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Baneado)) {
            return false;
        }
        Baneado other = (Baneado) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Negocio.Baneado[ placa=" + placa + " ]";
    }
    
}
