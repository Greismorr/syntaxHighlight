package interfaces;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

public interface IAbstractFactory {
	public IBuilder createBuilder();
	public JFrame createHighlighter(File file) throws IOException;
}
