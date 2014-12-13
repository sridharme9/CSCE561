package com.reseed.mapred;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class TokenMapper extends Mapper<LongWritable, Text, Text, Text>{
	
	private static final Text word = new Text();

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// cleaning the data to remove special characters
		String data = value.toString().replaceAll("[^a-zA-Z0-9\\s]+"," ");
		// tokenizing the contents of the line
		StringTokenizer tokens = new StringTokenizer(data.toLowerCase());
		FileSplit fileSplit = (FileSplit)context.getInputSplit();
		// get file path location on the HDFS
		String filePath = fileSplit.getPath().toString();
		Text location = new Text(filePath);
		while(tokens.hasMoreTokens()){
			// output token as key and file path as value
			word.set(tokens.nextToken().trim());
			context.write(word, location);
		}		
	}
}
