package graphEditor.controller;
import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class UndoAction extends AbstractAction {
    private UndoManager undoManager;

    public UndoAction(UndoManager undoManager) {
                 this.undoManager=undoManager;
             }
             public void actionPerformed(ActionEvent evt ) {
                 try {
                     undoManager.undo();
                 }catch(CannotUndoException ex){
                     System.out.println("UNDO is empty");
                     JOptionPane.showMessageDialog(null, "UNDO is empty");
                 }
             }
 }