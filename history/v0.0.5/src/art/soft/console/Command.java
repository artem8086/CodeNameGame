package art.soft.console;

import art.soft.Loader;
import java.util.ArrayList;

/**
 *
 * @author Artem
 */
public abstract class Command {

    public String name;

    protected Console console;

    public Command(Console console) {
        this.console = console;
    }

    public abstract ArrayList<String> getKeys(String[] args);

    public abstract boolean runCommand(String[] args);

    public abstract String getHelp();
}
