import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class Main {
	public static void main(String []args) throws Exception{
		int fileSelected;
		do{
			JFileChooser fileChooser = new JFileChooser();
			System.out.println("Select a file: ");
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileSelected = fileChooser.showOpenDialog(fileChooser);
		  
			if(fileSelected == JFileChooser.APPROVE_OPTION){
				System.out.println(fileSelected);
				File fileToOpen = fileChooser.getSelectedFile();
				File currentDir = new File("./plugins");
			  	String[] plugins = currentDir.list();
			  	URL[] jars = new URL[plugins.length];
			  	boolean pluginExists = false;
			  	String pathToString;
			  	String pluginName;
		  		String pluginType;
			  	Hashtable<String, String> factoryNames = new Hashtable<String, String>();
			 
			  	String fileName = fileToOpen.getName();
			  	String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			  	System.out.println("You have chosen a " + fileType + " file.");  
			  	
			  	for(int i = 0; i < plugins.length; i++){
			  		jars[i] = (new File("./plugins/" + plugins[i])).toURI().toURL();
			  		pathToString = jars[i].toString();
			  		pluginName = pathToString.substring(pathToString.lastIndexOf('/') + 1).replace(".jar", "");
			  		pluginType = pluginName.substring(0, pluginName.length() - 7).toLowerCase();
			  		pluginExists = pluginName.toLowerCase().indexOf(fileType) !=-1? true: false;
		    	 
			  		factoryNames.put(pluginType, pluginName);
			  	}
		      
			  	if(pluginExists) {
			  		URLClassLoader ulc = new URLClassLoader(jars);
			  		IAbstractFactory factory = (IAbstractFactory) Class.forName(factoryNames.get(fileType).substring(0, 1).toLowerCase()
			  				+ factoryNames.get(fileType).substring(1) + "." + factoryNames.get(fileType), true, ulc)
			  				.getDeclaredConstructor().newInstance();
			  		
			  		IBuilder builder = factory.createBuilder();
			
			  		JFrame frame = factory.createHighlighter(fileToOpen);
			  		frame.setJMenuBar(builder.compile(fileToOpen));
			  		
			  		SwingUtilities.invokeLater(() -> {
			  			frame.setVisible(true);
			  		});
			  	}else {
			  		throw new Exception("Plugin doesn't exist.");
			  	}
			}else {
			  System.out.println("Please, select a file.");
			}
		}while(fileSelected != 0);
	}
}
