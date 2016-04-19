package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class MergeDocument {
	private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR400_Good\\";
	public static void main(String[] args) throws IOException {
		
		/*mergeDocument("Feature_SRC\\App_Description.txt.parsed.clr",
				"Feature_SRC\\App_DescriptionLDA_2000_100\\model-final.theta",
				"Feature_SRC\\App_topic_2000_100.txt");*/
		
		/*mergeDocument("Feature_SRC\\Src_DescriptionLFLDA_2000_100_50_0.6\\Src_Description.txt.parsed.clr2",
				"Feature_SRC\\Src_DescriptionLFLDA_2000_100_50_0.6\\testLFLDA.theta",
				"Feature_SRC\\Src_topic_2000_100_50_0.6_LFLDA.txt");*/
		
		mergeDocument("Feature_SRC\\App_DescriptionLFLDA_2000_100_100_0.6\\App_Description.txt.parsed.clr2",
		"Feature_SRC\\App_DescriptionLFLDA_2000_100_100_0.6\\testLFLDA.theta",
		"Feature_SRC\\App_topic_2000_100_100_0.6_LFLDA.txt");

		/*mergeDocument("Feature_Relation\\VUser_type.txt.parsed.clr",
				"Feature_Relation\\VUser_DescriptionLDA\\model-final.theta",
				"Feature_Relation\\VUser_topic_2000_100_LDA.txt");*/

		/*mergeDocument("Feature_Relation\\VUser_DescriptionLFDMM_2000_100_50_0.6\\VUser_type.txt.parsed.clr2",
				"Feature_Relation\\VUser_DescriptionLFDMM_2000_100_50_0.6\\testLFDMM.theta",
				"Feature_Relation\\VUser_topic_2000_100_50_0.6_LFDMM.txt");*/
		
	}
	private static void mergeDocument(String src1, String src2,
			String res) throws IOException {
		File f1 = new File(PATH+src1);
		File f2 = new File(PATH+src2);
		File f = new File(PATH+res);
		
		BufferedReader br2 = new BufferedReader(new FileReader(f2));
		BufferedReader br1 = new BufferedReader(new FileReader(f1));
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		String line1,line2;
		while((line1 = br1.readLine())!=null){
			line2 = br2.readLine();
			String id = line1.split("\t",2)[0];
			String feature = line2;
			w.write(id+"\t"+feature+"\r\n");
		}
		w.flush();
		w.close();
		br1.close();
		br2.close();
	}
}
