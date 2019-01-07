import UIinterface.GUI;
import com.alee.laf.WebLookAndFeel;

public class Main {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WebLookAndFeel.install ();
                new GUI().setVisible(true);
            }
        });
    }
}
