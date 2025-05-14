/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Model.DetalleFactura;
import Model.Factura;
import Model.Persona;
import Model.Producto;
import Negocio.FacturaServicio;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ronny
 */
public class frmFactura extends javax.swing.JInternalFrame {

       private List<DetalleFactura> detallesFactura = new ArrayList();

    private List<Factura> facturasCreadas = new ArrayList();

    private Persona personaEncontrada;

    private final FacturaServicio servicio;

    private Producto productoEncontrado;

    private DefaultTableModel modeloTablaDetalles;

    private double subtotal = 0;

    private double IVA = 0;

    private double total = 0;

    public frmFactura() {
        servicio = new FacturaServicio();
        initComponents();
        modeloTablaDetalles = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Precio", "Cantidad", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl_Factura.setModel(modeloTablaDetalles);
        tbl_Factura.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        controlarBotones(false);
        actualizarTotales();
    }

    private void registrarFactura() {
        if (this.personaEncontrada != null && this.detallesFactura.size() > 0) {
            Factura nuevaFactura = new Factura(this.personaEncontrada, this.detallesFactura);
            this.servicio.RegistrarNuevaFactura(nuevaFactura);
            facturasCreadas.add(nuevaFactura);
            agregarFacturaATabla(nuevaFactura);
        }
    }

    private double calcularIVA(double subtotal) {
        return subtotal * 0.15;
    }

    private void actualizarTotales() {

        subtotal = 0;
        for (DetalleFactura detalle : detallesFactura) {
            subtotal += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        IVA = calcularIVA(subtotal);
        total = subtotal + IVA;

        Subtotal.setText(String.format("%.2f", subtotal));
        ElIVA.setText(String.format("%.2f", IVA));
        Total.setText(String.format("%.2f", total));
    }

    private void agregarDetalleFactura() {
        int cantidad = Integer.parseInt(this.txt_Cantidad.getText());
        if (this.productoEncontrado != null) {
            DetalleFactura nuevoDetalle = new DetalleFactura(cantidad,
                    this.productoEncontrado.getPrecio(), this.productoEncontrado);
            this.detallesFactura.add(nuevoDetalle);

            for (DetalleFactura actualDetalle : this.detallesFactura) {
                System.out.println("Detalle: " + actualDetalle.getProducto().getNombre()
                        + ", " + actualDetalle.getProducto().getPrecio());
            }

        }
    }

    private double calcularTotalFactura() {
        double total = 0;
        for (DetalleFactura detalle : detallesFactura) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }
        return total;
    }

    private void buscarPersona() {
        String cedula = this.txt_Cedula.getText();
        this.personaEncontrada = this.servicio.BuscarPersonaPorCedula(cedula);
        if (personaEncontrada != null) {

            txt_Nombre.setText(personaEncontrada.getNombre());
            txt_Apellido.setText(personaEncontrada.getApellido());
            txt_NumeroCedula.setText(personaEncontrada.getCedula());
            txt_Correo.setText(personaEncontrada.getCorreo());
            System.out.println("La Persona es: " + this.personaEncontrada.getNombre());
        } else {

            txt_Nombre.setText("");
            txt_Apellido.setText("");
            txt_NumeroCedula.setText("");
            txt_Correo.setText("");
        }
    }

    private void buscarProducto() {
        String codigo = this.txt_Codigo.getText();
        this.productoEncontrado = this.servicio.BuscarProductoPorCodigo(codigo);
        if (this.productoEncontrado != null) {
            System.out.println("El producto encontrado es: " + productoEncontrado.getNombre());
            this.txt_Precio.setText(String.valueOf(this.productoEncontrado.getPrecio()));
        }
    }

