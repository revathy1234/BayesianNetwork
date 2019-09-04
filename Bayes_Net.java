
public class Bayes_Net 
{

	public static double prob (Node Burglary, Node Earthquake, Node Alarm, Node Johncalls, Node Marycalls) 
	{
		double denom = 0;
		for (int b = Burglary.st_given; b <= Burglary.end_given; b++)
		for (int e = Earthquake.st_given; e <= Earthquake.end_given; e++)
		for (int a = Alarm.st_given; a <= Alarm.end_given; a++)
		for (int j = Johncalls.st_given; j <= Johncalls.end_given; j++)
		for (int m = Marycalls.st_given; m <= Marycalls.end_given; m++)
		denom += (Node.probCalc(Burglary, b, -1, -1) * Node.probCalc(Earthquake, e, -1, -1) * Node.probCalc(Alarm, a, b, e) * Node.probCalc(Johncalls, j, a, -1) * Node.probCalc(Marycalls, m, a, -1));
		double numer = 0;
		for (int b = Burglary.q_beg; b <= Burglary.q_end; b++)
		for (int e = Earthquake.q_beg; e <= Earthquake.q_end; e++)
		for (int a = Alarm.q_beg; a <= Alarm.q_end; a++)
		for (int j = Johncalls.q_beg; j <= Johncalls.q_end; j++)
		for (int m = Marycalls.q_beg; m <= Marycalls.q_end; m++)
		numer += (Node.probCalc(Burglary, b, -1, -1) * Node.probCalc(Earthquake, e, -1, -1) * Node.probCalc(Alarm, a, b, e) * Node.probCalc(Johncalls, j, a, -1) * Node.probCalc(Marycalls, m, a, -1));
		
		return (numer/denom);
	}
	
	public void setTrue(Node temp_node, boolean bool) 
	{
		temp_node.q_end = 0;
		if (bool == true)
			temp_node.end_given = 0;
	}
	
	public void setFalse(Node temp_node, boolean bool) 
	{
		temp_node.q_beg = 1;
		if (bool == true)
			temp_node.st_given = 1;
	}
	
	public void setArgVal(String[] args) 
	{
		boolean bool = false;		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("given"))
			{
				bool = true;
			}
			else if (args[i].equalsIgnoreCase("Bf"))
			{
				setFalse(this.Burglary, bool);
			}
			else if (args[i].equalsIgnoreCase("Bt"))
			{
				setTrue(this.Burglary, bool);
			}
			else if (args[i].equalsIgnoreCase("Ef"))
			{
				setFalse(this.Earthquake, bool);
			}
			else if (args[i].equalsIgnoreCase("Et"))
			{
				setTrue(this.Earthquake, bool);
			}
			else if (args[i].equalsIgnoreCase("Af"))
			{
				setFalse(this.Alarm, bool);
			}
			else if (args[i].equalsIgnoreCase("At"))
			{
				setTrue(this.Alarm, bool);
			}
			else if (args[i].equalsIgnoreCase("Jf"))
			{
				setFalse(this.Johncalls, bool);
			}
			else if (args[i].equalsIgnoreCase("Jt"))
			{
				setTrue(this.Johncalls, bool);
			}
			else if (args[i].equalsIgnoreCase("Mf"))
			{
				setFalse(this.Marycalls, bool);
			}
			else if (args[i].equalsIgnoreCase("Mt"))
			{
				setTrue(this.Marycalls, bool);
			}
			
			else {
				System.out.println("Please enter a valid input");
				System.exit(0);
			}
		}
	}

	Node Burglary, Earthquake, Alarm, Johncalls, Marycalls;
	
	public Bayesian_Network() 
	{
		Burglary = new Node("Burglary", Node.Position.INIT);
		Earthquake = new Node("Earthquake", Node.Position.INIT);
		Alarm = new Node("Alarm", Node.Position.INTER);
		Johncalls = new Node("Johncalls", Node.Position.END);
		Marycalls = new Node("Marycalls", Node.Position.END);
	}

	static class Node 
{
	String id;
	Position ending;
	int q_beg, q_end, st_given, end_given;
	
	static final double PB = 0.001;
	static final double PE = 0.002;
	static final double PJ_AT = 0.90;
	static final double PJ_AF = 0.05;
	static final double PM_AT = 0.70;
	static final double PM_AF = 0.01;
	static final double PA_BT_ET = 0.95;
	static final double PA_BT_EF = 0.94;
	static final double PA_BF_ET = 0.29;
	static final double PA_BF_EF = 0.001;

	public enum Position 
	{
		INIT,
		INTER, 
		END, 
		NONE
	}
	
	public Node(String str, Position pos) 
	{
		id = str;
		ending = pos;
		q_beg = 0;
		q_end = 1;
		st_given = 0; 
		end_given = 1; 
	}
	
	public static double probCalc(Node temp_node, int flag, int prob1, int prob2) 
	{
		double probab= 0;
		if (temp_node.ending == Position.INIT)
		{
			if (temp_node.id == "Burglary")
			{ 
				if (flag == 1) probab = PB;
				else probab = 1 - PB;
			}  
			else
			{ 
				if (flag == 1) probab = PE;
				else probab = 1 - PE;
			}
		} 
		else if (temp_node.ending == Position.INTER) 
		{ 
			if ((prob1 == 1) && (prob2 == 1))
			{ 
				if (flag == 1)	probab = PA_BT_ET;
				else probab = 1 - PA_BT_ET;
			}
			else if ((prob1 == 1) && (prob2 == 0)) 
			{
				if (flag == 1) probab = PA_BT_EF;
				else probab = 1 - PA_BT_EF;
			} 
			else if ((prob1 == 0) && (prob2 == 1)) 
			{ 
				if (flag == 1) probab = PA_BF_ET;
				else probab = 1 - PA_BF_ET;
			}
			else if ((prob1 == 0) && (prob2 == 0)) 
			{ 
				if (flag == 1)	probab = PA_BF_EF;
				else probab = 1 - PA_BF_EF;
			} 
		} 
		else if (temp_node.ending == Position.END)
		{ 
			if (temp_node.id == "Johncalls")
			{
				if (prob1 == 1)
				{ 
					if (flag == 1) probab = PJ_AT;
					else probab = 1 - PJ_AT;
				} 
				else
				{ 
					if (flag == 1) probab = PJ_AF;
					else probab = 1 - PJ_AF;
				} 
			}
			else if (temp_node.id == "Marycalls")
			{
				if (prob1 == 1) 
				{ 
					if (flag == 1) probab = PM_AT;
					else probab = 1 - PM_AT;
				} 
				else 
				{ 
					if (flag == 1) probab = PM_AF;
					else probab = 1 - PM_AF;
				} 
			}
		}
		return probab;
	}
}
	
	public static void main(String[] args) 
	{
		if (args.length == 0) 
		{
			System.out.println("No input specified");
			System.exit(0);
		}
		if (args.length > 6) 
		{
			System.out.println("Please make sure the no. of args is between 1 and 6");
			System.exit(0);
		}
		Bayesian_Network bay_net = new Bayesian_Network();
		bay_net.setArgVal(args);
		System.out.println(" \n\n Probability : " + prob(bay_net.Burglary,bay_net.Earthquake, bay_net.Alarm, bay_net.Johncalls, bay_net.Marycalls));
			
	}
}


