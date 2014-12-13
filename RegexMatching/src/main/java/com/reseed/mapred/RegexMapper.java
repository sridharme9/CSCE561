package com.reseed.mapred;
import com.reseed.automata.RegExp;
import com.reseed.automata.Automaton;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RegexMapper extends Mapper<LongWritable, Text, Text, Text>{
	
	private static final Text word = new Text();
	String regex;
	Automaton automata;	
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		// get regex stored in the job configuration object
		// before map task is initialized
		regex = context.getConfiguration().get("regex");
		// pre process the regular expression to support wild-cards
		String processRegex = processExp(regex);
		RegExp re = new RegExp(processRegex);
		// convert regular expression to automata
		automata = re.toAutomaton(false);
	}

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String data = value.toString();
		StringTokenizer tokens = new StringTokenizer(data, "\t");
		// get token and filelist from the Sub Column store
		String token = tokens.nextToken();
		String files = tokens.nextToken();
		// run the automaton on the token
		// if token is matched then the token and filepath is
		// emitted
		if(automata.run(token)){
			word.set(token);
			context.write(word, new Text(files));
		}
	}
	
	public String processExp(String regex){
		// check if regular expression is empty
		// This method replaces wild-cards with
		// theit equivalent notation
		if(regex.length() == 0){
			return "";
		}
		if(regex.contains("\\s")){
			regex = regex.replaceAll("\\\\s", " ");
		}
		if(regex.contains("\\w")){
			regex = regex.replaceAll("\\\\w", "[a-zA-Z]");
		}
		if(regex.contains("\\d")){
			regex= regex.replaceAll("\\\\d", "[0-9]");
		}
		return regex;
	}
	
}
