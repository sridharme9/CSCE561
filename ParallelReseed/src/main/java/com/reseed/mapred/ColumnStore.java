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

public class ColumnStore {

	public static void main(String []args) throws Exception{
		// creating new job configuration shared accros all the nodes.
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
	    // creating job by passing configuration
		Job job = new Job(conf, "Sub-ColumnStore");
		// setting main driver class for the job
	    job.setJarByClass(ColumnStore.class);
	    // setting MapperClass, ReducerClass and CombinerClass for Hadoop framework
	    job.setMapperClass(TokenMapper.class);
	    job.setReducerClass(TokenReducer.class);
	    job.setCombinerClass(TokenReducer.class);
	    // Setting input format class. we are using text files. Hence text input format class is used
	    job.setInputFormatClass(TextInputFormat.class);
	    // setting how key value should be stored on the disk at the end of reduce step
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    //setting number of reducers
	    job.setNumReduceTasks(Integer.parseInt(otherArgs[2]));
	    // setting output and input directory
	    Path outDir = new Path(otherArgs[1]);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job, outDir);
	    
	    FileSystem.get(conf).delete(outDir, true);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
