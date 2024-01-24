import controllers.LoginSystem;

import java.io.FileNotFoundException;

/**
 * The class that contains the main method
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        LoginSystem ls = new LoginSystem();
        ls.run();
    }
}
