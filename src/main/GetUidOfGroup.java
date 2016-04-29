package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class GetUidOfGroup {
	public static final String PATH = "/home/zps/dataset/";
	public static final String srcfile = PATH+"youtube-groupmemberships.txt";
	public static final int GroupNumber = 47;
	public static int[] getMaxGroup() throws IOException{
		BufferedReader br =  new BufferedReader(new FileReader(new File(srcfile)));
		String line = null;
		Map<Integer, Integer> groupNumberMap = new HashMap<>();
		int groupId = 0;
		while(null!=(line=br.readLine())){
			groupId = Integer.parseInt(line.split("\t")[1]);
			if(groupNumberMap.containsKey(groupId)){
				groupNumberMap.put(groupId, groupNumberMap.get(groupId)+1);
			}else{
				groupNumberMap.put(groupId, 1);
			}
		}
		br.close();

		int[] groupIds = new int[GroupNumber];
		int[] groupCounts = new int[GroupNumber];
		Iterator<Entry<Integer, Integer>> it = groupNumberMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> entry = it.next();
			if(entry.getValue()>groupCounts[0]){
				groupIds[0] = entry.getKey();
				groupCounts[0] = entry.getValue();
				HeapAdjust(groupIds, groupCounts, 0);
			}
		}
		for(int i=0;i<groupIds.length;i++){System.out.println(groupIds[i]+":"+groupCounts[i]);}
		return groupIds;
	}
	public static void getGroupUser(int[] groupIds) throws IOException{
		BufferedWriter [] bws = new BufferedWriter[groupIds.length];
		for(int i=0;i<groupIds.length;i++){bws[i] = new BufferedWriter(new FileWriter(new File(PATH+(i+1)+".txt")));}
		BufferedReader br =  new BufferedReader(new FileReader(new File(srcfile)));
		String line = null;
		int groupId = 0,idx = -1;
		while(null!=(line=br.readLine())){
			String[] items = line.split("\t");
			groupId = Integer.parseInt(items[1]);
			idx = contains(groupIds, groupId);
			if(-1!=idx){
				bws[idx].write(Integer.parseInt(items[0])+"\r\n");
			}
		}
		br.close();
		for(int i=0;i<groupIds.length;i++){bws[i].flush();bws[i].close();}
	}
	public static void getGroupUserWithoutIntersection(int[] groupIds) throws IOException{
		int n = groupIds.length;
		Set<Integer>[] userIds = new Set[n];
		for(int i=0;i<n;i++){userIds[i] = new TreeSet<>();}
		BufferedReader br =  new BufferedReader(new FileReader(new File(srcfile)));
		String line = null;
		int uid = 0, groupId = 0,idx = -1;
		while(null!=(line=br.readLine())){
			StringTokenizer st = new StringTokenizer(line,"\t");
			uid = Integer.parseInt(st.nextToken());
			groupId = Integer.parseInt(st.nextToken());
			idx = contains(groupIds, groupId);
			if(-1!=idx){
				userIds[idx].add(uid);
			}
		}
		br.close();

		removeIntersection(userIds);

		BufferedWriter bw = null;
		for(int i=0;i<n;i++){
			bw = new BufferedWriter(new FileWriter(new File(PATH+(i+1)+".txt")));
			saveUserId(bw, userIds[i]);
			bw.flush();bw.close();
		}
	}
	private static void saveUserId(BufferedWriter bw, Set<Integer> set) throws IOException {
		Iterator<Integer> it = set.iterator();
		while(it.hasNext()){ 
			bw.write(it.next()+"\r\n");
		}
	}
	private static void removeIntersection(Set<Integer>[] userIds) {
		int n = userIds.length;
		int idxs[] = new int[n-1];
		boolean flag = false;
		for(int i=0;i<n;i++){
			for(int k=0;k<n-1;k++){idxs[k] = (i+k+1)%n;}
			Iterator<Integer> it = userIds[i].iterator();
			while(it.hasNext()){ 
				int id=it.next();
				flag = false;
				for(int k=0;k<n-1;k++){
					if(flag||userIds[idxs[k]].contains(id)) {userIds[idxs[k]].remove(id);flag = true;}
				}
				if(flag)it.remove();
			}
		}
	}
	private static void HeapAdjust(int[] groupIds, int[] groupCounts, int idx){
		int size = groupCounts.length;
		int left = (idx<<1) + 1;
		int right = (idx<<1) + 2;
		int min = idx;
		if(left<size&&groupCounts[min]>groupCounts[left]){min = left;}
		if(right<size&&groupCounts[min]>groupCounts[right]){min = right;}
		if(min==idx){return;}
		else{
			int tmp = groupCounts[idx];
			groupCounts[idx] = groupCounts[min];
			groupCounts[min] = tmp;
			tmp = groupIds[idx];
			groupIds[idx] = groupIds[min];
			groupIds[min] = tmp;
			HeapAdjust(groupIds,groupCounts,min);
		}
	}
	private static int contains(int[] groupIds, int groupId){
		for(int i=0;i<groupIds.length;i++){
			if(groupIds[i]==groupId){return i;}
		}
		return -1;
	}
	public static void main(String args[]) throws IOException {
		int[] groupIds = getMaxGroup();//获取用户数最多的5个groupId
		//getGroupUser(groupIds);//将属于各groupId的用户ID
		//上面的问题在于1个用户可能属于多个group
		getGroupUserWithoutIntersection(groupIds);
		

	}

}
