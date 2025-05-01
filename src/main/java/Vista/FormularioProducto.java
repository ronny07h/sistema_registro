package Vista;

import Model.Producto;
import Negocio.ProductoService;
import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class FormularioProducto extends javax.swing.JFrame {

    private final ProductoService servicio;
    private DefaultTableModel tabla;
    private boolean modoAgregar = true;

    public FormularioProducto() {
        initComponents();
        servicio = new ProductoService();
        inicializarTabla();
        tabla = (DefaultTableModel) tbl_producto.getModel();
        cargarDatos();
        controlarBotones(true);
    }

    private void inicializarTabla() {
        tbl_producto.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFila();
            }
        });

    }

    public void cargarDatos() {
        tabla.setRowCount(0);

        for (Producto producto : servicio.listarProductos()) {
            Object[] fila = {
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria()
            };
            tabla.addRow(fila);
        }
        limpiar();
    }

   public void agregar() {
        if (ValidarFormulario()) {
            String codigo = txt_Codigo.getText().trim();
            String nombre = txt_Nombre.getText().trim();
            String descripcion = txt_Descripcion.getText().trim();
            String categoria = txt_Categoria.getText().trim();
            float precio;
            int stock;

            try {
                precio = Float.parseFloat(txt_Precio.getText().trim());
                stock = Integer.parseInt(txt_Stock.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El precio y el stock deben ser valores numéricos.");
                return;
            }

            Producto producto = new Producto(codigo, nombre, descripcion, precio, stock, categoria);
            servicio.agregarNuevoProducto(producto);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente");
        }
    }

 public void actualizar() {
        if (ValidarFormulario()) {
            String codigo = txt_Codigo.getText().trim();
            String nombre = txt_Nombre.getText().trim();
            String descripcion = txt_Descripcion.getText().trim();
            String categoria = txt_Categoria.getText().trim();
            float precio;
            int stock;

            try {
                precio = Float.parseFloat(txt_Precio.getText().trim());
                stock = Integer.parseInt(txt_Stock.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El precio y el stock deben ser valores numéricos.");
                return;
            }

            
            Producto productoActual = servicio.buscarPorCodigo(codigo);

            if (productoActual != null) {
                Producto producto = new Producto(codigo, nombre, descripcion, precio, stock, categoria);
                producto.setId(productoActual.getId());
                servicio.actualizarProducto(producto);
                cargarDatos();
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto para actualizar",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean ValidarFormulario() {
        Border bordeRojo = BorderFactory.createLineBorder(Color.RED, 2);
        Border bordeNegro = BorderFactory.createLineBorder(Color.BLACK, 1);

        boolean codigoValidado = !txt_Codigo.getText().trim().isEmpty();
        boolean nombreValido = !txt_Nombre.getText().trim().isEmpty();
        boolean descripcionValido = !txt_Descripcion.getText().trim().isEmpty();
        boolean precioValido = !txt_Precio.getText().trim().isEmpty();
        boolean stockValida = !txt_Stock.getText().trim().isEmpty();
        boolean categoriaValida = !txt_Categoria.getText().trim().isEmpty();

        txt_Codigo.setBorder(codigoValidado ? bordeNegro : bordeRojo);
        txt_Nombre.setBorder(nombreValido ? bordeNegro : bordeRojo);
        txt_Descripcion.setBorder(descripcionValido ? bordeNegro : bordeRojo);
        txt_Precio.setBorder(precioValido ? bordeNegro : bordeRojo);
        txt_Stock.setBorder(stockValida ? bordeNegro : bordeRojo);
        txt_Categoria.setBorder(categoriaValida ? bordeNegro : bordeRojo);

        if (!codigoValidado || !nombreValido || !descripcionValido
                || !precioValido || !stockValida || !categoriaValida) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios");
            return false;
        }

        try {
            Double.parseDouble(txt_Precio.getText().trim());
            Integer.parseInt(txt_Stock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio y el stock deben ser valores numéricos.");
            return false;
        }

        return true;
    }

    private void limpiar() {
        txt_Codigo.setText("");
        txt_Nombre.setText("");
        txt_Descripcion.setText("");
        txt_Precio.setText("");
        txt_Stock.setText("");
        txt_Categoria.setText("");

    }

    public void eliminar() {
        String codigo = txt_Codigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este producto?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Producto producto = servicio.buscarPorCodigo(codigo);
                if (producto != null) {
                    boolean resultado = servicio.eliminarProducto(producto.getId().intValue());

                    if (resultado) {
                        cargarDatos();
                        JOptionPane.showMessageDialog(this, "Producto eliminado correctamente");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto por que se esta usando su id",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el producto para eliminar",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void buscar() {
        String criterio = txt_Codigo.getText().trim();
        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un criterio de búsqueda");
            return;
        }

        List<Producto> productos = servicio.buscarProductos(criterio);
        if (productos != null && !productos.isEmpty()) {

            Producto producto = productos.get(0);

            txt_Codigo.setText(producto.getCodigo());
            txt_Nombre.setText(producto.getNombre());
            txt_Descripcion.setText(producto.getDescripcion());
            txt_Precio.setText(String.valueOf(producto.getPrecio()));
            txt_Stock.setText(String.valueOf(producto.getStock()));
            txt_Categoria.setText(producto.getCategoria());

            if (productos.size() > 1) {
                JOptionPane.showMessageDialog(this,
                        "Se encontraron " + productos.size() + " productos. Mostrando el primero.",
                        "Múltiples resultados", JOptionPane.INFORMATION_MESSAGE);
            }

            controlarBotones(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron productos con ese criterio");
            limpiar();
        }
    }

    private void controlarBotones(boolean enModoAgregar) {
        this.modoAgregar = enModoAgregar;
        btn_Guardar.setEnabled(enModoAgregar);
        btn_Buscar.setEnabled(!enModoAgregar);
        btn_Actualizar.setEnabled(!enModoAgregar);
        btn_Eliminar.setEnabled(!enModoAgregar);

        txt_Codigo.setEditable(enModoAgregar);
    }

    private void seleccionarFila() {
        int filaSeleccionada = tbl_producto.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String codigo = tbl_producto.getValueAt(filaSeleccionada, 0).toString();
            String nombre = tbl_producto.getValueAt(filaSeleccionada, 1).toString();
            String descripcion = tbl_producto.getValueAt(filaSeleccionada, 2).toString();
            String precio = tbl_producto.getValueAt(filaSeleccionada, 3).toString();
            String stock = tbl_producto.getValueAt(filaSeleccionada, 4).toString();
            String categoria = tbl_producto.getValueAt(filaSeleccionada, 5).toString();

            txt_Codigo.setText(codigo);
            txt_Nombre.setText(nombre);
            txt_Descripcion.setText(descripcion);
            txt_Precio.setText(precio);
            txt_Stock.setText(stock);
            txt_Categoria.setText(categoria);

            controlarBotones(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btn_Guardar = new javax.swing.JButton();
        btn_Buscar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_producto = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_Codigo = new javax.swing.JTextField();
        txt_Nombre = new javax.swing.JTextField();
        txt_Descripcion = new javax.swing.JTextField();
        txt_Precio = new javax.swing.JTextField();
        txt_Stock = new javax.swing.JTextField();
        txt_Categoria = new javax.swing.JTextField();
        btn_Actualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setName(""); // NOI18N

        jLabel1.setBackground(new java.awt.Color(204, 204, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Producto");

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Codigo");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nombre:");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Descripcion:");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Precio:");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Stock:");

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
        btn_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarActionPerformed(evt);
            }
        });

        btn_Eliminar.setBackground(new java.awt.Color(51, 102, 255));
        btn_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/-delete-forever_90072.png"))); // NOI18N
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        tbl_producto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Descripcion", "Precio", "Stock", "Categoria"
            }
        ));
        tbl_producto.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tbl_productoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(tbl_producto);

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Categoria:");

        txt_Codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CodigoKeyTyped(evt);
            }
        });

        txt_Nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_NombreKeyTyped(evt);
            }
        });

        txt_Precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_PrecioKeyTyped(evt);
            }
        });

        txt_Stock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_StockKeyTyped(evt);
            }
        });

        txt_Categoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_CategoriaKeyTyped(evt);
            }
        });

        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(67, 67, 67)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2))
                            .addGap(88, 88, 88)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_Codigo)
                                .addComponent(txt_Nombre)
                                .addComponent(txt_Descripcion)
                                .addComponent(txt_Precio)
                                .addComponent(txt_Stock)
                                .addComponent(txt_Categoria, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(btn_Guardar)
                        .addGap(61, 61, 61)
                        .addComponent(btn_Actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(btn_Buscar)
                        .addGap(59, 59, 59)
                        .addComponent(btn_Eliminar)))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_Codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_Nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txt_Precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txt_Stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(63, 63, 63)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Actualizar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        agregar();
        limpiar();
        controlarBotones(true);
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btn_BuscarActionPerformed

    private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
        actualizar();
        controlarBotones(true);
    }//GEN-LAST:event_btn_ActualizarActionPerformed

    private void tbl_productoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tbl_productoAncestorAdded

    }//GEN-LAST:event_tbl_productoAncestorAdded

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        eliminar();
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void txt_CodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CodigoKeyTyped
        // TODO add your handling code here:
          char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_CodigoKeyTyped

    private void txt_PrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PrecioKeyTyped
        // TODO add your handling code here:
          char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_PrecioKeyTyped

    private void txt_StockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_StockKeyTyped
        // TODO add your handling code here:
          char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_StockKeyTyped

    private void txt_NombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_NombreKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_NombreKeyTyped

    private void txt_CategoriaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_CategoriaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            evt.consume(); 
        }
    }//GEN-LAST:event_txt_CategoriaKeyTyped

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioProducto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Buscar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_producto;
    private javax.swing.JTextField txt_Categoria;
    private javax.swing.JTextField txt_Codigo;
    private javax.swing.JTextField txt_Descripcion;
    private javax.swing.JTextField txt_Nombre;
    private javax.swing.JTextField txt_Precio;
    private javax.swing.JTextField txt_Stock;
    // End of variables declaration//GEN-END:variables
}
