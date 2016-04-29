package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONObject;
import utils.GetInfo;
public class GetUserFriendsFull {
	/**
	 * Line(降维工具)的输入文件格式为：
	 * ```
	 * good the 3
	 * the good 3
	 * good bad 1
	 * bad good 1
	 * bad of 4
	 * of bad 4
	 * ```
	 * 本程序将用户关系转为该格式：“ A B 1 ”，表示：A关注B
	 * 该程序的顶点只有两层用户
	 * @throws IOException 
	 */

	private static String path_root = "D:\\Project_DataMinning\\Data\\Sina_res\\";
	public static void main(String args[]) throws IOException{
		transfer(path_root+"Sina_NLPIR2223_GenderPre\\");//"Sina_NLPIRandTHUext1000_Mute_GenderPre\\");//
	}


	private static void transfer(String dir) throws IOException {
		Set<String> sids = new HashSet<String>(3000);
		GetInfo.getSet(dir+"ExpandID0.txt", sids);
		
		Set<String> fids = new HashSet<String>(800000);
		GetInfo.getSet(dir+"ExpandID1_fri.txt", fids);
		Set<String> vids = new HashSet<String>(600000);
		GetInfo.getSet(dir+"Config\\Dict_VFri.txt", vids);
		fids.retainAll(vids);
		fids.addAll(sids);

		Map<String, StringBuffer> rel_map = new HashMap<String, StringBuffer>(0xffff);
		//将数据写入StringBuffer
		getUserFriends(rel_map,sids,fids,dir+"UidInfo_friends0.txt");
		//getUserFriends(rel_map,sids,fids,dir+"UidInfo_friends1.txt");
		getUserOtherFriends(rel_map,sids,fids,dir+"UidInfo_follows0.txt");
		getUserOtherFriends(rel_map,sids,fids,dir+"UidInfo_follows1.txt");
		//将StringBuffer中的边去重，写入结果文件
		saveSetMap(rel_map,dir+"Line\\2333V_net_sina.txt",false);
		saveSetMap2(rel_map,dir+"Feature_UserInfo\\UserIdVFriends.txt",false);
	}


	private static void saveSetMap(Map<String, StringBuffer> rel_map,String resfile, boolean isAppend) throws IOException {
		File f1 = new File(resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f1,isAppend));
		Set<String> fri_id_set = new HashSet<String>();
		for(String id:rel_map.keySet()){
			String[] fri_ids = rel_map.get(id).toString().split(",");
			for(String fri_id : fri_ids){
				if(!fri_id_set.contains(fri_id)){
					fri_id_set.add(fri_id);
					w.write(id+"\t"+fri_id+"\t1.000000\r\n");
				}
			}
			fri_id_set.clear();
		}
		w.flush();
		w.close();
	}

	private static void saveSetMap2(Map<String, StringBuffer> rel_map,String resfile, boolean isAppend) throws IOException {
		File f1 = new File(resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f1,isAppend));
		Set<String> fri_id_set = new TreeSet<String>();
		for(String id:rel_map.keySet()){
			String[] fri_ids = rel_map.get(id).toString().split(",");
			fri_id_set.addAll(Arrays.asList(fri_ids));
			w.write(id);
			for(String fri_id : fri_id_set){
				w.write("\t"+fri_id);
			}
			w.write("\r\n");
			fri_id_set.clear();
		}
		w.flush();
		w.close();
	}


	/**
	 * 用户获取朋友数据
	 * @param rel_map  关系结果
	 * @param sids   原始用户集合（可能是只有0层用户，可能时0+1层用户）
	 * @param fids	  朋友用户集合（可能将朋友集合限制在0层用户+0层朋友用户、限制在大V用户）
	 * @param srcfile  关系文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserFriends(Map<String, StringBuffer> rel_map,Set<String> sids,Set<String> fids,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!sids.contains(id))continue;
			uids = (List<String>) json.get("uids");
			StringBuffer sb = null;
			if(rel_map.containsKey(id)){
				sb = rel_map.get(id);
			}else{
				sb = new StringBuffer();
			}

			for(String uid : uids){
				if(fids.contains(uid)){
					sb.append(uid+",");
				}
			}
			rel_map.put(id, sb);
		}
		b.close();
	}

	/**
	 * 主要用于补充朋友数据，如fid的粉丝有sid，但sid的关注集合中不存在fid时，将fid补充到sid的关注用户集合中
	 *@param rel_map  关系结果
	 * @param sids   原始用户集合（可能是只有0层用户，可能时0+1层用户）
	 * @param fids	  粉丝用户集合（可能将粉丝集合限制在0层用户+0层朋友用户、限制在大V用户）
	 * @param srcfile  关系文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserOtherFriends(Map<String, StringBuffer> rel_map,Set<String> sids,Set<String> fids,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";
		List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!fids.contains(id))continue;
			uids = (List<String>) json.get("uids");
			
			for(String uid : uids){
				if(sids.contains(uid)){
					if(rel_map.containsKey(uid)){
						rel_map.get(uid).append(id+",");
					}else{
						StringBuffer sb = new StringBuffer();
						sb.append(id+",");
						rel_map.put(uid, sb);
					}
				}
			}
		}
		b.close();
	}
}
