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
	//private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static String PATH = "/home/zps/sina2223/";
	private static String PATH2 = "/home/zps/some-tool/r2v/";
	public static void main(String[] args) throws IOException {
		Set<String> ids = new HashSet<String>(0xff);
		GetInfo.getSet(PATH+"UserID_Gender/2_newest_unequal.txt", ids);
		//spiltDocument(ids,PATH+"Line/1635_UserIdFriends_full.txt",PATH+"Line/1635_UserIdFriends_full_ordered.txt",false);
		spiltDocument(ids,PATH2+"4_UserIdFriends_train.txt",PATH2+"4_UserIdFriends_train_ordered21.txt",false);
		ids.clear();
		GetInfo.getSet(PATH+"UserID_Gender/1_newest_unequal.txt", ids);
		//spiltDocument(ids,PATH+"Line/1635_UserIdFriends_full.txt",PATH+"Line/1635_UserIdFriends_full_ordered.txt",true);
		spiltDocument(ids,PATH2+"4_UserIdFriends_train.txt",PATH2+"4_UserIdFriends_train_ordered21.txt",true);
	}
	private static void spiltDocument(Set<String> ids, String src,
			String res, boolean isAppend) throws IOException {
		File f = new File(src);
		File f1 = new File(res);
		
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
