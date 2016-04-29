package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.GetInfo;
import utils.SaveInfo;

public class GetClassfiedUid_ByAge {

	/**
编号	类别		年份			年龄段
1	青年		1992-1996	18-22
2	青中年	1982-1986	28-32
3	壮年		1972-1976	38-42
4	老年		1962-1966	48-52

new
编号	类别			年份			年龄段(2014)
1	初高中		1996-2000	14-18
2	大学/研究生	1990-1994	20-24
3	青中年		1974-1984	30-40
4	老年			1954-1964	50-60
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static Set<String> Young = new HashSet<String>();
	public static Set<String> Young_Adult = new HashSet<String>();
	public static Set<String> Adult = new HashSet<String>();
	public static Set<String> Old = new HashSet<String>();
	public static void main(String args[]) throws IOException {
		Set<String> id_set = new HashSet<String>();
		GetInfo.getSet(PATH+"UserID_Gender\\1_old2.txt", id_set , "\t", 0);
		GetInfo.getSet(PATH+"UserID_Gender\\2_old2.txt", id_set , "\t", 0);

		Map<String,String> user_age_map = new HashMap<String, String>();
		getMap("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR\\ExpandID0_Age.txt", user_age_map);
		
		for(String uid : id_set){
			int year = Integer.parseInt(user_age_map.get(uid).split("年")[0]);
			spilt(uid, year);
		}

		System.out.println(Young.size()+" "+Young_Adult.size()+" "+Adult.size()+" "+Old.size());
		SaveInfo.saveSet(PATH,"UserID_Age\\1_old2_new.txt",Young,false);
		SaveInfo.saveSet(PATH,"UserID_Age\\2_old2_new.txt",Young_Adult,false);
		SaveInfo.saveSet(PATH,"UserID_Age\\3_old2_new.txt",Adult,false);
		SaveInfo.saveSet(PATH,"UserID_Age\\4_old2_new.txt",Old,false);
		
	}
	private static void getMap(String filename,Map<String,String> map) throws IOException{
		File r=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] items = line.split("\t");
				map.put(items[0],items[1]);
			}
		}
		br.close();
	}
	private static void spilt(String id, int year) {
/*
new
编号	类别		年份			年龄段(2014)
1	初高中		1996-2000	14-18
2	大学/研究生	1990-1994	20-24
3	青中年		1974-1984	30-40
4	老年			1954-1964	50-60
	 */
		/*if(year>=1992&&year<=1996){Young.add(id);}
		else if(year>=1982&&year<=1986){Young_Adult.add(id);}
		else if(year>=1972&&year<=1976){Adult.add(id);}
		else if(year>=1962&&year<=1966){Old.add(id);}*/
		if(year>=1996&&year<=2000){Young.add(id);}
		else if(year>=1990&&year<=1994){Young_Adult.add(id);}
		else if(year>=1974&&year<=1984){Adult.add(id);}
		else if(year>=1954&&year<=1964){Old.add(id);}
		else {
			System.out.println(id+ ":" +year);
		}
	}
}
