package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import utils.GetInfo;

public class GetVectorOfItem {


	static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\//Sina_NLPIR2223_GenderPre\\";//Sina_NLPIRandTHUext1000_Mute_GenderPre\\";//"E:\\DataSource\\Youtube\\";////Sina_NLPIRandTHUext1000_Mute_GenderPre\\";//
	static String PATH2 = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	static int ignore = 2; //读取vector文件时（loadWordVec）时忽略前面几行？word2vec生成的2行，line生成的1行
	static boolean isSelf = true;//求平均时是否使用自己
	static boolean isNull = false;//当一个句子/一个用户的tag链、fri链，在词向量map中匹配不到任何一个向量，用全0向量代替这个句子/用户，还是用null代替这个用户，如果是null，相当于忽略这个句子/用户
	static boolean isStat = false;//是否统计测试用户有多少朋友得不到向量
	static int isNullCompletely = 0;//所有朋友（包括自己）都没有向量的用户数
	static int isNullPartialy = 0;//没有向量的朋友数
	static Set<String> isNullSet = new HashSet<String>();//没有向量的朋友集合（用来去重）
	static int friendsTotal = 0;//朋友总数
	static Set<String> friendsSet = new HashSet<String>();//朋友集合（用来去重）
	public static void main(String[] args) throws IOException {
		/*================================================================================================================*/
		/**
		 * 情景：求平均得到向量
		 * 每个用户对应的词
		 * 各个词的向量
		 * 结果文件：用户对应的向量
		 */


		Map<String, float[]> wordVec = new HashMap<String, float[]>();
		int length = 0;

		length = loadWordVec(wordVec, PATH2+"Vector\\1635_skn5wlr200l100i15_Self+Un2Fri_vector.txt");
		avgVecForUser(PATH2+"Vector\\UserIdFriends\\UserIdFriends_full.txt",wordVec, length,PATH+"Feature_Relation\\1635_Self+SUn2FriAvgVec_skn5wlr200l100i15_Train+Test_feature.txt",false);
		wordVec.clear();
		length = loadWordVec(wordVec, PATH2+"Vector\\1635_p2cbown5wcr100l100i15_Self+Un2Fri_vector.txt");
		avgVecForUser(PATH2+"Vector\\UserIdFriends\\UserIdFriends_full.txt",wordVec, length,PATH+"Feature_Relation\\1635_Self+SUn2FriAvgVec_p2cbown5wcr100l100i15_Train+Test_feature.txt",false);
		wordVec.clear();
	}

	private static void printStat(String info) {
		System.out.println(info);
		System.out.println(friendsTotal+"/"+friendsSet.size());
		System.out.println(isNullCompletely+"/"+isNullPartialy+"/"+isNullSet.size());
	}


