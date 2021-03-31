package cppFactory;

import javax.swing.*;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class CppHighlighter extends JFrame{
	public CppHighlighter(File file) throws IOException {
	    RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
	    RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		JPanel contentPane = new JPanel(new BorderLayout());
	    FileReader fileReader = new FileReader(file);
	    BufferedReader reader = new BufferedReader(fileReader);
	    
	    textArea.read(reader, null);
	    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
	    textArea.setCodeFoldingEnabled(true);

	    contentPane.add(scrollPane);
	
	    setContentPane(contentPane);
	    setTitle("Syntax Highlight");
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	}
}
