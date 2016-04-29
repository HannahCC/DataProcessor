package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GetVectorOfWord {

	public static void main(String argsStrings[]) throws IOException {
	
		Set<String> seed = new HashSet<String>();
		/*getSet(seed,"D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_Src\\Src_Description.txt.parsed.clr","\\s+");
		String srcVecFile = "D:\\Project_DataMinning\\DataSource\\BaikeVector\\allBaikeFc_100_vector.txt";
		String resVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_Src\\Src_Description_vector.txt";
		getVectorOfWord(seed,srcVecFile,resVecFile);*/

		/*getSet(seed,"D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdTag.txt","\t","\t");
		String srcVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win8_L100_FriTag_vector.txt";
		String resVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win8_L100_FriTag_vector_0.txt";
		getVectorOfWord(seed,srcVecFile,resVecFile);*/
		
		getSet(seed,"D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdFriends.txt","\t","\t");
		String srcVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win500_L100_Fri_vector.txt";
		String resVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win500_L100_Fri_vector_0.txt";
		getVectorOfWord(seed,srcVecFile,resVecFile);
		seed.clear();
		getSet(seed,"D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Feature_UserInfo\\UserIdVFriends.txt","\t","\t");
		srcVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win500_L100_VFri_vector.txt";
		resVecFile = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\Vector\\2223_Win500_L100_VFri_vector_0.txt";
		getVectorOfWord(seed,srcVecFile,resVecFile);
		
		
	}

	public static void getVecFeatureOfWord(Set<String> seed, String srcVecFile,
			String resVecFile) throws IOException {
		File f = new File(srcVecFile);
		BufferedReader br = new BufferedReader(new FileReader(f));	
		File f1 = new File(resVecFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f1));
		String line = "";
		br.readLine();
		br.readLine();
		//bw.write(br.readLine()+"\r\n");
		//bw.write(br.readLine()+"\r\n");
		while((line=br.readLine())!=null){
			String word = line.split("\\s+")[0];
			if(seed.contains(word)){
				bw.write(line+"\r\n");
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}
	private static void getVectorOfWord(Set<String> seed, String srcVecFile,
			String resVecFile) throws IOException {
		File f = new File(srcVecFile);
		BufferedReader br = new BufferedReader(new FileReader(f));	
		File f1 = new File(resVecFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f1));
		String line = "";
		/*br.readLine();
		br.readLine();*/
		bw.write(br.readLine()+"\r\n");
		bw.write(br.readLine()+"\r\n");
		while((line=br.readLine())!=null){
			String word = line.split("\\s+")[0];
			if(seed.contains(word)){
				bw.write(line+"\r\n");
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}

	public static void getSet(Set<String> set,String filename,String regex) throws IOException {
		File f1 = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		String line;
		while((line = br.readLine())!=null){
			//System.out.println(line);
			String[] items = line.split(regex);
			for(String it : items){set.add(it);}
		}
		br.close();
	}
	public static void getSet(Set<String> set,String filename,String regex1,String regex2) throws IOException {
		File f1 = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f1));
		String line;
		while((line = br.readLine())!=null){
			//System.out.println(line);
			String[] items = line.split(regex1,2)[1].split(regex2);
			for(String it : items){set.add(it);}
		}
		br.close();
	}
}
