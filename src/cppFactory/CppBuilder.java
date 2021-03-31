package cppFactory;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import interfaces.IBuilder;

public class CppBuilder implements IBuilder{
	public JMenuBar compile(File file) throws IOException{
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		
		JMenuItem compileButton = new JMenuItem(new AbstractAction("Compile"){
		    public void actionPerformed(ActionEvent e){	
				String filePath = file.toString();
				String command = "g++ -o "+ filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.indexOf(".")) + " " + file;
				File dir = new File(filePath.substring(0, filePath.lastIndexOf("\\")));
				
				try {
					Process compile = Runtime.getRuntime().exec(command, null, dir);
					compile.waitFor();
				}catch (IOException | InterruptedException error){
					error.printStackTrace();
				}
		    }
		});
		
	    menuBar.add(actionsMenu);
	    actionsMenu.add(compileButton);
		
	    return menuBar;
	}
}
