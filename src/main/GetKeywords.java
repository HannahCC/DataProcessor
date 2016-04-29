package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import utils.Utils;

public class GetKeywords {

	private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static int threshold = 0;
	public static void main(String[] args) throws IOException {
		File f1 = new File(PATH+"Feature_SRC\\Src_Description.txt.parsed.clr");
		BufferedReader br = new BufferedReader(new FileReader(f1));
		File f = new File(PATH+"Feature_SRC\\Src_Keywords.txt");
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		String line;
		while((line = br.readLine())!=null){
			Map<String,Integer> desc_words = new HashMap<String, Integer>();
			String[] items = line.split("\t");
			String[] item = items[1].split("\\s+");
			for(String it : item){
				Utils.putInMap(desc_words, it, 1);
			}
			StringBuffer keywords = new StringBuffer();
			keywords.append(items[0]+"\t");
			for(Entry<String, Integer> word_entry : desc_words.entrySet()){
				String word = word_entry.getKey();
				if(word_entry.getValue()>threshold){
					keywords.append(word+"##");
				}
			}
			w.write(keywords.toString().trim()+"\r\n");
		}
		br.close();
		w.close();
	}
}