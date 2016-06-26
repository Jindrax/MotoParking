/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santiago pc
 */
@Entity
@Table(name = "usuario_mensual")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioMensual.findAll", query = "SELECT u FROM UsuarioMensual u"),
    @NamedQuery(name = "UsuarioMensual.findByPlaca", query = "SELECT u FROM UsuarioMensual u WHERE u.placa = :placa"),
    @NamedQuery(name = "UsuarioMensual.findByFechaIngreso", query = "SELECT u FROM UsuarioMensual u WHERE u.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "UsuarioMensual.findBySigCobro", query = "SELECT u FROM UsuarioMensual u WHERE u.sigCobro = :sigCobro"),
    @NamedQuery(name = "UsuarioMensual.findByDocumento", query = "SELECT u FROM UsuarioMensual u WHERE u.documento = :documento"),
    @NamedQuery(name = "UsuarioMensual.findByNombre", query = "SELECT u FROM UsuarioMensual u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "UsuarioMensual.findByTelefono", query = "SELECT u FROM UsuarioMensual u WHERE u.telefono = :telefono"),
    @NamedQuery(name = "UsuarioMensual.findByMensualidad", query = "SELECT u FROM UsuarioMensual u WHERE u.mensualidad = :mensualidad")})
public class UsuarioMensual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @Column(name = "sig_cobro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sigCobro;
    @Column(name = "documento")
    private String documento;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "mensualidad")
    private long mensualidad;
    @JoinColumn(name = "placa", referencedColumnName = "placa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioMensual")
    private List<CobroMensual> cobroMensualList;

    public UsuarioMensual() {
    }

    public UsuarioMensual(String placa) {
        this.placa = placa;
    }

    public UsuarioMensual(String placa, Date fechaIngreso, Date sigCobro, long mensualidad) {
        this.placa = placa;
        this.fechaIngreso = fechaIngreso;
        this.sigCobro = sigCobro;
        this.mensualidad = mensualidad;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getSigCobro() {
        return sigCobro;
    }

    public void setSigCobro(Date sigCobro) {
        this.sigCobro = sigCobro;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public long getMensualidad() {
        return mensualidad;
    }

    public void setMensualidad(long mensualidad) {
        this.mensualidad = mensualidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public List<CobroMensual> getCobroMensualList() {
        return cobroMensualList;
    }

    public void setCobroMensualList(List<CobroMensual> cobroMensualList) {
        this.cobroMensualList = cobroMensualList;
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
        if (!(object instanceof UsuarioMensual)) {
            return false;
        }
        UsuarioMensual other = (UsuarioMensual) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return placa;
    }
    
}
