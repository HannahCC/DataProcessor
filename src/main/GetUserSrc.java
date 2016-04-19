package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

public class GetUserSrc {

	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException {
		getUserSrc("Feature_UserInfo\\UserIdSrc.txt",false,"WeibosSrc\\Src_map.txt");
	}
	private static void getUserSrc(String resf, boolean isAppend, String srcf) throws IOException {
		Map<String, String> uid_tag = new HashMap<String, String>();
		File f=new File(PATH+srcf);
		BufferedReader br=new BufferedReader(new FileReader(f));
		String line="";
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				JSONObject json = JSONObject.fromObject(line);
				String id = json.getString("id");
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
