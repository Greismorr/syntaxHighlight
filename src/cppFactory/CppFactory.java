package cppFactory;

import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

import abstractClasses.Singleton;
import interfaces.IAbstractFactory;
import interfaces.IBuilder;
import javaFactory.JavaFactory;

public class CppFactory extends Singleton implements IAbstractFactory{
	public static CppFactory instance = null;
	
	private CppFactory() {
		super();
	}
	
	public static synchronized CppFactory getInstance() {
		if(instance == null) {
			instance = new CppFactory();
		}
		return instance;
	}
	
	public IBuilder createBuilder() {
		return new CppBuilder();
	}

	public JFrame createHighlighter(File file) throws IOException{
		return new CppHighlighter(file);
	}	
}
