package interfaces;

import java.io.File;
import java.io.IOException;

import javax.swing.JMenuBar;

public interface IBuilder {
	public JMenuBar compile(File file) throws IOException;
}
