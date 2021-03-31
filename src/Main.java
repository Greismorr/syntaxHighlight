import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class Main {
	public static void main(String []args) throws Exception{
		int fileSelected;
		
		while(true){
	  		JOptionPane.showMessageDialog(null, "Select a file.");
	  		
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			fileSelected = fileChooser.showOpenDialog(fileChooser);
		  
			if(fileSelected == JFileChooser.APPROVE_OPTION){
				System.out.println(fileSelected);
				File fileToOpen = fileChooser.getSelectedFile();
				File currentDir = new File("./plugins");
			  	String[] plugins = currentDir.list();
			  	URL[] jars = new URL[plugins.length];
			  	String pathToString;
			  	String pluginName;
		  		String pluginType;
		  		Thread mainThread = Thread.currentThread();
			  	Hashtable<String, String> factoryNames = new Hashtable<String, String>();
			 
			  	String fileName = fileToOpen.getName();
			  	String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			  	
			  	for(int i = 0; i < plugins.length; i++){
			  		jars[i] = (new File("./plugins/" + plugins[i])).toURI().toURL();
			  		pathToString = jars[i].toString();
			  		pluginName = pathToString.substring(pathToString.lastIndexOf('/') + 1).replace(".jar", "");
			  		pluginType = pluginName.substring(0, pluginName.length() - 7).toLowerCase();
		    	 
			  		factoryNames.put(pluginType, pluginName);
			  	}
			  	
			  	if(factoryNames.containsKey(fileType)) {
			  		URLClassLoader ulc = new URLClassLoader(jars);
			  		IAbstractFactory factory = (IAbstractFactory) Class.forName(factoryNames.get(fileType).substring(0, 1).toLowerCase()
			  				+ factoryNames.get(fileType).substring(1) + "." + factoryNames.get(fileType), true, ulc)
			  				.getDeclaredConstructor().newInstance();
			  		
			  		IBuilder builder = factory.createBuilder();
			  		JFrame frame = factory.createHighlighter(fileToOpen);
			  		frame.setJMenuBar(builder.compile(fileToOpen));
			  		
			  		frame.addWindowListener(new WindowAdapter() {
			  		    public void windowClosing(WindowEvent e) {
					  		synchronized(mainThread){
					  			System.out.println(mainThread.getName());
					  			mainThread.notifyAll();
					  		}
			  		    }
			  		});
			
			  		SwingUtilities.invokeLater(() -> {
			  			frame.setVisible(true);
			  		});
			  		
			  		synchronized(mainThread){
			  			mainThread.wait();			
			  		}
			  		
			  		System.out.println(fileName);
			  	}else {
			  		JOptionPane.showMessageDialog(null, "Plugin doesn't exists.");
			  	}
			}else {
				break;
			}
		}
	}
}
