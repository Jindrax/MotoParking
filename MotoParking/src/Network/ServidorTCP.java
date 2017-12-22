/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import Negocio.Cupo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Todesser
 */
public class ServidorTCP extends Server {

    public ServidorTCP() {
        super();
    }
    
    public void broadcast(Object o){
        for(Connection conection: getConnections()){
            conection.sendTCP(o);
        }
    }
    
    public void notifyChanges(List<Cupo> cupos){
        List<CupoJSON> estado = new ArrayList<>();
        for(Cupo cupo: cupos){
            estado.add(cupo.toJSON());
        }
        broadcast(estado);
    }
}