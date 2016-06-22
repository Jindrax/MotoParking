/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santiago pc
 */
@Entity
@Table(name = "locker")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locker.findAll", query = "SELECT l FROM Locker l"),
    @NamedQuery(name = "Locker.findByIdentificador", query = "SELECT l FROM Locker l WHERE l.identificador = :identificador"),
    @NamedQuery(name = "Locker.findByAlojamiento", query = "SELECT l FROM Locker l WHERE l.alojamiento = :alojamiento"),
    @NamedQuery(name = "Locker.findByCapacidad", query = "SELECT l FROM Locker l WHERE l.capacidad = :capacidad")})
public class Locker implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificador")
    private String identificador;
    @Basic(optional = false)
    @Column(name = "alojamiento")
    private long alojamiento;
    @Basic(optional = false)
    @Column(name = "capacidad")
    private long capacidad;
    @OneToMany(mappedBy = "locker")
    private List<Cupo> cupoList;

    public Locker() {
    }

    public Locker(String identificador) {
        this.identificador = identificador;
    }

    public Locker(String identificador, long alojamiento, long capacidad) {
        this.identificador = identificador;
        this.alojamiento = alojamiento;
        this.capacidad = capacidad;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public long getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(long alojamiento) {
        this.alojamiento = alojamiento;
    }

    public long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(long capacidad) {
        this.capacidad = capacidad;
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
        hash += (identificador != null ? identificador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Locker)) {
            return false;
        }
        Locker other = (Locker) object;
        if ((this.identificador == null && other.identificador != null) || (this.identificador != null && !this.identificador.equals(other.identificador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return identificador;
    }
    
}
