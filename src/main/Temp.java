package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import utils.GetInfo;
import utils.SaveInfo;

public class Temp {
	//LOFTER客户端 3065
	public static String PATH = "E:\\DataSource\\Flickr\\";//"D:\\Project_DataMinning\\Data\\Sina_res\\";
	public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//getDifferentId();
		//readFile();
<<<<<<< HEAD
		/*lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\ExpandID1.txt");
		lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\ExpandID1_fri.txt");
=======
		lineCountforFile("/home/zps/sina2333/Feature_UserInfo/UserIdNeighbours_c8Fri.txt");
		/*lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\ExpandID1_fri.txt");
>>>>>>> 6b648f1ca62e2bb6f777f0ed0d5e05d03894e1e1
		lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\UserInfo1.txt");*/
		//lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdTag_01.txt");

		//544845<=569674+2223
		//lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdFriends_full.txt");
		//lineCountforFile("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win500_L100_Fri_vector_full.txt");
		//lineCountforDir("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdFriends_full");

		/*File src = new File("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\UidInfo_follows1.txt");
		SaveInfo.fileCopy(src , "D:\\Project_DataMinning\\Data\\Sina_res\\", false);*/
		
		MergeFile("UserID//all.txt","UserID//1.txt","UserID//2.txt","UserID//3.txt","UserID//4.txt","UserID//5.txt");
	}

	private static void MergeFile(String resfile, String ... srcfiles) throws IOException {
		Set<String> lines = new TreeSet<String>();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(PATH+resfile)));
		String line = null;
		for(String srcfile:srcfiles){
			BufferedReader br = new BufferedReader(new FileReader(new File(PATH+srcfile)));
			while(null!=(line=br.readLine())){
				//bw.write(line+"\r\n");
				lines.add(line);
			}
			br.close();
		}
		for(String li : lines){
			bw.write(li+"\r\n");
		}
		bw.flush();
		bw.close();
	}

	private static void lineCountforFile(String filename) throws IOException {

		int count = 0;
		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		while(br.readLine()!=null){count++;}
		br.close();
		System.out.println(count);

	}

	private static void lineCountforDir(String dirname) throws IOException {

		int count = 0;
		File dir = new File(dirname);
		File[] fs = dir.listFiles();
		for(File f : fs){
			BufferedReader br = new BufferedReader(new FileReader(f));
			while(br.readLine()!=null){count++;}
			br.close();
		}
		System.out.println(count);

	}
	private static void readFile() throws IOException {
		File f = new File("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdTag_fri.txt");
		BufferedReader r = new BufferedReader(new FileReader(f));
		File f2 = new File("D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdTag_fri.txt.tmep");
		BufferedWriter w = new BufferedWriter(new FileWriter(f2));
		String uid="";
		while((uid=r.readLine())!=null)
		{
			w.write(uid.split("\t",2)[1]+"\r\n");
		}
		r.close();
		w.flush();
		w.close();
	}

	private static void getDifferentId() throws IOException {
		Set<String> ids = new HashSet<String>();
		GetInfo.getSet(PATH+"UserID\\5_10_600_male.txt", ids);
		Set<String> ids2 = new HashSet<String>();
		GetInfo.getSet(PATH+"UserID\\2.txt", ids2);
		Set<String> ids3 = new HashSet<String>();
		int number = 0;
		for(String id : ids){
			if(ids2.contains(id)){
				continue;
			}
			ids3.add(id);
			number++;
			if(number==200)break;
		}
		SaveInfo.saveSet(PATH, "UserID\\L2.txt", ids3, false);
	}
}
