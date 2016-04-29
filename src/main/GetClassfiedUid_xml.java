package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class GetClassfiedUid_xml{
	/**
	 * @param args
	 * @throws IOException
	 *
	 * @throws JAXBException 
	 */
	public static void main(String args[]) throws IOException{
		File srcf = new File("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR微博博主语料库.xml");
		BufferedReader r = new BufferedReader(new FileReader(srcf));
		File resf = new File("D:\\Project_DataMinning\\DataSource\\Sina_NLPIR_UserID.txt");
		BufferedWriter w = new BufferedWriter(new FileWriter(resf));
		String line = "";
		String regex = "<[/]?[a-zA-Z]{2,10}>";
		int num = 0;
		while((line = r.readLine())!=null){
			if(line.contains("<id>")){
				String id = line.split(regex)[1];
				for(int i=0;i<=10;i++){line = r.readLine();}
				//System.out.println(line);
				String brithday = null;
				if(!line.contains("<brithday></brithday>")){
					brithday = line.split(regex)[1]; 
				}
				w.write(id+"\t"+brithday+"\r\n");
				num++;
			}
		}
		System.out.println(num);
		r.close();
		w.flush();
		w.close();
	}

	
	
}