    private boolean validarFormularioDetalle() {
        Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
        Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

        boolean codigoValido = !txt_Codigo.getText().trim().isEmpty();
        boolean cantidadValida = !txt_Cantidad.getText().trim().isEmpty();
        boolean precioValido = !txt_Precio.getText().trim().isEmpty();

        txt_Codigo.setBorder(codigoValido ? bordeNegro : bordeRojo);
        txt_Cantidad.setBorder(cantidadValida ? bordeNegro : bordeRojo);
        txt_Precio.setBorder(precioValido ? bordeNegro : bordeRojo);

        if (!codigoValido || !cantidadValida || !precioValido) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos requeridos.");
            return false;
        }

        try {
            Integer.parseInt(txt_Cantidad.getText().trim());
            Double.parseDouble(txt_Precio.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero y el precio debe ser un número decimal.");
            return false;
        }

        return true;
    }

    private void limpiarTodoFormulario() {
        txt_Cedula.setText("");
        txt_Nombre.setText("");
        txt_Apellido.setText("");
        txt_NumeroCedula.setText("");
        txt_Correo.setText("");
        limpiarCamposProducto();
        detallesFactura.clear();
        personaEncontrada = null;
        modeloTablaDetalles.setRowCount(0);
        controlarBotones(false);
        actualizarTotales();
    }

    private void controlarBotones(boolean enModoAgregar) {
        btn_BuscarPersona.setEnabled(!enModoAgregar);
        btn_BuscarProducto.setEnabled(enModoAgregar);
        btn_Agregar.setEnabled(enModoAgregar);
        btn_CrearFactura.setEnabled(!enModoAgregar && !detallesFactura.isEmpty());

        txt_Codigo.setEditable(enModoAgregar);
        txt_Cantidad.setEditable(enModoAgregar);
        txt_Precio.setEditable(false);

        txt_Nombre.setEditable(false);
        txt_Apellido.setEditable(false);
        txt_NumeroCedula.setEditable(false);
        txt_Correo.setEditable(false);
    }

    private void limpiarCamposProducto() {
        txt_Codigo.setText("");
        txt_Cantidad.setText("");
        txt_Precio.setText("");
        this.productoEncontrado = null;
    }

    private void actualizarTablaDetalles() {
        modeloTablaDetalles.setRowCount(0);

        for (DetalleFactura detalle : detallesFactura) {
            double totalDetalle = detalle.getCantidad() * detalle.getPrecioUnitario();
            Object[] fila = {
                detalle.getProducto().getCodigo(),
                detalle.getProducto().getNombre(),
                String.format("%.2f", detalle.getPrecioUnitario()),
                detalle.getCantidad(),
                String.format("%.2f", totalDetalle)
            };
            modeloTablaDetalles.addRow(fila);
        }

        actualizarTotales();
    }

    private void eliminarDetalleSeleccionado() {
        int filaSeleccionada = tbl_Factura.getSelectedRow();
        if (filaSeleccionada >= 0) {
            detallesFactura.remove(filaSeleccionada);
            actualizarTablaDetalles();
            btn_EliminarDetalle.setEnabled(!detallesFactura.isEmpty());
            btn_CrearFactura.setEnabled(!detallesFactura.isEmpty());
        } else {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un producto para eliminar.");
        }
    }

