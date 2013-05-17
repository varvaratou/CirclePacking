package circlepacking;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application {

	public static void main(String[] args) {
		Complex k = new Complex();
		final JFrame frame = new JFrame("CIRCLE PACKING");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BoxLayout boxLayout = 
				new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS); 
	    frame.setLayout(boxLayout);
		
		JPanel topPanel = new JPanel();
		final CirclePackApplet cpApplet = new CirclePackApplet(600, 600, k);
		topPanel.add(cpApplet);
		cpApplet.init();

		JPanel bottomPanel = new JPanel();
		final LayoutApplet lcApplet = new LayoutApplet(600, 600, k);
		bottomPanel.add(lcApplet);
		lcApplet.init();

		frame.add(topPanel);
		frame.add(bottomPanel);

		frame.setSize(1600, 650);
		frame.setVisible(true);
	}
}

