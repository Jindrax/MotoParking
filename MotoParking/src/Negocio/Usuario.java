/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jairo
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByPlaca", query = "SELECT u FROM Usuario u WHERE u.placa = :placa"),
    @NamedQuery(name = "Usuario.findByTipo", query = "SELECT u FROM Usuario u WHERE u.tipo = :tipo"),
    @NamedQuery(name = "Usuario.findByObservacion", query = "SELECT u FROM Usuario u WHERE u.observacion = :observacion")})
public class Usuario implements Serializable {

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private UsuarioMensual usuarioMensual;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @Column(name = "observacion")
    private String observacion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Baneado baneado;

    public Usuario() {
    }

    public Usuario(String placa) {
        this.placa = placa;
    }

    public Usuario(String placa, String tipo, String observacion) {
        this.placa = placa;
        this.tipo = tipo;
        this.observacion = observacion;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Baneado getBaneado() {
        return baneado;
    }

    public void setBaneado(Baneado baneado) {
        this.baneado = baneado;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Negocio.Usuario[ placa=" + placa + " ]";
    }

    public UsuarioMensual getUsuarioMensual() {
        return usuarioMensual;
    }

    public void setUsuarioMensual(UsuarioMensual usuarioMensual) {
        this.usuarioMensual = usuarioMensual;
    }
    
}
