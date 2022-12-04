import org.apache.derby.tools.ij;

import java.io.IOException;

public class RunDbConsole {
    public static void main(String[] args) {

        try {
            // vytvoření databáze : connect 'jdbc:derby:ChatClientDb_skB;create=true';
            // zobrazení databáze :  select * from ChatMessages ;
            ij.main(args);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
