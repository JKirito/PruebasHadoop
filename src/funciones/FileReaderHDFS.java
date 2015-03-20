package funciones;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileReaderHDFS {
	FileSystem fs;
	String pathFile;
	private static final String uriHDFSDefault = "hdfs://localhost";

	/**
	 *
	 * @param uriHDFS
	 *            : example: "hdfs://localhost"
	 * @param pathFile
	 *            example: "/output/abc.txt"
	 * @throws IOException
	 */
	public FileReaderHDFS(String uriHDFS, String pathFile) throws IOException {
		this.pathFile = pathFile;
		Configuration conf = new Configuration();

		try {
			fs = FileSystem.get(URI.create(uriHDFS), conf);
		} catch (NullPointerException | IllegalArgumentException npe) {
			throw npe;
		} catch (IOException e) {
			throw e;
		}
	}

	public FileReaderHDFS(String pathFile) throws IOException {
		this(uriHDFSDefault, pathFile);
	}

	public InputStream getStream() throws IOException {
		return fs.open(new Path(pathFile));
	}

}
