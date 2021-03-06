package dmillerw.lore.common.lore.data;

/**
 * @author dmillerw
 */
public class Lore {

    public static class CommandWrapper {

        public String[] pickup = new String[0];
    }

    public int page;
    public int dimension = Integer.MAX_VALUE; // Default
    public String title = "";
    public String body = "";
    public String sound = "";
    public CommandWrapper commands = new CommandWrapper();
    public boolean autoplay = false;
    public boolean notify = true;

    public boolean hasSound() {
        return !sound.isEmpty();
    }

    public boolean validDimension(int dimension) {
        return this.dimension == dimension;
    }
}
