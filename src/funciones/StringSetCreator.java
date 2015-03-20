package funciones;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * DEVUELVE UN SET DE STRINGS dado un File o un inputStream. Cada elemente del
 * set es una linea del archivo que recibe como parámetro
 */
public class StringSetCreator {

	Set<String> stringSet;

	public StringSetCreator() {
	}

	public Set<String> getStringSet(File file) throws IOException {
		stringSet = new HashSet<String>();

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		armarStringSet(br);

		return stringSet;
	}

	public Set<String> getStringSet(InputStream in, String charset) throws IOException {
		stringSet = new HashSet<String>();

		BufferedReader br;

		if (charset != null && !charset.trim().isEmpty()) {
			br = new BufferedReader(new InputStreamReader(in, charset));
		} else {
			br = new BufferedReader(new InputStreamReader(in));
		}
		armarStringSet(br);

		return stringSet;
	}

	public Set<String> getStringSetSinCharset(InputStream in) throws IOException {
		return getStringSet(in, "");
	}

	private void armarStringSet(BufferedReader br) throws IOException {
		String linea = "";
		while ((linea = br.readLine()) != null) {
			stringSet.add(linea);
		}
		br.close();
	}

}
