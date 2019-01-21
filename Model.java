package bandit;

import java.util.*;
import java.io.*;

public class Model {
	String DataType = "";
	//double parameter1;
	//int parameter2;
	
	// Context id -> Arm name -> Arm
	public HashMap<String, HashMap<String, Arm>> armList = new HashMap<String, HashMap<String, Arm>>();
	
	public Model(String DataType, double parameter1, int parameter2) {
		this.DataType = DataType;
		//this.parameter1 = parameter1;
		//this.parameter2 = parameter2;
	}
	/*
	public void constructArm(String contextid, String armName) {
		if (!armList.containsKey(contextid)) {
			armList.put(contextid, new HashMap<String, Arm>());
		}
		
		if (!armList.get(contextid).containsKey(armName)) {
			if (DataType.equalsIgnoreCase("Binary")) {
				armList.get(contextid).put(armName, new Arm("Binary", armName, parameter1, parameter2));
			} else {
				armList.get(contextid).put(armName, new Arm("Real", armName, parameter1, parameter2));
			}
		}
	}
	*/
	public void update(String contextid, String armName, double reward) {
		//constructArm(contextid, armName);
		if (!armList.containsKey(contextid)) {
			return;
		}
		
		if (!armList.get(contextid).containsKey(armName)) {
			return;
		}
		
		armList.get(contextid).get(armName).update(reward);
	}
	
	public String select(String contextid) {
		String selectedArm = null;
		double bestReward = 0;
		
		if (!armList.containsKey(contextid))
			return null;
		
		for (Iterator<Map.Entry<String, Arm>> it=armList.get(contextid).entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, Arm> entry = it.next();
			String arm = entry.getKey();
			Arm g = entry.getValue();
			double reward = g.sample();
			if (selectedArm==null || reward>bestReward) {
				selectedArm = arm;
				bestReward = reward;
			}
		}
		
		return selectedArm;
	}
	
	public void AddArms(String[] newArmNames, double parameter1, int parameter2) {
		for (Iterator<Map.Entry<String, HashMap<String, Arm>>> it=armList.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, HashMap<String, Arm>> entry = it.next();
			//String contextID = entry.getKey();
			HashMap<String, Arm> arms = entry.getValue();
			
			for (String ArmName : newArmNames) {
				arms.put(ArmName, new Arm(DataType, ArmName, parameter1, parameter2));
			}
		}
	}
	
	public void importModel(String filenameModel) {
		armList = new HashMap<String, HashMap<String, Arm>>();
		
		try {
			FileReader fr = new FileReader(filenameModel);
			BufferedReader br = new BufferedReader(fr);
			String s;
			
			while ((s=br.readLine())!=null) {
				String[] ss = s.split(",");
				String contextID = ss[0];
				String ArmName = ss[1];
				double sum = Double.valueOf(ss[2]);
				int count = Integer.valueOf(ss[3]);
				double sum2 = Double.valueOf(ss[4]);
				double parameter1 = Double.valueOf(ss[5]);
				int parameter2 = Integer.valueOf(ss[6]);
				
				if (!armList.containsKey(contextID)) {
					armList.put(contextID, new HashMap<String, Arm>());
				}
				
				if (!armList.get(contextID).containsKey(ArmName)) {
					if (DataType.equalsIgnoreCase("Binary")) {
						armList.get(contextID).put(ArmName, new Arm("Binary", ArmName, parameter1, parameter2));
					} else if (DataType.equalsIgnoreCase("Real")) {
						armList.get(contextID).put(ArmName, new Arm("Real", ArmName, parameter1, parameter2));
					}
				}
				
				armList.get(contextID).get(ArmName).sum = sum;
				armList.get(contextID).get(ArmName).count = count;
				armList.get(contextID).get(ArmName).sum2 = sum2;
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		/*
		if (parameter2>0) {
			for (Iterator<Map.Entry<String, HashMap<String, Arm>>> it=armList.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, HashMap<String, Arm>> entry = it.next();
				HashMap<String, Arm> arms = entry.getValue();
				
				for (Iterator<Map.Entry<String, Arm>> it2= arms.entrySet().iterator(); it2.hasNext(); ) {
					Map.Entry<String, Arm> entry2 = it2.next();
					Arm arm = entry2.getValue();
					arm.parameter1 = parameter1;
					arm.parameter2 = parameter2;
				}
			}
		}*/
	}
	
	public void exportModel(String filenameModel) {
		try {
			FileWriter fw = new FileWriter(filenameModel);
			
			for (Iterator<Map.Entry<String, HashMap<String, Arm>>> it=armList.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, HashMap<String, Arm>> entry = it.next();
				String contextID = entry.getKey();
				HashMap<String, Arm> arms = entry.getValue();
				
				for (Iterator<Map.Entry<String, Arm>> it2=arms.entrySet().iterator(); it2.hasNext(); ) {
					Map.Entry<String, Arm> entry2 = it2.next();
					String ArmName = entry2.getKey();
					Arm arm = entry2.getValue();
					
					double sum = arm.sum;
					int count = arm.count;
					double sum2 = arm.sum2;
					double parameter1 = arm.parameter1;
					int parameter2 = arm.parameter2;
					
					fw.write(contextID
							+ "," + ArmName
							+ "," + sum
							+ "," + count
							+ "," + sum2
							+ "," + parameter1
							+ "," + parameter2
							+ "\n");
				}
			}
			
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void removeArms(String[] armNames) {
		for (Iterator<Map.Entry<String, HashMap<String, Arm>>> it=armList.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, HashMap<String, Arm>> entry = it.next();
			HashMap<String, Arm> arms = entry.getValue();
			
			for (int i=0; i<armNames.length; i++) {
				if (arms.containsKey(armNames[i])) {
					arms.remove(armNames[i]);
				} else {
					System.out.println("Arm " + armNames[i] + " does not exist!");
				}
			}
		}
	}
}
