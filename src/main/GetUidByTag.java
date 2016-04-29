package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;
import utils.GetInfo;
import utils.SaveInfo;

public class GetUidByTag {

	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException {
		Set<String> uid_set = new HashSet<String>();
		GetInfo.getSet(PATH + "GenderUserID\\2.txt", uid_set);
		
		File srcf = new File(PATH+"UserInfo0.txt");
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		String line = "";
		while((line = r.readLine())!=null){
			JSONObject json = JSONObject.fromObject(line);
			String id = json.getString("id");
			int number = json.getJSONObject("tags").getInt("total_number");
			if (number < 5){
				System.out.println(id+":"+json.getJSONObject("tags").getString("tags"));
				uid_set.remove(id);
			}
		}
		r.close();
		SaveInfo.saveSet(PATH, "GenderUserID\\2_new.txt", uid_set, false);
	}
}
