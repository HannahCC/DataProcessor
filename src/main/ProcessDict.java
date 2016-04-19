package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ProcessDict {

	public static void main(String args[]) throws IOException {
		Set<String> seed = new HashSet<String>();
		getSet(seed,"D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Config\\Dict_UserType.txt","\t","##",0);

	}

	public static void getSet(Set<String> set,String filename,String regex1,String regex2,int i) throws IOException {
		File f1 = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		String line;
		while((line = br.readLine())!=null){
			String[] items = line.split(regex1)[i].split(regex2);
			for(String it : items){
				if(set.contains(it)){
					System.out.println(it);}
				else{				set.add(it);
				}
			}
		}
		br.close();
	}
}
