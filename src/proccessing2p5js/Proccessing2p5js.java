package proccessing2p5js;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author dahjon
 */
public class Proccessing2p5js extends JFrame {

    JTextArea proccesingKod = new JTextArea(40, 40);
    JTextArea p5jsKod = new JTextArea(40, 40);
    JTextField txfProcessing = new JTextField(40);
    
    Properties prop = new Properties();
    final private String PROP_FILE_NAME = "Processing2p5js.prop";
    final private String LAST_PROC_FILE = "LAST_PROC_FILE";

    InsertFunctions insertFunctions = new InsertFunctions();

    Font monoFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

//    final public static String CHARSET = "windows-1252";
    final public static String CHARSET = "utf8";

    public Proccessing2p5js() {
        setTitle("Proccessing2p5js");
        JPanel kodPanel = new JPanel();
        kodPanel.setLayout(new BoxLayout(kodPanel, BoxLayout.X_AXIS));
        add(kodPanel, BorderLayout.CENTER);
        proccesingKod.setFont(monoFont);
        p5jsKod.setFont(monoFont);
        kodPanel.add(new JScrollPane(proccesingKod));
        kodPanel.add(new JScrollPane(p5jsKod));

        //Knapppanel
        JPanel pnlKnappar = new JPanel();

        pnlKnappar.add(txfProcessing);
        JButton btnValjProcFil = new JButton("Välj fil");
        btnValjProcFil.addActionListener(new ValjFilListener());
        pnlKnappar.add(btnValjProcFil);
        JButton btnLadda = new JButton("Ladda");
        btnLadda.addActionListener(new LaddaListener());
        pnlKnappar.add(btnLadda)  ;      
        JButton btnKonv = new JButton("Konvertera");
        btnKonv.addActionListener(new KonverteraKnappLyssnare());
        pnlKnappar.add(btnKonv);
        add(pnlKnappar, BorderLayout.NORTH);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
       loadProperties();

        //proccesingKod.setText(Ex.EX);
        laddaFilOchKonvertera();

    }
    private void loadProperties() {
        try {
            prop.load(new FileInputStream(PROP_FILE_NAME));
            txfProcessing.setText(prop.getProperty(LAST_PROC_FILE));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void saveProperties(){
        try{
            prop.setProperty(LAST_PROC_FILE, txfProcessing.getText());
            prop.store(new FileOutputStream(PROP_FILE_NAME),null);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Proccessing2p5js();
    }

    private void laddaFilOchKonvertera() {
        laddaFil();
        konvertera();
        saveProperties();
        sparaFil();
    }

    private void konvertera() {
        StringBuffer proc = new StringBuffer(proccesingKod.getText());
        String res = new String(Konverter.konvert(proc));
        p5jsKod.setText(res);
        sparaFil();
    }

    private void laddaFil() {
        Path path = Paths.get(txfProcessing.getText());
        try {
            String c = new String(Files.readAllBytes(path), CHARSET);
            proccesingKod.setText(c);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void sparaFil(){
        try {
            String p5Code = p5jsKod.getText();
            
            String procFilePathStr = txfProcessing.getText();
            
            P5Filer.sparaFiler(procFilePathStr, p5Code);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }                

    }

    private  class LaddaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            laddaFilOchKonvertera();
        }
    }



    private class ValjFilListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JFileChooser valj = new JFileChooser();
            String lastPath=txfProcessing.getText();
            String startDir;
            if(lastPath.length()==0){
                 startDir = "C:\\Users\\dahjon\\Google Drive\\processing";
            }
            else {
                startDir = lastPath;
            }
            System.out.println("startDir. " + startDir);
            valj.setCurrentDirectory(new File(startDir));
            valj.setPreferredSize(new Dimension(800, 600));

            if (valj.showOpenDialog(Proccessing2p5js.this) == JFileChooser.APPROVE_OPTION) {
                txfProcessing.setText(valj.getSelectedFile().getAbsolutePath());
                laddaFilOchKonvertera();
            }

        }

    }

    private class KonverteraKnappLyssnare implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            konvertera();
        }
    }

}
