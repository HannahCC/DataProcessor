package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.json.JSONObject;
import utils.SaveInfo;

public class GetUserDescription {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static String[] ITEM = {"screenName","verifiedReason","description"};
	public static void main(String args[]) throws IOException{
		boolean isVUser = true;
		SaveInfo.mkdir(PATH+"Feature_UserInfo");
		getUserDescription(isVUser,"Feature_UserInfo\\VUser_Description.txt", "UserInfo0.txt","UserInfo1.txt");
	}
	private static void getUserDescription(boolean isVUser, String resf,
			String ... srcfs) throws IOException {
		File f = new File(PATH+resf);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for(String srcf : srcfs){
			File f0 = new File(PATH+srcf);
			BufferedReader br0 = new BufferedReader(new FileReader(f0));
			String line;
			while((line = br0.readLine())!=null){
				JSONObject user = JSONObject.fromObject(line);
				int type = user.getInt("verifiedType");
				if(isVUser){//是否只判断黄V或蓝V
					if(type<0||type>7){continue;}
					//if(type!=0){continue;}
				}
				String id = user.getString("id");
				StringBuffer item_str_buffer = new StringBuffer();
				for(String item : ITEM){item_str_buffer.append(user.getString(item)+";");}
				String item_str = item_str_buffer.toString();
				if(item_str.equals("")){continue;}
				bw.write(id+"\t"+item_str+"\r\n");
			}
			br0.close();
		}
		bw.flush();
		bw.close();
	}
}
