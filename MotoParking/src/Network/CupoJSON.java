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
public class CupoJSON {
    public long consecutivo;
    public String placa;
    public long start;
    public long end;
    public long horas;
    public long minutos;
    public long cobro;
    public String locker;
    //-1 error no especificado
    //1 error en la entrada
    //2 tickete no encontrado
    public int codigoError;

    public CupoJSON(long consecutivo, String placa, long start, long end, long horas, long minutos, long cobro, String locker) {
        this.consecutivo = consecutivo;
        this.placa = placa;
        this.start = start;
        this.end = end;
        this.horas = horas;
        this.minutos = minutos;
        this.cobro = cobro;
        this.locker = locker;
        this.codigoError = 0;
    }
    
    public CupoJSON(int codigoError){
        this.codigoError = codigoError;
    }

    public CupoJSON() {
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

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
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

    public long getCobro() {
        return cobro;
    }

    public void setCobro(long cobro) {
        this.cobro = cobro;
    }

    public String getLocker() {
        return locker;
    }

    public void setLocker(String locker) {
        this.locker = locker;
    } 

    public int getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(int codigoError) {
        this.codigoError = codigoError;
    }
        
}
