package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.GetInfo;

public class GetIntersection {
	//LOFTER客户端 3065
	//public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\";
	public static String PATH = "D:\\Project_DataMinning\\DataProcessd\\Sina_GenderPre_1635\\Public_Info_Rel\\";
	public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		getFileIntersection("0\\4_1_trainingid.txt","Vector\\UserIdFriends\\4_0_UserIdFriends_train.txt","tmp.txt");
	}
	private static void getFileIntersection(String file1, String file2, String resfile) throws IOException {
		List<String> intersect_list = new ArrayList<String>();
		Set<String> file1_id = new HashSet<String>();
		GetInfo.getSet(PATH+file1, file1_id, "\t", 0);
		System.out.println(file1_id.size());
		File r=new File(PATH+file2);
		BufferedReader br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String item = line.split("\t")[0];
				if(file1_id.contains(item)){
					intersect_list.add(line);
				}else {
					//System.out.println(line);
				}
			}
		}
		br.close();
		//SaveInfo.saveList(PATH, resfile, intersect_list, false);
		System.out.println(intersect_list.size());
	}


}