	private static int loadWordVecFromFeature(Map<String, float[]> wordVec,	String wordVecFeatureFile) throws IOException {
		FileReader fr = new FileReader(wordVecFeatureFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		int len = 0;
		for(int i=0;i<ignore;i++){br.readLine();}
		while ((line = br.readLine()) != null) {
			String[] items = line.split("\t");
			len = items.length-1;
			String key = items[0];
			float[] vec = new float[len];
			for(int i=1;i<=len;i++){
				vec[i-1] = Float.parseFloat(items[i].split(":")[1]);
			}
			wordVec.put(key, vec);
		}

		br.close();
		fr.close();

		return len;
	}
	//将每个词和对应的词向量加载到Hashmap中
	private static int loadWordVec(Map<String, float[]> wordVec, String wordVecFile) throws IOException {
		int overrides = 0;
		FileReader fr = new FileReader(wordVecFile);
		BufferedReader br = new BufferedReader(fr);
		String key = null;
		String[] ss = null;
		String line = null;
		for(int i=0;i<ignore;i++){br.readLine();}
		while ((line = br.readLine()) != null) {
			if(line.split("\t", 2).length>1){
				key = line.split("\t",2)[0];
				ss = line.split("\t", 2)[1].split("\\s+");
			}else{
				key = line.split("\\s+",2)[0];
				ss = line.split("\\s+", 2)[1].split("\\s+");
			}
			float[] vec = new float[ss.length];
			for (int i = 0; i < ss.length; i++) {
				vec[i] = Float.parseFloat(ss[i]);
			}
			if(wordVec.containsKey(key)){
				//System.out.println("override " + key);
				overrides++;
			}
			wordVec.put(key, vec);
		}
		System.out.println(wordVecFile +" override vectors : " + overrides + "/" +wordVec.size());
		br.close();
		fr.close();

		return ss.length;
	}
	//计算数组ss中词的向量的均值
	private static float[] getAvgVec(String[] ss, int length, Map<String, float[]> wordVec) {
		int count = 0;
		float[] avgVec = new float[length];
		for (int j = 0; j < ss.length; j++) {//对每个词
			if (wordVec.containsKey(ss[j])) {
				count++;
				float[] vec = wordVec.get(ss[j]);
				for (int i = 0; i < length; i++) {
					avgVec[i] += vec[i];
				}
			}
		}
		if(isNull&&count==0){
			return null;
		}else if(count==0){
			for (int i = 0; i < length; i++) {
				avgVec[i] = 0;
			}
		}else{
			for (int i = 0; i < length; i++) {
				avgVec[i] /= count;
			}
		}
		return avgVec;
	}
	//计算数组ss中词的向量的均值
	private static float[] getAvgVec(String uid, String[] ss, int length, Map<String, float[]> wordVec) {
		if(isStat){
			friendsTotal+=ss.length;
			if(isSelf){
				friendsSet.add(uid);
				friendsTotal++;
			}
		}
		int count = 0;
		float[] avgVec = new float[length];
		if(isSelf&&wordVec.containsKey(uid)){
			count++;
			float[] vec = wordVec.get(uid);
			for (int i = 0; i < length; i++) {
				avgVec[i] += vec[i];
			}
		}else if(isSelf&&isStat){
			isNullPartialy++;
			isNullSet.add(uid);
		}
		for (int j = 0; j < ss.length; j++) {//对每个词
			if (wordVec.containsKey(ss[j])) {
				count++;
				float[] vec = wordVec.get(ss[j]);
				for (int i = 0; i < length; i++) {
					avgVec[i] += vec[i];
				}
			}else if(isStat){
				isNullPartialy++;
				isNullSet.add(ss[j]);
			}
			friendsSet.add(ss[j]);
		}
		if(isStat&&count==0)isNullCompletely++;
		if(isNull&&count==0){
			return null;
		}else if(count==0){
			for (int i = 0; i < length; i++) {
				avgVec[i] = 0;
			}
		}else{
			for (int i = 0; i < length; i++) {
				avgVec[i] /= count;
			}
		}
		return avgVec;
	}

	public static void avgVecForWeibo(Set<String> uids, String weiboTextDir,Map<String, float[]> wordVec, int length, String userTextAvgVecFile, boolean isAppend) throws IOException {
		if(!isNull){System.out.println("error!当没有在wordVec中找到一条微博的任意一个词的向量时，应选择忽略该微博");return;}
		BufferedWriter fw0 = new BufferedWriter(new FileWriter(new File(userTextAvgVecFile), isAppend));
		float[] userAvgVec = new float[length];
		for(String uid : uids){
			for (int i = 0; i < length; i++) {
				userAvgVec[i] = 0;
			}
			int count = 0;
			File weiboTextFile = new File(weiboTextDir+uid+".txt");
			if(!weiboTextFile.exists()){
				System.out.println("user" + uid +" 没有微博!");
			}else{
				BufferedReader brtag = new BufferedReader(new FileReader(weiboTextFile));
				String tline = null;
				String[] ss = null;
				while ((tline = brtag.readLine()) != null) {
					ss = tline.split("\\s+");
					if (ss.length!=0) {
						float[] avgVec = getAvgVec(ss, length, wordVec);
						if(avgVec==null){//即没有在wordVec中找到任意一个词的向量，忽略这个用户
							//System.out.println(uid+"没有在wordVec中找到此条微博的任意一个词的向量.");
							continue;
						}
						count++;
						for (int i = 0; i < length; i++) {
							userAvgVec[i]+=avgVec[i];
						}
					}
				}
				brtag.close();
				if(count==0){
					System.out.println("user" + uid +" 没有一条微博能够形成词向量!");
					//continue;
					//不使用continue，即用全0表示这个用户的特征向量
				}else{
					for (int i = 0; i < length; i++) {
						userAvgVec[i] /= count;
					}
				}
			}

			fw0.write(uid + "\t");
			for (int i = 0; i < length; i++) {
				fw0.write(i + 1 + ":" + userAvgVec[i] + "\t");
			}
			fw0.write("\r\n");
		}

		fw0.flush();
		fw0.close();

	}

	// 词向量已经存放在map中。将用户每个标签的词向量通过求平均的方式生成可输入SVM的用户向量
	public static void avgVecForUser(String wordFile, Map<String, float[]> wordVec, int length, String avgVecFile, boolean isAppend) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(wordFile)));
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(avgVecFile),isAppend));
		String tline = null;
		String[] ss = null;
		String uid = null;
		while ((tline = br.readLine()) != null) {
			ss = tline.split("\t", 2)[1].split("\t");
			uid = tline.split("\t",2)[0];
			if (ss.length==0) {
				System.out.println(tline);
				continue;
			} else {
				float[] avgVec = getAvgVec(uid, ss, length, wordVec);
				if(isNull&&avgVec==null){//即没有在wordVec中找到任意一个词的向量
					System.out.println(uid+"不匹配-"+tline);
					continue;
				}
				fw.write(uid + "\t");
				for (int i = 0; i < length; i++) {
					fw.write(i + 1 + ":" + avgVec[i] + "\t");
				}
				fw.write("\r\n");
			}
		}
		br.close();
		fw.flush();
		fw.close();
		System.out.println(avgVecFile);
	}
	// 将用户每个标签的词向量通过求平均的方式生成可输入SVM的用户向量
	/*	public static void avgVec(String wordFile, String wordVecFile, String avgVecFile) throws IOException {
		//Map<String, float[]> wordVec,
		BufferedReader wbr = new BufferedReader(new FileReader(new File(wordFile)));
		Map<Long, long[]> user_word_map = new HashMap<Long, long[]>(3000);
		Map<Long, Integer> user_count_map = new HashMap<Long, Integer>(3000);
		String line = null;
		long uid;
		int i;
		StringTokenizer st = null;
		while ((line = wbr.readLine()) != null) {
			st = new StringTokenizer(line,"\t");
			uid = Long.parseLong(st.nextToken().intern());
			long[] user_word = new long[st.countTokens()];
			i = 0;
			while(st.hasMoreTokens()){//对0层用户的每个朋友
				String tmp = st.nextToken().intern();
				user_word[i++] = Long.parseLong(tmp);
			}
			user_word_map.put(uid, user_word);
			user_count_map.put(uid, 0);
		}
		wbr.close();

		Map<Long, float[]> user_feature_map = new HashMap<Long, float[]>(3000);
		BufferedReader vbr = new BufferedReader(new FileReader(new File(wordVecFile)));
		line = vbr.readLine();
		int length = Integer.parseInt(line.split("\\s+")[1]);
		vbr.readLine();
		long wordId;
		float[] vector = new float[length];
		Iterator<Entry<Long, long[]>> it = null;
		Entry<Long, long[]> entry = null;
		while ((line = vbr.readLine()) != null) {
			st = new StringTokenizer(line," ");
			wordId = Long.parseLong(st.nextToken());
			i = 0;
			while(st.hasMoreTokens()){vector[i++] = Float.parseFloat(st.nextToken());}
			it = user_word_map.entrySet().iterator();
			while(it.hasNext()){
				entry = it.next();
				uid = entry.getKey();
				int idx = Arrays.binarySearch(entry.getValue(), wordId);
				if(idx!=-1){
					if(!user_feature_map.containsKey(uid)){user_feature_map.put(uid, new float[length]);}
					int number = user_count_map.get(uid);
					updateAvgVec(user_feature_map.get(uid),vector,number);
					user_count_map.put(uid, number+1);
				}
			}
		}
		wbr.close();
		vbr.close();

		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(avgVecFile)));
		Iterator<Entry<Long, float[]>> it1 = user_feature_map.entrySet().iterator();
		Entry<Long, float[]> entry1 = null;
		while(it1.hasNext()){
			entry1 = it1.next();
			uid = entry1.getKey();
			vector = entry1.getValue();
			fw.write(uid + "\t");
			for (int k = 0; k < length; k++) {
				fw.write(k + 1 + ":" + vector[k] + "\t");
			}
			fw.write("\r\n");
		}
		fw.flush();
		fw.close();
	}*/
	/*public static void avgVec(String wordFile, String wordVecFile, String avgVecFile) throws IOException {
		Map<String, float[]> wordVec = new HashMap<String, float[]>(0xffff);
		int length = loadWordVec(wordVec, wordVecFile); // 词向量维度
		FileReader frtag = new FileReader(wordFile);
		BufferedReader brtag = new BufferedReader(frtag);
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(avgVecFile)));

		String tline = null;
		String[] ss = null;
		String uid = null;
		while ((tline = brtag.readLine()) != null) {
			ss = tline.split("\t", 2)[1].split("\t");
			uid = tline.split("\t",2)[0];
			if (ss.length==0) {
				System.out.println(tline);
				continue;
			} else {
				float[] avgVec = getAvgVec(ss, length, wordVec);
				if(avgVec==null){//即没有在wordVec中找到任意一个词的向量
					System.out.println(uid+"不匹配-"+tline);
					continue;
				}
				fw.write(uid + "\t");
				for (int i = 0; i < length; i++) {
					fw.write(i + 1 + ":" + avgVec[i] + "\t");
				}
				fw.write("\r\n");
			}
		}
		brtag.close();
		fw.flush();
		fw.close();
		frtag.close();
	}*/

	/*private static void updateAvgVec(float[] vector, float[] add_vector, int number) {
		int size = vector.length;
		for(int i=0;i<size;i++){vector[i]=(vector[i]*number+add_vector[i])/(number+1);}
	}*/


	/*// 将用户每个标签的词向量通过求平均的方式生成向量文件
	public static void avgList(String wordFile, String wordVecFile, String avgListFile) throws IOException {
		Map<String, float[]> wordVec = new HashMap<String, float[]>(0xffff);
		int length = loadWordVec(wordVec, wordVecFile); // 词向量维度
		FileReader frtag = new FileReader(wordFile);
		BufferedReader brtag = new BufferedReader(frtag);
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(avgListFile)));

		String tline = null;
		String[] ss = null;
		String uid = null;
		fw.write("\r\n");
		fw.write("\r\n");
		while ((tline = brtag.readLine()) != null) {
			uid = tline.split("\t",2)[0];
			ss = tline.split("\t",2)[1].split("\\s+");
			if (ss.length==0) {
				System.out.println(tline);
				continue;
			} else {
				float[] avgVec = getAvgVec(ss, length, wordVec);
				if(avgVec==null){//即没有在wordVec中找到任意一个词的向量
					System.out.println(uid+"不匹配-"+tline);
					continue;
				}
				fw.write(uid + "\t");
				for (int i = 0; i < length; i++) {
					fw.write(avgVec[i] + " ");
				}
				fw.write("\r\n");
			}
		}

		brtag.close();
		fw.flush();
		fw.close();
		frtag.close();
	}*/

	public static void vec2Feature(String idFile,  Map<String, float[]> vecs, String featureFile) throws IOException {
		Set<String> ids = new HashSet<String>(2223);
		GetInfo.getSet(idFile, ids);

		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(featureFile)));
		for(String id : ids){
			if(vecs.containsKey(id)){
				float[] vec = vecs.get(id);
				fw.write(id + "\t");
				for (int i = 0; i < vec.length; i++) {
					fw.write(i + 1 + ":" + vec[i] + "\t");
				}
				fw.write("\r\n");
			}else{
				System.out.println(id+" has no vector!!!!!!!!!!!!!!!");
			}
		}
		fw.flush();
		fw.close();
		System.out.println(featureFile);
	}

	public static void vec2Feature(String idFile,String wordVecFile,String featureFile) throws IOException {
		Set<String> ids = new HashSet<String>(2223);
		GetInfo.getSet(idFile, ids);

		BufferedReader br = new BufferedReader(new FileReader(new File(wordVecFile)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(featureFile)));
		for(int i=0;i<ignore;i++){br.readLine();}
		String line = null;
		StringTokenizer st = null;
		int i = 1;
		while(null!=(line=br.readLine())){
			st = new StringTokenizer(line," ");
			String uid = st.nextToken();
			if(ids.contains(uid)){
				bw.write(uid + "\t");
				i = 1;
				while(st.hasMoreTokens()){
					bw.write(i++ + ":" + st.nextToken() + "\t");
				}
				bw.write("\r\n");

			}
		}
		bw.flush();
		bw.close();
		br.close();
		System.out.println(featureFile);
	}
}

