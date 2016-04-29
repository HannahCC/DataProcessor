package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
public class SaveInfo {
	
	public static void saveString(String dirname, String filename, String info,boolean isAppend) throws IOException {
		if(null==info||"".equals(info))return;
		mkdir(dirname);
		File f = new File(dirname+"\\"+filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(f,isAppend));
		w.write(info+"\r\n");
		w.flush();
		w.close();
	}
	
	public static void saveSet(String dirname, String filename, Set<?> set, boolean isAppend) throws IOException {
		mkdir(dirname);
		File f = new File(dirname+"\\"+filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(f,isAppend));
		for(Object item : set){
			w.write(item+"\r\n");
		}
		w.flush();
		w.close();
	}

	public static void saveSet(String dir, String filename, Set<?> set, int max, boolean isAppend) {
		mkdir(dir);
		File f = new File(dir+filename);
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(f,isAppend));
			int i = 0;
			for(Object item : set){
				i++;
				w.write(item+"\r\n");
				if(i>=max)break;
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveList(String dirname, String filename,List<?> list, boolean isAppend) throws IOException {
		mkdir(dirname);
		File f = new File(dirname+"\\"+filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(f,isAppend));
		for(Object item : list){
			w.write(item+"\r\n");
		}
		w.flush();
		w.close();
	}

	public static void saveMap(String dirname, String filename, Map<?, ?> feature_map, String regex, boolean isAppend) throws IOException {
		mkdir(dirname);
		File f = new File(dirname+filename);
		BufferedWriter w = new BufferedWriter(new FileWriter(f,isAppend));
		Iterator<?> it = feature_map.entrySet().iterator();
		while(it.hasNext()){
			Entry<?, ?> entry = (Entry<?, ?>) it.next();
			if(entry.getValue().equals("")){continue;}
			else{w.write(entry.getKey()+regex+entry.getValue()+"\r\n");}
		}
		w.flush();
		w.close();
	}

	/**
	 * 复制文件,isDelete决定是否删除源文件
	 * @param src
	 * @param des_dir
	 * @throws IOException
	 */
	public static void fileCopy(File src,String des_dir, boolean isDelete) throws IOException{
		FileInputStream fi = new FileInputStream(src);
		FileChannel in = fi.getChannel();
		mkdir(des_dir);	
		File t = new File(des_dir+"\\"+src.getName());
		FileOutputStream fo = new FileOutputStream(t);
		FileChannel out = fo.getChannel();
		//out.transferFrom(in, 0, in.size());
		in.transferTo(0, in.size(), out);
		fi.close();
		in.close();
		fo.close();
		out.close();
		if(isDelete)src.delete();
	}

	public static void mkdir(String dir) {
		File dir_root1 = new File(dir);
		if(!dir_root1.exists())dir_root1.mkdir();
	}
	public static boolean isFileExist(String filename){
		File dir= new File(filename);
		if(dir.exists()){return true;}
		else{return false;}
	}

}
