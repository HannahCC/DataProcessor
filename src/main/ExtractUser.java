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

import net.sf.json.JSONObject;
import utils.GetInfo;
import utils.SaveInfo;
import utils.Utils;

/**
 * 从SRC_PATH中提取部分蓝V企业用户、非蓝V企业用户、普通个人用户放入RES_PATH中
 * @author Hannah
 *
 */
public class ExtractUser {
	static String SRC_PATH ="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR400_Good\\"; //ext1000_Mute_GenderPre
	static String RES_PATH ="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\"; 

	static{
		File resfile = new File(RES_PATH);
		if(!resfile.exists())resfile.mkdirs();
	}
	public static void main(String args[]) throws IOException{
		//从SRC_PATH下的文件"ToQian\\5_10_600_female.txt"中随机抽取500个ID，并提取他们的用户信息、朋友信息
		Set<String> uid = new HashSet<String>();
		//getRandomUid(uid,500,"ToQian\\5_10_600_female.txt");4
		//getRandomUid(uid,500,"ToQian\\5_10_600_male.txt");
		GetInfo.getSet(RES_PATH+"ExpandID1.txt", uid);//所有女性ID
		//SaveInfo.saveSet(RES_PATH, "UserID_Age\\All_old2.txt", uid, false);
		
		//extractUserInfo(uid,"UidInfo_follows1.txt","UserID_Age\\UidInfo_follows1.txt",false);
		//extractUserInfo(uid,"UidInfo_friends1.txt","UserID_Age\\UidInfo_friends1.txt",false);
		//extractUserInfo(uid,"UserInfo1.txt","UserID_Age\\3_UserInfo1.txt",true);
		//extractUserInfo(uid,"UserInfoOfEnterprise1.txt","UserInfoOfEnterprise2.txt",true);
		
		/*extractUserInfo(uid,"UserInfo0.txt","UserInfo0.txt",false);
		extractUserInfo(uid,"UserInfoOfEnterprise0.txt","UserInfoOfEnterprise0.txt",false);
		extractUserInfoNormal(uid,"UserInfo0.txt","UserInfo0_Normal.txt");
		extractUserInfoNormal(uid,"UserInfoOfEnterprise0.txt","UserInfo0_Normal.txt");*/
		extractWeibo(uid, "Weibos", "Weibos1");

	}
	private static void extractId(Set<String> uid,String srcfile,String resfile,boolean isRemove) throws IOException {
		File f1 = new File(SRC_PATH+srcfile);
		BufferedReader r1 = new BufferedReader(new FileReader(f1));
		File f2 = new File(RES_PATH+resfile);
		BufferedWriter w1 = new BufferedWriter(new FileWriter(f2));
		String line = null;
		while(null!=(line=r1.readLine())){
			if(line.equals(""))continue;
			String id = line.split("\t")[0];
			if(uid.contains(id)){
				w1.write(line+"\r\n");
			}
		}
		w1.flush();w1.close();
		r1.close();
	}
	/**
	 * 
	 * @param uid
	 * @param string
	 * @param string2
	 * @throws IOException 
	 */
	private static void extractWeibo(Set<String> uid,String srcdir,String resdir) throws IOException {
		for(String id : uid){
			File f = new File(SRC_PATH+srcdir+"\\"+id+".txt");
			if(f.exists()){
				SaveInfo.fileCopy(f, RES_PATH+resdir, false);
			}
		}
	}
	/**
	 * 获取srcfile中不是大V或者达人的用户的信息
	 * @param uid
	 * @param srcfile
	 * @param resfile
	 * @throws IOException 
	 */
	private static void extractUserInfoNormal(Set<String> uid,String srcfile,String resfile) throws IOException {
		File f = new File(SRC_PATH+srcfile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		File fw = new File(RES_PATH+resfile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fw,true));
		String line = null;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			int type = user.getInt("verifiedType");
			if(uid.contains(id)&&(type<0)){
				bw.write(line+"\r\n");
			}
		}
		br.close();bw.flush();bw.close();
	}

	private static void getRandomUid(Set<String> uid, int n, String srcfile) throws IOException {
		List<String> uid_tmp = new ArrayList<String>();
		GetInfo.getList(SRC_PATH+srcfile, uid_tmp, true);
		Set<Integer> number = Utils.getRandomNumer(uid_tmp.size(), n);
		for(Integer i : number){
			uid.add(uid_tmp.get(i));
		}
	}

	/**
	 * 选取已经爬取了的用户
	 * @param uid
	 * @param size
	 * @throws IOException
	 */
	private static void extractUserInfo(Set<String> uid,String srcfile,String resfile,boolean isRemove) throws IOException {
		File f = new File(SRC_PATH+srcfile);
		BufferedReader br = new BufferedReader(new FileReader(f));
		idFilter(uid,RES_PATH+resfile);
		File fw = new File(RES_PATH+resfile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fw,true));
		String line = null;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			if(uid.contains(id)){
				bw.write(line+"\r\n");
				if(isRemove)uid.remove(id);
			}
		}
		br.close();bw.flush();bw.close();
	}
	private static void idFilter(Set<String> uid,String filename) throws IOException{
		System.out.println("before filter:"+uid.size());
		File f=new File(filename);
		if(!f.exists()){return;}
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = "";
		while((line = br.readLine())!=null){
			JSONObject user_json = JSONObject.fromObject(line);
			String id = user_json.getString("id");
			uid.remove(id);
		}
		br.close();
		System.out.println("after filter:"+uid.size());
	}
}
