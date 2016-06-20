/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santiago pc
 */
@Entity
@Table(name = "usuario_diario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioDiario.findAll", query = "SELECT u FROM UsuarioDiario u"),
    @NamedQuery(name = "UsuarioDiario.findByPlaca", query = "SELECT u FROM UsuarioDiario u WHERE u.placa = :placa"),
    @NamedQuery(name = "UsuarioDiario.findByEntradas", query = "SELECT u FROM UsuarioDiario u WHERE u.entradas = :entradas"),
    @NamedQuery(name = "UsuarioDiario.findByMinutosRegistrados", query = "SELECT u FROM UsuarioDiario u WHERE u.minutosRegistrados = :minutosRegistrados"),
    @NamedQuery(name = "UsuarioDiario.findByCobroTotal", query = "SELECT u FROM UsuarioDiario u WHERE u.cobroTotal = :cobroTotal")})
public class UsuarioDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "entradas")
    private long entradas;
    @Basic(optional = false)
    @Column(name = "minutos_registrados")
    private long minutosRegistrados;
    @Basic(optional = false)
    @Column(name = "cobro_total")
    private long cobroTotal;
    @JoinColumn(name = "placa", referencedColumnName = "placa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "placa")
    private List<Cupo> cupoList;

    public UsuarioDiario() {
    }

    public UsuarioDiario(String placa) {
        this.placa = placa;
    }

    public UsuarioDiario(String placa, long entradas, long minutosRegistrados, long cobroTotal) {
        this.placa = placa;
        this.entradas = entradas;
        this.minutosRegistrados = minutosRegistrados;
        this.cobroTotal = cobroTotal;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public long getEntradas() {
        return entradas;
    }

    public void setEntradas(long entradas) {
        this.entradas = entradas;
    }

    public long getMinutosRegistrados() {
        return minutosRegistrados;
    }

    public void setMinutosRegistrados(long minutosRegistrados) {
        this.minutosRegistrados = minutosRegistrados;
    }

    public long getCobroTotal() {
        return cobroTotal;
    }

    public void setCobroTotal(long cobroTotal) {
        this.cobroTotal = cobroTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public List<Cupo> getCupoList() {
        return cupoList;
    }

    public void setCupoList(List<Cupo> cupoList) {
        this.cupoList = cupoList;
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
        if (!(object instanceof UsuarioDiario)) {
            return false;
        }
        UsuarioDiario other = (UsuarioDiario) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Negocio.UsuarioDiario[ placa=" + placa + " ]";
    }
    
}
