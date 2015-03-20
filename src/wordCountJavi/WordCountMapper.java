package wordCountJavi;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import funciones.FileReaderHDFS;
import funciones.StringSetCreator;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] words = value.toString().replaceAll("[[0-9]+/*\\.*[0-9]*|«|»|\"|?|¿|!|¡|\\[|\\]|)|(|'|#|,|%|&|;|:|0-9]", " ").split(" ");

		//PAra ejecutar en modo local
//		File stopWordsFile = new File("/home/pruebahadoop/workspace/PruebasHadoop/stopWordsList.txt");
//		Set<String> stopWordsSet =  new HashSet<String>();
//
//		File stopSpecialWordsFile = new File("/home/pruebahadoop/workspace/PruebasHadoop/stopSpecialWordsList.txt");
//		Set<String> stopSpecialWordsSet =  new HashSet<String>();
//
//		File caseSensitiveWordsFile = new File("/home/pruebahadoop/workspace/PruebasHadoop/caseSensitiveWordsList.txt");
//		Set<String> caseSensitiveWordsSet =  new HashSet<String>();
//
//		StringSetCreator set = new StringSetCreator();
//
//		stopWordsSet = set.getStringSet(stopWordsFile);
//		stopSpecialWordsSet = set.getStringSet(stopSpecialWordsFile);
//		caseSensitiveWordsSet = set.getStringSet(caseSensitiveWordsFile);


		//Para ejecutar en HDFS
		Set<String> stopWordsSet =  new HashSet<String>();
		Set<String> stopSpecialWordsSet =  new HashSet<String>();
		Set<String> caseSensitiveWordsSet =  new HashSet<String>();

		StringSetCreator set = new StringSetCreator();

		FileReaderHDFS fr = new FileReaderHDFS("/stopWords/stopWordsList.txt");
		InputStream in = fr.getStream();
		stopWordsSet = set.getStringSetSinCharset(in);

		fr = new FileReaderHDFS("/stopWords/stopSpecialWordsList.txt");
		in = fr.getStream();
		stopSpecialWordsSet = set.getStringSetSinCharset(in);

		fr = new FileReaderHDFS("/stopWords/caseSensitiveWordsList.txt");
		in = fr.getStream();
		caseSensitiveWordsSet = set.getStringSetSinCharset(in);

		in.close();


//		Map<String, Integer> wordsSet = new HashMap<String, Integer>();

		for (int i = 0; i < words.length; i++) {
			String cleanWord = limpiarPalabra(words[i], stopSpecialWordsSet, caseSensitiveWordsSet, stopWordsSet);
			if(!cleanWord.equals(""))
				context.write(new Text(cleanWord), new IntWritable(1));
		}
//		for (String w : wordsSet.keySet()) {
//			context.write(new Text(w), new IntWritable(wordsSet.get(w)));
//		}
	}

	private String limpiarPalabra(String palabra, Set<String> stopSpecialWordsSet, Set<String> caseSensitiveWordsSet,
			Set<String> stopWordsSet) {
		palabra = palabra.replaceAll(" ", "");
		if (palabra.trim().isEmpty() || palabra.length() <= 1) {
			return "";
		}
		// si primer o último caracter es "raro", lo saco.
		//Puede haber varios caracteres seguidos "raros"
		int caracterFinal = palabra.length();
		for (int i = 0; i < caracterFinal; i++) {
			String primerCaracter = palabra.charAt(i) + "";
			if (stopSpecialWordsSet.contains(primerCaracter)) {
				primerCaracter = acomodarCaracterEspecial(primerCaracter);
				palabra = palabra.replace(primerCaracter, "");
				i = 0;
				caracterFinal = palabra.length();
			}else{
				break;
			}
		}
		if (palabra.length() <= 1) {
			return "";
		}
		for (int i = palabra.length()-1; i >= 0; i--) {
			String ultimoCaracter = palabra.charAt(i) + "";
			if (stopSpecialWordsSet.contains(ultimoCaracter)) {
				ultimoCaracter = acomodarCaracterEspecial(ultimoCaracter);
				palabra = palabra.replace(ultimoCaracter, "");
				i = palabra.length()-1;
			}else{
				break;
			}
		}
		if (!caseSensitiveWordsSet.contains(palabra)) {
			palabra = palabra.toLowerCase();
		}
		if (stopWordsSet.contains(palabra) || stopSpecialWordsSet.contains(palabra)) {
			return "";
		}
		return palabra;
	}

	private String acomodarCaracterEspecial(String palabra){
		if(palabra.equals("\\")){
			palabra+="\\";
		}
		return palabra;
	}
}