package Engine;

import Entities.ImageLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Textures {

    public static BufferedImage floor;
    public static BufferedImage wall;
    public static BufferedImage door;
    public static BufferedImage RedSun;
    
    public static void init() throws IOException{
        wall = ImageIO.read(new File("assets/images/textures/wall.png"));
		door = ImageLoader.loadImageAsRGB("assets/images/textures/door.png");
		floor = ImageLoader.loadImageAsRGB("assets/images/textures/floor.png");
        RedSun = ImageLoader.loadImageAsRGB("assets/images/entities/RedSunAngel.jpg");
    }
}