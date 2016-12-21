/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controladores.BaneadoJpaController;
import Controladores.CobroDiarioJpaController;
import Controladores.CobroMensualJpaController;
import Controladores.ConfiguracionesJpaController;
import Controladores.CupoJpaController;
import Controladores.LockerJpaController;
import Controladores.UsuarioDiarioJpaController;
import Controladores.UsuarioJpaController;
import Controladores.UsuarioMensualJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Todesser
 */
public class Conection {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("MotoParkingPU");
    private static CobroDiarioJpaController cobroDiaro = new CobroDiarioJpaController(EMF);
    private static CobroMensualJpaController cobroMensual = new CobroMensualJpaController(EMF);
    private static ConfiguracionesJpaController configuraciones = new ConfiguracionesJpaController(EMF);
    private static CupoJpaController cupo = new CupoJpaController(EMF);
    private static LockerJpaController locker = new LockerJpaController(EMF);
    private static UsuarioJpaController usuario = new UsuarioJpaController(EMF);
    private static UsuarioDiarioJpaController usuarioDiario = new UsuarioDiarioJpaController(EMF);
    private static UsuarioMensualJpaController usuarioMensual = new UsuarioMensualJpaController(EMF);
    private static BaneadoJpaController baneado = new BaneadoJpaController(EMF);

    public static CobroDiarioJpaController getCobroDiaro() {
        return cobroDiaro;
    }

    public static void setCobroDiaro(CobroDiarioJpaController cobroDiaro) {
        Conection.cobroDiaro = cobroDiaro;
    }

    public static CobroMensualJpaController getCobroMensual() {
        return cobroMensual;
    }

    public static void setCobroMensual(CobroMensualJpaController cobroMensual) {
        Conection.cobroMensual = cobroMensual;
    }

    public static ConfiguracionesJpaController getConfiguraciones() {
        return configuraciones;
    }

    public static void setConfiguraciones(ConfiguracionesJpaController configuraciones) {
        Conection.configuraciones = configuraciones;
    }

    public static CupoJpaController getCupo() {
        return cupo;
    }

    public static void setCupo(CupoJpaController cupo) {
        Conection.cupo = cupo;
    }

    public static LockerJpaController getLocker() {
        return locker;
    }

    public static void setLocker(LockerJpaController locker) {
        Conection.locker = locker;
    }

    public static UsuarioDiarioJpaController getUsuarioDiario() {
        return usuarioDiario;
    }

    public static void setUsuarioDiario(UsuarioDiarioJpaController usuarioDiario) {
        Conection.usuarioDiario = usuarioDiario;
    }

    public static UsuarioMensualJpaController getUsuarioMensual() {
        return usuarioMensual;
    }

    public static void setUsuarioMensual(UsuarioMensualJpaController usuarioMensual) {
        Conection.usuarioMensual = usuarioMensual;
    }

    public static EntityManagerFactory getEMF(){
        return EMF;
    }

    public static UsuarioJpaController getUsuario() {
        return usuario;
    }

    public static void setUsuario(UsuarioJpaController usuario) {
        Conection.usuario = usuario;
    }

    public static BaneadoJpaController getBaneado() {
        return baneado;
    }

    public static void setBaneado(BaneadoJpaController baneado) {
        Conection.baneado = baneado;
    }      
}
