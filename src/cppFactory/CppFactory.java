package cppFactory;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class CppFactory implements IAbstractFactory{
	public IBuilder createBuilder() {
		return new CppBuilder();
	}

	public JFrame createHighlighter(File file) throws IOException{
		return new CppHighlighter(file);
	}	
}
