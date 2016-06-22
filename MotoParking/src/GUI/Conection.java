/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controladores.CobroDiarioJpaController;
import Controladores.CobroMensualJpaController;
import Controladores.ConfiguracionesJpaController;
import Controladores.CupoJpaController;
import Controladores.LockerJpaController;
import Controladores.UsuarioDiarioJpaController;
import Controladores.UsuarioMensualJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Todesser
 */
public class Conection {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("MotoParkingPU");
    private static CobroDiarioJpaController cobroDiaro = new CobroDiarioJpaController(emf);
    private static CobroMensualJpaController cobroMensual = new CobroMensualJpaController(emf);
    private static ConfiguracionesJpaController configuraciones = new ConfiguracionesJpaController(emf);
    private static CupoJpaController cupo = new CupoJpaController(emf);
    private static LockerJpaController locker = new LockerJpaController(emf);
    private static UsuarioDiarioJpaController usuarioDiario = new UsuarioDiarioJpaController(emf);
    private static UsuarioMensualJpaController usuarioMensual = new UsuarioMensualJpaController(emf);

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

    
}
