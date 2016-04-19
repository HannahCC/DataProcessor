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

import utils.GetInfo;
import utils.SaveInfo;

public class GetVectorOfItem {


	static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";//Sina_NLPIRandTHUext1000_Mute_GenderPre\\";//
	static int ignore = 2; //读取vector文件时（loadWordVec）时忽略前面几行？word2vec生成的2行，line生成的1行
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

		//Text
		//每个用户对应一个文件，每个微博对应一行，===》每个微博得到一个向量，每个用户得到一个向量
		/*avgVecForWeibo(PATH+"Weibos_Text", PATH+"Vector\\2223_Win8_L200_Text_vector.txt",
				PATH+"Vector\\Weibos_Text", PATH+"Feature_Textual\\2223_Win8_L200_TextAvgVec_feature.txt");
		avgVecForWeibo(PATH+"Weibos_Text", PATH+"Vector\\2223_Win8_L300_Text_vector.txt",
				PATH+"Vector\\Weibos_Text", PATH+"Feature_Textual\\2223_Win8_L300_TextAvgVec_feature.txt");
		//POS
		avgVecForWeibo(PATH+"Weibos_POS", PATH+"Vector\\2223_Win8_L200_POS_vector.txt",
				PATH+"Vector\\Weibos_POS", PATH+"Feature_Textual\\2223_Win8_L200_POSAvgVec_feature.txt");
		avgVecForWeibo(PATH+"Weibos_POS", PATH+"Vector\\2223_Win8_L300_POS_vector.txt",
				PATH+"Vector\\Weibos_POS", PATH+"Feature_Textual\\2223_Win8_L300_POSAvgVec_feature.txt");*/

		//Tag
		/*avgVec(PATH+"Feature_UserInfo\\UserIdTag.txt",PATH+"Vector\\2223_Win8_L100_Tag_fri_vector.txt",
				PATH+"Feature_UserInfo\\TagAvgVec_fri_feature.txt");*/
		avgVec(PATH+"Feature_UserInfo\\UserIdTag.txt",PATH+"Vector\\2223_Win8_L100_Tag_vfri_vector.txt",
				PATH+"Feature_UserInfo\\TagAvgVec_vfri_feature.txt");
		
		//Src
		/*avgVec(PATH+"Feature_UserInfo\\UserIdSrc.txt",PATH+"Vector\\2223_Win500_L100_Src_fri_vector.txt",
				PATH+"Feature_Src\\SrcAvgVec_fri_feature.txt");
		length = loadWordVecFromFeature(wordVec, PATH+"Feature_Src\\SrcAvgVec_fri_feature.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriSrcAvgVec_fri_feature.txt");
		wordVec.clear();
		avgVec(PATH+"Feature_UserInfo\\UserIdSrc.txt",PATH+"Vector\\2223_Win500_L100_Src_vfri_vector.txt",
				PATH+"Feature_Src\\SrcAvgVec_vfri_feature.txt");
		length = loadWordVecFromFeature(wordVec, PATH+"Feature_Src\\SrcAvgVec_vfri_feature.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriSrcAvgVec_vfri_feature.txt");
		wordVec.clear();*/
		
		//Fri
		//根据用户的向量（w2v生成），得到用户的特征
		//根据用户的朋友，以及朋友的向量（W2V训练生成），得到用户特征向量（朋友求平均）
		/*length = loadWordVec(wordVec, PATH+"Vector\\2223_Win500_L100_Fri_fri_vector.txt");
		vec2Feature(PATH+"ExpandID0.txt",wordVec,PATH+"Feature_Relation\\FriVec_fri_feature.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",wordVec, length,PATH+"Feature_Relation\\FriAvgVec_fri_feature.txt");
		wordVec.clear();*/
		/*length = loadWordVec(wordVec, PATH+"Vector\\2223_Win500_L100_VFri_vfri_vector.txt");
		vec2Feature(PATH+"ExpandID0.txt",wordVec,PATH+"Feature_Relation\\VFriVec_vfri_feature.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",wordVec, length,PATH+"Feature_Relation\\VFriAvgVec_vfri_feature.txt");
		wordVec.clear();*/
		
