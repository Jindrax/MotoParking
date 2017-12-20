/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Network.ServidorUDP;
import Network.ServidorTCP;
import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Dialogos.AutenticarDialogo;
import Dialogos.MensualidadDialogo;
import Dialogos.RetiroDialogo;
import Impresion.PrintNow;
import Negocio.Baneado;
import Negocio.CobroDiario;
import Negocio.CobroMensual;
import Negocio.Configuraciones;
import Negocio.Cupo;
import Negocio.CupoPK;
import Negocio.Locker;
import Negocio.Usuario;
import Negocio.UsuarioDiario;
import Negocio.UsuarioMensual;
import Utilidades.Autenticador;
import Utilidades.Auxi;
import Utilidades.CustomComparator;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.joda.time.LocalDate;

/**
 *
 * @author santiago pc
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    Map<Long, Cupo> cuposActivos = new HashMap<>();
    Cupo cupoActual = null;
    EventList<String> colaEntrada;
    EventList<String> mensuales;

    private void inicializarTablaDiario() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("select c from Cupo c where c.salida is NULL ORDER BY c.cupoPK.consecutivo DESC");
        List<Cupo> cupos = query.getResultList();
        String[] columnas = {"#", "Placa", "Ingreso", "Locker", "Cascos", "Entradas"};
        Object[][] campos = new Object[cupos.size()][columnas.length];
        int i = 0;
        for (Cupo cupo : cupos) {
            campos[i][0] = cupo;
            campos[i][1] = cupo.getPlaca().getPlaca();
            campos[i][2] = Auxi.formaterHora(cupo.getCupoPK().getIngreso());
            if (cupo.getLocker() == null) {
                campos[i][3] = "Ninguno";
                campos[i][4] = "-";
            } else {
                campos[i][3] = cupo.getLocker();
                campos[i][4] = cupo.getLocker().getAlojamiento();
            }
            campos[i][5] = cupo.getPlaca().getEntradas();
            i++;
            cuposActivos.put(cupo.getCupoPK().getConsecutivo(), cupo);
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaDiario.setModel(modelo);
        tablaDiario.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
        tablaDiario.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int tamano[] = {70, 350, 120, 100, 100, 120};
        for (i = 0; i < tablaDiario.getColumnCount(); i++) {
            TableColumn columna = tablaDiario.getColumnModel().getColumn(i);
            columna.setPreferredWidth(tamano[i]);
        }
    }

    public GUI() {
        initComponents();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        this.setTitle(Conection.getConfiguraciones().findConfiguraciones("nombreEmpresa").getValor());
        ImageIcon icono = new ImageIcon(getClass().getResource("/Recursos/IcoMotoParqueo.png"));
        this.setIconImage(icono.getImage());
        inicializarTablaDiario();
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date fechaActual = LocalDate.now().toDate();
        Configuraciones fecha = Conection.getConfiguraciones().findConfiguraciones("fechaActual"),
                consecutivo = Conection.getConfiguraciones().findConfiguraciones("consecutivoDiario");
        if (fecha == null) {
            inicializarBase();
            fecha = Conection.getConfiguraciones().findConfiguraciones("fechaActual");
            consecutivo = Conection.getConfiguraciones().findConfiguraciones("consecutivoDiario");
        }
        if (fecha.getValor().compareTo(formato.format(fechaActual)) != 0) {
            fecha.setValor(formato.format(fechaActual));
            consecutivo.setValor("1");
        }
        try {
            Conection.getConfiguraciones().edit(fecha);
            Conection.getConfiguraciones().edit(consecutivo);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        colaEntrada = new BasicEventList<>();
        DefaultEventListModel<String> modelo = GlazedListsSwing.eventListModel(colaEntrada);
        listaEspera.setModel(modelo);
        AutoCompleteDecorator.decorate(placaDiario, colaEntrada, false);
        mensuales = new BasicEventList<>();
        AutoCompleteDecorator.decorate(placaCobroMensual, mensuales, false);
        (new Thread(new ServidorTCP(this))).start();
        (new Thread(new ServidorUDP(this))).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Contenedor = new javax.swing.JTabbedPane();
        panelDiario = new javax.swing.JPanel();
        ScrollDiario = new javax.swing.JScrollPane();
        tablaDiario = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        observacinoesDiario = new javax.swing.JTextArea();
        actionCobroMensual2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        listaEspera = new javax.swing.JList<>();
        jLabel27 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        placaDiario = new javax.swing.JTextField();
        actionDiario = new javax.swing.JButton();
        cascosDiario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        seltiempo = new javax.swing.JLabel();
        selcobro = new javax.swing.JLabel();
        actionAnularDiario = new javax.swing.JButton();
        actionCobroMensual1 = new javax.swing.JButton();
        selplaca = new javax.swing.JLabel();
        PanelPlacaMensual = new javax.swing.JPanel();
        ScrollMensual = new javax.swing.JScrollPane();
        tablaMorosos = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        placaCobroMensual = new javax.swing.JTextField();
        actionCobroMensual = new javax.swing.JButton();
        panelAdmin = new javax.swing.JTabbedPane();
        historialPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        fechaSelector = new org.jdesktop.swingx.JXDatePicker();
        jLabel6 = new javax.swing.JLabel();
        iniconsHistorial = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        finconsHistorial = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        totalHistorial = new javax.swing.JLabel();
        actionHistorial = new javax.swing.JButton();
        actionHistorial1 = new javax.swing.JButton();
        actionModHistorial = new javax.swing.JButton();
        panelMensual = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaMensual = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaCobroMensual = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        selectorMensual = new org.jdesktop.swingx.JXDatePicker();
        jLabel25 = new javax.swing.JLabel();
        actionMensual1 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        nombreMensual = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        actionMensual2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        cobroMensual = new org.jdesktop.swingx.JXDatePicker();
        jLabel11 = new javax.swing.JLabel();
        actionMensual = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        ingresoMensual = new org.jdesktop.swingx.JXDatePicker();
        mensualidadMensual = new javax.swing.JTextField();
        documentoMensual = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        placaMensual = new javax.swing.JTextField();
        telefonoMensual = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        userPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        placaClientes = new javax.swing.JTextField();
        actionClientes = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        entradasClientes = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        minutosClientes = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cobradoClientes = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        observacionesClientes = new javax.swing.JTextArea();
        actionGuardarClientes = new javax.swing.JButton();
        actionBanClientes = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        razonClientes = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        actionUnbanClientes = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        banDesdeClientes = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        panelLockers = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaLockers = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idLocker = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        aloLocker = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        capLocker = new javax.swing.JTextField();
        actionLocker = new javax.swing.JButton();
        configPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaConfig = new javax.swing.JTable();
        valorConfig = new javax.swing.JTextField();
        descConfig = new javax.swing.JLabel();
        actionImport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MotoParqueo 259");
        setPreferredSize(new java.awt.Dimension(1440, 860));
        setSize(new java.awt.Dimension(1440, 860));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Contenedor.setBackground(new java.awt.Color(0, 0, 0));
        Contenedor.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        Contenedor.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                ContenedorComponentShown(evt);
            }
        });

        panelDiario.setBackground(new java.awt.Color(255, 255, 0));
        panelDiario.setPreferredSize(new java.awt.Dimension(1406, 860));
        panelDiario.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelDiarioComponentShown(evt);
            }
        });
        panelDiario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ScrollDiario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ScrollDiarioMouseClicked(evt);
            }
        });

        tablaDiario.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaDiario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaDiario.setRowHeight(30);
        tablaDiario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDiarioMouseClicked(evt);
            }
        });
        ScrollDiario.setViewportView(tablaDiario);

        panelDiario.add(ScrollDiario, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, 860, 400));

        jPanel2.setBackground(new java.awt.Color(255, 255, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        panelDiario.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel16.setText("Clientes mensuales en estado de mora:");
        panelDiario.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, -1, -1));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        panelDiario.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 204, 480, 12));

        jPanel5.setBackground(new java.awt.Color(255, 255, 0));
        java.awt.GridBagLayout jPanel5Layout = new java.awt.GridBagLayout();
        jPanel5Layout.columnWidths = new int[] {0, 5, 0};
        jPanel5Layout.rowHeights = new int[] {0};
        jPanel5.setLayout(jPanel5Layout);
        panelDiario.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 793, 208, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 0));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane8.setHorizontalScrollBar(null);

        observacinoesDiario.setColumns(20);
        observacinoesDiario.setLineWrap(true);
        observacinoesDiario.setRows(5);
        observacinoesDiario.setWrapStyleWord(true);
        jScrollPane8.setViewportView(observacinoesDiario);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 290;
        gridBagConstraints.ipady = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane8, gridBagConstraints);

        actionCobroMensual2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        actionCobroMensual2.setText("Guardar Observacion");
        actionCobroMensual2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionCobroMensual2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(actionCobroMensual2, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel17.setText("Observaciones:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(jLabel17, gridBagConstraints);

        panelDiario.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1116, 222, 240, 196));

        jPanel7.setBackground(new java.awt.Color(255, 255, 0));
        jPanel7.setPreferredSize(new java.awt.Dimension(150, 181));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jScrollPane12.setPreferredSize(new java.awt.Dimension(50, 250));

        listaEspera.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        listaEspera.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listaEspera.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listaEspera.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listaEsperaValueChanged(evt);
            }
        });
        jScrollPane12.setViewportView(listaEspera);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 281;
        gridBagConstraints.ipady = 330;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel7.add(jScrollPane12, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel27.setText("Lista de espera:");
        jPanel7.add(jLabel27, new java.awt.GridBagConstraints());

        panelDiario.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1124, 14, 230, 172));

        jPanel6.setBackground(new java.awt.Color(255, 255, 0));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("Placa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel6.add(jLabel4, gridBagConstraints);

        placaDiario.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        placaDiario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        placaDiario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                placaDiarioMouseClicked(evt);
            }
        });
        placaDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placaDiarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 129;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel6.add(placaDiario, gridBagConstraints);

        actionDiario.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionDiario.setText("Registrar");
        actionDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionDiarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 69;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.weightx = 0.5;
        jPanel6.add(actionDiario, gridBagConstraints);

        cascosDiario.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        cascosDiario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cascosDiario.setText("0");
        cascosDiario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cascosDiarioMouseClicked(evt);
            }
        });
        cascosDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cascosDiarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel6.add(cascosDiario, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Cascos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        jPanel6.add(jLabel5, gridBagConstraints);

        panelDiario.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(886, 14, -1, 172));

        jPanel3.setBackground(new java.awt.Color(255, 255, 0));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        seltiempo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        seltiempo.setText("Tiempo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel3.add(seltiempo, gridBagConstraints);

        selcobro.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        selcobro.setText("Cobro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel3.add(selcobro, gridBagConstraints);

        actionAnularDiario.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionAnularDiario.setText("Anular");
        actionAnularDiario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionAnularDiarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 103;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel3.add(actionAnularDiario, gridBagConstraints);

        actionCobroMensual1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionCobroMensual1.setText("Ticket");
        actionCobroMensual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionCobroMensual1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 111;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(actionCobroMensual1, gridBagConstraints);

        selplaca.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        selplaca.setText("Placa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel3.add(selplaca, gridBagConstraints);

        panelDiario.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 222, 228, -1));

        PanelPlacaMensual.setBackground(new java.awt.Color(255, 255, 0));
        PanelPlacaMensual.setLayout(new java.awt.GridBagLayout());
        panelDiario.add(PanelPlacaMensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 502, -1, -1));

        tablaMorosos.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaMorosos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaMorosos.setRowHeight(30);
        ScrollMensual.setViewportView(tablaMorosos);

        panelDiario.add(ScrollMensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 1072, 200));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        panelDiario.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 432, 480, 10));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel26.setText("Placa");
        panelDiario.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 450, -1, -1));

        placaCobroMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        placaCobroMensual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        placaCobroMensual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                placaCobroMensualMouseClicked(evt);
            }
        });
        placaCobroMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placaCobroMensualActionPerformed(evt);
            }
        });
        panelDiario.add(placaCobroMensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 450, 180, -1));

        actionCobroMensual.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        actionCobroMensual.setText("Cobrar Mensualidad");
        actionCobroMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionCobroMensualActionPerformed(evt);
            }
        });
        panelDiario.add(actionCobroMensual, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 490, 250, -1));

        Contenedor.addTab("Diario", panelDiario);

        panelAdmin.setBackground(new java.awt.Color(0, 0, 0));
        panelAdmin.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        panelAdmin.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelAdminComponentShown(evt);
            }
        });

        historialPanel.setBackground(new java.awt.Color(255, 255, 0));
        historialPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                historialPanelComponentShown(evt);
            }
        });

        tablaHistorial.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaHistorial.setRowHeight(30);
        jScrollPane4.setViewportView(tablaHistorial);

        fechaSelector.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        fechaSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaSelectorActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("Desde");

        iniconsHistorial.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        iniconsHistorial.setText("jLabel7");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setText("Hasta");

        finconsHistorial.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        finconsHistorial.setText("jLabel8");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel8.setText("Total");

        totalHistorial.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        totalHistorial.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalHistorial.setText("jLabel9");

        actionHistorial.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionHistorial.setText("Imprimir Resumen");
        actionHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionHistorialActionPerformed(evt);
            }
        });

        actionHistorial1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionHistorial1.setText("Imprimir Recibo");
        actionHistorial1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionHistorial1ActionPerformed(evt);
            }
        });

        actionModHistorial.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionModHistorial.setText("Modificar");
        actionModHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionModHistorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historialPanelLayout = new javax.swing.GroupLayout(historialPanel);
        historialPanel.setLayout(historialPanelLayout);
        historialPanelLayout.setHorizontalGroup(
            historialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iniconsHistorial)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finconsHistorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialPanelLayout.createSequentialGroup()
                        .addComponent(actionHistorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actionHistorial1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actionModHistorial)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fechaSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        historialPanelLayout.setVerticalGroup(
            historialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fechaSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(actionHistorial)
                    .addComponent(actionHistorial1)
                    .addComponent(actionModHistorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(historialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(iniconsHistorial)
                    .addComponent(jLabel7)
                    .addComponent(finconsHistorial)
                    .addComponent(jLabel8)
                    .addComponent(totalHistorial))
                .addContainerGap())
        );

        panelAdmin.addTab("Historial", historialPanel);

        panelMensual.setBackground(new java.awt.Color(255, 255, 0));
        panelMensual.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelMensualComponentShown(evt);
            }
        });

        tablaMensual.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaMensual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaMensual.setRowHeight(30);
        tablaMensual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMensualMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tablaMensual);

        tablaCobroMensual.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaCobroMensual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaCobroMensual.setRowHeight(30);
        jScrollPane7.setViewportView(tablaCobroMensual);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        selectorMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        selectorMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectorMensualActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel25.setText("Cobros de mensualidades:");

        actionMensual1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionMensual1.setText("Imprimir Recibo");
        actionMensual1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionMensual1ActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 255, 0));
        java.awt.GridBagLayout jPanel8Layout = new java.awt.GridBagLayout();
        jPanel8Layout.columnWidths = new int[] {0, 5, 0};
        jPanel8Layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        jPanel8.setLayout(jPanel8Layout);

        nombreMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        nombreMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(nombreMensual, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel9.setText("Placa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel9, gridBagConstraints);

        actionMensual2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionMensual2.setText("Eliminar");
        actionMensual2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionMensual2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 186;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(actionMensual2, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel14.setText("Ingreso");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel14, gridBagConstraints);

        cobroMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 47;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(cobroMensual, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel11.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel11, gridBagConstraints);

        actionMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionMensual.setText("Añadir/Editar");
        actionMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 134;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(actionMensual, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel10.setText("Cedula");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel10, gridBagConstraints);

        ingresoMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 47;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(ingresoMensual, gridBagConstraints);

        mensualidadMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        mensualidadMensual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mensualidadMensual.setText("0");
        mensualidadMensual.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mensualidadMensualFocusGained(evt);
            }
        });
        mensualidadMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mensualidadMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(mensualidadMensual, gridBagConstraints);

        documentoMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        documentoMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentoMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(documentoMensual, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel15.setText("Cobro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel15, gridBagConstraints);

        placaMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        placaMensual.setMinimumSize(new java.awt.Dimension(6, 30));
        placaMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placaMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel8.add(placaMensual, gridBagConstraints);

        telefonoMensual.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        telefonoMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telefonoMensualActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 166;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(telefonoMensual, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel13.setText("Mensualidad");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel13, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel12.setText("Telefono");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel8.add(jLabel12, gridBagConstraints);

        javax.swing.GroupLayout panelMensualLayout = new javax.swing.GroupLayout(panelMensual);
        panelMensual.setLayout(panelMensualLayout);
        panelMensualLayout.setHorizontalGroup(
            panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMensualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMensualLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(0, 725, Short.MAX_VALUE))
                    .addGroup(panelMensualLayout.createSequentialGroup()
                        .addGroup(panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addComponent(jScrollPane7))
                        .addGap(18, 18, 18)))
                .addGroup(panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(actionMensual1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectorMensual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelMensualLayout.setVerticalGroup(
            panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMensualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelMensualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMensualLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(panelMensualLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectorMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actionMensual1)
                        .addGap(0, 72, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelAdmin.addTab("Mensual", panelMensual);

        userPanel.setBackground(new java.awt.Color(255, 255, 0));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel18.setText("Placa");

        placaClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        placaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                placaClientesMouseClicked(evt);
            }
        });
        placaClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placaClientesActionPerformed(evt);
            }
        });

        actionClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionClientes.setText("Buscar");
        actionClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionClientesActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel19.setText("Entradas");

        entradasClientes.setEditable(false);
        entradasClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel20.setText("Minutos");

        minutosClientes.setEditable(false);
        minutosClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel21.setText("Cobrado");

        cobradoClientes.setEditable(false);
        cobradoClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        jLabel22.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel22.setText("Observaciones");

        observacionesClientes.setColumns(20);
        observacionesClientes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        observacionesClientes.setLineWrap(true);
        observacionesClientes.setRows(5);
        observacionesClientes.setWrapStyleWord(true);
        jScrollPane9.setViewportView(observacionesClientes);

        actionGuardarClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionGuardarClientes.setText("Guardar");
        actionGuardarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionGuardarClientesActionPerformed(evt);
            }
        });

        actionBanClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionBanClientes.setText("Banear");
        actionBanClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionBanClientesActionPerformed(evt);
            }
        });

        razonClientes.setColumns(20);
        razonClientes.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        razonClientes.setLineWrap(true);
        razonClientes.setRows(5);
        razonClientes.setWrapStyleWord(true);
        jScrollPane10.setViewportView(razonClientes);

        jLabel23.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel23.setText("Razon Baneo");

        actionUnbanClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionUnbanClientes.setText("Desbanear");
        actionUnbanClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionUnbanClientesActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel24.setText("Baneado desde:");

        banDesdeClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        banDesdeClientes.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        banDesdeClientes.setText("0");

        tablaClientes.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaClientes.setRowHeight(30);
        jScrollPane11.setViewportView(tablaClientes);

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(placaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actionClientes))
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cobradoClientes)
                            .addComponent(minutosClientes)
                            .addComponent(entradasClientes)))
                    .addComponent(jLabel22)
                    .addComponent(jScrollPane9)
                    .addComponent(actionGuardarClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(actionBanClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23)
                    .addComponent(jScrollPane10)
                    .addComponent(actionUnbanClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(banDesdeClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(userPanelLayout.createSequentialGroup()
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(placaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(actionClientes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(entradasClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(minutosClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(cobradoClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actionGuardarClientes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(banDesdeClientes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(actionBanClientes))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(actionUnbanClientes)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelAdmin.addTab("Clientes", userPanel);

        panelLockers.setBackground(new java.awt.Color(255, 255, 0));
        panelLockers.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelLockersComponentShown(evt);
            }
        });

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tablaLockers.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaLockers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaLockers.setRowHeight(30);
        tablaLockers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaLockersMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tablaLockersMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablaLockers);

        jPanel1.setBackground(new java.awt.Color(255, 255, 0));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel1.setText("Identificacion");

        idLocker.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel2.setText("Alojamiento");

        aloLocker.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel3.setText("Capacidad");

        capLocker.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        actionLocker.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionLocker.setText("Añadir/Editar");
        actionLocker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionLockerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(actionLocker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idLocker))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(aloLocker))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(capLocker)))
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(idLocker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(aloLocker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(capLocker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(actionLocker)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelLockersLayout = new javax.swing.GroupLayout(panelLockers);
        panelLockers.setLayout(panelLockersLayout);
        panelLockersLayout.setHorizontalGroup(
            panelLockersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLockersLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLockersLayout.setVerticalGroup(
            panelLockersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelAdmin.addTab("Lockers", panelLockers);

        configPanel.setBackground(new java.awt.Color(255, 255, 0));
        configPanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        configPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                configPanelComponentShown(evt);
            }
        });

        tablaConfig.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tablaConfig.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaConfig.setRowHeight(30);
        tablaConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaConfigMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaConfig);

        valorConfig.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        valorConfig.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        valorConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valorConfigActionPerformed(evt);
            }
        });

        descConfig.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        descConfig.setText("Nuevo Valor");

        actionImport.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        actionImport.setText("Importar Data");

        javax.swing.GroupLayout configPanelLayout = new javax.swing.GroupLayout(configPanel);
        configPanel.setLayout(configPanelLayout);
        configPanelLayout.setHorizontalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configPanelLayout.createSequentialGroup()
                        .addComponent(descConfig)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(valorConfig)
                    .addComponent(actionImport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        configPanelLayout.setVerticalGroup(
            configPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(configPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descConfig)
                .addGap(8, 8, 8)
                .addComponent(valorConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(actionImport)
                .addContainerGap())
        );

        panelAdmin.addTab("Configuracion", null, configPanel, "");

        Contenedor.addTab("Administracion", panelAdmin);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Contenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 1374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ContenedorComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_ContenedorComponentShown
        placaDiario.requestFocus();
    }//GEN-LAST:event_ContenedorComponentShown

    private void panelAdminComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelAdminComponentShown
        new AutenticarDialogo(this, true, panelAdmin);
        actionModHistorial.setEnabled(Autenticador.estado);
        fechaSelector.setDate(new Date());
        inicializarTablaHistorial(fechaSelector.getDate());
    }//GEN-LAST:event_panelAdminComponentShown

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void tablaLockersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaLockersMouseReleased

    }//GEN-LAST:event_tablaLockersMouseReleased

    private void tablaLockersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaLockersMouseClicked
        if (tablaLockers.getSelectedRow() != -1) {
            Locker locker = (Locker) tablaLockers.getValueAt(tablaLockers.getSelectedRow(), 0);
            idLocker.setText(locker.getIdentificador());
            aloLocker.setText(String.valueOf(locker.getAlojamiento()));
            capLocker.setText(String.valueOf(locker.getCapacidad()));
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un locker primero.");
        }
    }//GEN-LAST:event_tablaLockersMouseClicked

    private void actionLockerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionLockerActionPerformed
        Locker locker = Conection.getLocker().findLocker(idLocker.getText());
        if (locker == null) {
            locker = new Locker(idLocker.getText(), Long.parseLong(aloLocker.getText()), Long.parseLong(capLocker.getText()));
            try {
                Conection.getLocker().create(locker);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            locker.setIdentificador(idLocker.getText());
            locker.setAlojamiento(Long.parseLong(aloLocker.getText()));
            locker.setCapacidad(Long.parseLong(capLocker.getText()));
            try {
                Conection.getLocker().edit(locker);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        inicializarTablaLockers();
    }//GEN-LAST:event_actionLockerActionPerformed

    public String prospectoPorRed(String ingreso) {
        Cupo cupo = cuposActivos.get(Long.parseLong(ingreso));
        if(cupo==null){
            return "Ticket no existente o retirado";
        }
        String[] aux = Auxi.calcularTiempoMotoTentativo(cupo);
        return "Placa: " + cupo.getPlaca().getPlaca() + "\nTiempo: " + aux[0] + "\nCobro: " + aux[1];
    }
    
    public String retiroPorRed(String ingreso, boolean imprimir) {
        Cupo cupo = cuposActivos.remove(Long.parseLong(ingreso));
        if(cupo==null){
            return "Ticket no existente o retirado";
        }
        retiroDiario(cupo);
        Configuraciones consecutivo = Conection.getConfiguraciones().findConfiguraciones("consecutivo");
        CobroDiario cobro = new CobroDiario();
        UsuarioDiario usuario = cupo.getPlaca();
        cobro.setConsecutivo(Long.parseLong(consecutivo.getValor()));
        cobro.setFecha(new Date());
        cobro.setCobro(cupo.getCobroSugerido());
        cobro.setCupo(cupoActual);
        usuario.setEntradas(usuario.getEntradas() + 1);
        usuario.setMinutosRegistrados(usuario.getMinutosRegistrados() + cupo.getHoras() * 60 + cupo.getMinutos());
        usuario.setCobroTotal(usuario.getCobroTotal() + cobro.getCobro());
        try {
            Conection.getCupo().edit(cupoActual);
            Conection.getUsuarioDiario().edit(usuario);
            Conection.getCobroDiaro().create(cobro);
            consecutivo.setValor(String.valueOf(Integer.parseInt(consecutivo.getValor()) + 1));
            Conection.getConfiguraciones().edit(consecutivo);
            Locker locker = cupo.getLocker();
            if (locker != null) {
                locker.setAlojamiento(0);
                Conection.getLocker().edit(locker);
            }
        } catch (NonexistentEntityException ex) {
            return "Ha ocurrido un error";
        } catch (Exception ex) {
            return "Ha ocurrido un error";
        }
        if(imprimir){
            PrintNow.imprimirReciboSalida(cupo, cobro.getCobro());
        }
        if(cupo.getLocker()!=null){
            return "Cobro: " + String.valueOf(cupo.getCobroSugerido()) + "\n" + "Cascos: " + cupo.getLocker().getIdentificador();
        }else{
            return "Cobro: " + String.valueOf(cupo.getCobroSugerido()) + "\n" + "Sin cascos";
        }
    }

    public String actionDiarioProceso(String ingreso, int cascos, int origen) {
        int seleccion = Auxi.selector(ingreso);
        String retorno = null;
        switch (seleccion) {
            case 0:
                if (origen == 0) {
                    JOptionPane.showMessageDialog(null, "El ingreso no es un formato permitido.");
                    break;
                } else {
                    retorno = "Invalido";
                    break;
                }
            case 1:
                retorno = ingresoDiario(ingreso.toUpperCase(), cascos);
                break;
            case 2:
                retorno = ingresoDiario(ingreso.toUpperCase(), cascos);
                break;
            case 3:
                retorno = ingresoDiario(ingreso.toUpperCase(), cascos);
                break;
            case 4:
                if (cuposActivos.containsKey(Long.parseLong(ingreso))) {
                    if (origen == 0) {
                        Cupo cupo = cuposActivos.remove(Long.parseLong(ingreso));
                        retiroDiario(cupo);
                        new RetiroDialogo(this, rootPaneCheckingEnabled, cupo);
                        inicializarTablaDiario();
                    } else {
                        retiroPorRed(ingreso, false);
                    }
                    inicializarTablaDiario();
                } else {
                    if (origen == 0) {
                        JOptionPane.showMessageDialog(null, "Ese cupo no existe en el sistema.");
                    } else {
                        retorno = "No existe ese ticket";
                    }
                }
                break;
            case 5:
                if (origen == 0) {
                    JOptionPane.showMessageDialog(null, "El cliente es un cliente mensual.");
                    break;
                } else {
                    retorno = "Mensual";
                    break;
                }
            default:
                if (origen == 0) {
                    JOptionPane.showMessageDialog(null, "El ingreso no es un formato permitido.");
                    break;
                } else {
                    retorno = "Invalido";
                    break;
                }
        }
        placaDiario.setText("");
        cascosDiario.setText("0");
        placaDiario.requestFocus();
        return retorno;
    }

    private boolean estadoUsuario(String placa) {
        UsuarioDiario user = Conection.getUsuarioDiario().findUsuarioDiario(placa);
        if (user != null) {
            if (user.getUsuario().getBaneado() != null) {
                observacinoesDiario.setText(user.getUsuario().getBaneado().getRazon());
                JOptionPane.showMessageDialog(null, "El usuario se encuentra impedido en el sistema.");
                placaDiario.setText("");
                placaDiario.requestFocus();
                return false;
            } else {
                if (user.getUsuario().getObservacion() != null) {
                    observacinoesDiario.setText(user.getUsuario().getObservacion());
                    return true;
                } else {
                    observacinoesDiario.setText("");
                    return true;
                }
            }
        }
        return true;
    }

    private void configPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_configPanelComponentShown
        inicializarTablaConfig();
    }//GEN-LAST:event_configPanelComponentShown

    private void tablaConfigMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConfigMouseClicked
        if (tablaConfig.getSelectedRow() != -1) {
            Configuraciones config = (Configuraciones) tablaConfig.getValueAt(tablaConfig.getSelectedRow(), 0);
            descConfig.setText(config.getDescripcion());
            valorConfig.setText(config.getValor());
        }
    }//GEN-LAST:event_tablaConfigMouseClicked

    private void valorConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valorConfigActionPerformed
        Configuraciones config = Conection.getConfiguraciones().findConfiguraciones(descConfig.getText());
        config.setValor(valorConfig.getText());
        try {
            Conection.getConfiguraciones().edit(config);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        inicializarTablaConfig();
    }//GEN-LAST:event_valorConfigActionPerformed

    private void historialPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_historialPanelComponentShown
        fechaSelector.setDate(new Date());
        inicializarTablaHistorial(fechaSelector.getDate());
    }//GEN-LAST:event_historialPanelComponentShown

    private void fechaSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaSelectorActionPerformed
        inicializarTablaHistorial(fechaSelector.getDate());
    }//GEN-LAST:event_fechaSelectorActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        inicializarTablaMorosos();
        cargarListaMensuales();
    }//GEN-LAST:event_formWindowOpened

    private void actionHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionHistorialActionPerformed
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT c FROM CobroMensual c WHERE c.cobroMensualPK.fecha = :fecha");
        query.setParameter("fecha", fechaSelector.getDate());
        List<CobroMensual> cobroList = query.getResultList();
        PrintNow.printResumenDia(fechaSelector.getDate(), iniconsHistorial.getText(), finconsHistorial.getText(), totalHistorial.getText(), cobroList);
    }//GEN-LAST:event_actionHistorialActionPerformed

    private void panelMensualComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelMensualComponentShown
        limpiarUsuariosMensuales();
        selectorMensual.setDate(new Date());
        inicializarTablaCobroMensual(selectorMensual.getDate());
    }//GEN-LAST:event_panelMensualComponentShown

    private void actionMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionMensualActionPerformed
        UsuarioMensual usuarioMensual = Conection.getUsuarioMensual().findUsuarioMensual(placaMensual.getText().toUpperCase());
        if (usuarioMensual == null) {
            Usuario usuario = Conection.getUsuario().findUsuario(placaMensual.getText().toUpperCase());
            if (usuario == null) {
                usuario = new Usuario(placaMensual.getText().toUpperCase());
                usuario.setTipo("Moto");
                usuario.setObservacion("");
                try {
                    Conection.getUsuario().create(usuario);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            usuarioMensual = new UsuarioMensual(placaMensual.getText().toUpperCase());
            usuarioMensual.setDocumento(documentoMensual.getText().toUpperCase());
            usuarioMensual.setNombre(nombreMensual.getText().toUpperCase());
            usuarioMensual.setTelefono(telefonoMensual.getText().toUpperCase());
            usuarioMensual.setMensualidad(Long.parseLong(mensualidadMensual.getText().toUpperCase()));
            usuarioMensual.setFechaIngreso(ingresoMensual.getDate());
            usuarioMensual.setSigCobro(cobroMensual.getDate());
            usuarioMensual.setUsuario(usuario);
            try {
                Conection.getUsuarioMensual().create(usuarioMensual);
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            mensuales.add(usuarioMensual.getPlaca());
            JOptionPane.showMessageDialog(null, "Cliente mensual añadido correctamente.");
        } else {
            usuarioMensual.setDocumento(documentoMensual.getText().toUpperCase());
            usuarioMensual.setNombre(nombreMensual.getText().toUpperCase());
            usuarioMensual.setTelefono(telefonoMensual.getText().toUpperCase());
            usuarioMensual.setMensualidad(Long.parseLong(mensualidadMensual.getText().toUpperCase()));
            usuarioMensual.setFechaIngreso(ingresoMensual.getDate());
            usuarioMensual.setSigCobro(cobroMensual.getDate());
            try {
                Conection.getUsuarioMensual().edit(usuarioMensual);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Cliente mensual modificado correctamente.");
        }
        limpiarUsuariosMensuales();
    }//GEN-LAST:event_actionMensualActionPerformed

    private void mensualidadMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mensualidadMensualActionPerformed
        actionMensual.requestFocus();
    }//GEN-LAST:event_mensualidadMensualActionPerformed

    private void tablaMensualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMensualMouseClicked
        if (tablaMensual.getSelectedRow() != -1) {
            UsuarioMensual usuario = (UsuarioMensual) tablaMensual.getValueAt(tablaMensual.getSelectedRow(), 0);
            placaMensual.setText(usuario.getPlaca());
            documentoMensual.setText(usuario.getDocumento());
            nombreMensual.setText(usuario.getNombre());
            telefonoMensual.setText(usuario.getTelefono());
            mensualidadMensual.setText(String.valueOf(usuario.getMensualidad()));
            ingresoMensual.setDate(usuario.getFechaIngreso());
            cobroMensual.setDate(usuario.getSigCobro());
        }
    }//GEN-LAST:event_tablaMensualMouseClicked

    private void selectorMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectorMensualActionPerformed
        inicializarTablaCobroMensual(selectorMensual.getDate());
    }//GEN-LAST:event_selectorMensualActionPerformed

    private void placaClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placaClientesActionPerformed
        actionClientesActionPerformed(evt);
    }//GEN-LAST:event_placaClientesActionPerformed

    private void actionClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionClientesActionPerformed
        UsuarioDiario usuario = Conection.getUsuarioDiario().findUsuarioDiario(placaClientes.getText());
        if (usuario != null) {
            entradasClientes.setText(String.valueOf(usuario.getEntradas()));
            minutosClientes.setText(String.valueOf(usuario.getMinutosRegistrados()));
            cobradoClientes.setText(String.valueOf(usuario.getCobroTotal()));
            observacionesClientes.setText(usuario.getUsuario().getObservacion());
            if (usuario.getUsuario().getBaneado() != null) {
                razonClientes.setText(usuario.getUsuario().getBaneado().getRazon());
                banDesdeClientes.setText(Auxi.formaterFecha(usuario.getUsuario().getBaneado().getFecha()));
            }
            inicializarTablaClientes(usuario);
        } else {
            JOptionPane.showMessageDialog(null, "El usuario buscado no existe.");
            placaClientes.setText("");
            placaClientes.requestFocus();
        }
    }//GEN-LAST:event_actionClientesActionPerformed

    private void actionGuardarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionGuardarClientesActionPerformed
        UsuarioDiario usuario = Conection.getUsuarioDiario().findUsuarioDiario(placaClientes.getText());
        if (usuario != null) {
            Usuario user = usuario.getUsuario();
            user.setObservacion(observacionesClientes.getText());
            try {
                Conection.getUsuario().edit(user);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            placaClientes.setText("");
            entradasClientes.setText("");
            minutosClientes.setText("");
            cobradoClientes.setText("");
            observacionesClientes.setText("");
            razonClientes.setText("");
            banDesdeClientes.setText("");
            placaClientes.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario invalido.");
        }
    }//GEN-LAST:event_actionGuardarClientesActionPerformed

    private void actionBanClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionBanClientesActionPerformed
        UsuarioDiario usuario = Conection.getUsuarioDiario().findUsuarioDiario(placaClientes.getText());
        if (usuario != null) {
            Usuario user = usuario.getUsuario();
            Baneado ban = new Baneado();
            ban.setPlaca(user.getPlaca());
            ban.setUsuario(user);
            ban.setRazon(razonClientes.getText());
            ban.setFecha(new Date());
            try {
                Conection.getBaneado().create(ban);
                user.setBaneado(ban);
                Conection.getUsuario().edit(user);
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            placaClientes.setText("");
            entradasClientes.setText("");
            minutosClientes.setText("");
            cobradoClientes.setText("");
            observacionesClientes.setText("");
            razonClientes.setText("");
            banDesdeClientes.setText("");
            placaClientes.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario invalido.");
        }
    }//GEN-LAST:event_actionBanClientesActionPerformed

    private void actionUnbanClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionUnbanClientesActionPerformed
        UsuarioDiario usuario = Conection.getUsuarioDiario().findUsuarioDiario(placaClientes.getText());
        if (usuario != null) {
            Usuario user = usuario.getUsuario();
            if (user.getBaneado() != null) {
                Baneado ban = user.getBaneado();
                user.setBaneado(null);
                try {
                    Conection.getBaneado().destroy(ban.getPlaca());
                    Conection.getUsuario().edit(user);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario no ha estado baneado.");
            }
            placaClientes.setText("");
            entradasClientes.setText("");
            minutosClientes.setText("");
            cobradoClientes.setText("");
            observacionesClientes.setText("");
            razonClientes.setText("");
            banDesdeClientes.setText("");
            placaClientes.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario invalido.");
        }
    }//GEN-LAST:event_actionUnbanClientesActionPerformed

    private void placaMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placaMensualActionPerformed
        documentoMensual.requestFocus();
        documentoMensual.selectAll();
    }//GEN-LAST:event_placaMensualActionPerformed

    private void documentoMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentoMensualActionPerformed
        nombreMensual.requestFocus();
        nombreMensual.selectAll();
    }//GEN-LAST:event_documentoMensualActionPerformed

    private void nombreMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreMensualActionPerformed
        telefonoMensual.requestFocus();
        telefonoMensual.selectAll();
    }//GEN-LAST:event_nombreMensualActionPerformed

    private void telefonoMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telefonoMensualActionPerformed
        mensualidadMensual.requestFocus();
        mensualidadMensual.selectAll();
    }//GEN-LAST:event_telefonoMensualActionPerformed

    private void placaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_placaClientesMouseClicked
        placaClientes.selectAll();
    }//GEN-LAST:event_placaClientesMouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        placaDiario.requestFocus();
    }//GEN-LAST:event_formWindowGainedFocus

    private void panelLockersComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelLockersComponentShown
        inicializarTablaLockers();
    }//GEN-LAST:event_panelLockersComponentShown

    private void actionMensual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionMensual1ActionPerformed
        if (tablaCobroMensual.getSelectedRow() != -1) {
            CobroMensual cobro = (CobroMensual) tablaCobroMensual.getValueAt(tablaCobroMensual.getSelectedRow(), 0);
            PrintNow.printReciboMensual(cobro);
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un cobro.");
        }
    }//GEN-LAST:event_actionMensual1ActionPerformed

    private void actionHistorial1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionHistorial1ActionPerformed
        if (tablaHistorial.getSelectedRow() != -1) {
            CobroDiario cobro = (CobroDiario) tablaHistorial.getValueAt(tablaHistorial.getSelectedRow(), 0);
            PrintNow.imprimirReciboSalida(cobro.getCupo(), cobro.getCobro());
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un elemento.");
        }
    }//GEN-LAST:event_actionHistorial1ActionPerformed

    private void actionMensual2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionMensual2ActionPerformed
        UsuarioMensual user = Conection.getUsuarioMensual().findUsuarioMensual(placaMensual.getText());
        if (user != null) {
            try {
                for (CobroMensual cobro : user.getCobroMensualList()) {
                    Conection.getCobroMensual().destroy(cobro.getCobroMensualPK());
                }
                Conection.getUsuarioMensual().destroy(user.getPlaca());
                mensuales.remove(user.getPlaca());
                limpiarUsuariosMensuales();
                JOptionPane.showMessageDialog(null, "Cliente mensual eliminado correctamente.");
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cliente mensual no encontrado.");
        }
    }//GEN-LAST:event_actionMensual2ActionPerformed

    private void actionModHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionModHistorialActionPerformed
        if (tablaHistorial.getSelectedRow() != -1) {
            CobroDiario cobro = (CobroDiario) tablaHistorial.getValueAt(tablaHistorial.getSelectedRow(), 0);
            Cupo cupo = cobro.getCupo();
            String valorIngreso = JOptionPane.showInputDialog("Ingrese el valor correspondiente: ", cupo.getCobroSugerido());
            if (valorIngreso != null) {
                cobro.setCobro(Long.parseLong(valorIngreso));
                try {
                    Conection.getCobroDiaro().edit(cobro);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(null, "Cobro modificado correctamente.");
            }
            inicializarTablaHistorial(fechaSelector.getDate());
        }
    }//GEN-LAST:event_actionModHistorialActionPerformed

    private void mensualidadMensualFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mensualidadMensualFocusGained
        mensualidadMensual.selectAll();
    }//GEN-LAST:event_mensualidadMensualFocusGained

    private void panelDiarioComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelDiarioComponentShown
        placaDiario.requestFocus();
    }//GEN-LAST:event_panelDiarioComponentShown

    private void cascosDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cascosDiarioActionPerformed
        if (cascosDiario.getText().compareTo("-") == 0) {
            colaEntrada.add(placaDiario.getText().toUpperCase().trim());
            cascosDiario.setText("0");
            placaDiario.setText("");
            placaDiario.requestFocus();
        } else {
            actionDiario.requestFocus();
        }
    }//GEN-LAST:event_cascosDiarioActionPerformed

    private void cascosDiarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cascosDiarioMouseClicked
        cascosDiario.selectAll();
    }//GEN-LAST:event_cascosDiarioMouseClicked

    private void actionDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionDiarioActionPerformed
        if (estadoUsuario(placaDiario.getText().trim())) {
            String ingreso = placaDiario.getText().trim();
            int cascos = Integer.parseInt(cascosDiario.getText().trim());
            actionDiarioProceso(ingreso, cascos, 0);
            observacinoesDiario.setText("");
            inicializarTablaDiario();
        }
    }//GEN-LAST:event_actionDiarioActionPerformed

    private void placaDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placaDiarioActionPerformed
        switch (Auxi.selector(placaDiario.getText().trim())) {
            case 1:
                cascosDiario.requestFocus();
                cascosDiario.selectAll();
                Usuario usuario = Conection.getUsuario().findUsuario(placaDiario.getText().trim());
                if (usuario != null) {
                    if (usuario.getBaneado() != null) {
                        observacinoesDiario.setText(usuario.getBaneado().getRazon());
                        JOptionPane.showMessageDialog(null, "El usuario esta baneado del sistema, revisar razon.");
                        placaDiario.requestFocus();
                        placaDiario.setText("");
                    } else {
                        observacinoesDiario.setText(usuario.getObservacion());
                    }
                }
                break;
            case 0:
                JOptionPane.showMessageDialog(null, "Entrada erronea.");
                placaDiario.requestFocus();
                placaDiario.selectAll();
                break;
            default:
                actionDiario.requestFocus();
                break;
        }
    }//GEN-LAST:event_placaDiarioActionPerformed

    private void placaDiarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_placaDiarioMouseClicked
        placaDiario.selectAll();
    }//GEN-LAST:event_placaDiarioMouseClicked

    private void actionCobroMensual1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionCobroMensual1ActionPerformed
        if (tablaDiario.getSelectedRow() != -1) {
            Cupo cupo = (Cupo) tablaDiario.getValueAt(tablaDiario.getSelectedRow(), 0);
            PrintNow.imprimirReciboEntrada(cupo);
            tablaDiario.clearSelection();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un elemento.");
        }
    }//GEN-LAST:event_actionCobroMensual1ActionPerformed

    private void actionAnularDiarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionAnularDiarioActionPerformed
        if (tablaDiario.getSelectedRow() != -1) {
            Cupo cupo = (Cupo) tablaDiario.getValueAt(tablaDiario.getSelectedRow(), 0);
            cuposActivos.remove(cupo.getCupoPK().getConsecutivo());
            if (cupo.getLocker() != null) {
                Locker locker = cupo.getLocker();
                locker.setAlojamiento(0);
                try {
                    Conection.getLocker().edit(locker);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                Conection.getCupo().destroy(cupo.getCupoPK());
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            inicializarTablaDiario();
            tablaDiario.clearSelection();
            JOptionPane.showMessageDialog(null, "Elemento anulado existosamente.");
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento primero.");
        }
    }//GEN-LAST:event_actionAnularDiarioActionPerformed

    private void actionCobroMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionCobroMensualActionPerformed
        UsuarioMensual usuario = Conection.getUsuarioMensual().findUsuarioMensual(placaCobroMensual.getText());
        if (usuario != null) {
            new MensualidadDialogo(this, true, usuario);
        } else {
            JOptionPane.showMessageDialog(null, "No existe ese cliente mensual.");
        }
        placaCobroMensual.setText("");
        placaCobroMensual.requestFocus();
        inicializarTablaMorosos();
    }//GEN-LAST:event_actionCobroMensualActionPerformed

    private void placaCobroMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placaCobroMensualActionPerformed
        actionCobroMensualActionPerformed(evt);
    }//GEN-LAST:event_placaCobroMensualActionPerformed

    private void placaCobroMensualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_placaCobroMensualMouseClicked
        placaCobroMensual.selectAll();
    }//GEN-LAST:event_placaCobroMensualMouseClicked

    private void ScrollDiarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ScrollDiarioMouseClicked

    }//GEN-LAST:event_ScrollDiarioMouseClicked

    private void tablaDiarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDiarioMouseClicked
        if (tablaDiario.getSelectedRow() != -1) {
            Cupo cupo = (Cupo) tablaDiario.getValueAt(tablaDiario.getSelectedRow(), 0);
            observacinoesDiario.setText(cupo.getPlaca().getUsuario().getObservacion());
            String[] aux = Auxi.calcularTiempoMotoTentativo(cupo);
            selplaca.setText(cupo.getPlaca().getPlaca());
            seltiempo.setText(aux[0]);
            selcobro.setText(aux[1]);
        }
    }//GEN-LAST:event_tablaDiarioMouseClicked

    private void listaEsperaValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listaEsperaValueChanged
        colaEntrada.remove(listaEspera.getSelectedValue());
    }//GEN-LAST:event_listaEsperaValueChanged

    private void actionCobroMensual2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionCobroMensual2ActionPerformed
        if (tablaDiario.getSelectedRow() != -1) {
            Cupo cupo = (Cupo) tablaDiario.getValueAt(tablaDiario.getSelectedRow(), 0);
            Usuario user = cupo.getPlaca().getUsuario();
            user.setObservacion(observacinoesDiario.getText());
            try {
                Conection.getUsuario().edit(user);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            observacinoesDiario.setText("");
            tablaDiario.clearSelection();
        }
    }//GEN-LAST:event_actionCobroMensual2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Contenedor;
    private javax.swing.JPanel PanelPlacaMensual;
    private javax.swing.JScrollPane ScrollDiario;
    private javax.swing.JScrollPane ScrollMensual;
    private javax.swing.JButton actionAnularDiario;
    private javax.swing.JButton actionBanClientes;
    private javax.swing.JButton actionClientes;
    private javax.swing.JButton actionCobroMensual;
    private javax.swing.JButton actionCobroMensual1;
    private javax.swing.JButton actionCobroMensual2;
    private javax.swing.JButton actionDiario;
    private javax.swing.JButton actionGuardarClientes;
    private javax.swing.JButton actionHistorial;
    private javax.swing.JButton actionHistorial1;
    private javax.swing.JButton actionImport;
    private javax.swing.JButton actionLocker;
    private javax.swing.JButton actionMensual;
    private javax.swing.JButton actionMensual1;
    private javax.swing.JButton actionMensual2;
    private javax.swing.JButton actionModHistorial;
    private javax.swing.JButton actionUnbanClientes;
    private javax.swing.JTextField aloLocker;
    private javax.swing.JLabel banDesdeClientes;
    private javax.swing.JTextField capLocker;
    private javax.swing.JTextField cascosDiario;
    private javax.swing.JTextField cobradoClientes;
    private org.jdesktop.swingx.JXDatePicker cobroMensual;
    private javax.swing.JPanel configPanel;
    private javax.swing.JLabel descConfig;
    private javax.swing.JTextField documentoMensual;
    private javax.swing.JTextField entradasClientes;
    private org.jdesktop.swingx.JXDatePicker fechaSelector;
    private javax.swing.JLabel finconsHistorial;
    private javax.swing.JPanel historialPanel;
    private javax.swing.JTextField idLocker;
    private org.jdesktop.swingx.JXDatePicker ingresoMensual;
    private javax.swing.JLabel iniconsHistorial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JList<String> listaEspera;
    private javax.swing.JTextField mensualidadMensual;
    private javax.swing.JTextField minutosClientes;
    private javax.swing.JTextField nombreMensual;
    private javax.swing.JTextArea observacinoesDiario;
    private javax.swing.JTextArea observacionesClientes;
    private javax.swing.JTabbedPane panelAdmin;
    private javax.swing.JPanel panelDiario;
    private javax.swing.JPanel panelLockers;
    private javax.swing.JPanel panelMensual;
    private javax.swing.JTextField placaClientes;
    private javax.swing.JTextField placaCobroMensual;
    private javax.swing.JTextField placaDiario;
    private javax.swing.JTextField placaMensual;
    private javax.swing.JTextArea razonClientes;
    private javax.swing.JLabel selcobro;
    private org.jdesktop.swingx.JXDatePicker selectorMensual;
    private javax.swing.JLabel selplaca;
    private javax.swing.JLabel seltiempo;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaCobroMensual;
    private javax.swing.JTable tablaConfig;
    private javax.swing.JTable tablaDiario;
    private javax.swing.JTable tablaHistorial;
    private javax.swing.JTable tablaLockers;
    private javax.swing.JTable tablaMensual;
    private javax.swing.JTable tablaMorosos;
    private javax.swing.JTextField telefonoMensual;
    private javax.swing.JLabel totalHistorial;
    private javax.swing.JPanel userPanel;
    private javax.swing.JTextField valorConfig;
    // End of variables declaration//GEN-END:variables

    private void inicializarTablaLockers() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("select l from Locker l ORDER BY l.identificador");
        List<Locker> lockerList = query.getResultList();
        Collections.sort(lockerList, new CustomComparator());
        String[] columnas = {"Identificador", "Alojamiento", "Capacidad"};
        Object[][] campos = new Object[lockerList.size()][columnas.length];
        int i = 0;
        for (Locker locker : lockerList) {
            campos[i][0] = locker;
            campos[i][1] = locker.getAlojamiento();
            campos[i][2] = locker.getCapacidad();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaLockers.setModel(modelo);
        tablaLockers.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void inicializarTablaConfig() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("select c from Configuraciones c");
        List<Configuraciones> confList = query.getResultList();
        String[] columnas = {"Descripcion", "Valor"};
        Object[][] campos = new Object[confList.size()][columnas.length];
        int i = 0;
        for (Configuraciones conf : confList) {
            campos[i][0] = conf;
            campos[i][1] = conf.getValor();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaConfig.setModel(modelo);
        tablaConfig.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    public String ingresoDiario(String placa, int cascos) {
        UsuarioDiario usuario = Conection.getUsuarioDiario().findUsuarioDiario(placa);
        if (usuario == null) {
            Usuario usuarioGeneral = Conection.getUsuario().findUsuario(placa);
            if (usuarioGeneral == null) {
                usuarioGeneral = new Usuario(placa, "Moto", "");
                try {
                    Conection.getUsuario().create(usuarioGeneral);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            usuario = new UsuarioDiario(placa, 0, 0, 0);
            usuario.setUsuario(usuarioGeneral);
            try {
                Conection.getUsuarioDiario().create(usuario);
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        long consecutivo = Long.parseLong(Conection.getConfiguraciones().findConfiguraciones("consecutivoDiario").getValor());
        Cupo cupo = new Cupo(new CupoPK(consecutivo, new GregorianCalendar().getTime()), 0, 0, 0);
        cupo.setPlaca(usuario);
        if (cascos > 0) {
            EntityManager em = Conection.getEMF().createEntityManager();
            Query query = em.createQuery("select l from Locker l where l.alojamiento = 0 and l.capacidad >= :capacidad ORDER BY l.identificador");
            query.setParameter("capacidad", cascos);
            List<Locker> lockers = query.getResultList();
            Collections.sort(lockers, new CustomComparator());
            if (lockers.size() > 0) {
                Locker locker = lockers.get(0);
                locker.setAlojamiento(cascos);
                try {
                    Conection.getLocker().edit(locker);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                cupo.setLocker(locker);
            } else {
                JOptionPane.showMessageDialog(null, "No existen lockers disponibles.");
            }
        }
        try {
            Conection.getCupo().create(cupo);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        consecutivo++;
        Configuraciones cons = Conection.getConfiguraciones().findConfiguraciones("consecutivoDiario");
        cons.setValor(String.valueOf(consecutivo));
        try {
            Conection.getConfiguraciones().edit(cons);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        colaEntrada.remove(placa);
        PrintNow.imprimirReciboEntrada(cupo);
        inicializarTablaDiario();
        if (cupo.getLocker() != null) {
            return cupo.getLocker().getIdentificador();
        } else {
            return "Sin Cascos";
        }
    }

    private void retiroDiario(Cupo cupo) {
        cupo.setSalida(new GregorianCalendar().getTime());
        Auxi.calcularTiempoMoto(cupo);
        cupoActual = cupo;
    }

    private void inicializarTablaHistorial(Date date) {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT c FROM CobroDiario c WHERE c.fecha = :fecha ORDER BY c.consecutivo DESC");
        query.setParameter("fecha", date);
        List<CobroDiario> cobros = query.getResultList();
        List<Long> cons = new ArrayList<>();
        long total = 0;
        int i = 0;
        String[] columnas = {"Consecutivo", "Placa", "Entrada", "Salida", "Cobrado", "Locker"};
        Object[][] campos = new Object[cobros.size()][columnas.length];
        for (CobroDiario cobro : cobros) {
            campos[i][0] = cobro;
            campos[i][1] = cobro.getCupo().getPlaca().getPlaca();
            campos[i][2] = Auxi.formaterHora(cobro.getCupo().getCupoPK().getIngreso());
            campos[i][3] = Auxi.formaterHora(cobro.getCupo().getSalida());
            campos[i][4] = cobro.getCobro();
            if (cobro.getCupo().getLocker() != null) {
                campos[i][5] = cobro.getCupo().getLocker();
            } else {
                campos[i][5] = "Ninguno";
            }
            cons.add(cobro.getConsecutivo());
            total += cobro.getCobro();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaHistorial.setModel(modelo);
        if (cons.size() > 0) {
            finconsHistorial.setText(String.valueOf(cons.get(0)));
            iniconsHistorial.setText(String.valueOf(cons.get(cons.size() - 1)));
        } else {
            iniconsHistorial.setText("N/A");
            finconsHistorial.setText("N/A");
        }
        totalHistorial.setText(String.valueOf(total));
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void inicializarTablaMensual() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT u FROM UsuarioMensual u");
        List<UsuarioMensual> userList = query.getResultList();
        String[] columnas = {"Placa", "Cedula", "Nombre", "Telefono", "Ingreso", "Cobro", "Mensualidad"};
        Object[][] campos = new Object[userList.size()][columnas.length];
        int i = 0;
        for (UsuarioMensual user : userList) {
            campos[i][0] = user;
            campos[i][1] = user.getDocumento();
            campos[i][2] = user.getNombre();
            campos[i][3] = user.getTelefono();
            campos[i][4] = Auxi.formaterFecha(user.getFechaIngreso());
            campos[i][5] = Auxi.formaterFecha(user.getSigCobro());
            campos[i][6] = user.getMensualidad();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaMensual.setModel(modelo);
        tablaMensual.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void inicializarTablaMorosos() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT u FROM UsuarioMensual u WHERE u.sigCobro <= CURRENT_DATE");
        List<UsuarioMensual> userList = query.getResultList();
        String[] columnas = {"Placa", "Cedula", "Nombre", "Telefono", "Cobro", "Mensualidad"};
        Object[][] campos = new Object[userList.size()][columnas.length];
        int i = 0;
        for (UsuarioMensual user : userList) {
            campos[i][0] = user;
            campos[i][1] = user.getDocumento();
            campos[i][2] = user.getNombre();
            campos[i][3] = user.getTelefono();
            campos[i][4] = Auxi.formaterFecha(user.getSigCobro());
            campos[i][5] = user.getMensualidad();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaMorosos.setModel(modelo);
        tablaMorosos.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void inicializarTablaCobroMensual(Date date) {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT c FROM CobroMensual c WHERE c.cobroMensualPK.fecha = :fecha");
        query.setParameter("fecha", date);
        List<CobroMensual> cobroList = query.getResultList();
        String[] columnas = {"Placa", "Fecha", "Nombre", "Desde", "Hasta", "Cobro"};
        Object[][] campos = new Object[cobroList.size()][columnas.length];
        int i = 0;
        for (CobroMensual cobro : cobroList) {
            campos[i][0] = cobro;
            campos[i][1] = Auxi.formaterFecha(cobro.getCobroMensualPK().getFecha());
            campos[i][2] = cobro.getUsuarioMensual().getNombre();
            campos[i][3] = Auxi.formaterFecha(cobro.getDesde());
            campos[i][4] = Auxi.formaterFecha(cobro.getHasta());
            campos[i][5] = cobro.getCobro();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaCobroMensual.setModel(modelo);
        tablaCobroMensual.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void inicializarTablaClientes(UsuarioDiario user) {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT c FROM Cupo c WHERE c.placa = :placa AND c.salida IS NOT NULL ORDER BY c.salida DESC");
        query.setParameter("placa", user);
        List<Cupo> cupoList = query.getResultList();
        String[] columnas = {"Fecha", "Ingreso", "Salido", "Horas", "Minutos", "Locker", "Sugerido", "Cobro"};
        Object[][] campos = new Object[cupoList.size()][columnas.length];
        int i = 0;
        for (Cupo cupo : cupoList) {
            CobroDiario cobro = cupo.getCobroDiarioList().get(0);
            campos[i][0] = Auxi.formaterFecha(cobro.getFecha());
            campos[i][1] = Auxi.formaterHora(cupo.getCupoPK().getIngreso());
            campos[i][2] = Auxi.formaterHora(cupo.getSalida());
            campos[i][3] = cupo.getHoras();
            campos[i][4] = cupo.getMinutos();
            if (cupo.getLocker() != null) {
                campos[i][5] = cupo.getLocker().getIdentificador();
            } else {
                campos[i][5] = "Ninguno";
            }
            campos[i][6] = cupo.getCobroSugerido();
            campos[i][7] = cobro.getCobro();
            i++;
        }
        DefaultTableModel modelo = new DefaultTableModel(campos, columnas);
        tablaClientes.setModel(modelo);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 24));
    }

    private void cargarListaMensuales() {
        EntityManager em = Conection.getEMF().createEntityManager();
        Query query = em.createQuery("SELECT m FROM UsuarioMensual m");
        List<UsuarioMensual> mensualesList = query.getResultList();
        for (UsuarioMensual cliente : mensualesList) {
            mensuales.add(cliente.getPlaca());
        }
    }

    private void limpiarUsuariosMensuales() {
        placaMensual.setText("");
        documentoMensual.setText("");
        nombreMensual.setText("");
        telefonoMensual.setText("");
        mensualidadMensual.setText("0");
        ingresoMensual.setDate(new Date());
        cobroMensual.setDate(new Date());
        inicializarTablaMensual();
        inicializarTablaMorosos();
    }

    private void inicializarBase() {
        try {
            Configuraciones fecha = new Configuraciones("fechaActual", "03-05-1992");
            Configuraciones consecutivoDiario = new Configuraciones("consecutivoDiario", "1");
            Configuraciones horaCierre = new Configuraciones("horaCierre", "8:00 pm");
            Configuraciones mhMoto = new Configuraciones("mediaHoraMoto", "700");
            Configuraciones uhMoto = new Configuraciones("unaHoraMoto", "1000");
            Configuraciones phMoto = new Configuraciones("porHoraMoto", "800");
            Configuraciones mhCarro = new Configuraciones("mediaHoraCarro", "1000");
            Configuraciones uhCarro = new Configuraciones("unaHoraCarro", "2000");
            Configuraciones phCarro = new Configuraciones("porHoraCarro", "800");
            Configuraciones contacto = new Configuraciones("contacto", "numero//contacto");
            Configuraciones consecutivo = new Configuraciones("consecutivo", "1");
            Configuraciones adminPass = new Configuraciones("adminPass", "0000");
            Conection.getConfiguraciones().create(fecha);
            Conection.getConfiguraciones().create(consecutivoDiario);
            Conection.getConfiguraciones().create(horaCierre);
            Conection.getConfiguraciones().create(mhMoto);
            Conection.getConfiguraciones().create(uhMoto);
            Conection.getConfiguraciones().create(phMoto);
            Conection.getConfiguraciones().create(mhCarro);
            Conection.getConfiguraciones().create(uhCarro);
            Conection.getConfiguraciones().create(phCarro);
            Conection.getConfiguraciones().create(contacto);
            Conection.getConfiguraciones().create(consecutivo);
            Conection.getConfiguraciones().create(adminPass);
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
