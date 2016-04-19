package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import utils.GetInfo;
import utils.SaveInfo;

public class GetMin {
	//LOFTER客户端 3065
	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_NLPIR2223_GenderPre\\";
	public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//getDifferentId();
		readFile();
		//D:\Project_DataMinning\Data\Sina_res\Sina_NLPIR\Config\manual
		
	}

	private static void readFile() throws IOException {
		File f = new File(PATH+"Config\\Dict_SrcType_vector_inBaiKe.txt");
		BufferedReader r = new BufferedReader(new FileReader(f));
		String line="";
		double min = Double.POSITIVE_INFINITY;
		System.out.println(min);
		System.out.println(Math.pow(10, -6));
		while((line=r.readLine())!=null)
		{
			String[] items = line.split("\t",2)[1].split("\\s+");
			for(int i=0;i<items.length;i++){
				double item = Double.parseDouble(items[i]);
				if(item<min){min = item;}//-0.62824,-0.74673444,-0.72693276,-1.077052
			}
		}
		r.close();
		System.out.println(min);
	}

	
}
