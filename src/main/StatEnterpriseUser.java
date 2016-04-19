package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 统计企业用户的分布情况
 * Result:
 * 						NormalUser	EnterpriseUser
 * Sum					1611842		130343
 * Description_Tags		653833		102027
 * NoDescription_Tags	247683		6833
 * Description_NoTags	281652		17362
 * NoDescription_NoTags	428674		4121
 * @author Hannah
 *
 */
public class StatEnterpriseUser {

	static int sum = 0;
	static int attr_tags = 0;
	static int noattr_tags = 0;
	static int attr_notags = 0;
	static int noattr_notags = 0;
	static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON_1000\\";
	public static void main(String args[]) throws IOException{
		getStats("UserInfo1.txt","description");
		System.out.println("sum="+sum+",attr_tags="+attr_tags+",noattr_tags="+noattr_tags+",attr_notags="+attr_notags+",noattr_notags="+noattr_notags);
		sum = 0;attr_tags = 0;noattr_tags = 0;attr_notags = 0;noattr_notags = 0;
		getStats("UserInfoOfEnterprise1.txt","description");
		System.out.println("sum="+sum+",attr_tags="+attr_tags+",noattr_tags="+noattr_tags+",attr_notags="+attr_notags+",noattr_notags="+noattr_notags);
	}

	private static void getStats(String filename,String attr) throws IOException {
		File f = new File(PATH+filename);
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line = null;
		while((line = r.readLine())!=null){
			if(line.equals(""))continue;
			JSONObject json = JSONObject.fromObject(line);
			String attr_value = json.getString(attr);
			@SuppressWarnings("unchecked")
			List<String> tags = (List<String>) json.getJSONObject("tags").get("tags");
			sum++;
			if(attr_value==null||attr_value.equals("")){
				if(tags.size()==0){noattr_notags++;}
				else{noattr_tags++;}
			}
			else{
				if(tags.size()==0){attr_notags++;}
				else{attr_tags++;}
			}
		}
		r.close();
	}
}
