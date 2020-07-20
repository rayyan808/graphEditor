package graphEditor.controller;

import graphEditor.GraphEditor;
import graphEditor.model.GraphModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveModel implements ActionListener {
    private GraphEditor main;

    public SaveModel(GraphEditor main, JMenuItem s) {
        this.main=main;
        s.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e){
        System.out.println("Save clicked");
        String s = JOptionPane.showInputDialog(null, "Enter filename. (Excluding .awesome extension)");
        if (s != null && s != "") { //Cancel was not clicked and Empty string was not given
            save(System.getProperty("user.dir") + "\\" + s + ".awesome");
        } else {
            JOptionPane.showMessageDialog(null, "You cancelled.");
        }
    }
    private void save(String dir){
        // Serialization
        try {
            // Saving of object in a file
            FileOutputStream file = new FileOutputStream(dir);
            ObjectOutputStream out = new ObjectOutputStream
                    (file);
            GraphModel m = main.getCurrentModel();
                out.writeObject(m);
            out.close();
            file.close();
            System.out.println("Object has been serialized\n");
        }
        catch (IOException ex) {
            System.out.println("IOException is caught");
            JOptionPane.showMessageDialog(null, "The name you entered is not supported. \n" +
                    "(Try avoiding symbols unsupported by your system.");
        }
    }
}
