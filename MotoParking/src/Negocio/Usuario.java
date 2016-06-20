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
 * @author santiago pc
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByPlaca", query = "SELECT u FROM Usuario u WHERE u.placa = :placa"),
    @NamedQuery(name = "Usuario.findByTipo", query = "SELECT u FROM Usuario u WHERE u.tipo = :tipo")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private UsuarioMensual usuarioMensual;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private UsuarioDiario usuarioDiario;

    public Usuario() {
    }

    public Usuario(String placa) {
        this.placa = placa;
    }

    public Usuario(String placa, String tipo) {
        this.placa = placa;
        this.tipo = tipo;
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

    public UsuarioMensual getUsuarioMensual() {
        return usuarioMensual;
    }

    public void setUsuarioMensual(UsuarioMensual usuarioMensual) {
        this.usuarioMensual = usuarioMensual;
    }

    public UsuarioDiario getUsuarioDiario() {
        return usuarioDiario;
    }

    public void setUsuarioDiario(UsuarioDiario usuarioDiario) {
        this.usuarioDiario = usuarioDiario;
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
    
}
