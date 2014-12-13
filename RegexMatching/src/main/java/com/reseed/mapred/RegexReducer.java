package com.reseed.mapred;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class RegexReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Context context) throws IOException,
			InterruptedException {
		Iterator<Text> iter = values.iterator();
		StringBuilder postings = new StringBuilder();
		// get all the file paths from the iterator
		// and appending them using comma as the delimiter
		while(iter.hasNext()){
			postings.append(iter.next().toString());
		}
		context.write(key, new Text(postings.toString()));
	}
}

