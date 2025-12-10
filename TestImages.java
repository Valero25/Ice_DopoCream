import presentation.ImageLoader;
import java.awt.Image;
import javax.swing.ImageIcon;

public class TestImages {
    public static void main(String[] args) {
        System.out.println("Testing Image Loading...");
        ImageLoader loader = new ImageLoader();

        String[] types = { "CAMPFIRE", "HOT_TILE", "ICE", "BANANA" };

        for (String type : types) {
            System.out.println("Loading " + type + "...");
            Image img = loader.getImage(type, 32, 32);
            if (img != null) {
                System.out.println("  SUCCESS: " + type + " loaded. " + img);
            } else {
                System.out.println("  FAILURE: " + type + " returned null.");
            }
        }
    }
}
