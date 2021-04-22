package javaFactory;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

import abstractClasses.Singleton;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;

public class JavaFactory extends Singleton implements IAbstractFactory{
	public static JavaFactory instance = null;
	
	private JavaFactory(){
		super();
	}
	
	public static synchronized JavaFactory getInstance() {
		if(instance == null) {
			instance = new JavaFactory();
		}
		return instance;
	}
	
	public IBuilder createBuilder(){
		return new JavaBuilder();
	}

	public JFrame createHighlighter(File file) throws IOException{
		return new JavaHighlighter(file);
	}
}
