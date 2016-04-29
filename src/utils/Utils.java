package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Utils {
	//将id_Set按比例ratio分成两个id_set（treeset）,放在id_set_list中
	public static List<Set<String>> spilt(Set<String> id_set, double ratio) {
		int num = id_set.size(); 
		int i_num = (int) (num*ratio);
		List<Set<String>> id_set_list = new ArrayList<Set<String>>();
		Set<String> id_set_1 = new TreeSet<String>();
		Set<String> id_set_2 = new TreeSet<String>();
		int i=0;
		Iterator<String> it = id_set.iterator();
		while(it.hasNext()){
			String id = it.next();
			if(i<i_num){
				id_set_1.add(id);
			}else{
				id_set_2.add(id);
			}
			i++;
		}
		id_set_list.add(id_set_1);
		id_set_list.add(id_set_2);
		return id_set_list;
	}

	//将id_Set均分成fold个id_set（treeSet）,若不能完全平均，则最后一个会多余数个元素。放在id_set_list中
	public static List<Set<String>> spilt(Set<String> id_set, int fold) {
		int num = id_set.size();
		int i_num = num/fold;
		List<Set<String>> id_set_list = new ArrayList<Set<String>>();
		Set<String> id_set_i = new TreeSet<String>();
		int i=0;
		Iterator<String> it = id_set.iterator();
		while(it.hasNext()){
			String id = it.next();
			if(i<i_num/*||id_set_list.size()+1==fold*/){
				id_set_i.add(id);
			}else if(id_set_list.size()<fold){
				id_set_list.add(id_set_i);
				id_set_i = new TreeSet<String>();
				i=0;
				id_set_i.add(id);
			}
			i++;
		}
		id_set_list.add(id_set_i);
		return id_set_list;
	}
	//从数据集id_set中选取count个数据作为子集
	public static Set<String> subSet(Set<String> id_set,int count) {
		Set<String> new_set = new TreeSet<String>();
		int n = id_set.size();
		if(count>=n){return id_set;}
		for(String id : id_set){
			new_set.add(id);
			if(new_set.size()==count)break;
		}
		return new_set;
	}

	//从n个数中，取m个随机数
	public static Set<Integer> getRandomNumer(int n,int m) {
		Set<Integer> number = new TreeSet<Integer>();
		if(m>n/2){//如从10个数中取8个随机数，转化为从10个数中取2个
			for(int i=0;i<n;i++){number.add(i);}
			Random r = new Random();
			while(number.size()>m){
				number.remove(Math.abs(r.nextInt())%n);
			}
		}else{
			Random r = new Random();
			while(number.size()<m){
				number.add(Math.abs(r.nextInt())%n);
			}
		}
		return number;
	}

	//得到group_size的全排列，如group_size={1,2} ，结果为{{0,0}，{0,1}，{0,2}，{1,0}，{1,1}，{1,2}}
	public static int[][] recursion_getFullArray(int[] group_size) {
		int group_num = group_size.length;
		int[][] fullArray = null;
		if(group_num==1){
			fullArray = new int[group_size[0]][group_num];
			for(int i=0;i<group_size[0];i++){
				fullArray[i][0] = i;
			}
		}else{
			int[] new_group_size = getSubArray(group_size,1,group_num-1);//得到后面n-1个元素的全排列
			int[][] subfullArray = recursion_getFullArray(new_group_size);
			fullArray = new int[group_size[0]*subfullArray.length][group_num];
			for(int i=0;i<group_size[0];i++){
				for(int j=0;j<subfullArray.length;j++){
					fullArray[i*subfullArray.length+j] = mergeArray(i,subfullArray[j]);
				}
			}
		}
		return fullArray;
	}
	//得到group_size中由第i个元素开始，长度为length的子元素集合
	public static int[] getSubArray(int[] group_size,int s,int length) {
		int[] new_group_size = new int[length];
		for(int i=0;i<length;i++){
			new_group_size[i] = group_size[i+s];
		}
		return new_group_size;
	}
	//将元素value分别于array组合，如value=1，array={{0,0}，{0,1}，{1,0}，{1,1}} ==》{{1,0,0}，{1,0,1}，{1,1,0}，{1,1,1}}
	public static int[] mergeArray(int value, int[] array) {
		int len = array.length+1;
		int[] new_array = new int[len];
		new_array[0] = value;
		for(int i=1;i<len;i++){
			new_array[i] = array[i-1];
		}
		return new_array;
	}
	public static void mergeMap(Map<String, Integer> map,Map<String, Integer> new_map) {
		Iterator<Entry<String, Integer>> it = new_map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Integer> entry = it.next();
			String key = entry.getKey();
			int value = entry.getValue();
			if(map.containsKey(key)){map.put(key, map.get(key)+value);}
			else{map.put(key,value);}
		}
	}

	//将n个特征list按顺序转化为String
	@SafeVarargs
	public static String lists_ToString(List<String> ... lists) {
		StringBuffer str = new StringBuffer();
		for(List<String> list : lists){
			if(list==null||list.size()==0)continue;
			for(String emo : list){
				str.append(emo);//emo特征向量自带"\t"
			}
		}
		return str.toString();
	}
	public static String array_ToString(int[] array) {
		StringBuffer str = new StringBuffer();
		for(int i : array){
			str.append("_"+i);
		}
		return str.toString();
	}
	public static String map_toString(int lableid_index, Map<String, StringBuffer> map, String regex) {
		if(map==null||map.size()==0)return null;
		StringBuffer str = new StringBuffer();
		Iterator<Entry<String, StringBuffer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, StringBuffer> entry = it.next();
			str.append(lableid_index+regex+entry.getValue().toString()+"\r\n");
		}
		return str.toString();
	}
	public static List<String> string_toNgram(String str, int gram) {
		List<String> str_ngram = new ArrayList<String>();
		for (int i = 0; i < str.length()-gram+1; i++) {
			String ngram = str.substring(i, i+gram);
			str_ngram.add(ngram);
		}
		return str_ngram;
	}
	/**
	 * 求交集
	 * @param id_set_1
	 * @param id_set_2
	 * @return
	 */
	public static Set<String> intersection(Set<String> id_set_1,Set<String> id_set_2) {
		Set<String> id_set = new HashSet<String>();
		for(String id :id_set_1){
			if(id_set_2.contains(id))id_set.add(id);
		}
		return id_set;
	}
	/**
	 * 利用子串匹配检测item是否包含word_set里面的词项
	 * @param word_set
	 * @param item
	 * @return
	 */
	public static Set<String> checkwords(Set<String> word_set, String item) {
		item = item.toUpperCase();
		Set<String> word_subset = new HashSet<String>();
		for(String word : word_set){
			if(item.contains(word)){
				word_subset.add(word.toString());
			}
		}
		return word_subset;
	}
	/**
	 * 求最大值的下标
	 * @param res_list
	 * @return
	 */
	public static int getMax(double[] res_list) {
		double max = -1;int max_index = -1;int j = 0;
		for(double res : res_list){
			if(max<res){max_index = j;max = res;}
			j++;
		}
		return max_index+1;
	}
	/**
	 * 求最大值的下标
	 * @param res_list
	 * @return
	 */
	public static int getMax(List<Double> res_list) {
		double max = -1;int max_index = -1;int j = 0;
		for(double res : res_list){
			if(max<res){max_index = j;max = res;}
			j++;
		}
		return max_index;
	}

	public static void putInMap( Map<Integer, Integer> map,int index,int value) {
		if(map.containsKey(index)){map.put(index,map.get(index)+value);
		}else{map.put(index,value);}
	}
	public static void putInMap( Map<Integer, Double> map,int index,Double value) {
		if(map.containsKey(index)){map.put(index,map.get(index)+value);
		}else{map.put(index,value);}
	}
	public static void putInMap( Map<String, Integer> map,String index,int value) {
		if(map.containsKey(index)){map.put(index,map.get(index)+value);
		}else{map.put(index,value);}
	}
	public static void putInMap( Map<String, Double> map,String index,Double value) {
		if(map.containsKey(index)){map.put(index,map.get(index)+value);
		}else{map.put(index,value);}
	}
	public static void putInMap(Map<String, StringBuffer> map,String id, String string) {
		if(map.containsKey(id)){map.put(id, map.get(id).append(string));}
		else{map.put(id, new StringBuffer(string));}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void mapSortByValueInteger(List<String> list,Map<String, Integer> map) {
		List<Entry<String,Integer>> list_tmp = new ArrayList<Entry<String,Integer>>(map.entrySet());
        Object[] a = list_tmp.toArray();
        Comparator<Entry<String, Integer>> c = new Comparator<Entry<String,Integer>>(){
			public int compare(Entry<String,Integer> arg0,Entry<String,Integer> arg1) {
				double r = arg1.getValue()-arg0.getValue();
				if(r>0)return 1;
				else if(r<0)return -1;
				else return 0;
			}
		};
		//list_tmp.sort(c);
		Arrays.parallelSort(a, (Comparator)c);
		System.out.println(a);
		for(Object entry : a){
			Entry<String,Integer> entrynode = (Entry<String, Integer>) entry;
			String item = entrynode.getKey()+":"+entrynode.getValue();
			list.add(item);
		}
	}
	//按降序排列
	public static void mapSortByValueDouble(List<String> list,Map<String,Double> map) {
		List<Entry<String,Double>> list_tmp = new ArrayList<Entry<String,Double>>(map.entrySet());
		Collections.sort(list_tmp,new Comparator<Entry<String,Double>>(){
			public int compare(Entry<String,Double> arg0,Entry<String,Double> arg1) {
				double r = arg1.getValue()-arg0.getValue();
				if(r>0)return 1;
				else if(r<0)return -1;
				else return 0;
			}
		});
		for(Entry<String,Double> entry : list_tmp){
			String item = entry.getKey()+":"+entry.getValue();
			list.add(item);
		}
	}
}
