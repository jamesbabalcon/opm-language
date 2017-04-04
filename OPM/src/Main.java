import javax.swing.JFrame;

import opmlanguage.OPMLanguage;
import view.OPM;

public class Main {
	public static void main(String[] args) {
//		new OPMLanguage();
		
		OPM opmPanel = new OPM();
		JFrame frame = new JFrame("OPM Language");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(opmPanel);
		frame.setVisible(true);
	}
}
