/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.util.List;

/**
 *
 * @author Santiago
 */
public class RespuestaServidor {
    
    public long tipoRespuesta;
    public List<CupoJSON> cupos;

    public RespuestaServidor() {
    }

    public RespuestaServidor(long tipoRespuesta, List<CupoJSON> cupos) {
        this.tipoRespuesta = tipoRespuesta;
        this.cupos = cupos;
    }

    public long getTipoRespuesta() {
        return tipoRespuesta;
    }

    public void setTipoRespuesta(long tipoRespuesta) {
        this.tipoRespuesta = tipoRespuesta;
    }

    public List<CupoJSON> getCupos() {
        return cupos;
    }

    public void setCupos(List<CupoJSON> cupos) {
        this.cupos = cupos;
    }
}
