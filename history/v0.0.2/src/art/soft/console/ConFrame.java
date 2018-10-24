package art.soft.console;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author Artem
 */
public class ConFrame extends javax.swing.JFrame {

    /**
     * Creates new form ConFrame
     */
    public ConFrame() {
        initComponents();
        FindMenu.add(FindScroll);
        add(FindMenu);
    }

    private final ArrayList<String> history = new ArrayList<>();

    private Console console;

    public JTextArea getConOut() {
        return ConOut;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void clearHistory() {
        history.clear();
    }

    private AutoComplete autoComplete;

    public void setCosole(Console console) {
        this.console = console;

        autoComplete = new AutoComplete(ConIn, console, FindMenu, FindList);
        ConIn.getDocument().addDocumentListener(autoComplete);
    }

    public static void setStyle() {
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
            java.util.logging.Logger.getLogger(ConFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FindMenu = new javax.swing.JPopupMenu();
        FindScroll = new javax.swing.JScrollPane();
        FindList = new javax.swing.JList();
        ConEnter = new javax.swing.JButton();
        ConIn = new javax.swing.JTextField();
        ConScrollPane = new javax.swing.JScrollPane();
        ConOut = new javax.swing.JTextArea();

        FindMenu.setAutoscrolls(true);
        FindMenu.setBorder(null);
        FindMenu.setFocusCycleRoot(true);
        FindMenu.setMaximumSize(new java.awt.Dimension(1000, 100));
        FindMenu.setSelectionModel(new javax.swing.DefaultSingleSelectionModel());

        FindScroll.setAutoscrolls(true);
        FindScroll.setNextFocusableComponent(ConIn);

        FindList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        FindList.setNextFocusableComponent(ConIn);
        FindList.setVisibleRowCount(2);
        FindList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FindListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                FindListMousePressed(evt);
            }
        });
        FindScroll.setViewportView(FindList);

        setTitle("Console " + Console.CONSOLE_VERSION);
        setAlwaysOnTop(true);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setName("Console"); // NOI18N
        setType(java.awt.Window.Type.UTILITY);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        ConEnter.setText("ENTER");
        ConEnter.setFocusable(false);
        ConEnter.setNextFocusableComponent(ConIn);
        ConEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConEnterActionPerformed(evt);
            }
        });

        ConIn.setFocusCycleRoot(true);
        ConIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConInActionPerformed(evt);
            }
        });
        ConIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ConInKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ConInKeyReleased(evt);
            }
        });

        ConOut.setEditable(false);
        ConOut.setColumns(20);
        ConOut.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        ConOut.setRows(5);
        ConOut.setNextFocusableComponent(ConIn);
        ConOut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ConOutKeyPressed(evt);
            }
        });
        ConScrollPane.setViewportView(ConOut);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ConIn)
                .addGap(0, 0, 0)
                .addComponent(ConEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(ConScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ConScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConEnter)
                    .addComponent(ConIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enterText() {
        String text = ConIn.getText();
        if (!"".equals(text)) {
            history.remove(text);
            history.add(text);
        }
        ConIn.setText("");
        autoComplete.hide();
        console.runCommand(text, true);
    }

    private void ConEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConEnterActionPerformed
        enterText();
    }//GEN-LAST:event_ConEnterActionPerformed

    private void ConInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConInActionPerformed
        if (autoComplete.enterAct()) enterText();
    }//GEN-LAST:event_ConInActionPerformed

    private void ConOutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ConOutKeyPressed
        if (!evt.isShiftDown() && !evt.isAltDown() && !evt.isControlDown()) {
            ConIn.requestFocus();
        }
    }//GEN-LAST:event_ConOutKeyPressed

    private void setHistory(int select) {
        if (history.size() != 0) {
            ConIn.setText("");
            autoComplete.setWords(history.toArray(), select);
            autoComplete.addToEditFromPos(0);
            autoComplete.refresh();
        }
    }

    private void ConInKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ConInKeyReleased
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_BACK_QUOTE) {
            setVisible(false);
        }
    }//GEN-LAST:event_ConInKeyReleased

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        autoComplete.setNewLocation();
    }//GEN-LAST:event_formComponentMoved

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        autoComplete.hide();
    }//GEN-LAST:event_formComponentHidden

    private void FindListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FindListMouseClicked
        autoComplete.refresh();
        autoComplete.enterAct();
    }//GEN-LAST:event_FindListMouseClicked

    private void FindListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FindListMousePressed
        autoComplete.refresh();
        autoComplete.enterAct();
    }//GEN-LAST:event_FindListMousePressed

    private void ConInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ConInKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            autoComplete.enterAct();
        } else
        if (key == KeyEvent.VK_RIGHT) {
            if (history.size() != 0) {
                String text = ConIn.getText();
                int len = text.length();
                if (ConIn.getCaretPosition() >= len) {
                    String hist = history.get(history.size() - 1);
                    if (len < hist.length()) {
                        autoComplete.isEdit = false;
                        ConIn.setText(text + hist.charAt(len));
                        ConIn.setCaretPosition(len + 1);
                        autoComplete.isEdit = true;
                    }
                }
            }
        } else
        if (key == KeyEvent.VK_UP) {
            if (autoComplete.canHistory()) {
                setHistory(history.size() - 1);
            } else {
                int index = FindList.getSelectedIndex() - 1;
                if (index >= 0) {
                    FindList.setSelectedIndex(index);
                }
                autoComplete.refresh();
                autoComplete.showPopUpWindow();
            }
        } else
        if (key == KeyEvent.VK_DOWN) {
            if (autoComplete.canHistory()) {
                setHistory(0);
            } else {
                int index = FindList.getSelectedIndex() + 1;
                if (FindList.getModel().getSize() > index) {
                    FindList.setSelectedIndex(index);
                }
                autoComplete.refresh();
                autoComplete.showPopUpWindow();
            }
        }
    }//GEN-LAST:event_ConInKeyPressed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (autoComplete != null) autoComplete.refresh();
    }//GEN-LAST:event_formComponentResized

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        ConIn.requestFocus();
    }//GEN-LAST:event_formFocusGained

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        ConIn.requestFocus();
    }//GEN-LAST:event_formFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConEnter;
    private javax.swing.JTextField ConIn;
    public javax.swing.JTextArea ConOut;
    private javax.swing.JScrollPane ConScrollPane;
    private javax.swing.JList FindList;
    private javax.swing.JPopupMenu FindMenu;
    private javax.swing.JScrollPane FindScroll;
    // End of variables declaration//GEN-END:variables
}
