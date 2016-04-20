package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONObject;
import utils.GetInfo;
public class GetUserNeighbours {
	/**
	 * 获取ids用户集的邻居（如有共同粉丝/关注的人）   
	 * 获取方式   a -- 关注-- >{b,c,d,e,f}    b <--关注-- {a,g,h,i,j}   c <--关注-- {a,h,i,j,k}       若共同关注数阈值为2，则a的邻居为{h,i,j}，他们有共同的关注b,c
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static final String dir = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";//ext1000_Mute_GenderPre
	public static final String _0srcfile = "UidInfo_friends0.txt";  //0层用户的关系数据
	public static final String _1srcfile = "UidInfo_follows1.txt"; //1层用户的关系数据
	private static final String resfile = "Feature_UserInfo\\UserIdNeighboursIn1_c1Fri.txt"; //1层用户的关系数据
	private static final int threshold = 1;
	private static final boolean isV = false;//only v fri
	public static void main(String args[]) throws IOException{
		Set<String> _0sids = new HashSet<String>(3000);
		GetInfo.getSet(dir+"ExpandID0.txt", _0sids);
		Set<String> _1sids = new HashSet<String>(600000);

		Set<String> vids = null;
		if(isV){
			vids = new HashSet<String>(600000);
			GetInfo.getSet(dir+"Config\\Dict_VFri.txt", vids);
		}
		
		Map<String, StringBuffer> rel_map1 = new HashMap<String, StringBuffer>(0xff);
		getUserFriends(rel_map1,_0sids,vids,_1sids,dir+_0srcfile);
		System.out.println(_1sids.size());
		Map<String, StringBuffer> rel_map2 = new HashMap<String, StringBuffer>(_1sids.size()<<1);
		getUserFriends(rel_map2,_1sids,null,null,dir+_1srcfile);
		System.out.println(rel_map2.size());

		//help gc
		_1sids = null;
		vids = null;
		
		if(threshold<=1){getNeighbour1(_0sids,rel_map1,rel_map2,dir+resfile);}
		else{getNeighbour2(_0sids,rel_map1,rel_map2,dir+resfile);}

	}
	/**
	 * 为每一个0层用户找到邻居
	 * @param _0sids
	 * @param rel_map1
	 * @param rel_map2
	 * @param resfile 
	 * @throws IOException 
	 */
	private static void getNeighbour1(Set<String> _0sids,
			Map<String, StringBuffer> rel_map1,
			Map<String, StringBuffer> rel_map2, String resfile) throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Set<String> neighbours_ids = new TreeSet<String>();

		StringTokenizer st0 = null,st1 = null;
		String fri_id = null;
		for(String sid : _0sids){//对每个0层用户
			bw.write(sid+"\t");
			st0 = new StringTokenizer(rel_map1.get(sid).toString().intern(),",");
			while(st0.hasMoreTokens()){//对0层用户的每个朋友
				fri_id = st0.nextToken().intern();
				if(!rel_map2.containsKey(fri_id))continue;
				st1 = new StringTokenizer(rel_map2.get(fri_id).toString().intern(),",");
				while(st1.hasMoreTokens()){//对0层用户的每个朋友的关注
					neighbours_ids.add(st1.nextToken().intern());
				}
			}
			Iterator<String> it = neighbours_ids.iterator();
			while(it.hasNext()){
				String entry = it.next();
				bw.write(entry+"\t");
			}
			bw.write("\r\n");
			neighbours_ids.clear();
		}
		bw.flush();
		bw.close();
	}
	/**
	 * 为每一个0层用户找到邻居
	 * @param _0sids
	 * @param rel_map1
	 * @param rel_map2
	 * @param resfile 
	 * @throws IOException 
	 */
	private static void getNeighbour2(Set<String> _0sids,
			Map<String, StringBuffer> rel_map1,
			Map<String, StringBuffer> rel_map2, String resfile) throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Map<String,Integer> neighbours_ids = new TreeMap<String,Integer>();
		StringTokenizer st0 = null,st1 = null;
		String fri_id=null,nei_id = null;
		for(String sid : _0sids){//对每个0层用户
			bw.write(sid+"\t");
			st0 = new StringTokenizer(rel_map1.get(sid).toString().intern(),",");
			while(st0.hasMoreTokens()){//对0层用户的每个朋友
				fri_id = st0.nextToken().intern();
				if(!rel_map2.containsKey(fri_id))continue;
				st1 = new StringTokenizer(rel_map2.get(fri_id).toString().intern(),",");
				while(st1.hasMoreTokens()){//对0层用户的每个朋友的关注
					nei_id = st1.nextToken().intern();
					if(neighbours_ids.containsKey(nei_id)){neighbours_ids.put(nei_id, neighbours_ids.get(nei_id)+1);}
					else{neighbours_ids.put(nei_id,1);}
				}
			}
			Iterator<Entry<String, Integer>> it = neighbours_ids.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, Integer> entry = it.next();
				if(entry.getValue()>=threshold){bw.write(entry.getKey()+"\t");}
			}
			bw.write("\r\n");
			neighbours_ids.clear();
		}
		bw.flush();
		bw.close();
	}
	/**
	 * 用户获取朋友数
	 * @param rel_map  关系结果
	 * @param sids   原始用户集合（可能是只有0层用户，可能时0+1层用户）
	 * @param fids	  朋友用户集合（可能将朋友集合限制在0层用户+0层朋友用户、限制在大V用户）null即不会对朋友做限制
	 * @param srcfile  关系文件
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void getUserFriends(Map<String, StringBuffer> rel_map,Set<String> sids,Set<String> fids,Set<String> _1sids,String srcfile) throws IOException {
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

			if(fids==null){//对朋友集合不做限制
				for(String uid : uids){sb.append(uid+",");}
				if(_1sids!=null){_1sids.addAll(uids);}
			}else{//对朋友集合进行限制
				if(_1sids==null){//不将朋友加入集合_1sids中
					for(String uid : uids){
						if(fids.contains(uid)){sb.append(uid+",");}
					}
				}else{//将朋友加入集合_1sids中
					for(String uid : uids){
						if(fids.contains(uid)){sb.append(uid+",");_1sids.add(uid);}
					}
				}
			}
			rel_map.put(id, sb);
		}
		b.close();
	}

}
