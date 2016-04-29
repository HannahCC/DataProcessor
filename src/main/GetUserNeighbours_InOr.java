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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
public class GetUserNeighbours_InOr {
	/**
	 * 获取ids用户集的邻居（如有共同粉丝/关注的人）   
	 * 获取方式   a -- 关注-- >{b,c,d,e,f}    b <--关注-- {a,g,h,i,j}   c <--关注-- {a,h,i,j,k}       若共同关注数阈值为2，则a的邻居为{h,i,j}，他们有共同的关注b,c
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static final String dir = "/home/zps/sina2333/";//ext1000_Mute_GenderPre
	private static final String srcfile_t = "Feature_UserInfo/UserIdNeighbours_c"; //1层用户的关系数据
	private static final Byte[] thresholds = {8/*,2,3,5,8*/};
	public static void main(String args[]) throws IOException{
		Map<Long, long[]> rel_map1 = new HashMap<Long, long[]>(3000);
		Map<Long, long[]> rel_map2 = new HashMap<Long, long[]>(3000);
		for(byte threshold : thresholds){
			getUserNeighbours(rel_map1,dir+srcfile_t+threshold+"Fol.txt");
			getUserNeighbours(rel_map2,dir+srcfile_t+threshold+"Fri.txt");
			//getUserNeighbours_OR(rel_map1,rel_map2,dir+srcfile_t+threshold+"FolOrFri.txt");
			getUserNeighbours_AND(rel_map1,rel_map2,dir+srcfile_t+threshold+"FolAndFri.txt");
			rel_map1.clear();
			rel_map2.clear();
		}
	}


	private static void getUserNeighbours_OR(Map<Long, long[]> rel_map1,
			Map<Long, long[]> rel_map2, String resfile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Iterator<Entry<Long, long[]>> it = rel_map1.entrySet().iterator();
		Entry<Long, long[]> entry  = null;
		Set<Long> neiSet = new TreeSet<>();
		while(it.hasNext()){
			entry = it.next();
			for(long id : entry.getValue()){neiSet.add(id);}
			if(rel_map2.containsKey(entry.getKey())){
				for(long id : rel_map2.get(entry.getKey())){neiSet.add(id);}
			}
			saveSet(bw,entry.getKey(),neiSet);
			neiSet.clear();
		}
		bw.flush();
		bw.close();
	}

	private static void getUserNeighbours_AND(Map<Long, long[]> rel_map1,
			Map<Long, long[]> rel_map2, String resfile) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		Iterator<Entry<Long, long[]>> it = rel_map1.entrySet().iterator();
		Entry<Long, long[]> entry  = null;
		Set<Long> neiSet = new TreeSet<>();
		Set<Long> neiSet2 = new HashSet<Long>(0xfff);
		while(it.hasNext()){
			entry = it.next();
			if(rel_map2.containsKey(entry.getKey())){
				for(long id : entry.getValue()){neiSet2.add(id);}
				for(long id : rel_map2.get(entry.getKey())){
					if(neiSet2.contains(id)){
						neiSet.add(id);
					}
				}
			}
			neiSet2.clear();
			saveSet(bw,entry.getKey(),neiSet);
			neiSet.clear();
		}
		bw.flush();
		bw.close();
	}



	private static void getUserNeighbours(Map<Long, long[]> rel_map,String filename) throws IOException {
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				StringTokenizer stringTokenizer = new StringTokenizer(line,"\t");
				long id = Long.parseLong(stringTokenizer.nextToken());
				long[]  nei_idSet = new long[stringTokenizer.countTokens()];
				int i=0;
				while(stringTokenizer.hasMoreElements()){
					nei_idSet[i++]=Long.parseLong(stringTokenizer.nextToken());
				}
				rel_map.put(id, nei_idSet);
			}
		}
		br.close();
	}


	private static void saveSet(BufferedWriter bw, Long id, Set<Long> neiSet) throws IOException {
		bw.write(id+"\t");
		for(long nei_id : neiSet){
			bw.write(nei_id+"\t");
		}
		bw.write("\r\n");
	}
}
