package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import utils.SaveInfo;
import utils.Utils;
import net.sf.json.JSONObject;
/** 
 * GetUserItemNgram: 获取用户对"screenName","verifiedReason","description"分别进行2gram、3gram分词后形成的特征
	=UserInfoTMP
		-TESTUSER_PATH.txt.screenName.2gram
		-TESTUSER_PATH.txt.screenName.3gram
		-TESTUSER_PATH.txt.verifiedReason.2gram
		-TESTUSER_PATH.txt.verifiedReason.3gram
		-TESTUSER_PATH.txt.description.2gram
		-TESTUSER_PATH.txt.description.3gram
 * @author Hannah
 *
 */
public class GetUserItemNgram {
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR400_Good\\";
	public static String[] ITEM = {"screenName","verifiedReason","description"};
	public static String TESTUSER_PATH = "UserInfo0.txt";
	public static void main(String args[]) throws IOException{
		SaveInfo.mkdir("Feature_UserInfo");
		getUserInfoNgram("UserInfo0.txt","screenName");
		//getUserInfoNgram("UserInfo1_Friends.txt","screenName");
		/*getUserInfoNgram("UserInfo0.txt","verifiedReason");
		getUserInfoNgram("UserInfo0.txt","description");*/
	}
	/*********************************************用户信息提取*****************************************************************/	
	public static void getUserInfoNgram(String TESTUSER_PATH, String INFO) throws IOException {
		File f0 = new File(PATH+TESTUSER_PATH);
		BufferedReader br0 = new BufferedReader(new FileReader(f0));
		File f1 = new File(PATH+"Feature_UserInfo\\"+"UserId"+INFO+"_1gram.txt");
		BufferedWriter w1 = new BufferedWriter(new FileWriter(f1,true));
		File f2 = new File(PATH+"Feature_UserInfo\\"+"UserId"+INFO+"_2gram.txt");
		BufferedWriter w2 = new BufferedWriter(new FileWriter(f2,true));
		File f3 = new File(PATH+"Feature_UserInfo\\"+"UserId"+INFO+"_3gram.txt");
		BufferedWriter w3 = new BufferedWriter(new FileWriter(f3,true));
		File f4 = new File(PATH+"Feature_UserInfo\\"+"UserId"+INFO+".txt");
		BufferedWriter w4 = new BufferedWriter(new FileWriter(f4,true));


		String line;
		while((line = br0.readLine())!=null){
			JSONObject user = JSONObject.fromObject(line);
			String id = user.getString("id");
			
			//"screenName"
			String user_attr = user.getString(INFO);
			List<String> user_ngram = Utils.string_toNgram(user_attr, 1);//如果为"",会得到一个长度为0的list
			writeMap(w1,id,user_ngram);
			
			user_ngram = Utils.string_toNgram(user_attr, 2);//如果为"",会得到一个长度为0的list
			writeMap(w2,id,user_ngram);
			
			user_ngram = Utils.string_toNgram(user_attr, 3);
			writeMap(w3,id,user_ngram);

			writeString(w4,id,user_attr);
			
		}
		br0.close();
		w1.flush();w1.close();
		w2.flush();w2.close();
		w3.flush();w3.close();
		w4.flush();w4.close();
	}

	/*********************************************
	 * @throws IOException *****************************************************************/
	private static void writeMap(BufferedWriter w, String id, List<String> user_ngram) throws IOException {
		if(user_ngram.size()==0)return;
		w.write(id+"\t");
		for(String ngram : user_ngram){
			w.write(ngram+" ");
		}
		w.write("\r\n");
	}
	private static void writeString(BufferedWriter w, String id, String str) throws IOException {
		if(str==null||str.length()==0)return;
		w.write(id+"\t"+str+"\r\n");
	}
}
