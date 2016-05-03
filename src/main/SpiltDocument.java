package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import utils.GetInfo;
public class SpiltDocument {
	private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	//private static String PATH2 = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	public static void main(String[] args) throws IOException {
		Set<String> ids = new HashSet<String>(0xff);
		GetInfo.getSet(PATH+"UserID_Gender\\1_newest_unequal.txt", ids);
		spiltDocument(ids,"Line//1635_a0_full.txt","Line//1635_a0_full_ordered.txt",false);
		ids.clear();
		GetInfo.getSet(PATH+"UserID_Gender\\2_newest_unequal.txt", ids);
		spiltDocument(ids,"Line//1635_a0_full.txt","Line//1635_a0_full_ordered.txt",true);
	}
	private static void spiltDocument(Set<String> ids, String src,
			String res, boolean isAppend) throws IOException {
		File f = new File(PATH+src);
		File f1 = new File(PATH+res);
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		BufferedWriter bw = new BufferedWriter(new FileWriter(f1,isAppend));
		String line;
		while((line = br.readLine())!=null){
			String id = line.split("\t",2)[0];
			if(ids.contains(id)){bw.write(line+"\r\n");}
		}
		bw.flush();bw.close();
		br.close();
	}
}
