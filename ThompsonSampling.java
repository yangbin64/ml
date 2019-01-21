package bandit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ThompsonSampling {

	public void help() {
		System.out.println();
		System.out.println("Usage 1 (Update the model):");
		System.out.println("  java -jar ThompsonSampling.jar Update <DataType> <parameter 1> <parameter 2> <log file> <current model file> <new model file>");
		System.out.println("[Parameters]");
		System.out.println("  <DataType> - 'Binary' or 'Real'");
		System.out.println("  <parameter 1> - The first parameter");
		System.out.println("  <parameter 2> - The second parameter");
		System.out.println("  <log file> - The file name of input file (history data)");
		System.out.println("  <current model file> - The file name of current model file");
		System.out.println("  <new model file> - The file name of new model file");
		System.out.println("e.g., java -jar bandit.jar Update Binary 1 100 log1.csv - model1.csv");
		System.out.println("      java -jar bandit.jar Update Binary 1 100 log2.csv model1.csv model2.csv");
		System.out.println();
		
		System.out.println("Usage 2 (Assign the arms):");
		System.out.println("  java -jar ThompsonSampling.jar Assign <DataType> <parameter 1> <parameter 2> <log file> <current model file> <new model file>");
		System.out.println("[Parameters]");
		System.out.println("  <DataType> - 'Binary' or 'Real'");
		System.out.println("  <parameter 1> - The first parameter");
		System.out.println("  <parameter 2> - The second parameter");
		System.out.println("  <current model file> - The file name of current model file");
		System.out.println("  <user list file> - The file name of user list file");
		System.out.println("  <user arm list file> - The file name of user arm list file");
		System.out.println("e.g., java -jar bandit.jar Assign Binary 1 100 model.csv user_list.csv user_arm_list.csv");
		System.out.println();
		
		System.out.println("Usage 3 (Add new arms):");
		System.out.println("  java -jar ThompsonSampling.jar AddArms <DataType> <current model file> <new model file> <Arm 1> ...");
		System.out.println("[Parameters]");
		System.out.println("  <DataType> - 'Binary' or 'Real'");
		System.out.println("  <current model file> - The file name of current model file");
		System.out.println("  <new model file> - The file name of new model file");
		System.out.println("  <Arm n> - The name list of new arms");
		System.out.println("e.g., java -jar bandit.jar AddArms Binary model1.csv model2.csv Arm1 Arm2");
		System.out.println();
		
		System.out.println("Usage 4 (Remove arms):");
		System.out.println("  java -jar ThompsonSampling.jar RemoveArms <DataType> <current model file> <new model file> <Arm 1> ...");
		System.out.println("[Parameters]");
		System.out.println("  <DataType> - 'Binary' or 'Real'");
		System.out.println("  <current model file> - The file name of current model file");
		System.out.println("  <new model file> - The file name of new model file");
		System.out.println("  <Arm n> - The name list of new arms");
		System.out.println("e.g., java -jar bandit.jar RemoveArms Binary model1.csv model2.csv Arm1 Arm2");
		System.out.println();
	}
	
	public void run(String[] args) {
		if (args.length<5) {
			System.out.println("[Error] Parameter not enough!");
			help();
			return;
		}
		
		String Operation = args[0];
		String DataType = args[1];
		
		if ((!Operation.equalsIgnoreCase("Update")) &&
			(!Operation.equalsIgnoreCase("Assign")) &&
			(!Operation.equalsIgnoreCase("AddArms")) &&
			(!Operation.equalsIgnoreCase("RemoveArms"))) {
			
			System.out.println("[Error] Parameter is invalid!");
			help();
			return;
		}
		
		if (!DataType.equalsIgnoreCase("Binary")&&!DataType.equalsIgnoreCase("Real")) {
			System.out.println("[Error] Parameter is invalid!");
			help();
			return;
		}
		
		if (Operation.equalsIgnoreCase("Update")) {
			update(args);
		} else if (Operation.equalsIgnoreCase("Assign")) {
			assign(args);
		} else if (Operation.equalsIgnoreCase("AddArms")) {
			AddArms(args);
		} else if (Operation.equalsIgnoreCase("RemoveArms")) {
			RemoveArms(args);
		}
	}

	public void update(String[] args) {
		if (args.length<7) {
			System.out.println("[Error] Parameter not enough!");
			help();
			return;
		}
		
		String DataType = args[1];
		double parameter1 = Double.valueOf(args[2]);
		int parameter2 = Integer.valueOf(args[3]);
		String fn_data = args[4];
		String fn_model = args[5];
		String fn_model_new = args[6];
		
		File file = new File(fn_data);
		if (!file.exists()) {
			System.out.println("[Error] File " + fn_data + " not exist!");
			help();
			return;
		}
		
		//BanditContextualBatch bb = new BanditContextualBatch();
		//bb.updateModel(DataType, fn_data, fn_model, fn_model_new, sum_prior, count_prior);
		
		Model model = new Model(DataType, parameter1, parameter2);
		
		File file_model = new File(fn_model);
		if (file_model.exists()) {
			model.importModel(fn_model);
		}
		
		try {
			FileReader fr = new FileReader(fn_data);
			BufferedReader buf = new BufferedReader(fr);
			String s;
			
			while ((s=buf.readLine())!=null) {
				String[] ss = s.split(",");
				
				String contextid = ss[0];
				String armName = ss[1];
				double reward = Double.valueOf(ss[2]);
				
				model.update(contextid, armName, reward);
			}
			
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		model.exportModel(fn_model_new);
	}
	
	public void assign(String[] args) {
		if (args.length<7) {
			System.out.println("[Error] Parameter not enough!");
			help();
			return;
		}
		
		String DataType = args[1];
		double parameter1 = Double.valueOf(args[2]);
		int parameter2 = Integer.valueOf(args[3]);
		String fn_model = args[4];
		String filenameui = args[5];
		String filenameuo = args[6];
		
		File file = new File(fn_model);
		if (!file.exists()) {
			System.out.println("[Error] File " + fn_model + " not exist!");
			help();
			return;
		}
		
		//BanditContextualBatch bb = new BanditContextualBatch();
		//bb.assign(DataType, fn_model, sum_prior, count_prior, filenameui, filenameuo);
		
		Model model = new Model(DataType, parameter1, parameter2);
		
		File file_model = new File(fn_model);
		if (file_model.exists()) {
			model.importModel(fn_model);
		}
		
		try {
			FileReader fr = new FileReader(filenameui);
			BufferedReader br = new BufferedReader(fr);
			String s;
			
			FileWriter fw = new FileWriter(filenameuo);
			
			while ((s=br.readLine())!=null) {
				String[] ss = s.split(",");
				String userID = ss[0];
				String contextID = ss[1];
				
				String ArmName = model.select(contextID);
				
				fw.write(userID + "," + contextID + "," + ArmName + "\n");
			}
			
			fw.close();
			fr.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void AddArms(String[] args) {
		if (args.length<5) {
			System.out.println("[Error] Parameter not enough!");
			help();
			return;
		}
		
		String DataType = args[1];
		String fn_model = args[2];
		String fn_model_new = args[3];
		String[] newArmNames = new String[args.length-6];
		for (int i=4; i<args.length; i++) {
			newArmNames[i-6] = args[i];
		}
		
		File file = new File(fn_model);
		if (!file.exists()) {
			System.out.println("[Error] File " + fn_model + " not exist!");
			help();
			return;
		}
		
		//BanditContextualBatch bb = new BanditContextualBatch();
		//bb.AddArms(DataType, fn_model, fn_model_new, newArmNames, sum_prior, count_prior);
		
		Model model = new Model(DataType, 1, 2);
		
		File file_model = new File(fn_model);
		if (file_model.exists()) {
			model.importModel(fn_model);
		}
		
		model.AddArms(newArmNames, 1, 2);
		
		model.exportModel(fn_model_new);
	}
	
	public void RemoveArms(String[] args) {
		if (args.length<5) {
			System.out.println("[Error] Parameter not enough!");
			help();
			return;
		}
		
		String DataType = args[1];
		String fn_model = args[2];
		String fn_model_new = args[3];
		String[] armNames = new String[args.length-4];
		for (int i=4; i<args.length; i++) {
			armNames[i-4] = args[i];
		}
		
		File file = new File(fn_model);
		if (!file.exists()) {
			System.out.println("[Error] File " + fn_model + " not exist!");
			help();
			return;
		}
		
		//BanditContextualBatch bb = new BanditContextualBatch();
		//bb.removeArms(DataType, fn_model, fn_model_new, armNames);

		Model model = new Model(DataType, 1, 2);
		
		File file_model = new File(fn_model);
		if (file_model.exists()) {
			model.importModel(fn_model);
		}
		
		model.removeArms(armNames);
		
		model.exportModel(fn_model_new);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThompsonSampling ts = new ThompsonSampling();
		ts.run(args);
	}
}
