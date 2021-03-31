package javaFactory;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class JavaFactory implements IAbstractFactory{
	public IBuilder createBuilder(){
		return new JavaBuilder();
	}

	public JFrame createHighlighter(File file) throws IOException{
		return new JavaHighlighter(file);
	}
}
