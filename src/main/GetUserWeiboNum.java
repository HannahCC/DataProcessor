package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.GetInfo;

public class GetUserWeiboNum { 

	static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON\\";
	static int fold = 5;
	static int classnum = 4;
	static int id_number = 800;
	public static void main(String args[]) throws IOException{
		File f = new File(PATH+"UserID");
		if(!f.exists())f.mkdir();
		for(int i=1;i<classnum+1;i++){
			getUserWeibo(i);
		}


	}
	//获取每类用户的微博平均数目
	public static void getUserWeibo(int classid) throws IOException {
		Set<String> id_set = new HashSet<String>();
		GetInfo.getSet(PATH+"UserID\\"+classid+".txt", id_set);
		long sum = 0;
		for(String id : id_set){
			List<String> list = new ArrayList<String>();
			GetInfo.getList(PATH+"WeibosCon\\"+id+".txt", list, false);
			sum += list.size();
		}
		System.out.println(classid+"----"+sum/(double)id_set.size());
	}
}
