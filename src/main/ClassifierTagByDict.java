package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.GetInfo;
import utils.Utils;
/**
 * @author Hannah
 *
 */
public class ClassifierTagByDict {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR400_Good\\";
	private static int OTHER_TYPE = 0;
	public static void main(String args[]) throws IOException{
		classiferTag("Config\\Dict_TagType.txt", "Feature_UserInfo\\UserTag.txt", "Feature_UserInfo\\Tag_type.txt","Feature_UserInfo\\Tag_typeUndefined.txt");

		classiferTag("Config\\Dict_TagType_Expand25.txt", "Feature_UserInfo\\UserTag.txt", "Feature_UserInfo\\Tag_type_Expand25.txt","Feature_UserInfo\\Tag_typeUndefined_Expand25.txt");



	}

	public static void classiferTag(String dict_file, String Tag_file, String res_file1, String res_file2) throws IOException {
		File f2 = new File(PATH+res_file1);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file2);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));

		Map<String,Set<String>> Tag_type = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(Tag_type,PATH+dict_file,"\t","##",1,0);
		File f0 = new File(PATH+Tag_file);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		String line;
		int i = 0;
		while((line = br0.readLine())!=null){
			String[] Tags = line.trim().split("\t");
			for(String tag : Tags){
				StringBuffer newline = new StringBuffer();
				newline.append(tag+"\t");
				boolean flag = false;
				for(Entry<String,Set<String>> type_words_entry : Tag_type.entrySet()){
					String type = type_words_entry.getKey();
					Set<String> type_words_set = type_words_entry.getValue();
					Set<String> keywords = Utils.checkwords(type_words_set, tag);
					if(keywords.size()>0){
						flag = true;
						newline.append(type);
						for(String keyword : keywords){newline.append("##"+keyword);}
						newline.append("\t");
					}	
				}
				if(!flag){
					newline.append(OTHER_TYPE+"##\t");
					//System.out.println(newline);
					bw3.write(newline.toString()+"\r\n");
				}else{
					i++;
				}
				bw.write(newline.toString()+"\r\n");
			}
		}
		br0.close();
		bw.flush();
		bw.close();
		bw3.flush();
		bw3.close();
		System.out.println(i);
	}

}
