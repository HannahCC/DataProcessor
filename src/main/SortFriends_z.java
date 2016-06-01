package main;

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

public class SortFriends_z {
	private static final String path = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	
	public static Map<String,Integer> calIdFre(String input) throws IOException {
		FileReader fr = new FileReader(input);
		BufferedReader br = new BufferedReader(fr);
		Map<String,Integer> idFre = new HashMap<String, Integer>();
		String line = null;
		String[] ss = null;
		while((line = br.readLine()) != null) {
			ss = line.trim().split("\\s{1,}");
			for(int i = 0; i < ss.length; i++) {
				if(idFre.containsKey(ss[i])) {
					int f = idFre.get(ss[i]);
					idFre.put(ss[i], f + 1);
				} else {
					idFre.put(ss[i], 1);
				}
			}
		}
		
		br.close();
		fr.close();
		return idFre;
	}
	
	public static void sortByFriNum(String input, String output) throws NumberFormatException, IOException {
		FileReader fr = new FileReader(input);
		BufferedReader br = new BufferedReader(fr); //缓存指定的输入文件
		FileWriter fw = new FileWriter(output);
		Map<String, Integer> idFre = calIdFre(input);
		String line = null;
		String[] ss = null;
		while((line = br.readLine()) != null) {
			Map<String, Integer> uidAndFriNum = new HashMap<String, Integer>();
			ss = line.split("\\s{1,}");
			fw.write(ss[0] + "\t");
			if (ss.length > 1) {
				for(int i = 1; i < ss.length; i++) {
					int num = idFre.get(ss[i]);
					uidAndFriNum.put(ss[i], num);
				}
				List<Map.Entry<String, Integer>> list_uidAndFriNum = new ArrayList<Map.Entry<String, Integer>>(uidAndFriNum.entrySet());
				// 通过Collections.sort(List I,Comparator c)方法进行排序
				Collections.sort(list_uidAndFriNum, new Comparator<Map.Entry<String, Integer>>() {

					@Override
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						if (o1.getValue() < o2.getValue())
							return 1;
						else if (o1.getValue() > o2.getValue())
							return -1;
						else
							return 0;

					}
				});
				for (int i = 0; i < list_uidAndFriNum.size(); i++) {
					fw.write(list_uidAndFriNum.get(i).getKey() + "\t");
				}
				fw.write("\n");
			} else {
				fw.write("\n");
			}
		}
		
		br.close();
		fw.flush();
		fw.close();
		fr.close();
	}

	public static void main(String args[]) throws NumberFormatException, IOException{
		sortByFriNum(path+"Vector\\UserIdFriends\\UserIdFriends_full_sorted1.txt", path+"Vector\\UserIdFriends\\UserIdFriends_full_sorted2.txt");
	}
}
