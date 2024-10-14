package Engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Textures {

    public static BufferedImage floor;
    public static BufferedImage wall;
    public static BufferedImage door;
    public static BufferedImage RedSun;
    public static BufferedImage pan;
    
    public static void init() throws IOException{
        wall = ImageIO.read(new File("assets/images/textures/window.png"));
		door = ImageLoader.loadImageAsRGB("assets/images/textures/door.png");
		floor = ImageLoader.loadImageAsRGB("assets/images/textures/floor.png");
        RedSun = ImageLoader.loadImageAsRGB("assets/images/entities/RedSunAngel.jpg");
        pan = ImageLoader.loadImageAsRGB("assets/images/textures/pan.jpg");
    }
}