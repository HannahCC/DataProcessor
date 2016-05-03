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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
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

	private static final String path_root = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static final String pathr_root2 = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	private static final boolean isV = false;//false  1635_a0_full；true 1635_v0_full
	public static void main(String args[]) throws IOException{
		//getUserFriendsFull(path_root+"Line\\2333_a0_full.txt",path_root+"Feature_UserInfo\\UserIdFriends_new.txt",path_root+"ExpandID0.txt");
		getUserFriendsFull(path_root+"Line\\1635_a01_full.txt",pathr_root2+"\\UserIdFriends_fri_all.txt",path_root+"UserID_Gender\\1_newest_unequal.txt",path_root+"UserID_Gender\\2_newest_unequal.txt");
		/*for(int i=0;i<5;i++){
			getUserFriendsFull("",pathr_root2+"\\"+i+"_UserIdFriends_train.txt",pathr_root2+i+"\\4_1_trainingid.txt",pathr_root2+i+"\\4_2_trainingid.txt");
			getUserFriendsFull("",pathr_root2+"\\"+i+"_UserIdFriends_test.txt",pathr_root2+i+"\\1_testingid.txt",pathr_root2+i+"\\2_testingid.txt");
		}*/
	}


	private static void getUserFriendsFull(String resfile1, String resfile2, String ... idfiles) throws IOException {
		Set<String> vertex_set0 = new HashSet<String>(0xff);
		for(String idfile : idfiles){
			GetInfo.getSet(idfile, vertex_set0);
		}
		
		Set<String> vertex_set1 = new HashSet<String>(0xffff);
		Set<String> vids = null;
		if(isV){
			vids = new HashSet<String>(0xff);//大V集合，注意：不能将vertex_set中已存在的非大V删掉。
			GetInfo.getSet(path_root+"Config\\Dict_VFri.txt", vids);
		}

		Map<String, StringBuffer> rel_map = new HashMap<String, StringBuffer>(0xffff);
		//将数据写入StringBuffer
		getUserFriends0(rel_map,vertex_set0,vertex_set1,vids,path_root+"UidInfo_friends0.txt");//获取0层用户的关注，并补充顶点集
		getUserOtherFriends0(rel_map,vertex_set0,vertex_set1,vids,path_root+"UidInfo_follows0.txt");//从粉丝文件中补充0层用户的关注，并补充顶点集
		getUserOtherFriends0(rel_map,vertex_set0,vertex_set1,vids,path_root+"UidInfo_follows1.txt");//从粉丝文件中补充0层用户的关注，并补充顶点集 
		getUserFriends1(rel_map,vertex_set0,vertex_set1,path_root+"UidInfo_friends1.txt");//获取1层用户的关注（补充顶点之间的边）
		getUserOtherFriends1(rel_map,vertex_set0,vertex_set1,path_root+"UidInfo_follows0.txt");//从粉丝文件中补充顶点集用户之间的关系 
		getUserOtherFriends1(rel_map,vertex_set0,vertex_set1,path_root+"UidInfo_follows1.txt");//从粉丝文件中补充顶点集用户之间的关系 
		//将StringBuffer中的边去重，写入结果文件
		if(!"".equals(resfile1))saveSetMap(rel_map,resfile1,false);
		if(!"".equals(resfile2))saveSetMap2(rel_map,resfile2,false);
	}


	private static void bufferClear(Map<String, StringBuffer> rel_map){
		Iterator<Entry<String, StringBuffer>> it = rel_map.entrySet().iterator();
		Set<String> fri_id_set = new HashSet<String>();
		StringTokenizer st = null;
		while(it.hasNext()){
			Entry<String, StringBuffer> entry = it.next();
			StringBuffer sb = entry.getValue();
			st = new StringTokenizer(sb.toString(),",");
			sb.delete(0, sb.length());
			while(st.hasMoreTokens()){
				String fri_id = st.nextToken().intern();
				if(!fri_id_set.contains(fri_id)){
					fri_id_set.add(fri_id);
					sb.append(fri_id+",");	
				}
			}
			fri_id_set.clear();
		}
	}
	private static void saveSetMap(Map<String, StringBuffer> rel_map,String resfile, boolean isAppend) throws IOException {
		File f1 = new File(resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f1,isAppend));
		Set<String> fri_id_set = new HashSet<String>();
		StringTokenizer st = null;
		for(String id:rel_map.keySet()){
			st = new StringTokenizer(rel_map.get(id).toString(),",");
			while(st.hasMoreTokens()){
				String fri_id = st.nextToken().intern();
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
	 * * 0层用户获取朋友数据
	 * @param rel_map  关系结果
	 * @param vertex_set   顶点集合（开始只有0层用户，遍历UidInfo_friends0.txt过程中加入1层（0层的关注/粉丝））
	 * @param vids	  大V用户集合（如果不为null，就是用来限制加入的1层用户，必须是大V）
	 * @param srcfile  关系数据源文件
	 * @param isAdd	是否将朋友/粉丝数据加入vertex_set
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserFriends0(Map<String, StringBuffer> rel_map,Set<String> vertex_set0,Set<String> vertex_set1,Set<String> vids,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!vertex_set0.contains(id))continue;
			uids = (List<String>) json.get("uids");
			StringBuffer sb = null;
			if(rel_map.containsKey(id)){
				sb = rel_map.get(id);
				System.out.println(id+"在第一层朋友关系文件中出现了两次。");
			}else{
				sb = new StringBuffer();
			}

			if(vids==null){
				for(String uid : uids){
					sb.append(uid+",");
					vertex_set1.add(uid);
				}
			}else{
				for(String uid : uids){
					if(vids.contains(uid)||vertex_set0.contains(uid)){
						sb.append(uid+",");
						vertex_set1.add(uid);
					}
				}
			}
			rel_map.put(id, sb);
		}
		b.close();
		System.out.println("have read "+srcfile);
	}

	/**
	 * * 用户获取朋友数据
	 * @param rel_map  关系结果
	 * @param vertex_set   顶点集合（开始只有0层用户，遍历UidInfo_friends0.txt过程中加入1层（0层的关注/粉丝））
	 * @param srcfile  关系数据源文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserFriends1(Map<String, StringBuffer> rel_map,Set<String> vertex_set0,Set<String> vertex_set1,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!vertex_set0.contains(id)&&!vertex_set1.contains(id))continue;
			uids = (List<String>) json.get("uids");
			StringBuffer sb = null;
			if(rel_map.containsKey(id)){
				sb = rel_map.get(id);
				System.out.println(id+"在两层/第二层朋友关系文件中出现了两次。");
			}else{
				sb = new StringBuffer();
			}

			for(String uid : uids){
				if(vertex_set0.contains(uid)||vertex_set1.contains(uid)){
					sb.append(uid+",");
				}
			}
			rel_map.put(id, sb);
		}
		b.close();
		System.out.println("have read "+srcfile);
	}

	/**
	 *  主要用于补充0层用户的朋友数据，如fid的粉丝有sid，但sid的关注集合中不存在fid时，将fid补充到sid的关注用户集合中，并且更新1层用户顶点集
	 * @param rel_map	关系map
	 * @param vertex_set0	0层用户顶点集
	 * @param vertex_set1	1层用户顶点集
	 * @param vids	大V用户集
	 * @param srcfile	关系源文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserOtherFriends0(Map<String, StringBuffer> rel_map,Set<String> vertex_set0,Set<String> vertex_set1,Set<String> vids,String srcfile) throws IOException {
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";
		List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(vids==null||vids.contains(id)||vertex_set0.contains(id)){//不限制朋友范围，或朋友在范围内，或者在原顶点集内
				uids = (List<String>) json.get("uids");
				for(String uid : uids){
					if(vertex_set0.contains(uid)){//说明用户id被uid关注，且uid是原始用户(可能uid已经在id的关注列表中，即重复加入了id的stringbuffer)
						vertex_set1.add(id);//将id加入拓展顶点集
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
		}
		b.close();
		System.out.println("have read "+srcfile);
	}

	/**
	 *  主要用于补充顶点集用户之间的朋友关系
	 * @param rel_map	关系map
	 * @param vertex_set0	0层用户顶点集
	 * @param vertex_set1	1层用户顶点集
	 * @param srcfile	关系源文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserOtherFriends1(Map<String, StringBuffer> rel_map,Set<String> vertex_set0,Set<String> vertex_set1,String srcfile) throws IOException {
		bufferClear(rel_map);
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";
		List<String> uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!vertex_set0.contains(id)&&!vertex_set1.contains(id))continue;
			uids = (List<String>) json.get("uids");

			for(String uid : uids){
				if(vertex_set0.contains(id)||vertex_set1.contains(id)){//说明用户id被uid关注，且uid是顶点集中的用户
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
		System.out.println("have read "+srcfile);
	}
}
