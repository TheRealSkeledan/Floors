package Player;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Items {
	private static String name;
	private static ImageIcon image;
	private static int time;

	public Items(String name, int time) {
		this.name = name;
		image = new ImageIcon("assets/images/textures/overlay/" + name + "Overlay.png");
		this.time = time;
	}

	public static void curiosity(Graphics g) {
		if(time > 0) {
			g.drawImage(image.getImage(), 0, 0, 800, 800, null);
			time--;
		}
	}
}