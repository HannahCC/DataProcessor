package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;
import utils.GetInfo;
import utils.SaveInfo;
public class GetUidOfFri {
	/**
	 * 获取ids用户集的关注/粉丝用户集
	 * @param args
	 * @throws IOException
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";//ext1000_Mute_GenderPre
	public static void main(String args[]) throws IOException{

		small();
		//huge();
	}
	@SuppressWarnings("unchecked")
	private static void huge() throws IOException {

		/*List<BufferedWriter> bw_list = init();
		Set<String> ids = new HashSet<String>();
		GetInfo.getSet(PATH+"ExpandID1.txt", ids);

		File f1 = new File(PATH+"UidInfo_follows1.txt");
		BufferedReader b1 = new BufferedReader(new FileReader(f1));
		File f2 = new File(PATH+"UidInfo_friends1.txt");
		BufferedReader b2 = new BufferedReader(new FileReader(f2));
		String line = "";
		JSONObject json = null;
		String id = "";
		while((line = b1.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(ids.contains(id)){
				save(bw_list, (List<String>) json.get("uids"));
			}
		}
		b1.close();
		while((line = b2.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(ids.contains(id)){
				save(bw_list, (List<String>) json.get("uids"));
			}
		}
		b2.close();
		clear(bw_list);*/
		deduplicate();
	}
	private static void save(List<BufferedWriter> bw_list, List<String> list) throws IOException {
		for(String id : list){
			long id_int = Long.parseLong(id);
			id_int = id_int ^ (id_int >>> 16);
			int index = (int) (id_int & 15);
			bw_list.get(index).write(id+"\r\n");
		}
	}
	private static List<BufferedWriter> init() throws IOException {
		List<BufferedWriter> bw_list = new ArrayList<BufferedWriter>();
		for(int i=0;i<16;i++){
			File f = new File(PATH+"ExpandID2_"+i+".txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw_list.add(bw);
		}
		return bw_list;
	}
	private static void clear(List<BufferedWriter> bw_list) throws IOException {
		for(BufferedWriter bw : bw_list){
			bw.flush();
			bw.close();
		}
	}
	private static void deduplicate() throws IOException {
		for(int i=0;i<16;i++){
			Set<String> key_Set = new HashSet<String>(10000000);
			File f = new File(PATH+"ExpandID2_"+i+".txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while((line = br.readLine())!=null){
				if(line.equals(""))continue;
				if(!key_Set.contains(line)){
					key_Set.add(line);
				}
			}
			br.close();
			SaveInfo.saveSet(PATH, "ExpandID2_"+i+".txt.c", key_Set, false);
		}
	}
	private static void small() throws IOException {

		Set<String> ids = new HashSet<String>();
		GetInfo.getSet(PATH+"ExpandID0.txt", ids);
		Set<String> uid_set = new HashSet<String>(100000000);
		//GetInfo.getSetFromList(PATH+"UidInfo_follows0.txt", uid_set, "uids", ids, "id");
		GetInfo.getSetFromList(PATH+"UidInfo_friends0.txt", uid_set, "uids", ids, "id");
		SaveInfo.saveSet(PATH, "ExpandID1_fri.txt", uid_set, false);
	}
}
