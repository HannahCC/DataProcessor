package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

public class GetInfo {
	/**
	 * 从filename中读取内容，每行作为list的一个元素，list是否去重isCleared决定
	 * @param filename
	 * @param list
	 * @param isCleared
	 * @throws IOException
	 */
	public static void getList(String filename, List<String> list, boolean isCleared) throws IOException {
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				if(isCleared&&list.contains(line))continue;
				list.add(line);
			}
		}
		br.close();
	}
	/**
	 * 从filename中读取内容，内容为json数据,按照names的层次读取json，直到最后一个name,将其值（String类型）作为list的一个元素，list是否去重isCleared决定
	 * @param filename
	 * @param list
	 * @param isCleared
	 * @param names
	 * @return
	 * @throws IOException
	 */
	public static List<String> getList(String filename,List<String> list,boolean isCleared,String...names) throws IOException {
		File f=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				for(int i=0;i<names.length-1;i++){
					json = json.getJSONObject(names[i]);
				}
				String item = json.getString(names[names.length-1]);
				if(isCleared&&line.contains(item)){continue;}
				list.add(item);
			}
		}
		br.close();
		return list;
	}
	/**
	 * 从filename中读取内容，内容为json数据,按照names的层次读取json，直到最后一个name,将其值（List<String>类型）作为list的一个元素，list是否去重isCleared决定
	 * @param filename
	 * @param list_list
	 * @param names
	 * @return
	 * @throws IOException
	 */
	public static List<List<String>> getListList(String filename,List<List<String>> list_list,String...names) throws IOException {
		File f=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				for(int i=0;i<names.length-1;i++){
					json = json.getJSONObject(names[i]);
				}
				@SuppressWarnings("unchecked")
				List<String> item = (List<String>) json.get(names[names.length-1]);
				list_list.add(item);
			}
		}
		br.close();
		return list_list;
	}
	/**
	 * 从filename中逐行读取内容，将内容用regex隔开，取第i个值（String类型）作为list的一个元素，list是否去重isCleared决定
	 * @param filename
	 * @param lines
	 * @param regex
	 * @param i
	 * @param isCleared
	 * @return
	 * @throws IOException
	 */
	public static List<String> getList(String filename,List<String> lines,String regex, int i,boolean isCleared) throws IOException {
		File f=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String item = line.split(regex)[i];
				if(isCleared&&lines.contains(item)){continue;}
				lines.add(item);
			}
		}
		br.close();
		return lines;
	}
	/**
	 * 从filename中读取内容，每行作为set的一个元素
	 * @param filename
	 * @param set
	 * @throws IOException
	 */
	public static void getSet(String filename, Set<String> set) throws IOException {
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String uid = line.split("\\s+")[0];
				/*if(set.contains(uid)){
					System.out.println(uid);
				}*/
				set.add(uid);
			}/*else{
				System.out.println("haha");
			}*/
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，将内容用regex隔开，取第i个值（String类型）作为set的一个元素
	 * @param filename
	 * @param lines
	 * @param regex
	 * @param i
	 * @param isCleared
	 * @return
	 * @throws IOException
	 */
	public static void getSet(String filename, Set<String> set,String regex, int i) throws IOException {
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String item = line.split(regex)[i];
				/*if(set.contains(item)){
					System.out.println(item);
				}*/
				set.add(item);
			}/*else{
				System.out.println("haha");
			}*/
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，将内容用regex隔开，当第j个值大于threshold时，取第i个值（String类型）作为set的一个元素
	 * @param filename
	 * @param set
	 * @param regex
	 * @param i
	 * @param j
	 * @param threshold
	 * @throws IOException
	 */
	public static void getSet(String filename, Set<String> set,String regex, int i, int j,int threshold) throws IOException {
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] items = line.split(regex);
				if(Integer.parseInt(items[j])>threshold){
					set.add(items[i]);
				}
			}
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，内容为json数据，取其名为keys的元素的值（String类型），将其加入到set中
	 * @param filename
	 * @param set
	 * @param keys
	 * @throws IOException
	 */
	public static void getSet(String filename, Set<String> set, String keys) throws IOException {
		File f = new File(filename);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = b.readLine())!=null){
			JSONObject uidinfo = JSONObject.fromObject(line);
			set.add(uidinfo.getString(keys));
		}
		b.close();
	}
	/**
	 * 从filename中逐行读取内容，内容为json数据，取其名为keys的元素的值（List<String>类型），将其加入到set中
	 * @param filename
	 * @param set
	 * @param keys
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void getSetFromList(String filename, Set<String> set, String keys) throws IOException {
		File f = new File(filename);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = b.readLine())!=null){
			JSONObject uidinfo = JSONObject.fromObject(line);
			set.addAll((List<String>) uidinfo.get(keys));
		}
		b.close();
	}
	/**
	 * 从filename中逐行读取内容，内容为json数据，
	 * 当名为con_keys的值存在于con_set中时，取名为keys的元素的值（List<String>类型），将其加入到set中
	 * @param filename
	 * @param set
	 * @param keys
	 * @param ids
	 * @param con_keys
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void getSetFromList(String filename, Set<String> set, String keys, Set<String> con_set, String con_keys) throws IOException {
		File f = new File(filename);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "";
		JSONObject json = null;
		String con = "";
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			con = json.getString(con_keys);
			if(con_set.contains(con)){
				set.addAll((List<String>) json.get(keys));
			}
		}
		b.close();
	}
	/**
	 * 获取词典文件，存为map
	 * @param filename
	 * @param lines
	 * @throws IOException
	 */
	public static void getDict(String filename,Map<String,Integer> lines) throws IOException{
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] item = line.split("\t");
				lines.put(item[0],Integer.parseInt(item[1]));//(我们做朋友吧\t2)	
			}
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，使用regex隔开，将第i个作为key,第j个转为int作为value,加入到map
	 * @param filename
	 * @param lines
	 * @param regex
	 * @param i
	 * @param j
	 * @throws IOException
	 */
	public static void getMap(String filename,Map<Integer,String> map,String regex) throws IOException{
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] item = line.split(regex,2);
				map.put(Integer.parseInt(item[0]),item[1]);
			}
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，使用regex隔开，将第i个作为key,第j个转为int作为value,加入到map
	 * @param filename
	 * @param lines
	 * @param regex
	 * @param i
	 * @param j
	 * @throws IOException
	 */
	public static void getMap(String filename,Map<String,Integer> map,String regex, int i, int j) throws IOException{
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] item = line.split(regex);
				map.put(item[i],Integer.parseInt(item[j]));
			}
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，使用regex隔开，将第i个作为key,第j个转为int作为value,加入到map.
	 * isReplace决定 当key相同时，后读到的value是否替换前面读到的值，不替换是，则累加
	 * @param filename
	 * @param lines
	 * @param regex
	 * @param key
	 * @param value
	 * @param isReplace
	 * @throws IOException
	 */
	public static void getMap(String filename,Map<String, Integer> lines,String regex,int key,int value,boolean isReplace) throws IOException{
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] item = line.split(regex);
				if(isReplace){lines.put(item[key], Integer.parseInt(item[value]));}
				else{Utils.putInMap(lines, item[key], Integer.parseInt(item[value]));}
			}
		}
		br.close();
	}
	public static int getFloatMap(String filename, Map<String,float[]> map,String regex, int key, int value, String regex2) throws IOException {
		int length = 0;
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] items = line.split(regex,2);
				
				String[] values_str = items[value].split(regex2);
				length = values_str.length;
				float[] values = new float[length];
 				for(int i=0;i<length;i++){
 					values[i]=Float.parseFloat(values_str[i]);
 				}
				map.put(items[key],values);
			}
		}
		br.close();
		return length;
	}
	/**
	 * 从filename中逐行读取内容，内容为json数据,将keys/values（Integer类型）放到map中
	 * @param filename
	 * @param map
	 * @param keys
	 * @param values
	 * @throws IOException
	 */
	public static void getMap(String filename, Map<String,String> map,String keys, String values) throws IOException {
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				String key = json.getString(keys);
				String value = json.getString(values);
				map.put(key,value);
			}
		}
		br.close();
	}
	/**
	 * 从filename中逐行读取内容，内容为json数据,按照keys/values的层次读取json，直到最后一个keys/values,将keys/values（List<String>类型）放到map中
	 * @param filename
	 * @param map
	 * @param keys
	 * @param values
	 * @throws IOException
	 */
	public static void getListMap(String filename, Map<String, List<String>> map,String[] keys, String[] values) throws IOException {
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				for(int i=0;i<keys.length-1;i++){
					json = json.getJSONObject(keys[i]);
				}
				String key = json.getString(keys[keys.length-1]);
				for(int i=0;i<values.length-1;i++){
					json = json.getJSONObject(values[i]);
				}
				@SuppressWarnings("unchecked")
				List<String> value = (List<String>) json.get(values[values.length-1]);
				map.put(key,value);
			}
		}
		br.close();
	}
	
	/**
	 * 从filename中逐行读取内容，内容为json数据,将keyname/valuename(List<String>类型)放到map中，其中value转成set
	 * @param filename
	 * @param list_map
	 * @param keyname
	 * @param valuename
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void getSetMap(String filename, Map<String, Set<String>> map,String keyname,String valuename) throws IOException {
		File f = new File(filename);
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line = "",key="";
		JSONObject json = null;
		List<String> lists = null;
		while((line = b.readLine())!=null){
			json = JSONObject.fromObject(line);
			key = json.getString(keyname);
			lists = (List<String>) json.get(valuename);
			Set<String> values = new HashSet<String>();
			values.addAll(lists);
			map.put(key, values);
		}
		b.close();
	}
	/**
	 * 从filename中逐行读取内容，使用regex1将内容分隔，第i个值为key,第j个值为value,且value使用regex2分隔加入set作为value
	 * @param set_map
	 * @param filename
	 * @param regex1
	 * @param regex2
	 * @param i
	 * @param j
	 * @throws IOException
	 */
	public static void getSetMap(Map<String, Set<String>> set_map,String filename,String regex1,String regex2,int i,int j) throws IOException {
		File f1 = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		String line;
		while((line = br.readLine())!=null){
			Set<String> word_set = new HashSet<String>();
			String[] items = line.split(regex1);
			String[] item = items[j].split(regex2);
			for(String it : item){word_set.add(it.toUpperCase());}
			set_map.put(items[i], word_set);
		}
		br.close();
	}
	public static String getStringFromStream(InputStream in) throws IOException {
		BufferedReader reader=null;
		reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer=new StringBuffer();
		String str=null;
		while((str=reader.readLine())!=null){
			buffer.append(str+"\n");
		}	
		reader.close();		
		return new String(buffer.toString().getBytes(),"utf-8");
	}

}
