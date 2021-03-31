package javaFactory;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import interfaces.IBuilder;

public class JavaBuilder implements IBuilder{
	public JMenuItem compile(File file) throws IOException {
		JMenuItem compileButton = new JMenuItem(new AbstractAction("Compile"){
		    public void actionPerformed(ActionEvent e){	
				String filePath = file.toString();
				String compileArgs = JOptionPane.showInputDialog("Insert arguments: ");
		    	String command = "javac "+ compileArgs + " " + file;
				File dir = new File(filePath.substring(0, filePath.lastIndexOf("\\")));
		    	
				try {
					Process compile = Runtime.getRuntime().exec(command, null, dir);
					compile.waitFor();
					
					if(compile.exitValue() == 0) {
						JOptionPane.showMessageDialog(null, "Code Compiled.");
					}else {
						JOptionPane.showMessageDialog(null, "Error while compiling files.");
					}
				}catch(Exception error){
					JOptionPane.showMessageDialog(null, "Error: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
		    }
		});
	    return compileButton;
	}
}
