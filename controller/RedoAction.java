package graphEditor.controller;
import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {
    private UndoManager undoManager;

    public RedoAction(UndoManager undoManager) {
        this.undoManager=undoManager;
    }
    public void actionPerformed(ActionEvent evt ) {
        try {
            undoManager.redo();
        }catch(CannotRedoException ex){
            System.out.println("REDO is empty");
            JOptionPane.showMessageDialog(null, "REDO is empty.");
        }
    }
}
