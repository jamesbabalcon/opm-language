package opmlanguage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OPMLanguage {
	
	public OPMLanguage() {
//		new SyntaxChecker(loadFile());
	}
	
	public String loadFile() {
		String text = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("res/file.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    text = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("File loaded");

		return text;
	}
}
