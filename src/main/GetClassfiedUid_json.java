package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import utils.SaveInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetClassfiedUid_json{
	/**
编号	类别		年份			年龄段
1	青少年	1997-2001	13-17	11 + 540 = 551
2	青年		1992-1996	18-22	81 + 470 = 551
3	青中年	1980-1991	23-34	496 + 50 = 546
4	壮年		1960-1979	35-54	145 + 29 + 470 = 554
5	老年		1944-1959	55-70	10 + 540 = 550
	 * @param args
	 * @throws IOException
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON\\";
	public static Set<String> Teen = new HashSet<String>();
	public static Set<String> Young = new HashSet<String>();
	public static Set<String> Young_Adult = new HashSet<String>();
	public static Set<String> Adult = new HashSet<String>();
	public static Set<String> Old = new HashSet<String>();
	
	public static void main(String args[]) throws IOException{
		File dir = new File("D:\\Project_DataMinning\\DataSource\\32W用户数据");
		File[] filelist = dir.listFiles();
		SaveInfo.mkdir(PATH+"UserID");
		int num = 0;
		//List<String> id = new ArrayList<String>();
		for(File f:filelist){
			String id = f.getName().split("_")[1];
			BufferedReader r = new BufferedReader(new FileReader(f));
			String str = "";
			while((str = r.readLine())!=null){
				
				JSONArray jsonlist = JSONArray.fromObject(str);
				JSONObject json = jsonlist.getJSONObject(0);
				String birth = (String) json.getString("birthday");
				if(birth.contains("年")){
					int year = Integer.parseInt(birth.split("年")[0]);
					num=spilt(id,year,num,birth);
				}
			}
			r.close();
		}
		System.out.println(num);
		SaveInfo.saveSet(PATH,"UserID\\",Teen,false);
		SaveInfo.saveSet(PATH,"UserID\\",Young,false);
		SaveInfo.saveSet(PATH,"UserID\\",Young_Adult,false);
		SaveInfo.saveSet(PATH,"UserID\\",Adult,false);
		SaveInfo.saveSet(PATH,"UserID\\",Old,false);
	}
	private static int spilt(String id, int year ,int num, String birth) {
		num ++;
		if(year>=1997&&year<=2001){Teen.add(id+"\t"+birth);}
		else if(year>=1992&&year<=1996){Young.add(id+"\t"+birth);}
		else if(year>=1980&&year<=1991){Young_Adult.add(id+"\t"+birth);}
		else if(year>=1960&&year<=1979){Adult.add(id+"\t"+birth);}
		else if(year>=1944&&year<=1959){Old.add(id+"\t"+birth);}
		else {num--;System.out.println(id+"----"+year);}
		return num;
	}
	
	

}
