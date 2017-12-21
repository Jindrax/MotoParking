/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Network.CupoJSON;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "cupo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cupo.findAll", query = "SELECT c FROM Cupo c"),
    @NamedQuery(name = "Cupo.findByConsecutivo", query = "SELECT c FROM Cupo c WHERE c.cupoPK.consecutivo = :consecutivo"),
    @NamedQuery(name = "Cupo.findByIngreso", query = "SELECT c FROM Cupo c WHERE c.cupoPK.ingreso = :ingreso"),
    @NamedQuery(name = "Cupo.findBySalida", query = "SELECT c FROM Cupo c WHERE c.salida = :salida"),
    @NamedQuery(name = "Cupo.findByHoras", query = "SELECT c FROM Cupo c WHERE c.horas = :horas"),
    @NamedQuery(name = "Cupo.findByMinutos", query = "SELECT c FROM Cupo c WHERE c.minutos = :minutos"),
    @NamedQuery(name = "Cupo.findByCobroSugerido", query = "SELECT c FROM Cupo c WHERE c.cobroSugerido = :cobroSugerido")})
public class Cupo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CupoPK cupoPK;
    @Column(name = "salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date salida;
    @Basic(optional = false)
    @Column(name = "horas")
    private long horas;
    @Basic(optional = false)
    @Column(name = "minutos")
    private long minutos;
    @Basic(optional = false)
    @Column(name = "cobro_sugerido")
    private long cobroSugerido;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cupo")
    private List<CobroDiario> cobroDiarioList;
    @JoinColumn(name = "locker", referencedColumnName = "identificador")
    @ManyToOne
    private Locker locker;
    @JoinColumn(name = "placa", referencedColumnName = "placa")
    @ManyToOne(optional = false)
    private UsuarioDiario placa;

    public Cupo() {
    }

    public Cupo(CupoPK cupoPK) {
        this.cupoPK = cupoPK;
    }

    public Cupo(CupoPK cupoPK, long horas, long minutos, long cobroSugerido) {
        this.cupoPK = cupoPK;
        this.horas = horas;
        this.minutos = minutos;
        this.cobroSugerido = cobroSugerido;
    }

    public Cupo(long consecutivo, Date ingreso) {
        this.cupoPK = new CupoPK(consecutivo, ingreso);
    }

    public CupoPK getCupoPK() {
        return cupoPK;
    }

    public void setCupoPK(CupoPK cupoPK) {
        this.cupoPK = cupoPK;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public long getHoras() {
        return horas;
    }

    public void setHoras(long horas) {
        this.horas = horas;
    }

    public long getMinutos() {
        return minutos;
    }

    public void setMinutos(long minutos) {
        this.minutos = minutos;
    }

    public long getCobroSugerido() {
        return cobroSugerido;
    }

    public void setCobroSugerido(long cobroSugerido) {
        this.cobroSugerido = cobroSugerido;
    }

    @XmlTransient
    public List<CobroDiario> getCobroDiarioList() {
        return cobroDiarioList;
    }

    public void setCobroDiarioList(List<CobroDiario> cobroDiarioList) {
        this.cobroDiarioList = cobroDiarioList;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public UsuarioDiario getPlaca() {
        return placa;
    }

    public void setPlaca(UsuarioDiario placa) {
        this.placa = placa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cupoPK != null ? cupoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cupo)) {
            return false;
        }
        Cupo other = (Cupo) object;
        if ((this.cupoPK == null && other.cupoPK != null) || (this.cupoPK != null && !this.cupoPK.equals(other.cupoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(cupoPK.getConsecutivo());
    }
    
    public CupoJSON toJSON(){
        return new CupoJSON(cupoPK.getConsecutivo(), placa.getPlaca(), cupoPK.getIngreso().getTime(), salida.getTime(), horas, minutos, cobroSugerido, locker.getIdentificador() + ":" + locker.getAlojamiento());
    }
    
    public Cupo clone(){
        Cupo retorno = new Cupo(cupoPK);
        retorno.setPlaca(placa);
        return retorno;
    }
    
}
