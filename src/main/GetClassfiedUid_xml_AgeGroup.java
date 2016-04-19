package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.SaveInfo;

public class GetClassfiedUid_xml_AgeGroup{
	/**
编号	类别		年份			年龄段
1	青少年	1997-2001	13-17	11+540+500=1051
2	青年		1992-1996	18-22	81+470+500=1051
3	青中年	1980-1991	23-34	496+50+500=1046
4	壮年		1960-1979	35-54	145+29+470+500=1054
5	老年		1944-1959	55-70	10+540+500=1050

18-22/28-32/38-42
编号	类别		年份			年龄段
2	青年		1992-1996	18-22
3	青中年	1982-1986	28-32
4	壮年		1972-1976	38-42
5	老年		1962-1966	48-52
	 * @param args
	 * @throws IOException
	 */
	public static String PATH = "D:\\Project_DataMinning\\DataSource\\Sina_NLPIR_UserID";
	public static Set<String> Teen = new HashSet<String>();
	public static Set<String> Young = new HashSet<String>();
	public static Set<String> Young_Adult = new HashSet<String>();
	public static Set<String> Adult = new HashSet<String>();
	public static Set<String> Old = new HashSet<String>();
	/**
	 * @throws JAXBException 


	 */
	public static void main(String args[]) throws IOException{
		File srcf = new File("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR微博博主语料库.xml");
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		SaveInfo.mkdir(PATH+"UserID");
		String line = "";
		String regex = "<[/]?[a-zA-Z]{2,10}>";
		int num = 0;
		while((line = r.readLine())!=null){
			if(line.contains("id")){
				String id = line.split(regex)[1];
				for(int i=0;i<=10;i++){line = r.readLine();}
				//System.out.println(line);
				if(line.contains("<brithday></brithday>"))continue;
				String brithday = line.split(regex)[1]; 
				if(brithday.contains("年")){
					int year = Integer.parseInt(brithday.split("年")[0]);
					num = spilt(id,year,num);
				}

			}
		}
		r.close();
		System.out.println(num);
		//Deduplication();
		SaveInfo.saveSet(PATH,"UserID\\",Teen,false);
		SaveInfo.saveSet(PATH,"UserID\\",Young,false);
		SaveInfo.saveSet(PATH,"UserID\\",Young_Adult,false);
		SaveInfo.saveSet(PATH,"UserID\\",Adult,false);
		SaveInfo.saveSet(PATH,"UserID\\",Old,false);
	}

	public static void Deduplication() {
		Map<String,String> ids = new HashMap<String,String>();
		for(String id : Teen){
			if(ids.containsKey(id)){System.out.println(id+"---Teen---"+ids.get(id));deleteId(ids.get(id),id);}
			else{ids.put(id,"Teen");}
		}
		for(String id : Young){
			if(ids.containsKey(id)){System.out.println(id+"---Young---"+ids.get(id));deleteId(ids.get(id),id);}
			else{ids.put(id,"Young");}
		}
		for(String id : Young_Adult){
			if(ids.containsKey(id)){System.out.println(id+"---Young_Adult---"+ids.get(id));deleteId(ids.get(id),id);}
			else{ids.put(id,"Young_Adult");}
		}
		for(String id : Adult){
			if(ids.containsKey(id)){System.out.println(id+"---Adult---"+ids.get(id));deleteId(ids.get(id),id);}
			else{ids.put(id,"Adult");}
		}
		for(String id : Old){
			if(ids.containsKey(id)){System.out.println(id+"---Old---"+ids.get(id));deleteId(ids.get(id),id);}
			else{ids.put(id,"Old");}
		}
	}

	private static void deleteId(String type, String id) {
		if(type.equals("Teen"))Teen.remove(id);
		else if(type.equals("Young"))Young.remove(id);
		else if(type.equals("Young_Adult"))Young_Adult.remove(id);
		else if(type.equals("Adult"))Adult.remove(id);
		else if(type.equals("Old"))Old.remove(id);
	}

	private static int spilt(String id, int year ,int num) {
		num++;
		/*if(year>=1997&&year<=2001){Teen.add(id);}
		else if(year>=1992&&year<=1996){Young.add(id);}
		else if(year>=1980&&year<=1991){Young_Adult.add(id);}
		else if(year>=1960&&year<=1979){Adult.add(id);}
		else if(year>=1944&&year<=1959){Old.add(id);}
		else {num--;}*/
	/*	2	青年		1992-1996	18-22
		3	青中年	1982-1986	28-32
		4	壮年		1972-1976	38-42
		5	老年		1962-1966	48-52*/
		if(year>=1992&&year<=1996){Teen.add(id);}
		else if(year>=1982&&year<=1986){Young_Adult.add(id);}
		else if(year>=1972&&year<=1976){Young.add(id);}
		else if(year>=1962&&year<=1966){Adult.add(id);}
		else if(year>=1952&&year<=1956){Old.add(id);}
		else {num--;}
		return num;
	}
	
}
