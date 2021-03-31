package interfaces;

import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;

public interface IBuilder {
	public JMenuItem compile(File file) throws IOException;
}
