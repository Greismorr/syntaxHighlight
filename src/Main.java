import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class Main {
	public static URL[] loadJars(Hashtable<String, String> factoryNames){
		File pluginDir = new File("./plugins");
	  	String[] plugins = pluginDir.list();
	  	URL[] jars = new URL[plugins.length];
	  	
	  	for(int i = 0; i < plugins.length; i++){
	  		try{		  		
				jars[i] = (new File("./plugins/" + plugins[i])).toURI().toURL();
		  		String pathToString = jars[i].toString();
		  		String pluginName = pathToString.substring(pathToString.lastIndexOf('/') + 1).replace(".jar", "");
		  		String pluginType = pluginName.substring(0, pluginName.length() - 7).toLowerCase();
		  		
		  		factoryNames.put(pluginType, pluginName);
			}catch(MalformedURLException error){
				error.printStackTrace();
			}
	  	}
	  	
	  	return jars;
	}
	
	public static void removeCurrentCompileButton(JMenu actionsMenu){
		for(int i = 0; i < actionsMenu.getItemCount(); i++) {
			if(actionsMenu.getItem(i).getText() == "Compile") {
				actionsMenu.remove(i);
			}
		}
	}
	
	public static String firstLetterToLowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	static JFrame mainFrame = new JFrame();
	
	public static void main(String []args) throws Exception{
	    RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
	    RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		JPanel contentPane = new JPanel(new BorderLayout());
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		
		JMenuItem openFileButton = new JMenuItem(new AbstractAction("Open File"){
		    public void actionPerformed(ActionEvent e){	
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int fileSelected = fileChooser.showOpenDialog(fileChooser);
			  
				if(fileSelected == JFileChooser.APPROVE_OPTION){
					File fileToOpen = fileChooser.getSelectedFile();
				  	Hashtable<String, String> factoryNames = new Hashtable<String, String>();
				 
				  	String fileName = fileToOpen.getName();
				  	String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
				  	
				  	URL[] jars = loadJars(factoryNames);
				  	
				  	if(factoryNames.containsKey(fileType)) {
				  		URLClassLoader classLoader = new URLClassLoader(jars);
				  		IAbstractFactory factorySingleton;
				  		
						try{
							String factoryName = factoryNames.get(fileType);
							String className = firstLetterToLowerCase(factoryName) + "." + factoryName;
							Class metaFactory = Class.forName(className, true, classLoader);
							Method getInstanceMethod = metaFactory.getDeclaredMethod("getInstance");
							
							factorySingleton = (IAbstractFactory)getInstanceMethod.invoke(metaFactory);
							
							IBuilder builder = factorySingleton.createBuilder();
							
							mainFrame.dispose();
							removeCurrentCompileButton(actionsMenu);
							
							mainFrame = factorySingleton.createHighlighter(fileToOpen);
							actionsMenu.add(builder.compile(fileToOpen));
							mainFrame.setJMenuBar(menuBar);
							
					  		SwingUtilities.invokeLater(() -> {
					  			mainFrame.setVisible(true);
					  		});
						}catch (Exception e1){
							e1.printStackTrace();
						} 
				  	}else{
				  		JOptionPane.showMessageDialog(null, "Plugin doesn't exists.");
				  	}
				}
		    }
		});
		
		actionsMenu.add(openFileButton);
	    contentPane.add(scrollPane);
	    menuBar.add(actionsMenu);
	    mainFrame.setJMenuBar(menuBar);
	    mainFrame.setContentPane(contentPane);
	    mainFrame.setTitle("Syntax Highlight");
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.pack();
	    mainFrame.setLocationRelativeTo(null);
	    
  		SwingUtilities.invokeLater(() -> {
  			mainFrame.setVisible(true);
  		});
	}
}