    private void agregarFacturaATabla(Factura factura) {

        if (!(tbl_Factura.getModel() instanceof DefaultTableModel)) {
            modeloTablaDetalles = new DefaultTableModel(
                    new Object[]{"Cliente", "Total"}, 0);
            tbl_Factura.setModel(modeloTablaDetalles);
        } else {
            modeloTablaDetalles = (DefaultTableModel) tbl_Factura.getModel();
        }

        double total = 0;
        for (DetalleFactura detalle : factura.getDetalles()) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_Total = new javax.swing.JLabel();
        btn_dlttodo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_Cantidad = new javax.swing.JTextField();
        txt_Precio = new javax.swing.JTextField();
        btn_Agregar = new javax.swing.JButton();
        btn_CrearFactura = new javax.swing.JButton();
        btn_EliminarDetalle = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_Apellido = new javax.swing.JTextField();
        txt_Nombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_NumeroCedula = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btn_BuscarPersona = new javax.swing.JButton();
        txt_Correo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Subtotal = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JTextField();
        ElIVA = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Factura = new javax.swing.JTable();
        Total = new javax.swing.JLabel();
        lbl_Subtotal = new javax.swing.JLabel();
        txt_Codigo = new javax.swing.JTextField();
        lbl_IVA = new javax.swing.JLabel();
        btn_BuscarProducto = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        lbl_Total.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Total.setText("TOTAL");

        btn_dlttodo.setBackground(new java.awt.Color(102, 102, 102));
        btn_dlttodo.setForeground(new java.awt.Color(0, 0, 0));
        btn_dlttodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/borrador.png"))); // NOI18N
        btn_dlttodo.setText("Limpiar todo");
        btn_dlttodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dlttodoActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/etiqueta-del-precio.png"))); // NOI18N
        jLabel4.setText("Precio:");

        txt_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CantidadKeyTyped(evt);
            }
        });

        btn_Agregar.setBackground(new java.awt.Color(102, 102, 102));
        btn_Agregar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Agregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/anadir-a-la-cesta.png"))); // NOI18N
        btn_Agregar.setText("Agregar");
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_CrearFactura.setBackground(new java.awt.Color(102, 153, 255));
        btn_CrearFactura.setForeground(new java.awt.Color(0, 0, 0));
        btn_CrearFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/factura.png"))); // NOI18N
        btn_CrearFactura.setText("Crear Factura ");
        btn_CrearFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CrearFacturaActionPerformed(evt);
            }
        });

        btn_EliminarDetalle.setBackground(new java.awt.Color(255, 102, 102));
        btn_EliminarDetalle.setForeground(new java.awt.Color(0, 0, 0));
        btn_EliminarDetalle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trash-bin_5055247 (2).png"))); // NOI18N
        btn_EliminarDetalle.setText("Eliminar Detalle");
        btn_EliminarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarDetalleActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/apoyo-tecnico.png"))); // NOI18N
        jLabel5.setText("INFORMACION DEL CLIENTE");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Nombre:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Apellido:");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/perfil.png"))); // NOI18N
        jLabel8.setText("Numero de cedula:");

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/perfil.png"))); // NOI18N
        jLabel1.setText("Numero de cedula:");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Correo electronico:");

        btn_BuscarPersona.setBackground(new java.awt.Color(102, 153, 255));
        btn_BuscarPersona.setForeground(new java.awt.Color(0, 0, 0));
        btn_BuscarPersona.setText("Buscar");
        btn_BuscarPersona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarPersonaActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Codigo:");

        Subtotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Subtotal.setForeground(new java.awt.Color(0, 0, 0));
        Subtotal.setText("----");

        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

        ElIVA.setForeground(new java.awt.Color(0, 0, 0));
        ElIVA.setText("-----");

        tbl_Factura.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbl_Factura);

        Total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Total.setForeground(new java.awt.Color(0, 0, 0));
        Total.setText("----");

        lbl_Subtotal.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Subtotal.setText("SUBTOTAL");

        txt_Codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CodigoKeyTyped(evt);
            }
        });

        lbl_IVA.setForeground(new java.awt.Color(0, 0, 0));
        lbl_IVA.setText("IVA 15%");

        btn_BuscarProducto.setBackground(new java.awt.Color(153, 204, 255));
        btn_BuscarProducto.setForeground(new java.awt.Color(0, 0, 0));
        btn_BuscarProducto.setText("Buscar");
        btn_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarProductoActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cantidad.png"))); // NOI18N
        jLabel3.setText("Cantidad:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cuenta.png"))); // NOI18N
        jLabel10.setText("FACTURACION");

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agregar-producto.png"))); // NOI18N
        jLabel11.setText("INFORMACION DEL PRODUCTO");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_Cedula)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_Correo, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_NumeroCedula, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btn_BuscarPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34)
                                    .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(35, 35, 35)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_BuscarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lbl_IVA, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(ElIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(41, 63, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_Subtotal)
                                            .addComponent(lbl_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(Total)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(Subtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(23, 69, Short.MAX_VALUE))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(111, 111, 111)
                                        .addComponent(jLabel10))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(76, 76, 76)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btn_EliminarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btn_dlttodo)
                        .addGap(0, 22, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_BuscarPersona)
                            .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_NumeroCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_Correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel11)
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Subtotal)
                            .addComponent(lbl_Subtotal))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_IVA)
                            .addComponent(ElIVA, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_Total)
                            .addComponent(Total)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_BuscarProducto))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_EliminarDetalle)
                    .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_dlttodo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_dlttodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dlttodoActionPerformed
        limpiarTodoFormulario();
    }//GEN-LAST:event_btn_dlttodoActionPerformed

    private void txt_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CantidadKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CantidadKeyTyped

    private void btn_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AgregarActionPerformed
        if (validarFormularioDetalle()) {
            int cantidad = Integer.parseInt(txt_Cantidad.getText().trim());

            if (cantidad > 0) {
                for(DetalleFactura d: detallesFactura){
                    if(d.getProducto().getCodigo().equals(productoEncontrado.getCodigo())){
                        d.setCantidad(d.getCantidad()+cantidad);
                        actualizarTablaDetalles();
                        limpiarCamposProducto();
                        return;
                    }
                }

                DetalleFactura detalle = new DetalleFactura();
                detalle.setProducto(productoEncontrado);
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(productoEncontrado.getPrecio());
                detallesFactura.add(detalle);

                actualizarTablaDetalles();
                limpiarCamposProducto();
                btn_CrearFactura.setEnabled(!detallesFactura.isEmpty());
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
            }
        }
    }//GEN-LAST:event_btn_AgregarActionPerformed

    private void btn_CrearFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CrearFacturaActionPerformed
        if (!detallesFactura.isEmpty()) {

            registrarFactura();

            JOptionPane.showMessageDialog(this, "Factura creada exitosamente");

        } else {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la factura.");
        }
    }//GEN-LAST:event_btn_CrearFacturaActionPerformed

    private void btn_EliminarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarDetalleActionPerformed
        eliminarDetalleSeleccionado();
    }//GEN-LAST:event_btn_EliminarDetalleActionPerformed

    private void btn_BuscarPersonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarPersonaActionPerformed
        buscarPersona();
        if (personaEncontrada != null) {
            controlarBotones(true);
            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + personaEncontrada.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Verifique la cédula.");
        }
    }//GEN-LAST:event_btn_BuscarPersonaActionPerformed

    private void txt_CedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CedulaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CedulaKeyTyped

    private void txt_CodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CodigoKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_CodigoKeyTyped

    private void btn_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarProductoActionPerformed
        buscarProducto();
        if (productoEncontrado != null) {
            JOptionPane.showMessageDialog(this, "Producto encontrado: " + productoEncontrado.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado. Verifique el código.");
        }
    }//GEN-LAST:event_btn_BuscarProductoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ElIVA;
    private javax.swing.JLabel Subtotal;
    private javax.swing.JLabel Total;
    private javax.swing.JButton btn_Agregar;
    private javax.swing.JButton btn_BuscarPersona;
    private javax.swing.JButton btn_BuscarProducto;
    private javax.swing.JButton btn_CrearFactura;
    private javax.swing.JButton btn_EliminarDetalle;
    private javax.swing.JButton btn_dlttodo;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_IVA;
    private javax.swing.JLabel lbl_Subtotal;
    private javax.swing.JLabel lbl_Total;
    private javax.swing.JTable tbl_Factura;
    private javax.swing.JTextField txt_Apellido;
    private javax.swing.JTextField txt_Cantidad;
    private javax.swing.JTextField txt_Cedula;
    private javax.swing.JTextField txt_Codigo;
    private javax.swing.JTextField txt_Correo;
    private javax.swing.JTextField txt_Nombre;
    private javax.swing.JTextField txt_NumeroCedula;
    private javax.swing.JTextField txt_Precio;
    // End of variables declaration//GEN-END:variables
}
