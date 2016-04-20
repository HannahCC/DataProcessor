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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONObject;
import utils.GetInfo;
public class GetUserNeighbours1 {
	/**
	 * 获取ids用户集的邻居（如有共同粉丝/关注的人）   
	 * 获取方式   a -- 关注-- >{b,c,d,e,f}    b <--关注-- {a,g,h,i,j}   c <--关注-- {a,h,i,j,k}       若共同关注数阈值为2，则a的邻居为{h,i,j}，他们有共同的关注b,c
	 * 
	 * @param args
	 * @throws IOException
	 */
	private static final String dir = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";//ext1000_Mute_GenderPre
	private static final String _0srcfile = "UidInfo_friends0.txt";  //0层用户的关系数据
	private static final String _1srcfile = "UidInfo_follows1.txt"; //1层用户的关系数据
	private static final String resfile = "Feature_UserInfo\\UserIdNeighbours_c2Fri.txt"; //1层用户的关系数据
	private static final int threshold = 2;
	public static void main(String args[]) throws IOException{
		Set<String> _0sids = new HashSet<String>(3000);
		GetInfo.getSet(dir+"ExpandID0.txt", _0sids);
		Set<String> _1sids = new HashSet<String>(600000);


		Map<String, String[]> rel_map1 = new HashMap<String, String[]>(0xff);
		getUserFriends(rel_map1,_0sids,_1sids,dir+_0srcfile);
		System.out.println(_1sids.size());
		Map<String, String[]> rel_map2 = new HashMap<String, String[]>(_1sids.size()<<1);
		getUserFriends(rel_map2,_1sids,null,dir+_1srcfile);
		System.out.println(rel_map2.size());

		//help gc
		_1sids = null;

		if(threshold<=1){getNeighbour1(_0sids,rel_map1,rel_map2,dir+resfile);}
		else{getNeighbour2(_0sids,rel_map1,rel_map2,dir+resfile);}

	}
	/**
	 * 为每一个0层用户找到邻居(是否为邻居只需根据是否有共同粉丝/关注，不做共同粉丝/关注的数量限制)
	 * @param _0sids
	 * @param rel_map1
	 * @param rel_map2
	 * @param resfile 
	 * @throws IOException 
	 */
	private static void getNeighbour1(Set<String> _0sids,
			Map<String, String[]> rel_map1,
			Map<String, String[]> rel_map2, String resfile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Set<String> neighbours_ids = new TreeSet<String>();
		Iterator<String> it = null;

		String[] fri_id_set = null, fri_fri_id_set = null;
		for(String sid : _0sids){//对每个0层用户
			if(!rel_map1.containsKey(sid)){
				System.out.println("user "+sid + "has no fri/fol");
				continue;
			}
			bw.write(sid+"\t");
			fri_id_set = rel_map1.get(sid);
			for(String fri_id:fri_id_set){
				if(!rel_map2.containsKey(fri_id))continue;
				fri_fri_id_set = rel_map2.get(fri_id);
				for(String fri_fir_id : fri_fri_id_set){
					neighbours_ids.add(fri_fir_id);
				}
			}
			it = neighbours_ids.iterator();
			while(it.hasNext()){
				String entry = it.next().intern();
				bw.write(entry+"\t");
			}
			bw.write("\r\n");
			neighbours_ids.clear();
		}
		bw.flush();
		bw.close();
	}
	/**
	 * 为每一个0层用户找到邻居(是否为邻居需要其根据共同粉丝/关注数大于等于阈值进行判断)
	 * @param _0sids
	 * @param rel_map1
	 * @param rel_map2
	 * @param resfile 
	 * @throws IOException 
	 */
	private static void getNeighbour2(Set<String> _0sids,
			Map<String, String[]> rel_map1,
			Map<String, String[]> rel_map2, String resfile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Map<String,Integer> neighbours_ids = new TreeMap<String,Integer>();
		Iterator<Entry<String, Integer>> it = null;
		String[] fri_id_set = null, fri_fri_id_set = null;
		for(String sid : _0sids){//对每个0层用户
			if(!rel_map1.containsKey(sid)){
				System.out.println("user "+sid + "has no fri/fol");
				continue;
			}
			bw.write(sid+"\t");
			fri_id_set = rel_map1.get(sid);
			for(String fri_id:fri_id_set){
				if(!rel_map2.containsKey(fri_id))continue;
				fri_fri_id_set = rel_map2.get(fri_id);
				for(String fri_fir_id : fri_fri_id_set){
					if(neighbours_ids.containsKey(fri_fir_id)){neighbours_ids.put(fri_fir_id, neighbours_ids.get(fri_fir_id)+1);}
					else{neighbours_ids.put(fri_fir_id,1);} 
				}
			}
			it = neighbours_ids.entrySet().iterator();
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
	 * 用户获取朋友数据
	 * @param rel_map1  关系结果
	 * @param sids   原始用户集合（可能是只有0层用户，可能时0+1层用户）
	 * @param fids	  朋友用户集合（可能将朋友集合限制在0层用户+0层朋友用户、限制在大V用户）null即不会对朋友做限制
	 * @param srcfile  关系文件
	 * @throws IOException
	 */
	private static void getUserFriends(Map<String, String[]> rel_map1,Set<String> sids,Set<String> _1sids,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",id="";String[] uids = null;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id = json.getString("id");
			if(!sids.contains(id))continue;
			uids = (String[]) json.get("uids");
			if(_1sids!=null){
				for(String uid : uids){_1sids.add(uid);}
			}
			rel_map1.put(id, uids);
		}
		b.close();
	}

}
