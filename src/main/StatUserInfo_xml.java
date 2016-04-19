package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

import utils.SaveInfo;

public class StatUserInfo_xml {
	/**
	 * 统计数据集用户的分布情况。如好友数分布情况、微博数分布情况等
	 * @param args
	 * @throws IOException
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
	private static String PATH = "D:\\Project_DataMinning\\DataSource\\";
	public static void main(String args[]) throws IOException {
		

		getDistribution(PATH+"Sina_NLPIR微博博主语料库.xml","fansNum");
		getDistribution(PATH+"Sina_NLPIR微博博主语料库.xml","gzNum");
		getDistribution(PATH+"Sina_NLPIR微博博主语料库.xml","wbNum");
		//统计filename中黄V用户微博数小于等于WBNUM，粉丝数和好友数大于等于FRINUM
		/*getUserID(PATH+"Sina_NLPIR微博博主语料库.xml",0,10,600);
		getUserID(PATH+"Sina_NLPIR微博博主语料库.xml",5,10,600);
		getUserID(PATH+"Sina_NLPIR微博博主语料库.xml",10,10,600);*/

	}
	private static void getDistribution(String filename,String item) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		String line = "";
		String regex = "<[/]?[a-zA-Z]{2,10}>";
		int tnum0=0,tnum1=0,tnum2=0,tnum3=0,tnum4=0,tnum5=0,tnum6=0,tnum7=0,tnum8=0,tnum9=0,tnum10=0,tnum11=0,tnum12=0;
		while((line = r.readLine())!=null){
			if(line.contains(item)){
				if(line.contains("null")){
					System.out.println();
					continue;
				}
				int number = Integer.parseInt(line.split(regex)[1]);
				if ( number == 0 )  tnum0 ++;
				else if ( (number >=1 ) && (number < 50) )  tnum1 ++;
				else if ( (number >=50 ) && (number < 100) )  tnum2 ++;
				else if ( (number >=100 ) && (number < 200) ) tnum3 ++;
				else if ( (number >=200 ) && (number < 300) ) tnum4 ++;
				else if ( (number >=300 ) && (number < 400) ) tnum5 ++;
				else if ( (number >=400 ) && (number < 500) ) tnum6 ++;
				else if ( (number >=500 ) && (number < 600) ) tnum7 ++;
				else if ( (number >=600 ) && (number < 700) ) tnum8 ++;
				else if ( (number >=700 ) && (number < 800) ) tnum9 ++;
				else if ( (number >=800 ) && (number < 900) ) tnum10 ++;
				else if ( (number >=900 ) && (number < 1000) ) tnum11 ++;
				else tnum12 ++;
			}
		}
		r.close();
		printResult(tnum0,tnum1,tnum2,tnum3,tnum4,tnum5,tnum6,tnum7,tnum8,tnum9,tnum10,tnum11,tnum12);

	}
	//统计filename中黄V用户微博数小于等于WBNUM，粉丝数和好友数大于等于FRINUM
	private static void getUserID(String filename,int WBNUM,int FRINUM_min,int FRINUM_max) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		SaveInfo.mkdir(PATH+"StatVUserInfo\\");
		File resf1 = new File(PATH+"StatVUserInfo\\"+WBNUM+"_"+FRINUM_min+"_"+FRINUM_max+"_female.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(resf1));
		File resf2 = new File(PATH+"StatVUserInfo\\"+WBNUM+"_"+FRINUM_min+"_"+FRINUM_max+"_male.txt");
		BufferedWriter w2 = new BufferedWriter(new FileWriter(resf2));
		String line = "";
		String regex = "<[/]?[a-zA-Z]{2,10}>";
		int tnum=0;
		while((line = r.readLine())!=null){
			if(line.contains("<id>")){
				String id = line.split(regex)[1];
				line = r.readLine();
				if(line.contains("<sex></sex>"))continue;
				String sex = line.split(regex)[1];
				if(!sex.equals("男")&&!sex.equals("女"))continue;
				
				for(int i=0;i<2;i++){line = r.readLine();}
				if(line.contains("null"))continue;
				int fansNum = Integer.parseInt(line.split(regex)[1].trim());

				for(int i=0;i<2;i++){line = r.readLine();}
				if(line.contains("null"))continue;
				int wbNum = Integer.parseInt(line.split(regex)[1].trim());

				for(int i=0;i<1;i++){line = r.readLine();}
				if(line.contains("null"))continue;
				int gzNum = Integer.parseInt(line.split(regex)[1].trim());

				for(int i=0;i<4;i++){line = r.readLine();}
				if(line.contains("null"))continue;
				int renZh = Integer.parseInt(line.split(regex)[1].trim());
				
				if (renZh==0&&wbNum<=WBNUM&&gzNum>=FRINUM_min&&fansNum>=FRINUM_min&&fansNum<=FRINUM_max&&fansNum<=FRINUM_max){
					tnum ++;
					if(sex.equals("女")) w1.write(id+"\r\n");
					if(sex.equals("男")) w2.write(id+"\r\n");
				}
			}
		}
		r.close();
		w1.flush();w2.flush();
		w1.close();w2.close();
		System.out.println(WBNUM+"_"+FRINUM_min/*+"_"+FRINUM_max*/+"_"+tnum);
	}

	private static void printResult(int ... num) {
		for(int n : num){
			System.out.print(n+"\t");
		}
		System.out.println();
	}
}
