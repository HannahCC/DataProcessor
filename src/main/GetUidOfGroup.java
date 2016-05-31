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
import java.util.StringTokenizer;
import java.util.TreeSet;

import utils.GetInfo;
import utils.SaveInfo;

public class GetUidOfGroup {
	public static final String PATH = "/home/zps/dataset/Flickr/";
	public static final String srcfile = PATH+"flickr-groupmemberships.txt";
	public static final int GroupNumber = 5;
	/**
	 * 获取人数最多的GroupNumber个GroupID
	 * 使用大顶堆算法
	 * @return
	 * @throws IOException
	 */
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
	/**
	 * 根据groupIds，获取各group的用户ID
	 * @param groupIds
	 * @throws IOException
	 */
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
	/**
	 * 根据groupIds，获取各group的用户ID，但去掉其中属于多个group的用户
	 * @param groupIds
	 * @throws IOException
	 */
	public static void getGroupUserWithoutIntersection(int[] groupIds) throws IOException{
		int n = groupIds.length;
		Set<Integer>[] userIds = null;
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
		for(int i=0;i<n;i++){SaveInfo.saveSet(PATH, (i+1)+".txt", userIds[i], false);}
	}
	/**
	 * 为userIdFilenames中的id
	 * 	获取他们的朋友ID列表，存储在userFriendsFilename文件中
	 * 	获取他们中没有朋友的ID集合，存储在userIdWithoutFriFilename文件中
	 * 	获取他们的朋友ID集合，生成朋友词典，存储在FriDictFilename文件中
	 * @param userFriendsFilename
	 * @param userIdWithoutFriFilename
	 * @param FriDictFilename
	 * @param linksFilename
	 * @param userIdFilenames
	 * @throws IOException
	 */
	public static void getUserFriends( String userFriendsFilename,String userIdWithoutFriFilename, String FriDictFilename, String linksFilename,String ... userIdFilenames) throws IOException{
		Map<Integer, Set<Integer>> userFriendsMap = new HashMap<>(100000);
		for(String userIdFilename : userIdFilenames){getUserId(PATH+userIdFilename, userFriendsMap);}
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(PATH+linksFilename)));
		String lineString = null;
		StringTokenizer st = null;
		int uid = 0, friId = 0;
		Set<Integer> allfriIdSet = new TreeSet<Integer>();
		while(null!=(lineString = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			uid = Integer.parseInt(st.nextToken());
			if(userFriendsMap.containsKey(uid)){
				friId = Integer.parseInt(st.nextToken());
				allfriIdSet.add(friId);
				if(null == userFriendsMap.get(uid)){
					Set<Integer> friIdSet  = new TreeSet<Integer>();
					friIdSet.add(friId);
					userFriendsMap.put(uid, friIdSet);
				}else{
					userFriendsMap.get(uid).add(friId);
				}
			}
		}
		bufferedReader.close();
		saveUserFriends(PATH+userFriendsFilename, PATH+userIdWithoutFriFilename, userFriendsMap);
		SaveInfo.saveDict(PATH, FriDictFilename, allfriIdSet, false);
	}

	public static void getUserFriends2(String userFriendsFilename, String linksFilename,String userIdFilename) throws NumberFormatException, IOException {
		//getUserId
		Map<Integer, Set<Integer>> userFriendsMap = new HashMap<>(100000);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(PATH+userIdFilename)));
		String lineString = null;
		StringTokenizer st = null;
		int uid = 0, friId = 0;
		while(null!=(lineString  = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			userFriendsMap.put(Integer.parseInt(st.nextToken()), null);
			/*st.nextToken();
			while(st.hasMoreTokens()){
				userFriendsMap.put(Integer.parseInt(st.nextToken()), null);
			}*/
		}
		bufferedReader.close();
		System.out.println(userFriendsMap.size());
		//获取粉丝的朋友
		bufferedReader = new BufferedReader(new FileReader(new File(PATH+linksFilename)));
		while(null!=(lineString = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			uid = Integer.parseInt(st.nextToken());
			if(userFriendsMap.containsKey(uid)){
				friId = Integer.parseInt(st.nextToken());
				if(null == userFriendsMap.get(uid)){
					Set<Integer> friIdSet  = new TreeSet<Integer>();
					friIdSet.add(friId);
					userFriendsMap.put(uid, friIdSet);
				}else{
					userFriendsMap.get(uid).add(friId);
				}
			}
		}
		bufferedReader.close();
		saveUserFriends(PATH+userFriendsFilename, userFriendsMap);
	}
	public static void getUserFollows(String userFollowsFilename,String userIdWithoutFolFilename, String linksFilename,String ... userIdFilenames ) throws IOException{
		Map<Integer, Set<Integer>> userFollowsMap = new HashMap<>(80000);
		for(String userIdFilename : userIdFilenames){getUserId(PATH+userIdFilename, userFollowsMap);}
		//获取每个用户的粉丝
		File linkFile = new File(PATH+linksFilename);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(linkFile));
		String lineString = null;
		StringTokenizer st = null;
		int uid = 0, friId = 0;
		while(null!=(lineString = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			uid = Integer.parseInt(st.nextToken());
			friId = Integer.parseInt(st.nextToken());
			if(userFollowsMap.containsKey(friId)){
				if(null == userFollowsMap.get(friId)){
					Set<Integer> folIdSet  = new TreeSet<Integer>();
					folIdSet.add(uid);
					userFollowsMap.put(friId, folIdSet);
				}else{
					userFollowsMap.get(friId).add(uid);
				}
			}
		}
		bufferedReader.close();	
		saveUserFriends(PATH+userFollowsFilename, PATH+userIdWithoutFolFilename, userFollowsMap);
	}
	/**
	 * 将userIdFilenames中的id，去除存储在userIdWithoutFriendFilename的ID
	 * @param userIdWithoutFriendFilename
	 * @param userIdFilenames
	 * @throws IOException
	 */
	public static void removeUserIdWitoutFriends(String userIdWithoutFriendFilename, String ... userIdFilenames) throws IOException {
		Set<String> userIdWithoutFriendSet = new HashSet<>(3000);
		GetInfo.getSet(PATH+userIdWithoutFriendFilename, userIdWithoutFriendSet);

		Set<String> userIdSet = new TreeSet<String>();
		for(String userIdFilename : userIdFilenames){
			GetInfo.getSet(PATH+userIdFilename, userIdSet);
			for(String id : userIdWithoutFriendSet){
				userIdSet.remove(id);
			}
			SaveInfo.saveSet(PATH, userIdFilename, userIdSet, false);
			userIdSet.clear();
		}
	}

	public static void getUserFriendsNet(String friendsNetFilename1,String friendsNetFilename2,String linksFilename,String ... userIdFilenames) throws IOException{
		Set<Integer> userFriendsSet = new HashSet<>(100000);
		for(String userIdFilename : userIdFilenames){getUserId(PATH+userIdFilename, userFriendsSet);}
		File linkFile = new File(PATH+linksFilename);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(linkFile));
		BufferedWriter w1 = new BufferedWriter(new FileWriter(new File(PATH+friendsNetFilename1)));
		BufferedWriter w2 = new BufferedWriter(new FileWriter(new File(PATH+friendsNetFilename2)));
		String lineString = null;
		StringTokenizer st = null;
		int uid = 0, friId = 0;
		//get line1 只用1层关系
		Set<Integer> allfriIdSet = new TreeSet<Integer>();
		while(null!=(lineString = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			uid = Integer.parseInt(st.nextToken());
			if(userFriendsSet.contains(uid)){
				allfriIdSet.add(friId);
				friId = Integer.parseInt(st.nextToken());
				w1.write(uid+"\t"+friId+"\t1.000000\r\n");
				w2.write(uid+"\t"+friId+"\t1.000000\r\n");
			}
		}
		w1.flush();w1.close();
		bufferedReader.close();
		//get line1+2 使用2层关系
		allfriIdSet.removeAll(userFriendsSet);
		bufferedReader = new BufferedReader(new FileReader(linkFile));
		while(null!=(lineString = bufferedReader.readLine())){
			st = new StringTokenizer(lineString,"\t");
			uid = Integer.parseInt(st.nextToken());
			friId = Integer.parseInt(st.nextToken());
			if(allfriIdSet.contains(uid)&&allfriIdSet.contains(friId)){ //只将出现在0/1层的用户之间的关系加入Net文件
				w2.write(uid+"\t"+friId+"\t1.000000\r\n");
			}
		}
		w2.flush();w2.close();
		bufferedReader.close();
	}


	public static void main(String args[]) throws IOException {
		/*int[] groupIds = getMaxGroup();//获取用户数最多的GroupNumber个groupId
		getGroupUser(groupIds);//将属于各groupId的用户ID
		 */		//上面的问题在于1个用户可能属于多个group
		//getGroupUserWithoutIntersection(groupIds);
		//  [Flickr-5的用户]

		//for w2v
		// UserNumber = 73284
		// 	Avg FriNumber = 138.20112166366465
		/*getUserFriends("UserIdFriends.txt", "UserIdWithoutFriend.txt", "Dict_Fri.txt", "flickr-links.txt", "1.txt", "2.txt", "3.txt", "4.txt", "5.txt");	
		removeUserIdWitoutFriends("UserIdWithoutFriend.txt", "1.txt", "2.txt", "3.txt", "4.txt", "5.txt");*/

		//for w2v incre

		int[] fold = {0,1,2,3,4};
		for(int i : fold){
			getUserFriends2(i+"_UserIdFriends_test.txt", "flickr-links.txt", "UserId/(train_1part)class_1_5fold_all/"+i+"/test.txt");	
		}

		//for line
		/*getUserFriendsNet("73284_Friends.txt","73284_Friends_fri.txt","flickr-links.txt", "1.txt", "2.txt", "3.txt", "4.txt", "5.txt");
		int[] ratio = {1,2,3,4};
		int[] fold = {0,1,2,3,4};
		for(int i : ratio){
			for(int j : fold){
				getUserFriendsNet(i+"_"+j+"_Friends_train.txt",i+"_"+j+"_Friends_fri_train.txt","flickr-links.txt", 
						"UserId/(train_"+i+"part)class_1_5fold_all/"+j+"/train.txt", 
						"UserId/(train_"+i+"part)class_2_5fold_all/"+j+"/train.txt",
						"UserId/(train_"+i+"part)class_3_5fold_all/"+j+"/train.txt",
						"UserId/(train_"+i+"part)class_4_5fold_all/"+j+"/train.txt",
						"UserId/(train_"+i+"part)class_5_5fold_all/"+j+"/train.txt");
			}
		}*/


		//for NVR
		//UserNumber = 70142
		//Avg FriNumber = 137.88912491802344
		//getUserFollows("UserIdFollows.txt", "UserIdWithoutFollow.txt", "flickr-links.txt", "1.txt", "2.txt", "3.txt", "4.txt", "5.txt");	
		//UserNumber = 514505
		//Avg FriNumber = 40.20483182865084
		//getUserFriends2("UserIdFriends_fri.txt", "flickr-links.txt", "UserIdFollows.txt");	
	}
	private static void saveUserFriends(String userFriendsFilename, Map<Integer, Set<Integer>> userFriendsMap) throws IOException {
		long friNumber = 0;
		long userNumber = 0;
		BufferedWriter w1 = new BufferedWriter(new FileWriter(new File(userFriendsFilename)));
		Iterator<Entry<Integer, Set<Integer>>> it = userFriendsMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Set<Integer>> entry = it.next();
			if(null==entry.getValue()){
				continue;
			}
			userNumber++;
			w1.write(entry.getKey()+"");
			friNumber += entry.getValue().size();
			for(Integer friId : entry.getValue()){
				w1.write("\t"+friId);
			}
			w1.write("\r\n");
		}
		System.out.println("UserNumber = " + userNumber);
		System.out.println("Avg FriNumber = "+ (friNumber/(double)userNumber));
		w1.flush();w1.close();
	}
	private static void saveUserFriends(String userFriendsFilename, String userIdWithoutFriFilename,
			Map<Integer, Set<Integer>> userFriendsMap) throws IOException {
		long friNumber = 0;
		long userNumber = 0;
		BufferedWriter w1 = new BufferedWriter(new FileWriter(new File(userFriendsFilename)));
		BufferedWriter w2 = new BufferedWriter(new FileWriter(new File(userIdWithoutFriFilename)));
		Iterator<Entry<Integer, Set<Integer>>> it = userFriendsMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, Set<Integer>> entry = it.next();
			if(null==entry.getValue()){
				w2.write(entry.getKey()+"\r\n");
				continue;
			}
			userNumber++;
			w1.write(entry.getKey()+"");
			friNumber += entry.getValue().size();
			for(Integer friId : entry.getValue()){
				w1.write("\t"+friId);
			}
			w1.write("\r\n");
		}
		System.out.println("UserNumber = " + userNumber);
		System.out.println("Avg FriNumber = "+ (friNumber/(double)userNumber));
		w1.flush();w1.close();
		w2.flush();w2.close();
	}
	private static void getUserId(String userIdFilename,Map<Integer, Set<Integer>> userFriendsMap) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(userIdFilename)));
		String lineString = null;
		int uid = 0;
		while(null!=(lineString  = bufferedReader.readLine())){
			uid = Integer.parseInt(lineString);
			userFriendsMap.put(uid, null);
		}
		bufferedReader.close();
	}
	private static void getUserId(String userIdFilename,Set<Integer> userFriendsSet) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(userIdFilename)));
		String lineString = null;
		int uid = 0;
		while(null!=(lineString  = bufferedReader.readLine())){
			uid = Integer.parseInt(lineString.split("\t")[0]);
			userFriendsSet.add(uid);
		}
		bufferedReader.close();
	}
	/*private static void saveUserId(String resFilename, Set<Integer> set) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resFilename)));
		Iterator<Integer> it = set.iterator();
		while(it.hasNext()){ 
			bw.write(it.next()+"\r\n");
		}
		bw.flush();bw.close();
	}*/
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

}
