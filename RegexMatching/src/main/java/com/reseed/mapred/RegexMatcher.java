package com.reseed.mapred;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class RegexMatcher {

	public static void main(String []args) throws Exception{
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		// taking regex from command line and storing it in conf object
		// to make it available to all the map and reduce tasks.
	    conf.set("regex", otherArgs[2]);
		Job job = new Job(conf, "Regex-Matching");
	    job.setJarByClass(RegexMatcher.class);
	    // setting map and reducer classes
	    job.setMapperClass(RegexMapper.class);
	    job.setReducerClass(RegexReducer.class);
	    // setting input and out formats
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    // setting input and output directories
	    Path outDir = new Path(otherArgs[1]);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job, outDir);
	    // overwrite the file
	    FileSystem.get(conf).delete(outDir, true);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
