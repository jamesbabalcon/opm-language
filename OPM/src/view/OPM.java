package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import opmlanguage.SyntaxChecker;

@SuppressWarnings("serial")
public class OPM extends JPanel implements ActionListener {

	private JTextArea textArea, console;
	private JScrollPane scroll;
	private JPanel buttons;
	private JButton run, save, open;
	private String consoleText = "";
	
	public OPM() {
		setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		scroll = new JScrollPane();
		
		scroll.setViewportView(textArea);
		add(scroll, BorderLayout.CENTER);
		
		console = new JTextArea();
		console.setWrapStyleWord(true);
		console.setEditable(false);
		console.setPreferredSize(new Dimension(250, 600));
		add(console, BorderLayout.EAST);
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		open = new JButton("Open");
		open.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		run = new JButton("Run");
		run.addActionListener(this);
		
		buttons.add(open);
		buttons.add(save);
		buttons.add(run);
		
		add(buttons, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == run) {
			console.setText("");
			new SyntaxChecker(textArea.getText(), this);
		}
	}
	
	public void setConsoleText(String str) {
		consoleText += str + "\n";
		console.setText(consoleText);
	}
}
