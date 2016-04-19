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
import java.util.Map.Entry;
import java.util.Set;

import utils.GetInfo;
import utils.Utils;
/**
 * ※ClassifierSrcByDict：根据词典和Src的描述信息（由SinaApp_Crawler\Baidu搜索结果得到），将src分类。
 * 即根据"\Config\Dict_SrcType.txt"区分"Feature_SRC\Src_Description.txt"中的src分别为哪一类别
	=Feature_SRC
		-Src_type.txt：区分成功的src
		-Src_typeUndefined.txt：区分失败的src

 * @author Hannah
 *
 */
public class ClassifierSrcByDict {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	private static int OTHER_TYPE = 0;
	public static void main(String args[]) throws IOException{

		classiferSrc("Config\\Dict_SrcType.txt",
				"Config\\Dict_SrcType_vector_inBaiKe.txt", 
				"Feature_SRC\\Src_Description.txt", 
				"Feature_SRC\\Src_type1.txt",
				"Feature_SRC\\Src_type1_vector.txt",
				"Feature_SRC\\Src_type1_detail.txt",
				"Feature_SRC\\Src_type1_Undefined.txt");

		classiferSrc("Config\\Dict_SrcNewType.txt", 
				"Config\\Dict_SrcNewType_vector_inBaiKe.txt", 
				"Feature_SRC\\Src_Description.txt", 
				"Feature_SRC\\Src_newtype1.txt",
				"Feature_SRC\\Src_newtype1_vector.txt",
				"Feature_SRC\\Src_newtype1_detail.txt",
				"Feature_SRC\\Src_newtype1_Undefined.txt");
		/*classiferSrcWithKeyword("Config\\Dict_SrcNewType.txt", 
				"\\Feature_SRC\\Src_Keywords.txt", 
				"Feature_SRC\\Src_type2.txt",
				"Feature_SRC\\Src_type2_detail.txt",
				"Feature_SRC\\Src_type2_Undefined.txt");*/
		/*classiferSrcWithKeyword("Config\\Dict_AppType.txt",
				"\\Feature_SRC\\App_Keywords.txt", 
				"Feature_SRC\\App_type2.txt",
				"Feature_SRC\\App_type2_detail.txt",
				"Feature_SRC\\App_type2_Undefined.txt");*/
		
		/*classiferSrc("Config\\Dict_SrcType_Expand25.txt", 
				"Feature_SRC\\Src_Description.txt", 
				"Feature_SRC\\Src_type1_Expand25.txt",
				"Feature_SRC\\Src_type1_Expand25_detail.txt",
				"Feature_SRC\\Src_type1_Expand25_Undefined.txt");
		classiferSrcWithKeyword("Config\\Dict_SrcType_Expand25.txt", 
				"\\Feature_SRC\\Src_Keywords.txt", 
				"Feature_SRC\\Src_type2_Expand25.txt",
				"Feature_SRC\\Src_type2_Expand25_detail.txt",
				"Feature_SRC\\Src_type2_Expand25_Undefined.txt");

		classiferSrc("Config\\Dict_AppType_Expand25.txt", 
				"Feature_SRC\\App_Description.txt", 
				"Feature_SRC\\App_type1_Expand25.txt",
				"Feature_SRC\\App_type1_Expand25_detail.txt",
				"Feature_SRC\\App_type1_Expand25_Undefined.txt");
		classiferSrcWithKeyword("Config\\Dict_AppType_Expand25.txt", 
				"\\Feature_SRC\\App_Keywords.txt", 
				"Feature_SRC\\App_type2_Expand25.txt",
				"Feature_SRC\\App_type2_Expand25_detail.txt",
				"Feature_SRC\\App_type2_Expand25_Undefined.txt");

		classiferSrc("Config\\Dict_SrcType_BaiKeExpand25.txt", 
				"Feature_SRC\\Src_Description.txt", 
				"Feature_SRC\\Src_type1_BaiKeExpand25.txt",
				"Feature_SRC\\Src_type1_BaiKeExpand25_detail.txt",
				"Feature_SRC\\Src_type1_BaiKeExpand25_Undefined.txt");
		classiferSrcWithKeyword("Config\\Dict_SrcType_BaiKeExpand25.txt", 
				"\\Feature_SRC\\Src_Keywords.txt", 
				"Feature_SRC\\Src_type2_BaiKeExpand25.txt",
				"Feature_SRC\\Src_type2_BaiKeExpand25_detail.txt",
				"Feature_SRC\\Src_type2_BaiKeExpand25_Undefined.txt");

		classiferSrc("Config\\Dict_AppType_BaiKeExpand25.txt", 
				"Feature_SRC\\App_Description.txt", 
				"Feature_SRC\\App_type1_BaiKeExpand25.txt",
				"Feature_SRC\\App_type1_BaiKeExpand25_detail.txt",
				"Feature_SRC\\App_type1_BaiKeExpand25_Undefined.txt");
		classiferSrcWithKeyword("Config\\Dict_AppType_BaiKeExpand25.txt", 
				"\\Feature_SRC\\App_Keywords.txt", 
				"Feature_SRC\\App_type2_BaiKeExpand25.txt",
				"Feature_SRC\\App_type2_BaiKeExpand25_detail.txt",
				"Feature_SRC\\App_type2_BaiKeExpand25_Undefined.txt");*/
		
		
		/*classiferSrcWithKeyword("Config\\Dict_AppNewType_BaiKeExpand25.txt", 
				"Config\\Dict_ApptypeType_BaiKeExpand25_vector_inBaiKe.txt", 
				"\\Feature_SRC\\App_Keywords.txt", 
				"Feature_SRC\\App_newtype2_BaiKeExpand25.txt",
				"Feature_SRC\\App_newtype2_BaiKeExpand25_vector.txt",
				"Feature_SRC\\App_newtype2_BaiKeExpand25_detail.txt",
				"Feature_SRC\\App_newtype2_BaiKeExpand25_Undefined.txt");*/

	}
	private static void classiferSrcWithKeyword(String dict_file, String dict_vector_file, String src_file, String res_file, String res_file1, String res_file2, String res_file3) throws IOException {
		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f1 = new File(PATH+res_file1);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(f1));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));

		Map<String,Set<String>> src_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(src_type_map,PATH+dict_file,"\t","##",1,0);
		Map<String,float[]> src_type_vector_map = new HashMap<String,float[]>();
		int vec_length = GetInfo.getFloatMap(PATH+dict_vector_file,src_type_vector_map,"\t",0,1," ");

		File f0 = new File(PATH+src_file);
		BufferedReader br = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br.readLine())!=null){
			boolean flag = false;
			float[] src_type = new float[src_type_map.size()+1];
			float[] src_type_vector = new float[vec_length];
			StringBuffer addition_info = new StringBuffer();
			Set<String> keywords = new HashSet<String>();
			String[] items = line.split("\t");
			String src_name = items[0];
			String src_desc = "";
			addition_info.append(src_name+"\t");
			if(items.length>1){
				String[] item = items[1].split("##");
				for(String it : item){keywords.add(it);}
				if(keywords.size()==0)continue;
				for(String keyword : keywords){src_desc+=keyword+"、";}
				addition_info.append(src_desc+"\t");
				for(Entry<String,Set<String>> type_entry : src_type_map.entrySet()){//对于每一类，检查该用户的src是否会可能属于他们
					int type = Integer.parseInt(type_entry.getKey());
					Set<String> type_set = type_entry.getValue();
					Set<String> hit_keywords = new HashSet<String>();//这些keywords属于该类
					for(String keyword : keywords){
						keyword = keyword.toUpperCase();
						if(type_set.contains(keyword)){
							hit_keywords.add(keyword);
							flag = true;
						}	
					}
					if(hit_keywords.size()>0){
						flag = true;
						src_type[type] += 1;
						//src_type[type] += hit_keywords.size();
						float[] type_vector = src_type_vector_map.get(type+"");
						for(int i=0;i<vec_length;i++){src_type_vector[i] += type_vector[i];}
						addition_info.append(type);//将类别添加到BufferString中，作为中间结果输出
						for(String keyword : hit_keywords){addition_info.append("##"+keyword);}
						addition_info.append("\t");
					}	

				}
			}else{//没有关键字的src.为了格式要添加一个tab占位符，表示src的描述为""
				addition_info.append(src_desc+"\t");
			}

			if(!flag){
				src_type[OTHER_TYPE] += 1;
				addition_info.append(OTHER_TYPE+"##\t");
				bw2.write(addition_info.toString()+"\r\n");
			}
			
			bw.write(src_name+"\t");
			for(int i=0;i<src_type.length;i++){bw.write(src_type[i]+" ");}
			bw.write("\r\n");
			
			bw1.write(src_name+"\t");
			for(int i=0;i<vec_length;i++){bw1.write(src_type_vector[i]+" ");}
			bw1.write("\r\n");
			
			bw2.write(addition_info.toString()+"\r\n");
		}
		br.close();
		bw.flush();
		bw.close();
		bw1.flush();
		bw1.close();
		bw2.flush();
		bw2.close();
		bw3.flush();
		bw3.close();
	}
	public static void classiferSrc(String dict_file, String dict_vector_file, String src_file, String res_file, String res_file1, String res_file2, String res_file3) throws IOException {
		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f1 = new File(PATH+res_file1);
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(f1));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));

		Map<String,Set<String>> src_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(src_type_map,PATH+dict_file,"\t","##",1,0);
		Map<String,float[]> src_type_vector_map = new HashMap<String,float[]>();
		int vec_length = GetInfo.getFloatMap(PATH+dict_vector_file,src_type_vector_map,"\t",0,1," ");

		File f0 = new File(PATH+src_file);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br0.readLine())!=null){
			String src_name = line.split("\t",2)[0];
			String src_desc = line.replace("\t", " ");
			if(src_desc.equals("")){continue;}
			float[] src_type = new float[src_type_map.size()+1];
			float[] src_type_vector = new float[vec_length];
			StringBuffer addition_info = new StringBuffer();
			addition_info.append(src_name+"\t");
			addition_info.append(src_desc+"\t");
			boolean flag = false;
			for(Entry<String,Set<String>> type_words_entry : src_type_map.entrySet()){
				String type = type_words_entry.getKey();
				Set<String> type_words_set = type_words_entry.getValue();
				Set<String> keywords = Utils.checkwords(type_words_set, src_desc);
				if(keywords.size()>0){
					flag = true;
					src_type[Integer.parseInt(type)]+=1;
					float[] type_vector = src_type_vector_map.get(type);
					for(int i=0;i<vec_length;i++){src_type_vector[i] += type_vector[i];}
					addition_info.append(type);
					for(String keyword : keywords){addition_info.append("##"+keyword);}
					addition_info.append("\t");
				}	
			}
			if(!flag){
				src_type[OTHER_TYPE]+=1;
				addition_info.append(OTHER_TYPE+"##\t");
				//System.out.println(newline);
				bw3.write(addition_info.toString()+"\r\n");
			}

			bw.write(src_name+"\t");
			for(int i=0;i<src_type.length;i++){bw.write(src_type[i]+" ");}
			bw.write("\r\n");
			bw1.write(src_name+"\t");
			for(int i=0;i<vec_length;i++){bw1.write(src_type_vector[i]+" ");}
			bw1.write("\r\n");
			bw2.write(addition_info.toString()+"\r\n");
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
	public static void classiferSrc(String dict_file, String src_file, String res_file, String res_file2, String res_file3) throws IOException {
		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));

		Map<String,Set<String>> src_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(src_type_map,PATH+dict_file,"\t","##",1,0);

		File f0 = new File(PATH+src_file);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br0.readLine())!=null){
			String src_name = line.split("\t",2)[0];
			String src_desc = line.replace("\t", " ");
			if(src_desc.equals("")){continue;}
			float[] src_type = new float[src_type_map.size()+1];
			StringBuffer addition_info = new StringBuffer();
			addition_info.append(src_name+"\t");
			addition_info.append(src_desc+"\t");
			boolean flag = false;
			for(Entry<String,Set<String>> type_words_entry : src_type_map.entrySet()){
				String type = type_words_entry.getKey();
				Set<String> type_words_set = type_words_entry.getValue();
				Set<String> keywords = Utils.checkwords(type_words_set, src_desc);
				if(keywords.size()>0){
					flag = true;
					src_type[Integer.parseInt(type)]+=1;
					addition_info.append(type);
					for(String keyword : keywords){addition_info.append("##"+keyword);}
					addition_info.append("\t");
				}	
			}
			if(!flag){
				src_type[OTHER_TYPE]+=1;
				addition_info.append(OTHER_TYPE+"##\t");
				//System.out.println(newline);
				bw3.write(addition_info.toString()+"\r\n");
			}

			bw.write(src_name+"\t");
			for(int i=0;i<src_type.length;i++){bw.write(src_type[i]+" ");}
			bw.write("\r\n");
			bw2.write(addition_info.toString()+"\r\n");
		}
		br0.close();
		bw.flush();
		bw.close();
		bw2.flush();
		bw2.close();
		bw3.flush();
		bw3.close();
	}
	private static void classiferSrcWithKeyword(String dict_file,  String src_file, String res_file, String res_file2, String res_file3) throws IOException {
		File f = new File(PATH+res_file);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		File f2 = new File(PATH+res_file2);
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		File f3 = new File(PATH+res_file3);
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(f3));

		Map<String,Set<String>> src_type_map = new HashMap<String,Set<String>>();
		GetInfo.getSetMap(src_type_map,PATH+dict_file,"\t","##",1,0);

		File f0 = new File(PATH+src_file);
		BufferedReader br = new BufferedReader(new FileReader(f0));
		String line;
		while((line = br.readLine())!=null){
			boolean flag = false;
			float[] src_type = new float[src_type_map.size()+1];
			StringBuffer addition_info = new StringBuffer();
			Set<String> keywords = new HashSet<String>();
			String[] items = line.split("\t");
			String src_name = items[0];
			String src_desc = "";
			addition_info.append(src_name+"\t");
			if(items.length>1){
				String[] item = items[1].split("##");
				for(String it : item){keywords.add(it);}
				if(keywords.size()==0)continue;
				for(String keyword : keywords){src_desc+=keyword+"、";}
				addition_info.append(src_desc+"\t");
				for(Entry<String,Set<String>> type_entry : src_type_map.entrySet()){//对于每一类，检查该用户的src是否会可能属于他们
					int type = Integer.parseInt(type_entry.getKey());
					Set<String> type_set = type_entry.getValue();
					Set<String> hit_keywords = new HashSet<String>();//这些keywords属于该类
					for(String keyword : keywords){
						keyword = keyword.toUpperCase();
						if(type_set.contains(keyword)){
							hit_keywords.add(keyword);
							flag = true;
						}	
					}
					if(hit_keywords.size()>0){
						flag = true;
						src_type[type] += 1;
						addition_info.append(type);//将类别添加到BufferString中，作为中间结果输出
						for(String keyword : hit_keywords){addition_info.append("##"+keyword);}
						addition_info.append("\t");
					}	

				}
			}else{//没有关键字的src.为了格式要添加一个tab占位符，表示src的描述为""
				addition_info.append(src_desc+"\t");
			}

			if(!flag){
				src_type[OTHER_TYPE] += 1;
				addition_info.append(OTHER_TYPE+"##\t");
				bw2.write(addition_info.toString()+"\r\n");
			}
			
			bw.write(src_name+"\t");
			for(int i=0;i<src_type.length;i++){bw.write(src_type[i]+" ");}
			bw.write("\r\n");
			
			bw2.write(addition_info.toString()+"\r\n");
		}
		br.close();
		bw.flush();
		bw.close();
		bw2.flush();
		bw2.close();
		bw3.flush();
		bw3.close();
	}
}
