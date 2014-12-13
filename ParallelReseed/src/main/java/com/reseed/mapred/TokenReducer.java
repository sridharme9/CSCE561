package com.reseed.mapred;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TokenReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Context context) throws IOException,
			InterruptedException {
		// to iterate over the file list
		Iterator<Text> iter = values.iterator();
		// to check for duplicate file path in file list
		HashMap<String, String> postingList = new HashMap<String, String>();
		StringBuilder postings = new StringBuilder();
		String currentValue;
		while(iter.hasNext()){
			currentValue = iter.next().toString();
			// if file path is already present, we will discard it
			if(!postingList.containsKey(currentValue)){
				postingList.put(currentValue, "");
			}
		}
		// collect all the files and append filepath by adding , as the delimiter
		for(String path : postingList.keySet()){
			postings.append(path);
			postings.append(",");
		}
		// writing to the HDFS
		context.write(key, new Text(postings.toString()));
	}
}