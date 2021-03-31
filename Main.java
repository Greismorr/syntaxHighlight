import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
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
	public static void main(String []args) throws Exception{
		JFrame initialFrame = new JFrame();
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
					System.out.println(fileSelected);
					File fileToOpen = fileChooser.getSelectedFile();
					File pluginDir = new File("./plugins");
				  	String[] plugins = pluginDir.list();
				  	URL[] jars = new URL[plugins.length];
				  	Hashtable<String, String> factoryNames = new Hashtable<String, String>();
				 
				  	String fileName = fileToOpen.getName();
				  	String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
				  	
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
				  	
				  	if(factoryNames.containsKey(fileType)) {
				  		URLClassLoader ulc = new URLClassLoader(jars);
				  		IAbstractFactory factory;
				  		
						try{
							factory = (IAbstractFactory) Class.forName(factoryNames.get(fileType).substring(0, 1).toLowerCase()
									+ factoryNames.get(fileType).substring(1) + "." + factoryNames.get(fileType), true, ulc)
									.getDeclaredConstructor().newInstance();
							IBuilder builder = factory.createBuilder();
							
							initialFrame.dispose();
							
							JFrame SyntaxFrame = factory.createHighlighter(fileToOpen);
							
							if(actionsMenu.getItemCount() > 1) {
								actionsMenu.remove(1);
							}
							
							actionsMenu.add(builder.compile(fileToOpen));
							SyntaxFrame.setJMenuBar(menuBar);
							
					  		SwingUtilities.invokeLater(() -> {
					  			SyntaxFrame.setVisible(true);
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
	    initialFrame.setJMenuBar(menuBar);
	    initialFrame.setContentPane(contentPane);
	    initialFrame.setTitle("Syntax Highlight");
	    initialFrame.setDefaultCloseOperation(3);
	    initialFrame.pack();
	    initialFrame.setLocationRelativeTo(null);
	    
  		SwingUtilities.invokeLater(() -> {
  			initialFrame.setVisible(true);
  		});
	}
}
