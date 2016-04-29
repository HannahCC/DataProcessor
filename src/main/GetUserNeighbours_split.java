package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
public class GetUserNeighbours_split {
	/**
	 * 获取ids用户集的邻居（如有共同粉丝/关注的人）   
	 * 获取方式   a -- 关注-- >{b,c,d,e,f}    b <--关注-- {a,g,h,i,j}   c <--关注-- {a,h,i,j,k}       若共同关注数阈值为2，则a的邻居为{h,i,j}，他们有共同的关注b,c
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static final String dir = "/home/zps/sina2333/";//ext1000_Mute_GenderPre
	private static final String srcfile_t =dir+ "Feature_UserInfo/UserIdNeighbours_c"; //1层用户的关系数据
	private static final String type= "FolAndFri.txt";
	private static final Byte[] thresholds = {1,2,3,5,8};
	private static final int MAX_SENTENCE_LENGTH = 1000-1;
	public static void main(String args[]) throws IOException{
		for(byte thre : thresholds){
			String filename = srcfile_t+thre+type,resfile = srcfile_t+thre+type+".s";
			splitUserNeighbour(filename,resfile);
		}

	}


	private static void splitUserNeighbour(String filename, String resfile) throws IOException {
		BufferedReader br=new BufferedReader(new FileReader(new File(filename)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resfile)));
		String line="";
		long id;
		int i  = 0;
		while((line=br.readLine())!=null)
		{
			if(!(line.equals(""))){
				StringTokenizer stringTokenizer = new StringTokenizer(line,"\t");
				id = Long.parseLong(stringTokenizer.nextToken());
				while(stringTokenizer.hasMoreElements()){
					i=0;
					bw.write(id+"\t");
					while(i++<MAX_SENTENCE_LENGTH&&stringTokenizer.hasMoreElements()){
						bw.write(stringTokenizer.nextToken()+"\t");
					}
					bw.write("\r\n");
				}
			}
		}
		br.close();
		bw.flush();
		bw.close();
	}
}
