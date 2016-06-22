/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adaptadores;

import Negocio.Cupo;
import java.util.Date;

/**
 *
 * @author Todesser
 */
public class adaptadorCupo {
    private Cupo cupo;
    long consecutivo;
    String placa;
    Date ingreso;
    String locker;
    long entradas;

    public adaptadorCupo(Cupo cupo) {
        this.cupo = cupo;
        this.consecutivo = cupo.getConsecutivo();
        this.placa = cupo.getPlaca().getPlaca();
        this.ingreso = cupo.getIngreso();
        this.locker = cupo.getLocker().toString();
        this.entradas = cupo.getPlaca().getEntradas();
    }   
    
    public Cupo getCupo() {
        return cupo;
    }

    public void setCupo(Cupo cupo) {
        this.cupo = cupo;
    }

    public long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    }

    public long getEntradas() {
        return entradas;
    }

    public void setEntradas(long entradas) {
        this.entradas = entradas;
    }
    
}
