package main;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import utils.GetInfo;

public class StatCommonFri {

	public static String PATH = "D:\\Project_DataMinning\\Data\\Sina_res\\Sina_AgePre_JSON_1000\\";
	public static void main(String args[]) throws IOException{
		getCommonFriNumber(PATH+"mute\\UidInfo_friends0.txt",PATH+"mute_fake\\UidInfo_friends0.txt");
		getCommonFriNumber(PATH+"mute\\UidInfo_follows0.txt",PATH+"mute_fake\\UidInfo_follows0.txt");
		getCommonNeiNumber(PATH+"mute\\UidInfo_friends0.txt",PATH+"mute\\UidInfo_follows0.txt",PATH+"mute_fake\\UidInfo_friends0.txt",PATH+"mute_fake\\UidInfo_follows0.txt");
		
	}
	
	
	private static int getCommonNeiNumber(String file1, String file2, String file3, String file4) throws IOException {
		// TODO Auto-generated method stub
		Set<String> uid_set1 = new HashSet<String>();
		GetInfo.getSetFromList(file1, uid_set1, "uids");
		GetInfo.getSetFromList(file2, uid_set1, "uids");
		Set<String> uid_set2 = new HashSet<String>();
		GetInfo.getSetFromList(file3, uid_set2, "uids");
		GetInfo.getSetFromList(file4, uid_set2, "uids");
		int number = 0;
		for(String id : uid_set1){
			if(uid_set2.contains(id)){
				number++;
			}
		}
		System.out.println(number+"\t"+(uid_set2.size()+uid_set1.size()-number));
		return number;
	}


	private static int getCommonFriNumber(String file1, String file2) throws IOException {
		Set<String> uid_set1 = new HashSet<String>();
		GetInfo.getSetFromList(file1, uid_set1, "uids");
		Set<String> uid_set2 = new HashSet<String>();
		GetInfo.getSetFromList(file2, uid_set2, "uids");
		int number = 0;
		for(String id : uid_set1){
			if(uid_set2.contains(id)){
				number++;
			}
		}
		System.out.println(number+"\t"+(uid_set2.size()+uid_set1.size()-number));
		return number;
	}
}
