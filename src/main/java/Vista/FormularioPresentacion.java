package Vista;

import Model.Persona;
import Negocio.PersonaServicio;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FormularioPresentacion extends javax.swing.JFrame {

    private final PersonaServicio servicio;
    private int idSeleccionado = -1;

    public FormularioPresentacion() {
        initComponents();
        servicio = new PersonaServicio();
        cargarDatos();
        controlarBotones(true);

        tbl_Persona.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tbl_Persona.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosDesdeFila(fila);
                    controlarBotones(false);
                }
            }
        });

        btn_Buscar.addActionListener(e -> buscarPersona());
    }

    private void controlarBotones(boolean enModoAgregar) {
        btn_Guardar.setEnabled(enModoAgregar);
        btn_Buscar.setEnabled(!enModoAgregar);
    }

    public void cargarDatos() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Correo");
        model.addColumn("Cédula");
        model.addColumn("Fecha de nacimiento");
        model.addColumn("Edad");

        List<Persona> personas = servicio.listarPersonas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Persona p : personas) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getApellido(),
                p.getCorreo(),
                p.getCedula(),
                p.getFecha_de_nacimiento().format(formatter),
                p.getEdad()
            };
            model.addRow(fila);
        }

        tbl_Persona.setModel(model);
        limpiarCampos();
    }

    private void buscarPersona() {
        String criterio = JOptionPane.showInputDialog(this, "Ingrese cédula a buscar:");

        if (criterio != null && !criterio.trim().isEmpty()) {
            Persona personaEncontrada = servicio.buscarPorCedula(criterio);

            if (personaEncontrada != null) {

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Nombre");
                model.addColumn("Apellido");
                model.addColumn("Correo");
                model.addColumn("Cédula");
                model.addColumn("Fecha de nacimiento");
                model.addColumn("Edad");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Object[] fila = {
                    personaEncontrada.getId(),
                    personaEncontrada.getNombre(),
                    personaEncontrada.getApellido(),
                    personaEncontrada.getCorreo(),
                    personaEncontrada.getCedula(),
                    personaEncontrada.getFecha_de_nacimiento().format(formatter),
                    personaEncontrada.getEdad()
                };
                model.addRow(fila);

                tbl_Persona.setModel(model);

                idSeleccionado = personaEncontrada.getId();
                txt_Nombre.setText(personaEncontrada.getNombre());
                txt_Apellido.setText(personaEncontrada.getApellido());
                txt_Correo.setText(personaEncontrada.getCorreo());
                txt_Cedula.setText(personaEncontrada.getCedula());

                Date fechaConvertida = Date.from(
                        personaEncontrada.getFecha_de_nacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant()
                );
                jXD_fecha.setDate(fechaConvertida);

                controlarBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna persona con la cédula: " + criterio);
            }
        }
    }

    public void agregar() {
        if (ValidarFormulario()) {
            String nombre = txt_Nombre.getText().trim();
            String apellido = txt_Apellido.getText().trim();
            String correo = txt_Correo.getText().trim();
            String cedula = txt_Cedula.getText().trim();

            Date fechaSeleccionada = jXD_fecha.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha válida.",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaNacimiento = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int edad = PersonaServicio.calcularEdad(fechaNacimiento);

            Persona persona = new Persona(nombre, apellido, correo, cedula, fechaNacimiento, edad);
            servicio.agregarNuevaPersona(persona);

            cargarDatos();
            controlarBotones(false);
        }
    }

    private void actualizarpersona() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una persona de la tabla para actualizar.");
                return;
            }

            if (!ValidarFormulario()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.");
                return;
            }

            String nombre = txt_Nombre.getText().trim();
            String apellido = txt_Apellido.getText().trim();
            String correo = txt_Correo.getText().trim();
            String cedula = txt_Cedula.getText().trim();
            Date fechaSeleccionada = jXD_fecha.getDate();

            LocalDate fechaNacimiento = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            int edad = PersonaServicio.calcularEdad(fechaNacimiento);

            Persona persona = new Persona();
            persona.setId(idSeleccionado);
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setCorreo(correo);
            persona.setCedula(cedula);
            persona.setFecha_de_nacimiento(fechaNacimiento);
            persona.setEdad(edad);

            boolean actualizado = servicio.actualizar(persona);

            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Persona actualizada correctamente.");
                cargarDatos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la persona.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_Nombre = new javax.swing.JTextField();
        txt_Apellido = new javax.swing.JTextField();
        txt_Correo = new javax.swing.JTextField();
        txt_Cedula = new javax.swing.JTextField();
        btn_Guardar = new javax.swing.JButton();
        btn_Buscar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Persona = new javax.swing.JTable();
        btm_actualizar = new javax.swing.JButton();
        jXD_fecha = new org.jdesktop.swingx.JXDatePicker();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Persona");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Apellido:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Correo:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Cedula:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Fecha de nacimiento:");

        txt_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_NombreKeyTyped(evt);
            }
        });

        txt_Apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ApellidoKeyTyped(evt);
            }
        });

        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

        btn_Guardar.setBackground(new java.awt.Color(51, 102, 255));
        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drive_diskette_computer_data_disk_floppy_icon_250689.png"))); // NOI18N
        btn_Guardar.setText("Guardar");
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Buscar.setBackground(new java.awt.Color(51, 102, 255));
        btn_Buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/find_search_icon_218876.png"))); // NOI18N
        btn_Buscar.setText("Buscar");

        btn_Eliminar.setBackground(new java.awt.Color(51, 102, 255));
        btn_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/-delete-forever_90072.png"))); // NOI18N
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        tbl_Persona.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellido", "Correo", "Cudula", "Edad"
            }
        ));
        jScrollPane1.setViewportView(tbl_Persona);

        btm_actualizar.setText("Actualizar");
        btm_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btm_actualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(21, 21, 21))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(btn_Guardar)
                .addGap(76, 76, 76)
                .addComponent(btn_Buscar)
                .addGap(260, 260, 260)
                .addComponent(btm_actualizar)
                .addGap(32, 32, 32))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(188, 188, 188)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_Eliminar)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txt_Nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                        .addComponent(txt_Apellido, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                        .addComponent(txt_Correo, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                        .addComponent(txt_Cedula, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                                    .addComponent(jXD_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jXD_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btm_actualizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        agregar();

    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        int fila = tbl_Persona.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(tbl_Persona.getValueAt(fila, 0).toString());
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este registro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            servicio.eliminarPersona(id);
            cargarDatos();
            limpiarCampos();
        }
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btm_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btm_actualizarActionPerformed

        actualizarpersona();
    }//GEN-LAST:event_btm_actualizarActionPerformed

    private void txt_CedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CedulaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CedulaKeyTyped

    private void txt_NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NombreKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            evt.consume(); 
        }

    }//GEN-LAST:event_txt_NombreKeyTyped

    private void txt_ApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ApellidoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_ApellidoKeyTyped

    private boolean ValidarFormulario() {

        Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
        Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

        boolean nombreValido = !txt_Nombre.getText().trim().isEmpty();
        boolean apellidoValido = !txt_Apellido.getText().trim().isEmpty();
        boolean correoValido = !txt_Correo.getText().trim().isEmpty();
        boolean cedulaValida = !txt_Cedula.getText().trim().isEmpty();
        boolean fechaValida = jXD_fecha.getDate() != null;

        txt_Nombre.setBorder(nombreValido ? bordeNegro : bordeRojo);
        txt_Apellido.setBorder(apellidoValido ? bordeNegro : bordeRojo);
        txt_Correo.setBorder(correoValido ? bordeNegro : bordeRojo);
        txt_Cedula.setBorder(cedulaValida ? bordeNegro : bordeRojo);
        jXD_fecha.setBorder(fechaValida ? bordeNegro : bordeRojo);

        return !(txt_Nombre.getText().isEmpty() || txt_Apellido.getText().isEmpty() || txt_Correo.getText().isEmpty()
                || txt_Cedula.getText().isEmpty() || jXD_fecha.getDate() == null);
    }

    private void limpiarCampos() {
        txt_Nombre.setText("");
        txt_Apellido.setText("");
        txt_Correo.setText("");
        txt_Cedula.setText("");
        jXD_fecha.setDate(null);
        idSeleccionado = -1;
        controlarBotones(true);
    }

    private void cargarDatosDesdeFila(int fila) {
        idSeleccionado = Integer.parseInt(tbl_Persona.getValueAt(fila, 0).toString());
        txt_Nombre.setText(tbl_Persona.getValueAt(fila, 1).toString());
        txt_Apellido.setText(tbl_Persona.getValueAt(fila, 2).toString());
        txt_Correo.setText(tbl_Persona.getValueAt(fila, 3).toString());
        txt_Cedula.setText(tbl_Persona.getValueAt(fila, 4).toString());

        String fechaTexto = tbl_Persona.getValueAt(fila, 5).toString();
        LocalDate fecha = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date fechaConvertida = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());

        jXD_fecha.setDate(fechaConvertida);
        controlarBotones(false);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormularioPresentacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormularioPresentacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormularioPresentacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioPresentacion.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioPresentacion().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btm_actualizar;
    private javax.swing.JButton btn_Buscar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXD_fecha;
    private javax.swing.JTable tbl_Persona;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Nombre;
    // End of variables declaration//GEN-END:variables

    private int getPersonaSeleccionadaId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
