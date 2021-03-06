/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import GUI.GUI;
import Negocio.Cupo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Todesser
 */
public class ServidorListener extends Listener{

    private GUI gui;
    
    public ServidorListener(GUI gui) {
        this.gui = gui;
    }
    
    @Override
    public void received(Connection cnctn, Object o) {
        if (!(o instanceof FrameworkMessage.KeepAlive)) {
            RespuestaServidor respuesta = null;
            SolicitudCliente solicitud = (SolicitudCliente) o;
            List<CupoJSON> cupos = new ArrayList<>();
            switch ((int) solicitud.getTipoSolicitud()) {
                case 0:
                    cupos.add(gui.ingresoDiario(solicitud.getPlaca(), (int) solicitud.getCascos()));
                    respuesta = new RespuestaServidor(1, cupos);
                    break;
                case 1:
                    cupos.add(gui.prospectoPorRed(solicitud.getConsecutivo()));
                    respuesta = new RespuestaServidor(2, cupos);
                    break;
                case 2:
                    cupos.add(gui.retiroPorRed(solicitud.getConsecutivo(), solicitud.isImpresion()));
                    respuesta = new RespuestaServidor(3, cupos);
                    break;
            }
            cnctn.sendTCP(respuesta);
        }
    }

    @Override
    public void disconnected(Connection cnctn) {
        super.disconnected(cnctn); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Cliente " + cnctn.toString() + " desconectado");
    }

    @Override
    public void connected(Connection cnctn) {
        List<CupoJSON> estado = new ArrayList<>();
        for(Cupo cupo: gui.getCuposActivos()){
            estado.add(cupo.toJSON());
        }
        cnctn.sendTCP(new RespuestaServidor(0, estado));
        System.out.println("Cliente: " + cnctn.getRemoteAddressTCP());
    }
    
}
