package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.json.JSONObject;
import utils.GetInfo;
public class GetUserFriends {
	/**
	 * 获取ids用户集的关注/粉丝用户集
	 * @param args
	 * @throws IOException
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";//ext1000_Mute_GenderPre
	public static void main(String args[]) throws IOException{

		/*Set<String> ids = new HashSet<String>();
		GetInfo.getSet(PATH+"UserID_Gender\\1_newest_unequal.txt", ids);
		GetInfo.getSet(PATH+"UserID_Gender\\2_newest_unequal.txt", ids);
		 */

		Set<String> ids = new HashSet<String>();
		GetInfo.getSet(PATH+"ExpandID0.txt", ids);
		GetInfo.getSet(PATH+"ExpandID1.txt", ids);

		getUserFriends(ids,"Feature_UserInfo\\UserIdFriends.txt",false,"UidInfo_friends0.txt");
		//getUserFriends(ids,"Feature_UserInfo\\UserIdFriends1.txt",true,"UidInfo_friends1.txt");

		/*Set<String> vids = new HashSet<String>();
		GetInfo.getSet(PATH+"Config\\Dict_VFri.txt", vids);
		getUserFriends(ids,vids,"Feature_UserInfo\\UserIdVFriends.txt","UidInfo_friends0.txt");*/

		System.out.println(ids.size());

	}
	@SuppressWarnings("unchecked")
	private static void getUserFriends(Set<String> ids, Set<String> vids, boolean isAppend, String resFile, String friUidFile) throws IOException {
		File rf = new File(PATH+resFile);
		BufferedWriter w = new BufferedWriter(new FileWriter(rf,isAppend));

		File f = new File(PATH+friUidFile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";
		JSONObject json = null;
		List<String> lists = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(ids.contains(id)){//如果是我们要获取的ID
				w.write(id+"\t");
				lists = (List<String>) json.get("uids");
				for(String fid:lists){
					if(vids.contains(id)){
						w.write(fid+"\t");
					}
				}
				w.write("\r\n");
				ids.remove(id);
			}
		}
		b.close();	

		w.flush();
		w.close();

	}
	@SuppressWarnings("unchecked")
	private static void getUserFriends(Set<String> ids, String resFile, boolean isAppend, String ... friUidFiles) throws IOException {
		File rf = new File(PATH+resFile);
		BufferedWriter w = new BufferedWriter(new FileWriter(rf,isAppend));
		for(String friUidFile : friUidFiles){
			File f = new File(PATH+friUidFile);
			BufferedReader b = new BufferedReader(new FileReader(f));
			String line = "",id="";
			JSONObject json = null;
			List<String> lists = null;
			while((line = b.readLine())!=null){
				json = JSONObject.fromObject(line);
				id = json.getString("id");
				if(ids.contains(id)){//如果是我们要获取的ID
					w.write(id+"\t");
					lists = (List<String>) json.get("uids");
					for(String fid:lists){
						//if(vids.contains(id)){
						w.write(fid+"\t");
						//}
					}
					w.write("\r\n");
					ids.remove(id);
				}
			}
			b.close();	
		}
		w.flush();
		w.close();
	}
}