		//根据用户的向量（Line生成），得到用户的特征
		//根据用户的朋友，以及朋友的向量（Line生成），得到用户特征向量（朋友求平均）
		/*wordVec.clear();
		length = loadWordVec(wordVec, PATH+"Line\\2333_s10000n5d128\\vec_all.txt");
		vec2Feature(PATH+"ExpandID0.txt",wordVec,PATH+"Feature_Relation\\FriVec_Lines10000n5d128_feature.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriAvgVec_Lines10000n5d128_feature.txt");*/

		
		//Fri+Tag/Src
		avgList(PATH+"Feature_UserInfo\\UserIdTag_fri.txt", PATH+"Vector\\2223_Win8_L100_Tag_fri_vector.txt",PATH+"Vector\\2223_Win8_L100_TagAvg_fri_vector.txt");
		length = loadWordVec(wordVec, PATH+"Vector\\2223_Win8_L100_TagAvg_fri_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriTagAvgVec_fri_feature.txt");
		wordVec.clear();
		avgList(PATH+"Feature_UserInfo\\UserIdTag_vfri.txt", PATH+"Vector\\2223_Win8_L100_Tag_vfri_vector.txt",PATH+"Vector\\2223_Win8_L100_TagAvg_vfri_vector.txt");
		length = loadWordVec(wordVec, PATH+"Vector\\2223_Win8_L100_TagAvg_vfri_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriTagAvgVec_vfri_feature.txt");
		wordVec.clear();
		/*avgList(PATH+"Feature_UserInfo\\UserIdSrc.txt", PATH+"Vector\\2223_Win8_L100_Src_fri_vector.txt",PATH+"Vector\\2223_Win8_L100_SrcAvg_fri_vector.txt");
		length = loadWordVec(wordVec, PATH+"Vector\\2223_Win8_L100_SrcAvg_fri_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriSrcAvgVec_fri_feature.txt");
		wordVec.clear();
		avgList(PATH+"Feature_UserInfo\\UserIdSrc.txt", PATH+"Vector\\2223_Win8_L100_Src_vfri_vector.txt",PATH+"Vector\\2223_Win8_L100_SrcAvg_vfri_vector.txt");
		length = loadWordVec(wordVec, PATH+"Vector\\2223_Win8_L100_SrcAvg_vfri_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",wordVec,length,PATH+"Feature_Relation\\FriSrcAvgVec_vfri_feature.txt");
		wordVec.clear();*/
		/*============================================暂时不用的向量特征=================================================*/
		/*//将Src描述分词，利用分词后的文件训练得到描述词的向量，得到Src的向量
		avgList(PATH+"Feature_Src\\Src_Description.txt.parsed.clr", PATH+"Vector\\2223_Win8_L100_SrcDesc_vector.txt",
				PATH+"Vector\\SrcDescAvg_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdSrc.txt",PATH+"Vector\\2223_Win8_L100_SrcDescAvg_vector.txt",
				PATH+"Feature_Src\\SrcDescAvgVec_feature.txt");
		//将Src描述分词，从Baike词向量文件中获取描述词的向量，得到Src的向量
		avgList(PATH+"Feature_Src\\Src_Description.txt.parsed.clr","D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_SrcDescAvg_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdSrc.txt",PATH+"Vector\\SrcDescAvg_Baike_vector.txt",
				PATH+"Feature_Src\\Baike_SrcDescAvgVec_feature.txt");

		//将User描述分词，利用分词后的文件训练得到描述词的向量，得到VUser的向量
		avgList(PATH+"Feature_UserInfo\\User_Description.txt.parsed.clr", PATH+"Vector\\2223_Win8_L100_UserDesc_vector.txt",
				PATH+"Vector\\2223_Win8_L100_UserDescAvg_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",PATH+"Vector\\2223_Win8_L100_UserDescAvg_vector.txt",
				PATH+"Feature_Relation\\FriDescAvgVec_feature.txt");
		//将User描述分词，从Baike词向量文件中获取描述词的向量，得到Src的向量
		avgList(PATH+"Feature_Relation\\User_Description.txt.parsed.clr", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_UserDescAvg_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdFriends.txt",PATH+"Vector\\UserDescAvg_Baike_vector.txt",
				PATH+"Feature_Relation\\Baike_FriDescAvgVec_feature.txt");

		//将VUser描述分词，利用分词后的文件训练得到描述词的向量，得到VUser的向量
		avgList(PATH+"Feature_UserInfo\\VUser_Description.txt.parsed.clr", PATH+"Vector\\2223_Win8_L100_VUserDesc_vector.txt",
				PATH+"Vector\\2223_Win8_L100_VUserDescAvg_vector.txt");
		avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",PATH+"Vector\\2223_Win8_L100_VUserDescAvg_vector.txt",
				PATH+"Feature_Relation\\VFriDescAvgVec_feature.txt");
		//将VUser描述分词，从Baike词向量文件中获取描述词的向量，得到Src的向量
		avgList(PATH+"Feature_Relation\\VUser_Description.txt.parsed.clr", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
						PATH+"Vector\\Baike_VUserDescAvg_vector.txt");
				avgVec(PATH+"Feature_UserInfo\\UserIdVFriends.txt",PATH+"Vector\\VUserDescAvg_Baike_vector.txt",
						PATH+"Feature_Relation\\Baike_VFriDescAvgVec_feature.txt");*/
		/*================================================================================================================*/
		/*//手动生成各类别的种子类别词，从Baike词向量文件中获取类别词的向量
		avgList(PATH+"Config\\Dict_SrcType.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_SrcType_vector.txt");
		avgList(PATH+"Config\\Dict_SrcNewType.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_SrcNewType_vector.txt");
		avgList(PATH+"Config\\Dict_UserType.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_UserType_vector.txt");
		avgList(PATH+"Config\\Dict_UserNewType.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_UserNewType_vector.txt");

		//手动生成各类别的种子类别词，经过相似度计算拓展后，从Baike词向量文件中获取类别词的向量
		avgList(PATH+"Config\\Dict_SrcType_BaiKeExpand25.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_SrcType_BaiKeExpand25_vector.txt");
		avgList(PATH+"Config\\Dict_SrcNewType_BaiKeExpand25.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_SrcNewType_BaiKeExpand25_vector.txt");
		avgList(PATH+"Config\\Dict_UserType_BaiKeExpand25.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_UserType_BaiKeExpand25_vector.txt");	
		avgList(PATH+"Config\\Dict_UserNewType_BaiKeExpand25.txt", "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt",
				PATH+"Vector\\Baike_Dict_UserNewType_BaiKeExpand25_vector.txt");*/

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
			wordVec.put(key, vec);
		}

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
		if(count==0){
			return null;
		}
		for (int i = 0; i < length; i++) {
			avgVec[i] /= count;
		}
		return avgVec;
	}

	public static void avgVecForWeibo(String wordDir, String wordVecFile,
			String avgVecDir, String userAvgVecFile) throws IOException {
		File dir = new File(wordDir);
		if(!dir.isDirectory()){return;}
		SaveInfo.mkdir(avgVecDir);
		Map<String, float[]> wordVec = new HashMap<String, float[]>(0xffff);
		int length = loadWordVec(wordVec, wordVecFile); // 获取所有微博Text/POS的向量
		BufferedWriter fw0 = new BufferedWriter(new FileWriter(new File(userAvgVecFile)));
		File[] wordFiles = dir.listFiles();
		for(File wordFile : wordFiles){
			if(wordFile.getName().startsWith("Weibos")){continue;}
			BufferedReader brtag = new BufferedReader(new FileReader(wordFile));
			BufferedWriter fw = new BufferedWriter(new FileWriter(new File(avgVecDir+"\\"+wordFile.getName())));
			String tline = null;
			String[] ss = null;
			String uid = wordFile.getName().replace(".txt", "");
			/*fw.write("\r\n");
			fw.write("\r\n");*/
			float[] userAvgVec = new float[length];
			int count = 0;
			while ((tline = brtag.readLine()) != null) {
				ss = tline.split("\\s+");
				if (ss.length==0) {
					//System.out.println(tline);
					fw.write("\r\n");
				} else {
					float[] avgVec = getAvgVec(ss, length, wordVec);
					if(avgVec==null){//即没有在wordVec中找到任意一个词的向量
						System.out.println(uid+"不匹配-"+tline);
						fw.write("\r\n");
						continue;
					}
					count++;
					for (int i = 0; i < length; i++) {
						userAvgVec[i]+=avgVec[i];
						fw.write(avgVec[i] + " ");
					}
					fw.write("\r\n");
				}
			}
			brtag.close();
			fw.flush();
			fw.close();

			if(count==0){
				continue;
			}
			fw0.write(uid + "\t");
			for (int i = 0; i < length; i++) {
				userAvgVec[i] /= count;
				fw0.write(i + 1 + ":" + userAvgVec[i] + "\t");
			}
			fw0.write("\r\n");
		}

		fw0.flush();
		fw0.close();

	}

	// 将用户每个标签的词向量通过求平均的方式生成可输入SVM的用户向量
	public static void avgVec(String wordFile, Map<String, float[]> wordVec, int length, String avgVecFile) throws IOException {
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
	}
	public static void avgVec(String wordFile, String wordVecFile, String avgVecFile) throws IOException {
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
	}

	// 将用户每个标签的词向量通过求平均的方式生成向量文件
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
	}

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
	}

}

