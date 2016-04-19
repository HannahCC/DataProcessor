package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.GetInfo;

public class ExtractInfo {
	public static String RES_PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static String SRC_PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR\\";
	public static void main(String args[]) throws IOException {
		getFromHtml();
		//getFromDescription();
	}

	private static void getFromDescription() throws IOException {
		Set<String> id_set = new HashSet<String>();
		GetInfo.getSet(RES_PATH+"Config\\Dict_Src.txt", id_set, "\t", 0);
		File f1 = new File(SRC_PATH+"Feature_Src\\Src_Description.txt");
		BufferedReader r1 = new BufferedReader(new FileReader(f1));
		File f2 = new File(RES_PATH+"Feature_Src\\Src_Description.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(f2));
		String line = null;
		
		while(null!=(line=r1.readLine())){
			if(line.equals(""))continue;
			String id = line.split("\t")[0];
			if(id_set.contains(id)){
				w1.write(line+"\r\n");
			}
		}
		w1.flush();w1.close();
		r1.close();
	}

	private static void getFromHtml() throws IOException {
		Set<String> id_set = new HashSet<String>();
		GetInfo.getSet(RES_PATH+"Config\\Dict_Src.txt", id_set, "\t", 0);
		Set<String> id_set_done = new HashSet<String>();
		GetInfo.getSet(RES_PATH+"Feature_Src\\Src_Description.txt", id_set_done, "\t", 0);
		id_set.removeAll(id_set_done);
		//id_set_done.removeAll(id_set);
		
		Map<String, String> src_dict = new HashMap<String, String>();
		getDict(SRC_PATH+"Config\\Dict_Src.txt", src_dict);
		File f1 = new File(SRC_PATH+"Feature_Src\\Src种类拓展\\ParseHTML.txt");
		BufferedReader r1 = new BufferedReader(new FileReader(f1));
		File f2 = new File(RES_PATH+"Feature_Src\\Src_Description.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(f2,true));
		String line = null;
		
		while(null!=(line=r1.readLine())){
			if(line.equals(""))continue;
			String id = line.split("\t",2)[0];
			String src_name = src_dict.get(id);

			if(id_set.contains(src_name)){
				w1.write(src_name+"\t"+line.split("\t",2)[1]+"\r\n");
			}
		}
		w1.flush();w1.close();
		r1.close();
	}

	public static void getDict(String filename,Map<String,String> lines) throws IOException{
		File r=new File(filename);
		BufferedReader br=null;
		br=new BufferedReader(new FileReader(r));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				String[] item = line.split("\t");
				lines.put(item[1],item[0]);//(我们做朋友吧\t2)	
			}
		}
		br.close();
	}
	
}
