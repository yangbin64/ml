package bandit;

import org.apache.commons.math3.distribution.*;

public class Arm {
	public String DataType;
	public String name;
	
	public double parameter1;
	public int parameter2;
	
	public double sum;
	public int count;
	public double sum2;
	
	public Arm(String DataType, String name, double parameter1, int parameter2) {
		this(DataType, name, 0, 0, 0);
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
	}
	
	public Arm(String DataType, String name, double sum, int count, double sum2) {
		this.DataType=DataType;
		this.name=name;
		this.sum=sum;
		this.count=count;
		this.sum2=sum2;
	}
	
	public AbstractRealDistribution getBinaryDistribution() {
		double count_p=sum;
		double count_n=count-sum;
		
		double count_p_prior = parameter1;
		double count_n_prior = parameter2 - parameter1;
		
		double alpha = count_p + count_p_prior;
		double beta = count_n + count_n_prior;
		
		return new BetaDistribution(alpha, beta);
	}
	
	public AbstractRealDistribution getRealDistribution() {
		double mean_prior = parameter1;
		double var_prior = (double)parameter2;
		double tau_prior = 1.0/(var_prior*var_prior);
		
		double tau = 0;
		if (count>1) {
			tau = (count-1)/(sum2-sum*sum/count);
		}
		
		double average = (tau_prior*mean_prior+tau*sum)/(tau_prior+tau*count);
		double variance = Math.sqrt(1.0/(tau_prior+tau*count));
		
		return new NormalDistribution(average, variance);
	}
	
	public void update(double reward) {
		sum += reward;
		count++;
		sum2 += (reward*reward);
	}
	
	public double sample() {
		AbstractRealDistribution distribution = null;
		if (DataType.equalsIgnoreCase("Binary")) {
			distribution = getBinaryDistribution();
		} else if (DataType.equalsIgnoreCase("Real")) {
			distribution = getRealDistribution();
		}
		double draw = distribution.sample();
		return draw;
	}
	/*
	public void output() {
		System.out.println(name + "\t" + getAvg() + "\t" + sum + "\t" + count);
	}
	
	public double getSum() {
		return sum;
	}
	
	public int getCount() {
		return count;
	}
	
	public double getAvg() {
		double avg = sum/count;
		avg = ((double)Math.round(avg*1000))/1000;
		return avg;
	}*/
}
