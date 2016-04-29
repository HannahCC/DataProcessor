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
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONObject;
public class GetUserNeighbours1 {
	/**
	 * 获取ids用户集的邻居（如有共同粉丝/关注的人）   
	 * 获取方式   a -- 关注-- >{b,c,d,e,f}    b <--关注-- {a,g,h,i,j}   c <--关注-- {a,h,i,j,k}       若共同关注数阈值为2，则a的邻居为{h,i,j}，他们有共同的关注b,c
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static final String dir = "/home/zps/sina2333/";//ext1000_Mute_GenderPre
	public static final String _0srcfile = "UidInfo_follows0.txt";  //0层用户的关系数据
	public static final String _1srcfile = "UidInfo_friends1.txt"; //1层用户的关系数据
	private static final String resfile = "Feature_UserInfo/UserIdNeighbours_c"; //1层用户的关系数据
	private static final String resfile_t = "Fol.txt"; //1层用户的关系数据
	private static final Byte[] thresholds = {1/*,2,3,5,8*/};
	public static void main(String args[]) throws IOException{
		Set<Long> _0sids = new HashSet<Long>(3000);
		getSet(dir+"ExpandID0.txt", _0sids);
		Set<Long> _1sids = new HashSet<Long>(600000);

		Map<Long, long[]> rel_map1 = new HashMap<Long, long[]>(0xff);
		getUserFriends(rel_map1,_0sids,_1sids,dir+_0srcfile);
		System.out.println(_1sids.size());
		Map<Long, long[]> rel_map2 = new HashMap<Long, long[]>(_1sids.size()<<1);
		getUserFriends(rel_map2,_1sids,null,dir+_1srcfile);
		System.out.println(rel_map2.size());

		//help gc
		_1sids = null;

		if(thresholds.length==1&&thresholds[0]==1){getNeighbour1(_0sids,rel_map1,rel_map2,dir+resfile);}
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
	private static void getNeighbour1(Set<Long> _0sids,
			Map<Long, long[]> rel_map1,
			Map<Long, long[]> rel_map2, String resfile) throws IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Set<Long> neighbours_ids = new TreeSet<Long>();

		long[] fri_id_set = null,fri_fri_id_set = null;
		for(long sid : _0sids){//对每个0层用户
			bw.write(sid+"\t");
			fri_id_set = rel_map1.get(sid);
			for(long fri_id:fri_id_set){//对0层用户的每个朋友
				if(!rel_map2.containsKey(fri_id))continue;
				fri_fri_id_set = rel_map2.get(fri_id);
				for(long fri_fir_id : fri_fri_id_set){//对0层用户的每个朋友的关注
					neighbours_ids.add(fri_fir_id);
				}
			}
			Iterator<Long> it = neighbours_ids.iterator();
			while(it.hasNext()){
				long entry = it.next();
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
	private static void getNeighbour2(Set<Long> _0sids,
			Map<Long, long[]> rel_map1,
			Map<Long, long[]> rel_map2, String resfile) throws IOException {

		BufferedWriter[] bws = new BufferedWriter[thresholds.length];
		for(int i=0;i<thresholds.length;i++){
			bws[i] = new BufferedWriter(new FileWriter(new File(resfile+thresholds[i]+resfile_t)));
		}

		Map<Long,Byte> neighbours_ids = new TreeMap<Long,Byte>();
		long[] fri_id_set = null,fri_fri_id_set = null;
		for(long sid : _0sids){//对每个0层用户
			for(int i=0;i<thresholds.length;i++){bws[i].write(sid+"\t");}
			fri_id_set = rel_map1.get(sid);
			for(long fri_id:fri_id_set){//对0层用户的每个朋友
				if(!rel_map2.containsKey(fri_id))continue;
				fri_fri_id_set = rel_map2.get(fri_id);
				for(long fri_fir_id : fri_fri_id_set){//对0层用户的每个朋友的关注
					if(neighbours_ids.containsKey(fri_fir_id)){
						byte c = neighbours_ids.get(fri_fir_id);
						neighbours_ids.put(fri_fir_id, c<127?(byte)(c+1):c);
					}else{neighbours_ids.put(fri_fir_id, (byte)1);}
				}
			}
			Iterator<Entry<Long, Byte>> it = neighbours_ids.entrySet().iterator();
			while(it.hasNext()){
				Entry<Long, Byte> entry = it.next();
				for(int i=0;i<thresholds.length;i++){
					if(entry.getValue()>=thresholds[i]){bws[i].write(entry.getKey()+"\t");}
				}
			}
			for(int i=0;i<thresholds.length;i++){bws[i].write("\r\n");}
			neighbours_ids.clear();
		}
		for(int i=0;i<thresholds.length;i++){bws[i].flush();bws[i].close();}
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
	private static void getUserFriends(Map<Long, long[]> rel_map,Set<Long> sids,Set<Long> _1sids,String srcfile) throws IOException {
		//GetInfo.getSetMap(srcfile, id_rel_map, "id", "uids");
		File f = new File(srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "";List<String> uids = null;
		long id = 0;int size=0;
		JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			id =Long.parseLong(json.getString("id"));
			if(!sids.contains(id))continue;
			uids = (List<String>) json.get("uids");
			size = uids.size();
			long[] sb = new long[size];
			if(_1sids==null){//不将朋友加入集合_1sids中
				for(int i=0;i<size;i++){sb[i]=Long.parseLong(uids.get(i));}
			}else{//将朋友加入集合_1sids中
				for(int i=0;i<size;i++){long luid = Long.parseLong(uids.get(i));sb[i]=luid;_1sids.add(luid);}
			}
			rel_map.put(id, sb);
		}
		b.close();
	}

	private static void getSet(String filename, Set<Long> set) throws IOException {
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String uid = line.split("\\s+")[0];
				set.add(Long.parseLong(uid));
			}
		}
		br.close();
	}
}
