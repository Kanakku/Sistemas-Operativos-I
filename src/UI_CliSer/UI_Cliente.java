/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI_CliSer;

    import UI_CliSer.*;
    import cliente.servidor.*;
    import java.net.*;
    import javax.swing.*;
    import cliente.servidor.Cliente;

import java.awt.GridLayout;

public class UI_Cliente extends javax.swing.JFrame {
    
    private final Cliente cliente;
    private final String puertodef="6013";
    private final String ipdef="127.0.0.1";
    DefaultListModel modelo = new DefaultListModel();
    
    public UI_Cliente() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String ip_puerto_nombre[]=getIP_Puerto_Nombre();
        String ip=ip_puerto_nombre[0];
        String puerto=ip_puerto_nombre[1];
        String nombre=ip_puerto_nombre[2];
        cliente= new Cliente(this, ip, Integer.valueOf(puerto), nombre);
        jlist_Cli1.setModel(modelo);
    }
    
    private String[] getIP_Puerto_Nombre(){
        String str[] = new String[3];
        str[0]=ipdef;
        str[1]=puertodef;
        JTextField nomusuario = new JTextField(50);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(3, 1));
        myPanel.add(new JLabel("Escriba su nombre de usuario:"));
        myPanel.add(nomusuario);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Conectarse al servidor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
                str[2]=nomusuario.getText();
        }else{
            System.exit(0);
        }
        return str;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jlist_Cli1 = new javax.swing.JList<>();
        txt_Cli1 = new javax.swing.JTextField();
        btn_Send1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cmb_receptor = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente 1");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jScrollPane1.setViewportView(jlist_Cli1);

        btn_Send1.setText("Enviar");
        btn_Send1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Send1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Enviar a:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_Send1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_Cli1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Cli1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Send1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(cmb_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_Send1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Send1ActionPerformed
        if(cmb_receptor.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this, "Escoja un destinatario, en caso de \n"
                + "no haver, espere a que se conecte.\n");       
            return;
        }
        String receptor = cmb_receptor.getSelectedItem().toString();
        String mensaje=txt_Cli1.getText();
        cliente.enviarMensaje(receptor, mensaje);
        modelo.addElement("Yo:- "+mensaje+"\n");
        txt_Cli1.setText("");      
    }//GEN-LAST:event_btn_Send1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        cliente.confirmarDesconexion();
    }//GEN-LAST:event_formWindowClosing
        
    public void addMensaje(String emisor, String mensaje) {
        modelo.addElement(emisor+":- "+mensaje+"\n");
    }
    
    public void addContacto(String contacto) {
        cmb_receptor.addItem(contacto);
    }
    
    public void eliminarContacto(String identificador) {
        for (int i = 0; i < cmb_receptor.getItemCount(); i++) {
            if(cmb_receptor.getItemAt(i).toString().equals(identificador)){
                cmb_receptor.removeItemAt(i);
                return;
            }
        }
    }
    
    public void sesionIniciada(String identificador) {
        this.setTitle(identificador);
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
            java.util.logging.Logger.getLogger(UI_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI_Cliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Send1;
    private javax.swing.JComboBox<String> cmb_receptor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> jlist_Cli1;
    private javax.swing.JTextField txt_Cli1;
    // End of variables declaration//GEN-END:variables
}
