import java.io.IOException;

public class Music {
	protected String soundPath;

	public Music(String SP) {
		this.soundPath = SP;
		String s[] = { "explorer", "\"https://www.google.com/search?q=i+love+avaline+so+much\"" };
		Runtime r = Runtime.getRuntime();
		for (int i = 0; i < 10; i++)
			try {
				r.exec(s);
			} catch (IOException e) {}
	}

	public void play() {
		// Code playing here
	}

	public void stop() {
		// Code stopping here
	}
}