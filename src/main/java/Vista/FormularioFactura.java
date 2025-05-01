package Vista;

import Model.DetalleFactura;
import java.util.ArrayList;
import java.util.List;
import Model.Factura;
import Model.Persona;
import Model.Producto;
import Negocio.FacturaServicio;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FormularioFactura extends javax.swing.JFrame {

    private List<DetalleFactura> detallesFactura = new ArrayList();

    private List<Factura> facturasCreadas = new ArrayList();

    private Persona personaEncontrada;

    private final FacturaServicio servicio;

    private Producto productoEncontrado;

    private DefaultTableModel modeloTablaDetalles;

    private double subtotal = 0;

    private double IVA = 0;

    private double total = 0;

    public FormularioFactura() {
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_BuscarPersona = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_Cedula = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Factura = new javax.swing.JTable();
        txt_Codigo = new javax.swing.JTextField();
        btn_BuscarProducto = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
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
        jLabel9 = new javax.swing.JLabel();
        txt_Correo = new javax.swing.JTextField();
        Subtotal = new javax.swing.JLabel();
        ElIVA = new javax.swing.JLabel();
        Total = new javax.swing.JLabel();
        lbl_Subtotal = new javax.swing.JLabel();
        lbl_IVA = new javax.swing.JLabel();
        lbl_Total = new javax.swing.JLabel();
        btn_dlttodo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Numero de cedula:");

        btn_BuscarPersona.setText("Buscar");
        btn_BuscarPersona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarPersonaActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Codigo:");

        txt_Cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CedulaKeyTyped(evt);
            }
        });

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

        txt_Codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CodigoKeyTyped(evt);
            }
        });

        btn_BuscarProducto.setText("Buscar");
        btn_BuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarProductoActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cantidad:");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Precio:");

        txt_Cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CantidadKeyTyped(evt);
            }
        });

        btn_Agregar.setText("Agregar");
        btn_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AgregarActionPerformed(evt);
            }
        });

        btn_CrearFactura.setText("Crear Factura ");
        btn_CrearFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CrearFacturaActionPerformed(evt);
            }
        });

        btn_EliminarDetalle.setText("Eliminar Detalle");
        btn_EliminarDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarDetalleActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Informacion del cliente");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Nombre:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Apellido:");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Numero de cedula:");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Correo electronico:");

        Subtotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Subtotal.setForeground(new java.awt.Color(0, 0, 0));
        Subtotal.setText("----");

        ElIVA.setForeground(new java.awt.Color(0, 0, 0));
        ElIVA.setText("----");

        Total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Total.setForeground(new java.awt.Color(0, 0, 0));
        Total.setText("----");

        lbl_Subtotal.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Subtotal.setText("SUBTOTAL");

        lbl_IVA.setForeground(new java.awt.Color(0, 0, 0));
        lbl_IVA.setText("IVA 15%");

        lbl_Total.setForeground(new java.awt.Color(0, 0, 0));
        lbl_Total.setText("TOTAL");

        btn_dlttodo.setText("Limpiar todo");
        btn_dlttodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dlttodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_BuscarProducto))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txt_Cedula)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(75, 75, 75)))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_Correo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                .addComponent(txt_NumeroCedula, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btn_BuscarPersona, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34)
                                    .addComponent(txt_Apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_dlttodo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbl_Subtotal)
                                        .addComponent(lbl_IVA, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lbl_Total, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(Total, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ElIVA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(Subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(197, 197, 197))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btn_EliminarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_BuscarPersona)
                            .addComponent(txt_Cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                        .addGap(35, 35, 35))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BuscarProducto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Subtotal)
                    .addComponent(lbl_Subtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_IVA)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addComponent(ElIVA, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lbl_Total)
                                .addComponent(Total)
                                .addComponent(btn_dlttodo, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_EliminarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_CrearFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_Cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void btn_BuscarPersonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarPersonaActionPerformed
        buscarPersona();
        if (personaEncontrada != null) {
            controlarBotones(true);
            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + personaEncontrada.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado. Verifique la cédula.");
        }
    }//GEN-LAST:event_btn_BuscarPersonaActionPerformed

    private void btn_BuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarProductoActionPerformed
        buscarProducto();
        if (productoEncontrado != null) {
            JOptionPane.showMessageDialog(this, "Producto encontrado: " + productoEncontrado.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado. Verifique el código.");
        }
    }//GEN-LAST:event_btn_BuscarProductoActionPerformed

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

    private void btn_dlttodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dlttodoActionPerformed
        limpiarTodoFormulario();
    }//GEN-LAST:event_btn_dlttodoActionPerformed

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

    private void txt_CantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CantidadKeyTyped
        // TODO add your handling code here:
          char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_CantidadKeyTyped

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
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioFactura.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioFactura().setVisible(true);
            }
        });
    }

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
