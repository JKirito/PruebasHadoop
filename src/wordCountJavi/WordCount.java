package wordCountJavi;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long init = new Date().getTime();
		// args = new String[2];
		// args[0] =
		// "/home/pruebahadoop/Documentos/DescargasPeriodicos/Procesado/LaNacion/archivoPorAño";
		// args[0] = "/home/pruebahadoop/Documentos/DataSets/otros/input";
		// args[1] = "/home/pruebahadoop/Documentos/DataSets/otros/output";
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "wordCount");
		job.setJarByClass(WordCount.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(WordCountMapper.class);
		job.setCombinerClass(WordCountReducer.class);
		job.setReducerClass(WordCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		long fin = new Date().getTime();
		if (job.waitForCompletion(true)) {
			System.out.println("Tardó estos segundos: " + (fin - init));
		}
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
