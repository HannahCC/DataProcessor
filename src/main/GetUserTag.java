package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import utils.GetInfo;
import net.sf.json.JSONObject;

public class GetUserTag {

	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException {
		Set<String> ids = new HashSet<String>(600000);
		GetInfo.getSet(PATH+"ExpandID1_fri.txt", ids);
		
		Set<String> vids = new HashSet<String>(600000);
		GetInfo.getSet(PATH+"Config\\Dict_VFri.txt", vids);
		ids.retainAll(vids);
		
		GetInfo.getSet(PATH+"ExpandID0.txt", ids);
		
		getUserTag(ids,"Feature_UserInfo\\UserIdTag_vfri.txt",false,"UserInfo0.txt");
		getUserTag(ids,"Feature_UserInfo\\UserIdTag_vfri.txt",true,"UserInfo1.txt");
		
		/*getUserTag("UserID_Age\\3_UserIdTag.txt",false,"UserID_Age\\3_UserInfo1.txt");
		getUserTag("UserID_Age\\4_UserIdTag.txt",true,"UserID_Age\\4_UserInfo1.txt");*/
	}
	private static void getUserTag(Set<String> ids,String resf, boolean isAppend, String srcf) throws IOException {
		Map<String, String> uid_tag = new HashMap<String, String>();
		File f=new File(PATH+srcf);
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				String id = json.getString("id");
				if(!ids.contains(id))continue;
				@SuppressWarnings("unchecked")
				List<String> tags = (List<String>)  json.getJSONObject("tags").get("tags");
				if(tags.size()==0)continue;
				StringBuffer tags_StringBuffer = new StringBuffer();
				tags_StringBuffer.append(id+"\t");
				for(String tag : tags){
					tags_StringBuffer.append(tag+"\t");
				}
				uid_tag.put(id, tags_StringBuffer.toString());
			}
		}
		br.close();

		File resFile = new File(PATH+resf);
		BufferedWriter bw = new BufferedWriter(new FileWriter(resFile,isAppend));
		Iterator<Entry<String, String>> it = uid_tag.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			bw.write(entry.getValue()+"\r\n");
		}
		bw.flush();
		bw.close();
	}
	
}
