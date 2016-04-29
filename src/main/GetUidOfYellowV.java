package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;
/**
 * 获取获得数据的用户中的大V的ID，存放在Config/WeibosId_V.txt中
 * @author Administrator
 * params :文件名
 */
public class GetUidOfYellowV {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static String USERFILE = "\\UserInfo1.txt";
	public static void main(String args[]) throws IOException{
		File fr1 = new File(PATH+USERFILE);
		BufferedReader r1 = new BufferedReader(new FileReader(fr1));
		Set<String> v_uid = new HashSet<String>();
		String line = "";
		while((line = r1.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String uid = user.getString("id");
			int type = user.getInt("verifiedType");
			if(type==0){v_uid.add(uid);}
			/*if(type<0||type>7){continue;}
			v_uid.add(uid);*/
		}
		r1.close();
		File fw = new File(PATH+"ExpandID1_YV.txt");
		BufferedWriter w = new BufferedWriter(new FileWriter(fw));
		for(String id:v_uid){
			w.write(id+"\r\n");
		}
		w.flush();
		w.close();
	}
}
