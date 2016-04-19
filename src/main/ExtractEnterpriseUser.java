package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import utils.SaveInfo;
import net.sf.json.JSONObject;

/**
 * 从SRC_PATH中提取部分蓝V企业用户、非蓝V企业用户、普通个人用户放入RES_PATH中
 * @author Hannah
 *
 */
public class ExtractEnterpriseUser {
	static String SRC_PATH ="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePred_WithEnterprise\\"; 
	static String RES_PATH ="D:\\Project_DataMinning\\Data\\Sina_res\\Sina_EnterpriseUserPred\\"; 
	static String WEIBO = "\\Weibos\\";
	static String USERINFO = "UserInfo.txt";
	static String NORMALUSER = "NormalUser.txt";
	static String ENTERPRISUSER = "UserInfoOfEnterprise0.txt";
	static String ENTERPRISUSER_BLUEV = "EnterpriseUser_BlueV0.txt";
	static String ENTERPRISUSER_NOTBLUEV = "EnterpriseUser_NotBlueV0.txt";
	
	static{
		File resfile = new File(RES_PATH);
		if(!resfile.exists())resfile.mkdirs();
	}
	public static void main(String args[]) throws IOException{
		Set<String> uid = new HashSet<String>();
		extractEnterpriseUser_BlueV(uid,2000);
		extractEnterpriseUser_NotBlueV(uid,2000);
		extractNormalUser(uid,2000);
		GetUserID(uid,RES_PATH+NORMALUSER);
		GetUserID(uid,RES_PATH+ENTERPRISUSER_NOTBLUEV);
		GetUserID(uid,RES_PATH+ENTERPRISUSER_BLUEV);
		extractUserWeibo(uid);
		SaveInfo.saveSet(RES_PATH,"ExpandID1.txt",uid,false);
	}

	public static void GetUserID(Set<String> uid_set, String filename) throws IOException {
		File f = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String uid = user.getString("id");
			uid_set.add(uid);
		}
		System.out.println(filename+"--"+uid_set.size());
		br.close();
	}

	//提取uid_set中对应ID的微博
	private static void extractUserWeibo(Set<String> uid_set) throws IOException {
		for(String uid : uid_set){
			if(SaveInfo.isFileExist(RES_PATH+WEIBO+uid+".txt"))continue;
			if(SaveInfo.isFileExist(SRC_PATH+WEIBO+uid+".txt")){
				File weibo = new File(SRC_PATH+WEIBO+uid+".txt");
				SaveInfo.fileCopy(weibo, RES_PATH+WEIBO+uid+".txt",false);
			}
		}
	}

	/**
	 * 选取已经爬取了微博的普通个人用户
	 * @param uid
	 * @param size
	 * @throws IOException
	 */
	private static void extractNormalUser(Set<String> uid, int size) throws IOException {
		File f = new File(SRC_PATH+USERINFO);
		BufferedReader br = new BufferedReader(new FileReader(f));
		File fw = new File(RES_PATH+NORMALUSER);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
		String line = null;
		int number = 0;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			if(uid.contains(id)){continue;}
			if(SaveInfo.isFileExist(id+".txt")){
				bw.write(line+"\r\n");
				uid.add(id);
				number++;
				if(number==size)break;
			}
		}
		System.out.println("NormalUser:"+number);
		br.close();bw.flush();bw.close();
	}
	/**
	 * 从企业用户中提取size个非蓝v用户（认证类别不为1-7的用户）
	 * @param uid
	 * @param size
	 * @throws IOException
	 */
	private static void extractEnterpriseUser_NotBlueV(Set<String> uid, int size) throws IOException {
		File f = new File(SRC_PATH+ENTERPRISUSER);
		BufferedReader br = new BufferedReader(new FileReader(f));
		File fw = new File(RES_PATH+ENTERPRISUSER_NOTBLUEV);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
		String line = null;int number = 0;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			if(uid.contains(id)){continue;}
			int type = user.getInt("verifiedType");
			if(type<1||type>7){
				bw.write(line+"\r\n");
				uid.add(id);
				number++;
				if(number==size)break;
			}
		}
		System.out.println("extractEnterpriseUser_NotBlueV:"+number);
		br.close();bw.flush();bw.close();
	}

	/**
	 * 从企业用户中提取size个蓝v用户（认证类别为1-7的用户）
	 * @param uid
	 * @param size
	 * @throws IOException
	 */
	private static void extractEnterpriseUser_BlueV(Set<String> uid, int size) throws IOException {
		File f = new File(SRC_PATH+ENTERPRISUSER);
		BufferedReader br = new BufferedReader(new FileReader(f));
		File fw = new File(RES_PATH+ENTERPRISUSER_BLUEV);
		BufferedWriter bw = new BufferedWriter(new FileWriter(fw));
		String line = null;
		while((line = br.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			if(uid.contains(id)){continue;}
			int type = user.getInt("verifiedType");
			if(type>=1&&type<=7){
				bw.write(line+"\r\n");
				uid.add(id);
				if(uid.size()==size)break;
			}
		}
		System.out.println("extractEnterpriseUser_BlueV:"+uid.size());
		br.close();bw.flush();bw.close();
	}
}
