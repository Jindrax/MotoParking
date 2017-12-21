/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 *
 * @author Santiago
 */
public class SolicitudCliente {
    public long tipoSolicitud;
    public String placa;
    public long cascos;
    public long consecutivo;
    public boolean impresion;

    public SolicitudCliente() {
    }

    public SolicitudCliente(long tipoSolicitud, String placa, long cascos, long consecutivo, boolean impresion) {
        //0 solicitud de ingreso.
        //1 solicitud de prospecto de salida.
        //2 solicitud de salida.
        this.tipoSolicitud = tipoSolicitud;
        this.placa = placa;
        this.cascos = cascos;
        this.consecutivo = consecutivo;
        this.impresion = impresion;
    }

    public long getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(long tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public long getCascos() {
        return cascos;
    }

    public void setCascos(long cascos) {
        this.cascos = cascos;
    }

    public long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public boolean isImpresion() {
        return impresion;
    }

    public void setImpresion(boolean impresion) {
        this.impresion = impresion;
    }
    
}
