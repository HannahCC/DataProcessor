package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utils.SaveInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetUserAge{
	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String args[]) throws IOException{
		Map<String,String> user_age_map = new HashMap<String,String>(16 << 2,(float) 0.8);
		getMap("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR\\ExpandID0.txt", user_age_map);
		GetUserAgeMap_xml("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR_UserID_withAge.txt",user_age_map);
		GetUserAgeMap_json("D:\\Project_DataMinning\\DataSource\\3955含年龄用户数据",user_age_map);
		SaveInfo.saveMap("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR\\", "ExpandID0_Age.txt", user_age_map, "\t", false);
	}
	private static void getMap(String filename,Map<String,String> map) throws IOException{
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				map.put(line,"");
			}
		}
		br.close();
	}
	private static Map<String, String> GetUserAgeMap_xml(String filename,Map<String,String> user_age_map) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		String line = "";
		while((line = r.readLine())!=null){
			String[] items = line.split("\t");
			if(user_age_map.containsKey(items[0])){
				if(items[1].contains("年")){
					user_age_map.put(items[0], items[1]);
				}
			}
		}
		r.close();
		return user_age_map;
	}
	private static void GetUserAgeMap_json(String filename,
			Map<String, String> user_age_map) throws IOException {
		File dir = new File(filename);
		File[] filelist = dir.listFiles();
		for(File f:filelist){
			String id = f.getName().split("_")[1];
			if(user_age_map.containsKey(id)&&user_age_map.get(id).equals("")){
				BufferedReader r = new BufferedReader(new FileReader(f));
				String str = "";
				while((str = r.readLine())!=null){
					JSONArray jsonlist = JSONArray.fromObject(str);
					JSONObject json = jsonlist.getJSONObject(0);
					String birth = (String) json.getString("birthday");
					if(birth.contains("年")){
						user_age_map.put(id, birth);
					}
				}
				r.close();
			}
		}
	}

}
