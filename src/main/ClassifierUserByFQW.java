package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.GetInfo;
import utils.SaveInfo;
import utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * ※提取企业用户（描述、昵称、tag）的高频词，从普通用户中识别出企业用户
	-UserItem_ITEM_WORDSIZE：存储用户ITEM属性切分成长度为WORDSIZE（tags默认为2）后得到的按频次排序的词组列表
	-UserItem_ITEM_WORDSIZE.user：存储根据UserItem_ITEM_WORDSIZE中的高频词识别的用户
 * @author Hannah
 *
 */
public class ClassifierUserByFQW {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON\\";
	public static String ITEM = "screenName";//"description";//
	public static int WORD_SIZE = 2;
	public static int THRESHOLD = 100;
	public static String TRAINUSER_PATH = "UserInfoOfEnterprise1.txt";//"\\UserInfo1.txt";//"
	public static String TESTUSER_PATH = "\\List2name.txt";//"\\UserInfo0.txt";
	public static void main(String args[]) throws IOException{
		/*getFreWordOfTag();
		getUserByTag();*/


		//getFreWordOfItem();
		//getSuffixOfItem();
		//getUserByItem();

		getUserByItemTmp();
	}
	/**********************************************获取集体用户高频词****************************************************************/
	/**
	 * 根据用户某一个字段提取高频词
	 */
	public static void getFreWordOfItem() throws IOException {
		File f1 = new File(PATH+TRAINUSER_PATH);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		Map<String,Integer> word_map = new HashMap<String,Integer>();
		String line;
		while((line = br.readLine())!=null){
			if(line.equals("")){continue;}
			JSONObject user = JSONObject.fromObject(line);
			int userType = user.getInt("verifiedType");
			if(userType!=-1)continue;
			String item_str = user.getString(ITEM);
			getwords(word_map,item_str,WORD_SIZE);
		}
		br.close();
		List<String> list = new ArrayList<String>();
		Utils.mapSortByValueInteger(list, word_map);
		SaveInfo.saveList(PATH,"//UserItem_"+ITEM+"_"+WORD_SIZE+".txt", list, false);
	}
	/**
	 * 根据用户某一个字段提取尾词
	 */
	public static void getSuffixOfItem() throws IOException {
		File f1 = new File(PATH+TRAINUSER_PATH);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		Map<String,Integer> word_map = new HashMap<String,Integer>();
		String line;
		while((line = br.readLine())!=null){
			if(line.equals("")){continue;}
			JSONObject user = JSONObject.fromObject(line);
			int userType = user.getInt("verifiedType");
			if(userType!=-1)continue;
			String item_str = user.getString(ITEM);
			getsuffix(word_map,item_str,WORD_SIZE);
		}
		br.close();
		List<String> list = new ArrayList<String>();
		Utils.mapSortByValueInteger(list, word_map);
		SaveInfo.saveList(PATH,"//UserItem_"+ITEM+"_"+WORD_SIZE+"_suffix.txt", list, false);
	}
	/**
	 * 根据用户标签提取高频词(gram默认为2)
	 * @throws IOException
	 */
	public static void getFreWordOfTag() throws IOException {
		File f1 = new File(PATH+TRAINUSER_PATH);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		Map<String,Integer> word_map = new HashMap<String,Integer>();
		String line;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			JSONArray tags = user.getJSONObject("tags").getJSONArray("tags");
			gettags(word_map,tags);
		}
		br.close();
		List<String> list = new ArrayList<String>();
		Utils.mapSortByValueInteger(list, word_map);
		SaveInfo.saveList(PATH,"//UserItem_tags_2.txt", list, false);
	}
	/**********************************************获取集体用户高频词END****************************************************************/



	/**********************************************根据集体用户高频词获取集体用户****************************************************************/
	/**
	 * 根据高频词得到集体用户
	 * @param word_size
	 * @throws IOException
	 */
	public static void getUserByItem() throws IOException {
		Set<String> word_set = new HashSet<String>();
		GetInfo.getSet("//UserItem_"+ITEM+"_"+WORD_SIZE+".txt",word_set,":",0);
		File f0 = new File(PATH+TESTUSER_PATH);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		File f2 = new File(PATH+"//UserItem_"+ITEM+"_"+WORD_SIZE+".user");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
		String line;
		while((line = br0.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			int type = user.getInt("verifiedType");
			if(type==0||type==220){continue;}
			String item_str = user.getString(ITEM);
			if(Utils.checkwords(word_set, item_str).size()>0){
				bw.write(line+"\r\n");
			}
		}
		br0.close();
		bw.flush();
		bw.close();
	}
	/**
	 * 根据高频词得到集体用户
	 * @param word_size
	 * @throws IOException
	 */
	public static void getUserByItemTmp() throws IOException {
		Set<String> word_set = new HashSet<String>();
		GetInfo.getSet("//UserItem_"+ITEM+"_"+WORD_SIZE+"_suffix.txt",word_set,":",0);
		File f0 = new File(PATH+TESTUSER_PATH);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		File f2 = new File(PATH+TESTUSER_PATH+"_"+ITEM+"_"+WORD_SIZE+"_suffix.user");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+TESTUSER_PATH+"_"+ITEM+"_"+WORD_SIZE+"_suffix.unuser");
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f3));
		String line;
		while((line = br0.readLine())!=null){
			String[] items = line.split(",");
			String item_str = items[1];
			System.out.println(item_str);
			if(item_str.contains("姜岳")){
				System.out.println(item_str);
			}
			if(Utils.checkwords(word_set, item_str).size()>0){
				bw.write(line+"\r\n");
			}else{
				bw2.write(line+"\r\n");
			}
		}
		br0.close();
		bw.flush();
		bw.close();
		bw2.flush();
		bw2.close();
	}
	
	
	public static void getUserByTag() throws IOException {
		Set<String> word_set = new HashSet<String>();
		GetInfo.getSet("\\UserItem_tags_2", word_set, ":",0,1,THRESHOLD);
		File f0 = new File(PATH+TESTUSER_PATH);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		File f2 = new File(PATH+"\\UserItem_tags_2.user");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
		String line;
		while((line = br0.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			int type = user.getInt("verifiedType");
			if(type==0||type==220){continue;}
			JSONArray tags = user.getJSONObject("tags").getJSONArray("tags");
			if(checktags(word_set,tags)){
				bw.write(line+"\r\n");
			}
		}
		br0.close();
		bw.flush();
		bw.close();
	}
	/**********************************************根据集体用户高频词获取集体用户END****************************************************************/

	/**********************************************UTILS****************************************************************/
	/**
	 * 从item中获取长度为size的尾词，加入到word_map中(或更新其次数)
	 * @param word_map
	 * @param item
	 * @param size
	 */
	private static void getsuffix(Map<String,Integer> word_map, String item, int size) {
		if(item.length()<2)return;
		int start = item.length()-2;
		String word = item.substring(start, item.length());
		if(word_map.containsKey(word)){word_map.put(word, word_map.get(word)+1);}
		else{word_map.put(word,1);}
	}
	/**
	 * 从item中获取连续size个词的词组，加入到word_map中(或更新其次数)
	 * @param word_map
	 * @param item
	 * @param size
	 */
	private static void getwords(Map<String,Integer> word_map, String item, int size) {
		int n = item.length() - size +1;
		//Pattern p = Pattern.compile("[\\p{P}]|[\\w]|[ ]");
		for(int i=0;i<n;i++){
			String word = item.substring(i, i+size);
			//Matcher m = p.matcher(word);
			//if(m.find()){continue;}
			if(word_map.containsKey(word)){word_map.put(word, word_map.get(word)+1);}
			else{word_map.put(word,1);}
		}
	}
	/**
	 * 从字符串tags中获取tag放到word_map中(或更新其次数)
	 * @param word_map
	 * @param tags
	 */
	private static void gettags(Map<String, Integer> word_map, JSONArray tags) {
		for(int i=0;i<tags.size();i++){
			String t = tags.getString(i);
			if(word_map.containsKey(t)){word_map.put(t, word_map.get(t)+1);}
			else{word_map.put(t,1);}
		}
	}
	/**
	 * 检测tags中是否包含word_set里面的词项
	 * @param word_set
	 * @param tags
	 * @return
	 */
	private static boolean checktags(Set<String> word_set, JSONArray tags) {
		for(int i=0;i<tags.size();i++){
			String t = tags.getString(i);
			if(word_set.contains(t)){return true;}
		}
		return false;
	}

}
