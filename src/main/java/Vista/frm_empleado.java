/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Model.Cliente;
import Model.Empleado;
import Model.Fidelidad;
import Model.Persona;
import Negocio.EmpleadoServicio;
import Negocio.PersonaServicio;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ronny
 */
public class frm_empleado extends javax.swing.JInternalFrame {
    public final EmpleadoServicio servicio;
    public DefaultTableModel tabla;
    private int idSeleccionado = -1;

    public frm_empleado() {
        initComponents();
        servicio = new EmpleadoServicio();
        cargarDatosEmpleado();
//        controlarBotonesEmpleado(true);

        tbl_empleado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tbl_empleado.getSelectedRow();
                if (fila >= 0) {
                    cargarDatosDesdeFila(fila);
//                    controlarBotonesEmpleado(false);
                }
            }
        });

        btn_buscarem.addActionListener(e -> buscarEmpleado());
        btn_guardarem.addActionListener(e -> agregarEmpleado());
        btn_actualizarem.addActionListener(e -> actualizarEmpleado());
        btn_eliminarem.addActionListener(e -> eliminarEmpleado());
    }

//    private void controlarBotonesEmpleado(boolean enModoAgregar) {
//        btn_guardarem.setEnabled(enModoAgregar);
//        btn_actualizarem.setEnabled(!enModoAgregar);
//        btn_eliminarem.setEnabled(!enModoAgregar);
//        btn_buscarem.setEnabled(!enModoAgregar);
//    }

    public void cargarDatosEmpleado() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Apellido");
        model.addColumn("Correo");
        model.addColumn("Cédula");
        model.addColumn("Fecha Nac.");
        model.addColumn("Edad");
        model.addColumn("Dirección");
        model.addColumn("Salario");
        model.addColumn("Turno");

        List<Empleado> empleados = servicio.listarEmpleados();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Empleado emp : empleados) {
            Object[] fila = {
                emp.getId(),
                emp.getNombre(),
                emp.getApellido(),
                emp.getCorreo(),
                emp.getCedula(),
                emp.getFecha_de_nacimiento().format(formatter),
                emp.getEdad(),
                emp.getDireccion(),
                emp.getSalario(),
                emp.getTurno()
            };
            model.addRow(fila);
        }

        tbl_empleado.setModel(model);
        limpiarCampos();
    }

    private boolean ValidarFormulario() {
        Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
        Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

        boolean nombreValido = !txt_nombreem.getText().trim().isEmpty();
        boolean apellidoValido = !txt_apellidoem.getText().trim().isEmpty();
        boolean correoValido = !txt_correoem.getText().trim().isEmpty();
        boolean cedulaValida = !txt_cedulaem.getText().trim().isEmpty();
        boolean fechaValida = jdx_fechanacem.getDate() != null;
        boolean direccionValida = !txt_direcem.getText().trim().isEmpty();
        boolean salarioValido = !txt_salarioem.getText().trim().isEmpty();
        boolean turnoValido = cbx_turnoem.getSelectedItem() != null;

        txt_nombreem.setBorder(nombreValido ? bordeNegro : bordeRojo);
        txt_apellidoem.setBorder(apellidoValido ? bordeNegro : bordeRojo);
        txt_correoem.setBorder(correoValido ? bordeNegro : bordeRojo);
        txt_cedulaem.setBorder(cedulaValida ? bordeNegro : bordeRojo);
        jdx_fechanacem.setBorder(fechaValida ? bordeNegro : bordeRojo);
        txt_direcem.setBorder(direccionValida ? bordeNegro : bordeRojo);
        txt_salarioem.setBorder(salarioValido ? bordeNegro : bordeRojo);
        cbx_turnoem.setBorder(turnoValido ? bordeNegro : bordeRojo);

        return nombreValido && apellidoValido && correoValido && cedulaValida && 
               fechaValida && direccionValida && salarioValido && turnoValido;
    }

    private void limpiarCampos() {
        txt_nombreem.setText("");
        txt_apellidoem.setText("");
        txt_correoem.setText("");
        txt_cedulaem.setText("");
        txt_direcem.setText("");
        txt_salarioem.setText("");
        jdx_fechanacem.setDate(null);
        cbx_turnoem.setSelectedIndex(-1);
        idSeleccionado = -1;
//        controlarBotonesEmpleado(true);
    }

    private void cargarDatosDesdeFila(int fila) {
        idSeleccionado = Integer.parseInt(tbl_empleado.getValueAt(fila, 0).toString());
        txt_nombreem.setText(tbl_empleado.getValueAt(fila, 1).toString());
        txt_apellidoem.setText(tbl_empleado.getValueAt(fila, 2).toString());
        txt_correoem.setText(tbl_empleado.getValueAt(fila, 3).toString());
        txt_cedulaem.setText(tbl_empleado.getValueAt(fila, 4).toString());
        txt_direcem.setText(tbl_empleado.getValueAt(fila, 7).toString());
        txt_salarioem.setText(tbl_empleado.getValueAt(fila, 8).toString());
        cbx_turnoem.setSelectedItem(tbl_empleado.getValueAt(fila, 9).toString());

        String fechaTexto = tbl_empleado.getValueAt(fila, 5).toString();
        LocalDate fecha = LocalDate.parse(fechaTexto, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date fechaConvertida = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        jdx_fechanacem.setDate(fechaConvertida);

//        controlarBotonesEmpleado(false);
    }

    private void buscarEmpleado() {
        String criterio = JOptionPane.showInputDialog(this, "Ingrese cédula a buscar:");

        if (criterio != null && !criterio.trim().isEmpty()) {
            Empleado empleadoEncontrado = servicio.buscarPorCedula(criterio);

            if (empleadoEncontrado != null) {
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Nombre");
                model.addColumn("Apellido");
                model.addColumn("Correo");
                model.addColumn("Cédula");
                model.addColumn("Fecha Nac.");
                model.addColumn("Edad");
                model.addColumn("Dirección");
                model.addColumn("Salario");
                model.addColumn("Turno");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                Object[] fila = {
                    empleadoEncontrado.getId(),
                    empleadoEncontrado.getNombre(),
                    empleadoEncontrado.getApellido(),
                    empleadoEncontrado.getCorreo(),
                    empleadoEncontrado.getCedula(),
                    empleadoEncontrado.getFecha_de_nacimiento().format(formatter),
                    empleadoEncontrado.getEdad(),
                    empleadoEncontrado.getDireccion(),
                    empleadoEncontrado.getSalario(),
                    empleadoEncontrado.getTurno()
                };
                model.addRow(fila);

                tbl_empleado.setModel(model);
                cargarDatosDesdeFila(0);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún empleado con la cédula: " + criterio);
            }
        }
    }

private void agregarEmpleado() {
    if (ValidarFormulario()) {
        try {
           
            String nombre = txt_nombreem.getText().trim();
            String apellido = txt_apellidoem.getText().trim();
            String correo = txt_correoem.getText().trim();
            String cedula = txt_cedulaem.getText().trim();
            String direccion = txt_direcem.getText().trim();
            double salario = Double.parseDouble(txt_salarioem.getText().trim());
            String turno = cbx_turnoem.getSelectedItem().toString();

        
            LocalDate fechaNacimiento = jdx_fechanacem.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            
  
            int edad = EmpleadoServicio.calcularEdad(fechaNacimiento);
            
       
            LocalDate fechaContratacion = LocalDate.now();

            Empleado nuevoEmpleado = new Empleado(
                nombre, 
                apellido, 
                correo, 
                cedula,
                fechaNacimiento, 
                edad,
                direccion, 
                salario, 
                turno, 
                fechaContratacion,
                new ArrayList<>(), // Lista vacía de roles
                null              // Usuario null inicialmente
            );

          
            int resultado = servicio.agregarNuevoEmpleado(nuevoEmpleado);

            if (resultado == 1) {
                JOptionPane.showMessageDialog(this, "Empleado agregado correctamente");
                cargarDatosEmpleado();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el empleado");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

private void actualizarEmpleado() {
    if (idSeleccionado == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla primero");
        return;
    }

    if (ValidarFormulario()) {
        try {
            
            String nombre = txt_nombreem.getText().trim();
            String apellido = txt_apellidoem.getText().trim();
            String correo = txt_correoem.getText().trim();
            String cedula = txt_cedulaem.getText().trim();
            String direccion = txt_direcem.getText().trim();
            double salario = Double.parseDouble(txt_salarioem.getText().trim());
            String turno = cbx_turnoem.getSelectedItem().toString();

        
            LocalDate fechaNacimiento = jdx_fechanacem.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            
           
            int edad = EmpleadoServicio.calcularEdad(fechaNacimiento);
            
 
            Empleado empleadoExistente = servicio.buscarPorCedula(cedula);
            
       
            Empleado empleadoActualizado = new Empleado(
                nombre, 
                apellido, 
                correo, 
                cedula,
                fechaNacimiento, 
                edad,
                direccion, 
                salario, 
                turno, 
                empleadoExistente != null ? empleadoExistente.getFechaContratacion() : LocalDate.now(),
                empleadoExistente != null ? empleadoExistente.getRoles() : new ArrayList<>(),
                empleadoExistente != null ? empleadoExistente.getUsuario() : null
            );
            
            empleadoActualizado.setId(idSeleccionado);

            
            boolean resultado = servicio.actualizarEmpleado(empleadoActualizado);

            if (resultado) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente");
                cargarDatosEmpleado();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el empleado");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

    private void eliminarEmpleado() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea eliminar este empleado?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = servicio.eliminarEmpleado(idSeleccionado);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente");
                cargarDatosEmpleado();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el empleado");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_correoem = new javax.swing.JTextField();
        txt_nombreem = new javax.swing.JTextField();
        txt_apellidoem = new javax.swing.JTextField();
        txt_cedulaem = new javax.swing.JTextField();
        txt_direcem = new javax.swing.JTextField();
        txt_salarioem = new javax.swing.JTextField();
        jdx_fechanacem = new org.jdesktop.swingx.JXDatePicker();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jComboBox1 = new javax.swing.JComboBox<>();
        cbx_turnoem = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_empleado = new javax.swing.JTable();
        btn_buscarem = new javax.swing.JButton();
        btn_eliminarem = new javax.swing.JButton();
        btn_actualizarem = new javax.swing.JButton();
        btn_guardarem = new javax.swing.JButton();

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI Semilight", 2, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/empleado-del-mes.png"))); // NOI18N
        jLabel1.setText("EMPLEADOS");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel4.setText("Correo:");

        jLabel5.setText("Cedula:");

        jLabel6.setText("Fecha de nacimiento:");

        jLabel7.setText("Direccion:");

        jLabel8.setText("Cargo:");

        jLabel9.setText("Salario:");

        jLabel10.setText("Fecha de contratacion:");

        txt_nombreem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nombreemActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cajero", "Administrador", "Gerente", " " }));

        cbx_turnoem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Diurno", "Nocturno", " " }));

        jLabel11.setText("Turno:");

        tbl_empleado.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_empleado);

        btn_buscarem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/zoom_2113465.png"))); // NOI18N
        btn_buscarem.setText("Buscar");
        btn_buscarem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscaremActionPerformed(evt);
            }
        });

        btn_eliminarem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trash-bin_5055247 (2).png"))); // NOI18N
        btn_eliminarem.setText("Eliminar ");

        btn_actualizarem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rotacion (1).png"))); // NOI18N
        btn_actualizarem.setText("Actualizar");

        btn_guardarem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/disco-flexible.png"))); // NOI18N
        btn_guardarem.setText("Guardar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btn_guardarem)
                        .addGap(41, 41, 41)
                        .addComponent(btn_buscarem)
                        .addGap(44, 44, 44)
                        .addComponent(btn_eliminarem)
                        .addGap(41, 41, 41)
                        .addComponent(btn_actualizarem))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel11))
                        .addGap(143, 143, 143)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbx_turnoem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdx_fechanacem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_cedulaem, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_correoem, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_apellidoem, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_nombreem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_salarioem, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_direcem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(158, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txt_nombreem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txt_apellidoem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txt_correoem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_cedulaem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jdx_fechanacem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_direcem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_salarioem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbx_turnoem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_buscarem)
                    .addComponent(btn_eliminarem)
                    .addComponent(btn_actualizarem)
                    .addComponent(btn_guardarem))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreemActionPerformed

    private void btn_buscaremActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscaremActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_buscaremActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizarem;
    private javax.swing.JButton btn_buscarem;
    private javax.swing.JButton btn_eliminarem;
    private javax.swing.JButton btn_guardarem;
    private javax.swing.JComboBox<String> cbx_turnoem;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private org.jdesktop.swingx.JXDatePicker jdx_fechanacem;
    private javax.swing.JTable tbl_empleado;
    private javax.swing.JTextField txt_apellidoem;
    private javax.swing.JTextField txt_cedulaem;
    private javax.swing.JTextField txt_correoem;
    private javax.swing.JTextField txt_direcem;
    private javax.swing.JTextField txt_nombreem;
    private javax.swing.JTextField txt_salarioem;
    // End of variables declaration//GEN-END:variables
}
