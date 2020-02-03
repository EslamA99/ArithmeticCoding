package com.arithmeticcoding;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUI extends JFrame {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField textField1;
    private JButton compressButton;
    private JTextField value;
    private JLabel range;
    private JTextField symbolInput;
    private JTextField deCompressedSeq;
    private JButton STARTButton;
    private JTextField valueInput;
    private JTextField numSymbolsInput;
    private JButton compressFileButton;
    private JButton decompressFileButton;
    private JTextField compressArea;
    private JButton compressPathButton;
    private JTextField deCompressArea;
    private JButton deCompressPathButton;
    private JTextArea charAndProb;
    ArrayList<charAndProb> data = new ArrayList<>();
    //Map<Character, LowUpperBoundry> table = new HashMap<>();
    //private JPanel panel1;

    public GUI() {
        setTitle("Arithmetic Coding");
        setSize(800, 600);

        add(panel1);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                value.setText(ArithmeticCoding.compressForGui(textField1.getText()));
                range.setText(ArithmeticCoding.getRangeForGui());

            }
        });


        STARTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               String []lines=charAndProb.getText().split("\n");
               for(int i=0;i<lines.length;i++){
                   String arr[]=lines[i].split(",");
                   data.add(new charAndProb(arr[0].charAt(0),Double.valueOf(arr[1])));
               }
                Collections.sort(data,Comparator.comparing(charAndProb1 -> charAndProb1.symbol));
               ArithmeticCoding.setTableDecompress(data);
                deCompressedSeq.setText(ArithmeticCoding.deCompress(Double.valueOf(valueInput.getText()), Integer.parseInt(numSymbolsInput.getText())));
                //table.clear();
            }
        });
        compressPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setControlButtonsAreShown(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                compressArea.setText(f.getAbsolutePath());
            }
        });
        deCompressPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setControlButtonsAreShown(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                deCompressArea.setText(f.getAbsolutePath());
            }
        });
        compressFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                chooser.setFileFilter(filter);
                chooser.setMultiSelectionEnabled(true);
                chooser.showOpenDialog(null);
                File[] files = chooser.getSelectedFiles();
                for (int i = 0; i < files.length; i++) {
                    try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                        String line;
                        line = br.readLine();
                        /**remove .txt and put .Arithmetic Coding**/
                        FileWriter fw = new FileWriter(compressArea.getText() + "\\" + files[i].getName().substring(0, files[i].getName().length() - 4) + ".ArithmeticCoding", true);//create file to store connected client ports
                        PrintWriter pw = new PrintWriter(fw);//to write in file
                        pw.println(ArithmeticCoding.compressForFile(line));
                        pw.print(ArithmeticCoding.getTableForFile());
                        pw.close();
                        fw.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        decompressFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("ArithmeticCoding FILES", "ArithmeticCoding", "text");
                chooser.setFileFilter(filter);
                chooser.setMultiSelectionEnabled(true);

                chooser.showOpenDialog(null);
                File[] files = chooser.getSelectedFiles();
                double value;
                int seqNum;
                for(int i=0;i<files.length;i++){
                    data.clear();
                    try (BufferedReader br = new BufferedReader(new FileReader(files[i]))) {
                        String line=br.readLine();
                        String[]arr=line.split(",");
                        value=Double.valueOf(arr[0]);
                        seqNum=Integer.parseInt(arr[1]);
                        while ((line = br.readLine()) != null){
                            arr=line.split(",");
                            data.add(new charAndProb(arr[0].charAt(0),Double.valueOf(arr[1])));
                        }
                        FileWriter fw = new FileWriter(deCompressArea.getText().toString() + "\\" + files[i].getName().substring(0, files[i].getName().length() - 16) + ".txt", true);//create file to store connected client ports
                        PrintWriter pw = new PrintWriter(fw);//to write in file
                        Collections.sort(data,Comparator.comparing(charAndProb1 -> charAndProb1.symbol));
                        ArithmeticCoding.setTableDecompress(data);
                        pw.print(ArithmeticCoding.deCompress(value,seqNum));
                        fw.close();
                        pw.close();
                    }catch (FileNotFoundException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }


}
