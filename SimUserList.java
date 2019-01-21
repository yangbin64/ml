package bandit;

import java.util.*;
import java.io.*;

public class SimUserList {

	public void makeUserList(String filenamew) {
		Random rnd = new Random();
		
		try {
			FileWriter fw = new FileWriter(filenamew);
			
			for (int i=0; i<20000; i++) {
				String userID = "U" + String.valueOf(rnd.nextInt(10000)+10000);
				String contextID = "C" + String.valueOf(rnd.nextInt(2)+1);
				
				fw.write(userID + "," + contextID + "\n");
			}
			
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		SimUserList s = new SimUserList();
		s.makeUserList("C:\\Bandit\\Simulation\\userlist.csv");
	}

}
