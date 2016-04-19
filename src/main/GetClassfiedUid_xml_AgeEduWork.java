package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.GetInfo;

public class GetClassfiedUid_xml_AgeEduWork{
	/**
编号	类别		年份			年龄段
1	青少年	1997-2001	13-17	11
2	青年		1989-1996	18-25	217
3	青中年	1980-1988	26-34	360
4	壮年		1960-1979	35-54	145+29 = 174
5	老年		1944-1959	55-70	10
	 * @param args
	 * @throws IOException
	 */
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON\\";
	/**
	 * @throws JAXBException 


	 */
	/*
<person>
  <id>80414</id> 
  <sex>男</sex> 
  <address>北京，海淀区</address> 
  <fansNum>437</fansNum> 
  <summary>Since 198X</summary> 
  <wbNum>333</wbNum> 
  <gzNum>242</gzNum> 
  <blog>null</blog> 
  <edu>大学： 中国人民大学</edu> 
  <work /> 
  <renZh>1</renZh> 
  <brithday>4月14日</brithday> 
 </person>

	 */
	public static void main(String args[]) throws IOException{
		File srcf = new File("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR微博博主语料库.xml");
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		File resf1 = new File("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR_Age_Edu_Work.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(resf1));
		String line = "";
		String regex = "<[/]?[a-zA-Z]{2,10}>";
		int num = 0;String id,sex,edu,work,brithday;
		while((line = r.readLine())!=null){
			if(line.contains("<id>")){
				id = line.split(regex)[1];
				//if(!id_list.contains(id)){continue;}//用于获取原有的ID中有性别和年龄的ID
				line = r.readLine();
				if(line.contains("<sex></sex>"))continue;
				sex = line.split(regex)[1];
				if(!sex.equals("男")&&!sex.equals("女"))continue;
				//edu
				for(int i=0;i<7;i++){line = r.readLine();}
				if(line.contains("<edu></edu>"))continue;
				if(line.contains("<edu>null</edu>"))continue;
				edu = line.split(regex)[1].trim();
				edu = edu.replaceAll("\\s", "-");
				edu = edu.replaceAll("&nbsp", ""); 
				//boolean result = edu.matches(".*\\p{Alpha}.*");
				if(!edu.contains("大学")||edu.matches(".*\\p{Alpha}.*")){continue;}
				//work
				line = r.readLine();
				if(line.contains("<work></work>"))continue;
				work = line.split(regex)[1].trim(); 
				work = work.replaceAll("\\s", "-");
				
				for(int i=0;i<2;i++){line = r.readLine();}
				if(line.contains("<brithday></brithday>"))continue;
				brithday = line.split(regex)[1]; 
				if(brithday.contains("年")){
					num++;
					//save(w1,id,sex,edu,work,brithday);
					save(w1,id,sex,edu,work,brithday);
				}
			}
		}
		r.close();
		w1.flush();w1.close();
		System.out.println(num);
	}
	private static void save(BufferedWriter w, String ... infos) throws IOException {
		for (String info : infos) {
			w.write(info+"\t");
		}
		w.write("\r\n");
	}
}
