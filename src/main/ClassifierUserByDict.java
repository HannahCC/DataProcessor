package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.GetInfo;
import utils.Utils;
/**
 * ※ClassifierUserByDict：根据词典和用户的某一（多）个属性，将用户分类。
 * 如根据"\Config\Dict_UserType.txt"区分"\UserInfo0.txt"中的用户分别为哪一类别
	=Feature_Relation
		-User_type.txt.type：区分成功的用户
		-User_typeUndefined.txt：区分失败的用户
	=Feature_Relation
		-VUser_type.txt.type：区分成功的用户
		-VUser_typeUndefined.txt：区分失败的用户
 * @author Hannah
 *
 */
public class ClassifierUserByDict {
	public static final String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static final int OTHER_TYPE = 1;
	public static void main(String args[]) throws IOException{
		/*classiferUser(
				"Config\\Dict_UserType.txt",
				"Config\\Dict_UserType_vector_inBaiKe.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_type.txt",
				"Feature_Relation\\VUser_type_vector.txt",
				"Feature_Relation\\VUser_type_detail.txt",
				"Feature_Relation\\VUser_type_Undefined.txt");*/
		classiferUser(
				"Config\\Dict_UserNewType.txt",
				"Config\\Dict_UserNewType_vector_inBaiKe.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_newtype.txt",
				"Feature_Relation\\VUser_newtype_vector.txt",
				"Feature_Relation\\VUser_newtype_detail.txt",
				"Feature_Relation\\VUser_newtype_Undefined.txt");


		/*classiferUser(
				"Config\\Dict_UserType_BaiKeExpand25.txt",
				"Config\\Dict_UserType_BaiKeExpand25_vector_inBaiKe.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_type_BaiKeExpand25.txt",
				"Feature_Relation\\VUser_type_BaiKeExpand25_vector.txt",
				"Feature_Relation\\VUser_type_BaiKeExpand25_detail.txt",
				"Feature_Relation\\VUser_type_BaiKeExpand25_Undefined.txt");

		classiferUser(
				"Config\\Dict_UserNewType_BaiKeExpand25.txt",
				"Config\\Dict_UserNewType_BaiKeExpand25_vector_inBaiKe.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_newtype_BaiKeExpand25.txt",
				"Feature_Relation\\VUser_newtype_BaiKeExpand25_vector.txt",
				"Feature_Relation\\VUser_newtype_BaiKeExpand25_detail.txt",
				"Feature_Relation\\VUser_newtype_BaiKeExpand25_Undefined.txt");*/
		

		/*classiferUser(
				"Config\\Dict_UserType_Expand25.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_type_Expand25.txt",
				"Feature_Relation\\VUser_type_Expand25_detail.txt",
				"Feature_Relation\\VUser_type_Expand25_Undefined.txt");

		classiferUser(
				"Config\\Dict_UserNewType_Expand25.txt",
				"Feature_Relation\\VUser_Description.txt",
				"Feature_Relation\\VUser_newtype_Expand25.txt",
				"Feature_Relation\\VUser_newtype_Expand25_detail.txt",
				"Feature_Relation\\VUser_newtype_Expand25_Undefined.txt");*/


	}
	/*********************************************用户分类*****************************************************************/	
	public static void classiferUser(String dict_file, String dict_vector_file, String user_desc_file, String res_file, String res_file1, String res_file2, String res_file3) throws IOException {
		Map<String,Set<String>> user_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(user_type_map,PATH+dict_file,"\t","##",1,0);
		Map<String,float[]> user_type_vector_map = new HashMap<String,float[]>();
		int vec_length = GetInfo.getFloatMap(PATH+dict_vector_file,user_type_vector_map,"\t",0,1," ");

		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f1 = new File(PATH+res_file1);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(f1));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));
		File f0 = new File(PATH+user_desc_file);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br0.readLine())!=null){
			String id = line.split("\t",2)[0];
			String item_str = line.split("\t",2)[1];
			if(item_str.equals("")){continue;}

			float[] user_type = new float[user_type_map.size()+1];
			double[] user_type_vector = new double[vec_length];
			/*double min = -1.077052;//Math.pow(10, -6);
			for(int i=0;i<vec_length;i++){user_type_vector[i] = min;}*/
			StringBuffer newline = new StringBuffer();
			newline.append(id+"\t"+item_str+"\t");
			boolean flag = false;
			for(Entry<String,Set<String>> keywords_set_entry : user_type_map.entrySet()){
				String type = keywords_set_entry.getKey();
				Set<String> keywords_set = keywords_set_entry.getValue();
				Set<String> keywords = Utils.checkwords(keywords_set, item_str);
				if(keywords.size()>0){
					flag = true;
					newline.append(type);
					user_type[Integer.parseInt(type)-1]+=1;
					float[] type_vector = user_type_vector_map.get(type);
					for(int i=0;i<vec_length;i++){user_type_vector[i] += type_vector[i];}
					for(String keyword : keywords){newline.append("##"+keyword);}
					newline.append("\t");
				}	
			}
			if(!flag){
				user_type[OTHER_TYPE-1]+=1;
				newline.append(OTHER_TYPE+"##\t");
				//System.out.println(newline);
				bw3.write(newline.toString()+"\r\n");
			}
			bw.write(id+"\t");
			for(int i=0;i<user_type.length;i++){bw.write(user_type[i]+" ");}
			bw.write("\r\n");

			bw1.write(id+"\t");
			for(int i=0;i<vec_length;i++){bw1.write(user_type_vector[i]+" ");}
			bw1.write("\r\n");

			bw2.write(newline.toString()+"\r\n");
		}
		br0.close();
		bw.flush();
		bw.close();
		bw1.flush();
		bw1.close();
		bw2.flush();
		bw2.close();
		bw3.flush();
		bw3.close();
	}
	public static void classiferUser(String dict_file, String user_desc_file, String res_file,  String res_file2, String res_file3) throws IOException {
		Map<String,Set<String>> user_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(user_type_map,PATH+dict_file,"\t","##",1,0);

		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));
		File f0 = new File(PATH+user_desc_file);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br0.readLine())!=null){
			String id = line.split("\t",2)[0];
			String item_str = line.split("\t",2)[1];
			if(item_str.equals("")){continue;}

			float[] user_type = new float[user_type_map.size()+1];
			StringBuffer newline = new StringBuffer();
			newline.append(id+"\t"+item_str+"\t");
			boolean flag = false;
			for(Entry<String,Set<String>> keywords_set_entry : user_type_map.entrySet()){
				String type = keywords_set_entry.getKey();
				Set<String> keywords_set = keywords_set_entry.getValue();
				Set<String> keywords = Utils.checkwords(keywords_set, item_str);
				if(keywords.size()>0){
					flag = true;
					newline.append(type);
					user_type[Integer.parseInt(type)-1]+=1;
					for(String keyword : keywords){newline.append("##"+keyword);}
					newline.append("\t");
				}	
			}
			if(!flag){
				user_type[OTHER_TYPE-1]+=1;
				newline.append(OTHER_TYPE+"##\t");
				//System.out.println(newline);
				bw3.write(newline.toString()+"\r\n");
			}
			bw.write(id+"\t");
			for(int i=0;i<user_type.length;i++){bw.write(user_type[i]+" ");}
			bw.write("\r\n");

			bw2.write(newline.toString()+"\r\n");
		}
		br0.close();
		bw.flush();
		bw.close();
		bw2.flush();
		bw2.close();
		bw3.flush();
		bw3.close();
	}
	/*********************************************用户分类*****************************************************************/

}
