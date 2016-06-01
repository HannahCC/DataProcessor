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

public class GetUserWeibo {
	private static final String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static final String PATH2 = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	public static void main(String args[]) throws IOException {
		Set<String> ids = new HashSet<String>(2000);
		File weiboFile = null;
		
		/*GetInfo.getSet(PATH+"UserID_Gender\\all_newest_unequal.txt", ids);
		weiboFile = new File(PATH2+"Vector\\UserIdText\\UserIdText_full.txt");
		getWeiboConforW2V(ids, weiboFile);*/

		for(int i=0;i<4;i++){
			for(int j=1;j<4;j++){
				ids.clear();
				ids = new HashSet<String>(600000);
				GetInfo.getSet(PATH2+i+"//"+j+"_1_trainingid.txt", ids);
				GetInfo.getSet(PATH2+i+"//"+j+"_2_trainingid.txt", ids);
				weiboFile = new File(PATH2+"Vector\\UserIdText\\"+j+"_"+i+"_UserIdText_train.txt");
				getWeiboConforW2V(ids, weiboFile);
			}
		}
	}
	private static void getWeiboConforW2V(Set<String> ids, File weiboFile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(weiboFile));
		BufferedReader br = null;
		File idWeibFile = null;
		String line = null;
		for(String id : ids){
			idWeibFile = new File(PATH+"Weibos0_Text\\"+id+".txt");
			if(!idWeibFile.exists())continue;
			br = new BufferedReader(new FileReader(idWeibFile));
			while(null!=(line = br.readLine())){
				bw.write(line+"\r\n");
			}
			br.close();
		}
		bw.flush();
		bw.close();
	}
}
