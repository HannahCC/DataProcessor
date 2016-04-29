package similarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ExpandSeed {

	static ExpandSeed es = new ExpandSeed();
	static SimilarityMeasurement sm = new SimilarityMeasurement();

	// 将词向量加载到Map中<String,[]>
	public Map<String, float[]> loadVec(String vecFile) throws IOException {
		FileReader fr = new FileReader(vecFile);
		BufferedReader br = new BufferedReader(fr);

		Map<String, float[]> vector = new HashMap<String, float[]>();
		String[] ss = null;
		String line = null;
		br.readLine();
		br.readLine();
		while ((line = br.readLine()) != null) {

			ss = line.split("\\s{1,}");
			float[] vec = new float[ss.length - 1];
			for (int i = 0; i < ss.length - 1; i++) {
				vec[i] = Float.parseFloat(ss[i + 1]);
				vector.put(ss[0], vec);
			}
		}

		br.close();
		fr.close();

		return vector;
	}

	// 将词向量加载到Map中<String,String>
	public Map<String, String> loadVecString(String vecFile) throws IOException {
		FileReader fr = new FileReader(vecFile);
		BufferedReader br = new BufferedReader(fr);
		
		Map<String, String> vector = new HashMap<String, String>();
		String[] ss = null;
		String line = null;
		br.readLine();
		br.readLine();
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}", 2);
			vector.put(ss[0], ss[1]);
		}

		br.close();
		fr.close();
		//fis.close();
		return vector;
	}

	// 表示出seed里每个词的词向量
	public void writeSeedVec(String seedFile, String seedVecFile, String wordVecFile) throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedVecFile);

		Map<String, String> wordVecStr = es.loadVecString(wordVecFile);
		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			for (int i = 0; i < ss.length; i++) {
				if (wordVecStr.containsKey(ss[i])) {
					fw.write(ss[i] + "\t");
					fw.write(wordVecStr.get(ss[i]) + "\n");
				}
			}
		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}
	
	public void writeSeedExpandVec(String seedFile, String seedVecFile, String wordVecFile, String word_400VecFile) throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileReader fr4 = new FileReader(word_400VecFile);
		BufferedReader br4 = new BufferedReader(fr4);
		FileWriter fw = new FileWriter(seedVecFile);

		Map<String, String> wordVecStr = es.loadVecString(wordVecFile);
		String line = null;
		String line4 = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			for (int i = 0; i < ss.length; i++) {
				if (wordVecStr.containsKey(ss[i])) {
					fw.write(ss[i] + "\t");
					fw.write(wordVecStr.get(ss[i]) + "\n");
				}
			}
		}
		
		while ((line4 = br4.readLine()) != null) {
			fw.write(line4+"\n");
		}

		br.close();
		br4.close();
		fw.flush();
		fw.close();
		fr.close();
		fr4.close();
	}
	

	// 用平均词向量表示seed里的每一类
	public Map<String, float[]> seedAvgVec(String seedFile, String seedVecFile) throws IOException {
		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		// FileWriter fw=new FileWriter(seedExpandFile);

		Map<String, float[]> vector = new HashMap<String, float[]>();
		Map<String, float[]> seedVec = es.loadVec(seedVecFile);
		String line = null;
		String[] ss = null;

		while ((line = br.readLine()) != null) {
			ss = line.split("\t",2)[0].split("##");
			float[] seedAvgVec = new float[100];
			int count = 0;
			for (int i = 0; i < ss.length; i++) {
				if (seedVec.containsKey(ss[i])) {
					for (int j = 0; j < seedVec.get(ss[i]).length; j++) {
						seedAvgVec[j] += seedVec.get(ss[i])[j];
					}
					count += 1;
				} else {

				}
			}

			if (count != 0) {
				for (int i = 0; i < seedAvgVec.length; i++) {
					seedAvgVec[i] = seedAvgVec[i] / count;
				}
				vector.put(line, seedAvgVec);
			} else {

			}

		}

		br.close();
		fr.close();
		return vector;
	}
	
	// 用点积表示每一类seed向量里的每一维
	public Map<String, float[]> seedMultiVec(String seedFile, String seedVecFile) throws IOException {
		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		// FileWriter fw=new FileWriter(seedExpandFile);

		Map<String, float[]> vector = new HashMap<String, float[]>();
		Map<String, float[]> seedVec = es.loadVec(seedVecFile);
		String line = null;
		String[] ss = null;

		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			float[] seedMultiVec = new float[100];
			for(int i = 0; i < seedMultiVec.length; i++) {
				seedMultiVec[i] = 1;
			}
		
			for (int i = 0; i < ss.length; i++) {
				if (seedVec.containsKey(ss[i])) {
					for (int j = 0; j < seedVec.get(ss[i]).length; j++) {
						seedMultiVec[j] *= seedVec.get(ss[i])[j];
					}
					
				} else {

				}
			}
			vector.put(line, seedMultiVec);

		}

		br.close();
		fr.close();
		return vector;
	}

	// 设置阈值，进行seed的扩展
	public void expandSeed(String seedFile, String seedVecFile, String wordVecFile, String seedExpandFile)
			throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedExpandFile);

		Map<String, float[]> seedVec = es.loadVec(seedVecFile);
		Map<String, float[]> wordVec = es.loadVec(wordVecFile);
		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			fw.write(line + "\t");
			for (Entry<String, float[]> entry : wordVec.entrySet()) {
				entry.getKey();
				entry.getValue();
				boolean bl = false;
				int j = 0;
				// double ts=0.5;
				for (int i = 0; i < ss.length; i++) {
					if (seedVec.containsKey(ss[i])) {
						double cos = sm.calVecCos(entry.getValue(), seedVec.get(ss[i]));
						if (cos < 0.7) {
							bl = false;
							break;
						} else
							bl = true;
					} else {
						j += 1;
						// ts+=0.1;
					}
				}
				// if(sm.calVecCos(entry.getValue(), seedVec.get(ss[0]))>0&&)
				if (bl && j != ss.length) {
					fw.write(entry.getKey() + "\t");
				}
			}

			fw.write("\n");

		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}
	
	// 取最相似的n个词，扩展seed,用最大相似度表示点和集合相似度
	public void expandSeedMax(String seedFile, String seedVecFile, String wordVecFile, String seedExpandFile)
			throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedExpandFile);

		Map<String, float[]> seedVec = es.loadVec(seedVecFile);
		
		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			fw.write(line + "\t");
			ss = line.split("\\s{1,}");
			// for(int j = 0; j<ss.length;j++) {

			Map<String, Double> wordCos = new HashMap<String, Double>();
			
			Map<String, float[]> wordVec = es.loadVec(wordVecFile);
			for (Entry<String, float[]> entry : wordVec.entrySet()) {
				entry.getKey();
				entry.getValue();
				if (sm.equalsString(line, entry.getKey())) {
				} else {
					double cos = sm.maxCos(entry.getValue(), line, seedVec);
					wordCos.put(entry.getKey(), cos);
				}
			}

			List<Map.Entry<String, Double>> list_wordCos = new ArrayList<Map.Entry<String, Double>>(wordCos.entrySet());
			// 通过Collections.sort(List I,Comparator c)方法进行排序
			Collections.sort(list_wordCos, new Comparator<Map.Entry<String, Double>>() {

				@Override
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
					if (o1.getValue() < o2.getValue())
						return 1;
					// if (o1.getValue() > o2.getValue())
					else
						return -1;

				}
			});
			for (int i = 0; i < 25; i++) {
				fw.write(list_wordCos.get(i).getKey() + "\t");
			}

			fw.write("\n");
		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}
	
	// 取最相似的n个词，扩展seed
	public void expandSeedMulti(String seedFile, String seedVecFile, String wordVecFile, String seedExpandFile)
			throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedExpandFile);

		Map<String, float[]> wordVec = es.loadVec(wordVecFile);
		//Map<String, float[]> seedAorMVec = es.seedAvgVec(seedFile, seedVecFile);
		Map<String, float[]> seedAorMVec = es.seedMultiVec(seedFile, seedVecFile);

		String line = null;
		// String[] ss = null;
		while ((line = br.readLine()) != null) {
			fw.write(line + "\t");
			// ss = line.split("\\s{1,}");
			if (seedAorMVec.containsKey(line)) {

				Map<String, Double> wordCos = new HashMap<String, Double>();
				
				for (Entry<String, float[]> entry : wordVec.entrySet()) {
					entry.getKey();
					entry.getValue();
					if (sm.equalsString(line, entry.getKey())) {
					} else {
						double cos = sm.calVecCos(entry.getValue(), seedAorMVec.get(line));
						wordCos.put(entry.getKey(), cos);
					}
				}
				List<Map.Entry<String, Double>> list_wordCos = new ArrayList<Map.Entry<String, Double>>(
						wordCos.entrySet());
				// 通过Collections.sort(List I,Comparator c)方法进行排序
				Collections.sort(list_wordCos, new Comparator<Map.Entry<String, Double>>() {

					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						if (o1.getValue() < o2.getValue())
							return 1;
						// if (o1.getValue() > o2.getValue())
						else
							return -1;

					}
				});
				for (int i = 0; i < 25; i++) {
					fw.write(list_wordCos.get(i).getKey() + "\t");
				}
			}

			fw.write("\n");
		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}
	
	// 取最相似的n个词，扩展seed
	public void expandSeedAvg(String seedFile, String seedVecFile, String wordVecFile, String seedExpandFile)
			throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedExpandFile);

		Map<String, float[]> wordVec = es.loadVec(wordVecFile);
		Map<String, float[]> seedAorMVec = es.seedAvgVec(seedFile, seedVecFile);
		// Map<String, float[]> seedAorMVec = es.seedMultiVec(seedFile,
		// seedVecFile);

		String line = null;
		// String[] ss = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			String[] line_item = line.split("\t", 2);
			fw.write(line_item[0]);
			// ss = line.split("\\s{1,}");
			if (seedAorMVec.containsKey(line)) {

				Map<String, Double> wordCos = new HashMap<String, Double>();
				
				for (Entry<String, float[]> entry : wordVec.entrySet()) {
					entry.getKey();
					entry.getValue();
					if (sm.equalsString(line, entry.getKey())) {
					} else {
						double cos = sm.calVecCos(entry.getValue(), seedAorMVec.get(line));
						wordCos.put(entry.getKey(), cos);
					}
				}
				List<Map.Entry<String, Double>> list_wordCos = new ArrayList<Map.Entry<String, Double>>(
						wordCos.entrySet());
				// 通过Collections.sort(List I,Comparator c)方法进行排序
				Collections.sort(list_wordCos, new Comparator<Map.Entry<String, Double>>() {

					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						if (o1.getValue() < o2.getValue())
							return 1;
						// if (o1.getValue() > o2.getValue())
						else
							return -1;

					}
				});
				for (int i = 0; i < 25; i++) {
					fw.write("##"+list_wordCos.get(i).getKey());
				}
			}

			fw.write("\t"+line_item[1]+"\r\n");
		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}

	// 计算欧式距离，取最相似的n个词，扩展seed
	public void expandSeedByDistance(String seedFile, String seedVecFile, String wordVecFile, String seedExpandFile)
			throws IOException {

		FileReader fr = new FileReader(seedFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(seedExpandFile);

		// Map<String, float[]> seedVec = es.loadVec(seedVecFile);
		Map<String, float[]> wordVec = es.loadVec(wordVecFile);
		Map<String, float[]> seedAvgVec = es.seedAvgVec(seedFile, seedVecFile);

		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			fw.write(line + "\t");
			ss = line.split("\\s{1,}");
			if (seedAvgVec.containsKey(line)) {

				Map<String, Double> wordDistance = new HashMap<String, Double>();

				for (Entry<String, float[]> entry : wordVec.entrySet()) {
					entry.getKey();
					entry.getValue();
					if (entry.getKey().equals(ss[0]) || entry.getKey().equals(ss[1]) || entry.getKey().equals(ss[2])) {
					} else {
						double distance = sm.calEuDistance(entry.getValue(), seedAvgVec.get(line));
						wordDistance.put(entry.getKey(), distance);
					}
				}
				List<Map.Entry<String, Double>> list_wordDistance = new ArrayList<Map.Entry<String, Double>>(
						wordDistance.entrySet());
				// 通过Collections.sort(List I,Comparator c)方法进行排序
				Collections.sort(list_wordDistance, new Comparator<Map.Entry<String, Double>>() {

					@Override
					public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
						if (o1.getValue() < o2.getValue())
							return -1;
						// if (o1.getValue() > o2.getValue())
						else
							return 1;

					}
				});
				for (int i = 0; i < 24; i++) {
					fw.write(list_wordDistance.get(i).getKey() + "\t");
				}
			}

			fw.write("\n");
		}

		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}

	public void writeSeed3(String seedExpandFile, String path, int num) throws IOException {
		FileReader fr = new FileReader(seedExpandFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(path+num+".txt");
		
		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			for (int i = 0; i<(num+3); i++) {
				fw.write (ss[i] + "\t");
			}
			fw.write("\n");
		}
		
		br.close();
		fw.flush();
		fw.close();
		fr.close();
		
	}
	
	public void writeSeed5(String seedExpandFile, String path, int num) throws IOException {
		FileReader fr = new FileReader(seedExpandFile);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(path+num+".txt");
		
		String line = null;
		String[] ss = null;
		while ((line = br.readLine()) != null) {
			ss = line.split("\\s{1,}");
			for (int i = 0; i<(num+5); i++) {
				fw.write (ss[i] + "\t");
			}
			fw.write("\n");
		}
		
		br.close();
		fw.flush();
		fw.close();
		fr.close();
		
	}
	
	public static void main(String[] args) throws IOException {

        String path = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR400_Good\\";
		String path2 = "D:\\Project_DataMinning\\DataSource\\BaikeVector\\";
		es.expandSeedAvg(path+"Config\\Dict_SrcNewType.txt",path+"Config\\Dict_SrcNewType_word_vector_inSrcDesc.txt",path+"Feature_Src\\Src_Description_vector.txt",path+"Config\\Dict_SrcNewType_Expand25.txt");
		es.expandSeedAvg(path+"Config\\Dict_AppNewType.txt",path+"Config\\Dict_AppNewType_word_vector_inAppDesc.txt",path+"Feature_Src\\App_Description_vector.txt",path+"Config\\Dict_AppNewType_Expand25.txt");
		//es.expandSeedAvg(path+"Config\\Dict_UserNewType.txt",path+"Config\\Dict_UserNewType_100_vector.txt",path+"Feature_Relation\\VUser_Description_vector.txt",path+"Config\\Dict_UserNewType_Expand25.txt");

	}

}
