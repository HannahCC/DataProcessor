package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.sf.json.JSONObject;
import utils.GetInfo;

public class StatUserInfo {
	/**
	 * 统计数据集用户的分布情况。如好友数分布情况、微博数分布情况等 
	 * @param args
	 * @throws IOException
	 */
	private static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException {

		//根据filename统计uids中用户的item数量的分布情况
		Set<String> uids = new HashSet<String>(2000);
		GetInfo.getSet(PATH+"UserID_Age\\1_old2_new.txt", uids);
		/*getDistribution(uids, PATH+"UserInfo0.txt","friendsCount");
		getDistribution(uids, PATH+"UserInfo0.txt","followersCount");
		getDistribution(uids, PATH+"UserInfo0.txt","statusesCount");
		getDistributionOfTag(uids, PATH+"UserInfo0.txt","tags");//标签数
*/	
		//统计用户已经爬取的微博数分布
		getDistribution(uids,PATH+"WeibosCon\\");

		//获取filename中黄V用户微博数小于等于WBNUM，粉丝数和好友数大于等于FRINUM的ID
		/*getUserID(PATH+"UserInfo1.txt",1,10,600);
		getUserID(PATH+"UserInfo1.txt",5,10,600);
		getUserID(PATH+"UserInfo1.txt",6,10,600);*/
		//getUserID(PATH+"UserInfo1.txt",10,10,600);

	}
	
	private static void getDistribution(Set<String> uids, String dirname) throws IOException {
		int tnum0=0,tnum1=0,tnum2=0,tnum3=0,tnum4=0,tnum5=0,tnum6=0,tnum7=0,tnum8=0,tnum9=0,tnum10=0,tnum11=0,tnum12=0;

		int number = 0;
		File srcf;
		BufferedReader r;
		for(String id: uids){
			srcf = new File(dirname+id+".txt");
			if(!srcf.exists()){
				System.out.println(id);
				continue;
			}
			r = new BufferedReader(new FileReader(srcf));
			number = 0;
			while(r.readLine()!=null){
				number++;
			}
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

		printResult(tnum0,tnum1,tnum2,tnum3,tnum4,tnum5,tnum6,tnum7,tnum8,tnum9,tnum10,tnum11,tnum12);
	}

	//统计filename中用户的item数量的分布情况
	private static void getDistribution(Set<String> uids, String filename,String item) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		String line = "";
		int tnum0=0,tnum1=0,tnum2=0,tnum3=0,tnum4=0,tnum5=0,tnum6=0,tnum7=0,tnum8=0,tnum9=0,tnum10=0,tnum11=0,tnum12=0;
		while((line = r.readLine())!=null){
			JSONObject json = JSONObject.fromObject(line);
			String id = json.getString("id");
			if(!uids.contains(id))continue;
			/*int usertype = json.getInt("verifiedType");
			if(usertype!=0&&usertype<200)continue;*/
			/*int statusesCount = json.getInt("statusesCount");
			if(statusesCount>4)continue;*/
			
			int number = json.getInt(item);
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
		r.close();
		printResult(tnum0,tnum1,tnum2,tnum3,tnum4,tnum5,tnum6,tnum7,tnum8,tnum9,tnum10,tnum11,tnum12);
	}

	//统计filename中用户的tags、desci等非数值量的item数量的分布情况
	private static void getDistributionOfTag(Set<String> uids,String filename, String item) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		String line = "";
		int tnum1=0,tnum2=0,tnum3=0,tnum4=0,tnum5=0,tnum6=0,tnum7=0;
		while((line = r.readLine())!=null){
			JSONObject json = JSONObject.fromObject(line);
			String id = json.getString("id");
			if(!uids.contains(id))continue;
			int number = 0;
			if(item.equalsIgnoreCase("tags")){
				number = json.getJSONObject("tags").getInt("total_number");
				if (number == 0 )  tnum1 ++;
				else if ( (number >=1 ) && (number < 3) )  tnum2 ++;
				else if ( (number >=3 ) && (number < 5) ) tnum3 ++;
				else if ( (number >=5 ) && (number < 7) ) tnum4 ++;
				else if ( (number >=7 ) && (number < 9) ) tnum5 ++;
				else if ( (number >=9 ) && (number < 11) ) tnum6 ++;
				else {
					tnum7 ++;
					System.out.println(number);
				}
			}else if(item.equalsIgnoreCase("description")){
				String desc = json.getString("description");
				if(desc==null||desc.equals("")){
					tnum1++;
				}else {
					tnum2++;
				}
			}
		}
		r.close();
		printResult(tnum1,tnum2,tnum3,tnum4,tnum5,tnum6,tnum7);
	}

	//统计filename中用户微博数小于等于WBNUM，粉丝数和好友数大于等于FRINUM
	private static void getUserID(String filename,int WBNUM,int FRINUM_min,int FRINUM_max) throws IOException {
		File srcf = new File(filename);
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		File resf1 = new File(PATH+"UserID_mute(保证有Tag)_UserInfo1_朋友数小于600\\"+WBNUM+"_"+FRINUM_min+"_"+FRINUM_max+"_female.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(resf1));
		File resf2 = new File(PATH+"UserID_mute(保证有Tag)_UserInfo1_朋友数小于600\\"+WBNUM+"_"+FRINUM_min+"_"+FRINUM_max+"_male.txt");
		BufferedWriter w2 = new BufferedWriter(new FileWriter(resf2));
		String line = "";
		int tnum=0;
		while((line = r.readLine())!=null){
			JSONObject json = JSONObject.fromObject(line);
			String id = json.getString("id");
			int usertype = json.getInt("verifiedType");
			int tag_number = json.getJSONObject("tags").getInt("total_number");
			if(tag_number>0&&(usertype==0||usertype>=200)){
				int number = json.getInt("statusesCount");
				int friNum = json.getInt("friendsCount");
				int folNum = json.getInt("followersCount");
				if (number<WBNUM&&friNum>=FRINUM_min&&folNum>=FRINUM_min&&friNum<=FRINUM_max&&folNum<=FRINUM_max){
					/*File f = new File(PATH+"weibos\\"+id+".txt");
					if(!f.exists()){continue;}*/
					tnum ++;
					String gender = json.getString("gender").intern();
					if(gender=="f")w1.write(id+"\r\n");
					else w2.write(id+"\r\n");
				}
			}
		}
		r.close();
		w1.flush();w1.close();
		w2.flush();w2.close();
		System.out.println(WBNUM+"_"+FRINUM_min+"_"+FRINUM_max+"_"+tnum);
	}

	private static void printResult(int ... num) {
		for(int n : num){
			System.out.print(n+"\t");
		}
		System.out.println();
	}

}
