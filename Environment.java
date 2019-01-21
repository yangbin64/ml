package bandit;

import java.util.*;
import java.io.*;
import org.apache.commons.math3.distribution.*;

public class Environment {
	
	class Arm_env {
		double mean;
		double var;
		
		NormalDistribution nd = new NormalDistribution();
		
		double draw() {
			return nd.sample()*var+mean;
		}
	}
	
	HashMap<String, HashMap<String, Arm_env>> arm_list = new HashMap<String, HashMap<String, Arm_env>>();
	
	class Arm_log {
		int count;
		double sum;
	}
	
	HashMap<String, HashMap<String, Arm_log>> arm_log_list = new HashMap<String, HashMap<String, Arm_log>>();
	
	public HashMap<String, HashMap<String, Double>> readRatios(String filenamer) {
		HashMap<String, HashMap<String, Double>> ratios = new HashMap<String, HashMap<String, Double>>();
		
		try {
			FileReader fr = new FileReader(filenamer);
			BufferedReader br = new BufferedReader(fr);
			String s;
			
			while ((s=br.readLine())!=null) {
				String[] ss = s.split(",");
				String contextID = ss[0];
				String ArmName = ss[1];
				double ratio = Double.valueOf(ss[2]);
				
				if (!ratios.containsKey(contextID)) {
					ratios.put(contextID, new HashMap<String, Double>());
				}
				
				ratios.get(contextID).put(ArmName, ratio);
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return ratios;
	}
	
	public void init(String contextID, String ArmName, double mean, double var) {
		// Initialize KPIs
		if (!arm_list.containsKey(contextID)) {
			arm_list.put(contextID, new HashMap<String, Arm_env>());
			arm_log_list.put(contextID, new HashMap<String, Arm_log>());
		}
		
		if (arm_list.get(contextID).containsKey(ArmName)) {
			System.out.println("KPI exists, overwrite it!");
		}
		arm_list.get(contextID).put(ArmName, new Arm_env());
		arm_log_list.get(contextID).put(ArmName, new Arm_log());
		
		arm_list.get(contextID).get(ArmName).mean = mean;
		arm_list.get(contextID).get(ArmName).var = var;
	}
	
	public void init() {
		/*
		init("C1", "A1", 50, 600);
		init("C1", "A2", 30, 400);
		init("C1", "A3", 20, 500);
		init("C2", "A1", 10, 700);
		init("C2", "A2", -10, 500);
		init("C2", "A3", 15, 400);
		*/
		init("C1", "A1", 10, 10);
		init("C1", "A2", 20, 10);
		init("C1", "A3", 30, 10);
		init("C1", "A4", 40, 10);
		init("C1", "A5", 50, 10);
		init("C1", "A6", 60, 10);
		init("C2", "A1", 10, 50);
		init("C2", "A2", 20, 50);
		init("C2", "A3", 30, 50);
		init("C2", "A4", 40, 50);
		init("C2", "A5", 50, 50);
		init("C2", "A6", 60, 50);
	}
	
	public void generate(String filenameuo, String filenamew) {
		try {
			FileReader fr = new FileReader(filenameuo);
			BufferedReader br = new BufferedReader(fr);
			String s;
			
			FileWriter fw = new FileWriter(filenamew);
			
			while ((s=br.readLine())!=null) {
				String[] ss = s.split(",");
				
				//String userID = ss[0];
				String contextID = ss[1];
				String ArmName = ss[2];
				
				Arm_env arm = arm_list.get(contextID).get(ArmName);
				double reward = arm.draw();
				
				fw.write(contextID + "," + ArmName + "," + reward + "\n");
				
				Arm_log arm_log = arm_log_list.get(contextID).get(ArmName);
				arm_log.count++;
				arm_log.sum += reward;
			}
			
			fw.close();
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		for (Iterator<Map.Entry<String, HashMap<String, Arm_log>>> it=arm_log_list.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, HashMap<String, Arm_log>> entry = it.next();
			String contextID = entry.getKey();
			HashMap<String, Arm_log> arm_logs = entry.getValue();
			
			System.out.print(contextID);
			
			for (Iterator<Map.Entry<String, Arm_log>> it2=arm_logs.entrySet().iterator(); it2.hasNext(); ) {
				Map.Entry<String, Arm_log> entry2 = it2.next();
				//String ArmName = entry2.getKey();
				Arm_log log = entry2.getValue();
				
				System.out.print("\t" + log.count);
			}
			
			System.out.println();
		}
	}
	
	public void run(String filenameuo, String filenameKPI) {
		init();
		generate(filenameuo, filenameKPI);
	}
	
	public static void main(String[] args) {
		//String filenameuo = "C:\\Bandit\\Simulation\\user_coupon_list1.csv";
		//String filenamew = "C:\\Bandit\\Simulation\\feedback1.csv";
		
		String filenameuo = args[0];
		String filenamew = args[1];
		
		Environment e = new Environment();
		e.run(filenameuo, filenamew);
	}

}
