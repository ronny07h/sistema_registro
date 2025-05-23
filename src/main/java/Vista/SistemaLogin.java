/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Negocio.LoginServicio;

/**
 *
 * @author XRLab
 */
public class SistemaLogin extends javax.swing.JFrame {
    
    private final LoginServicio servicio;

    /**
     * Creates new form SistemaLogin
     */
    public SistemaLogin() {
        initComponents();
        this.servicio = new LoginServicio();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNameUser = new javax.swing.JLabel();
        txtNameUser = new javax.swing.JTextField();
        txtPasswordUser = new javax.swing.JTextField();
        lblPasswordUser = new javax.swing.JLabel();
        lblSystemAuthentication = new javax.swing.JLabel();
        BtnSingIn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblNameUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNameUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/usuario.png"))); // NOI18N
        lblNameUser.setText("Usuario");

        txtNameUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtPasswordUser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblPasswordUser.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPasswordUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPasswordUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/seguro.png"))); // NOI18N
        lblPasswordUser.setText("Contraseña");

        lblSystemAuthentication.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSystemAuthentication.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSystemAuthentication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autenticacion.png"))); // NOI18N
        lblSystemAuthentication.setText("SISTEMA DE INICIO DE SESION");

        BtnSingIn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        BtnSingIn.setForeground(new java.awt.Color(255, 255, 255));
        BtnSingIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datos-del-usuario.png"))); // NOI18N
        BtnSingIn.setText("Iniciar sesión");
        BtnSingIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSingInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNameUser)
                            .addComponent(txtPasswordUser)
                            .addComponent(lblPasswordUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BtnSingIn, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lblSystemAuthentication, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lblSystemAuthentication, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPasswordUser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPasswordUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BtnSingIn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSingInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSingInActionPerformed
        Login();
    }//GEN-LAST:event_BtnSingInActionPerformed

    
    private void Login(){
        String usuario = this.txtNameUser.getText();
        String clave = this.txtPasswordUser.getText();
        // Se valida que el usuario y clave no sean vacios
        if(!usuario.isEmpty() && !clave.isEmpty()){
            boolean loginExitoso = this.servicio.LoginUsuarioClave(usuario, clave);
            if(loginExitoso){
                System.out.println("EXITO");
                // Iniciar sesion y enviar a lo menus iniciales 
            }else{
                System.out.println("FALLO");
                // Mostrar una alerta que diga usuario y clave incorrecto
            }
        }
    }
    
    
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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SistemaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SistemaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SistemaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SistemaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SistemaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSingIn;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JLabel lblPasswordUser;
    private javax.swing.JLabel lblSystemAuthentication;
    private javax.swing.JTextField txtNameUser;
    private javax.swing.JTextField txtPasswordUser;
    // End of variables declaration//GEN-END:variables
}
