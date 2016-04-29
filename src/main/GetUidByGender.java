package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeSet;

import utils.GetInfo;
import utils.SaveInfo;

public class GetUidByGender {

	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException {
		Map<String, String> id_label_map = new HashMap<String, String>();
		GetInfo.getMap(PATH+"UserInfo0.txt",id_label_map,"id","gender");
		SaveInfo.mkdir(PATH+"GenderUserID");
		Set<String> male_id_set = new TreeSet<String>();
		Set<String> female_id_set = new TreeSet<String>();
		
		for(Entry<String, String> id_label : id_label_map.entrySet()){
			String id = id_label.getKey();
			String label = id_label.getValue();
			if(label.equals("f")){
				female_id_set.add(id);
			}else if(label.equals("m")){
				male_id_set.add(id);
			}else {
				System.out.println(id);
			}
		}
		System.out.println(female_id_set.size()+"\t"+male_id_set.size());
		SaveInfo.saveSet(PATH+"GenderUserID\\", "1.txt", female_id_set,false);
		SaveInfo.saveSet(PATH+"GenderUserID\\", "2.txt", male_id_set,false);
		
	}
}
