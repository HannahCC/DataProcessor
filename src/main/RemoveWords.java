package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.RegexString;

public class RemoveWords {

	private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	/*private static String srcfile = "Feature_SRC\\Src_Description.txt.parsed";
	private static String resfile = "Feature_SRC\\Src_Description.txt.parsed.clr";*/
	private static String srcfile = "Feature_UserInfo\\User_Description.txt.parsed";
	private static String resfile = "Feature_UserInfo\\User_Description.txt.parsed.clr";
	public static void main(String[] args) throws IOException {

		//removeWordsByMatchPattern();
		removeWordsByPOS();
		
	}
	private static void removeWordsByPOS() throws IOException {
		File f1 = new File(PATH+srcfile);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		File f = new File(PATH+resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		Pattern p = Pattern.compile("(^w.*)|(^m.*)|(^x.*)");
		//Pattern p = Pattern.compile("(n.*)|(^g.*)");
		String line;
		boolean flag = false;
		while((line = br.readLine())!=null){
			StringBuffer newline = new StringBuffer();
			String[] items = line.split("\t");
			newline.append(items[0]+"\t");
			String[] item = items[1].split("\\s+");
			for(String it : item){
				String[] word_pos = it.split("/");
				if(word_pos.length!=2){
					System.out.println(it);
					continue;
				}
				Matcher m = p.matcher(word_pos[1]);
				if(!m.find()){
					flag = true;
					newline.append(word_pos[0]+" ");
				}
			}
			if(!flag)continue;
			flag=false;
			w.write(newline.toString().trim()+"\r\n");
		}
		br.close();
		w.close();
	}
	private static void removeWordsByMatchPattern() throws IOException {
		File f1 = new File(PATH+"Feature_SRC\\Src_Description.txt.parsed");
		BufferedReader br = new BufferedReader(new FileReader(f1));
		File f = new File(PATH+"Feature_SRC\\Src_Description.txt.parsed.clr");
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		Pattern p_word = Pattern.compile(RegexString.Regex_word);
		Pattern p_url = Pattern.compile(RegexString.Regex_url);
		Pattern p_email = Pattern.compile(RegexString.Regex_email);
		String line;
		while((line = br.readLine())!=null){
			StringBuffer newline = new StringBuffer();
			String[] items = line.split("\t");
			newline.append(items[0]+"\t");
			String[] item = items[1].split("\\s+");
			for(String it : item){
				//if(it.length()>1){
					Matcher m = p_word.matcher(it);
					if(m.find()){//匹配英文字母，汉字，去除标点符号
						m = p_url.matcher(it);
						if(!m.find()){//去除url
							m = p_email.matcher(it);
							if(!m.find()){//去除email
								newline.append(it+" ");
								continue;
							}
						}
					}
					//System.out.println(it);
				//}
			}
			w.write(newline.toString().trim()+"\r\n");
		}
		br.close();
		w.close();
	}
}