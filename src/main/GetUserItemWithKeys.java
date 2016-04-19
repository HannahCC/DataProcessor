package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.GetInfo;
import utils.SaveInfo;
import utils.Utils;
import net.sf.json.JSONObject;

public class GetUserItemWithKeys {
	/**
	 * 获取带有关键字的用户名
	 * 获取带有关键字的Tag 
	 * 获取用户名带有关键字的用户的Tag
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON_1000\\";
	public static void main(String args[]) throws IOException{
		//String[] key_words = new String[]{"女人","时尚","购物","服饰","搭配","穿衣","打扮","美容","化妆","彩妆","育儿","孕妇","婴幼","两性关系","家庭","情感","恋爱"};
		//String[] key_words = new String[]{"女","姐","妈","姨","妞"};
		//String[] key_words = new String[]{"男","爷","叔","哥","兄"};
		String[] key_words = new String[]{"足球","篮球","台球","游戏","围棋","桥牌","赛车","汽车","政治","军事","历史","人文","IT技术","哲学","科学",
				"瑜伽","肚皮舞","购物","化妆","美容","打扮","搭配","时尚","减肥","漫画","电视剧","家庭","婆媳关系","情感","恋爱","育儿","情绪化"};
		getUserItem(key_words,"screenName","UserInfo1.txt","ScreenName_male.txt");//获取带关键字的用户名、desc等String类型的item
		getTags(key_words,"UserInfo1.txt","Tag_interest.txt");//获取带关键字的tags
		getTagsWithKeyInUsername(key_words,"UserInfo1.txt","Tag_male.txt");//获取带关键字的tags
		//使用分词工具分词
		orderUserNameWord("Tag_interest.txt","Tag_interest_words.txt","/\\w+\\s+");//将分词后的用户名词按词频降序排列
		deduplicatedTags("Tag_male_words.txt","Tag_female_words.txt");//将两个文件中相同的标签去除

	}

	private static void deduplicatedTags(String malefile,String femalefile) throws IOException {
		
		Map<String,Integer> male_tags = new HashMap<String,Integer>();
		Map<String,Integer> female_tags = new HashMap<String,Integer>();
		
		Map<String,Integer> new_male_tags = new HashMap<String,Integer>();
		Map<String,Integer> new_female_tags = new HashMap<String,Integer>();
		
		GetInfo.getMap(PATH+malefile, male_tags, ":", 0, 1);
		GetInfo.getMap(PATH+malefile, female_tags, ":", 0, 1);
		for(String tag: male_tags.keySet()){
			int male_number = male_tags.get(tag);
			if(female_tags.containsKey(tag)){//相同的tag","检查其出现次数","
				/*int female_number = female_tags.get(tag);
				if(Math.abs(female_number-male_number)<500){
					continue;
				}*/
				continue;
			}
			new_male_tags.put(tag,male_number);
		}
		for(String tag: female_tags.keySet()){
			int female_number = female_tags.get(tag);
			if(male_tags.containsKey(tag)){//相同的tag","检查其出现次数
				/*int male_number = male_tags.get(tag);
				if(Math.abs(female_number-male_number)<1000){
					continue;
				}*/
				continue;
			}
			new_female_tags.put(tag,female_number);
		}
		List<String> male_list = new ArrayList<String>();
		Utils.mapSortByValueInteger(male_list, new_male_tags);
		SaveInfo.saveList(PATH, malefile+".sp", male_list, false);
		List<String> female_list = new ArrayList<String>();
		Utils.mapSortByValueInteger(female_list, new_female_tags);
		SaveInfo.saveList(PATH, femalefile+".sp", female_list, false);
		
	}

	private static void orderUserNameWord(String srcfile,String resfile,String regex) throws IOException {
		Map<String, Integer> word_map = new HashMap<String, Integer>();
		File r=new File(PATH+srcfile);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";String[] words = null;
		while((line=br.readLine())!=null){
			words = line.split(regex);
			for(String wd : words){
				Utils.putInMap(word_map, wd, 1);
			}
		}
		br.close();
		List<String> list = new ArrayList<String>();
		Utils.mapSortByValueInteger(list, word_map);
		SaveInfo.saveList(PATH, resfile, list, false);
	}
	private static void getUserItem(String[] key_words,String itemName, String srcfile,String resfile) throws IOException {
		File f = new File(PATH+srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		File f1 = new File(PATH+resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f1));
		String line = "",screenName="";int number =0;JSONObject json = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			screenName = json.getString(itemName);
			for(String word : key_words){
				if(screenName.contains(word)){
					w.write(screenName+"\r\n");
					number++;
					break;
				}
			}
		}
		System.out.println(number);
		b.close();
		w.flush();
		w.close();
	}
	
	@SuppressWarnings("unchecked")
	private static void getTagsWithKeyInUsername(String[] key_words,String srcfile,String resfile) throws IOException {
		File f = new File(PATH+srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		File f1 = new File(PATH+resfile);
		BufferedWriter w = new BufferedWriter(new FileWriter(f1));
		String line = "",screenName="";JSONObject json = null;List<String> tags = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			screenName = json.getString("screenName");
			for(String word : key_words){
				if(screenName.contains(word)){
					tags = (List<String>) json.getJSONObject("tags").get("tags");
					for(String tag : tags){
						w.write(tag+"\r\n");
					}
					break;
				}
			}
		}
		b.close();
		w.flush();
		w.close();
	}
	@SuppressWarnings("unchecked")
	private static void getTags(String[] key_words,String srcfile,String resfile) throws IOException {
		File f = new File(PATH+srcfile);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "";JSONObject json = null;List<String> tags = null;boolean flag = false;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			tags = (List<String>) json.getJSONObject("tags").get("tags");
			for(String tag : tags){
				for(String word : key_words){
					if(tag.contains(word)){
						flag = true;
						break;
					}
				}
				if(flag)break;
			}
			if(flag){
				String tagstr = json.getJSONObject("tags").getString("tags");
				tagstr = tagstr.substring(1, tagstr.length()-1);
				tagstr = tagstr.replace("\"", "");
				tagstr = tagstr.replace(",", " ");
				SaveInfo.saveString(PATH, resfile, tagstr, true);
			}
		}
		b.close();
	}
	
}
