package artificialintelligence;

public class Janken {
	public static void main(String[] args){
		Janken janken = new Janken();

		double[] a = new double[]{
			0.5, 0.0
		};

//		Janken.hand(a[0],a[1]);
		System.out.println(Janken.hand(a[0], a[1]));
	}

	public static String hand(double a1, double a2){

		String[][] defHand = new String[2][2];
		defHand[0][1] = "g";
		defHand[1][0] = "c";
		defHand[1][1] = "p";

		int v1 = (int)(a1+0.5);
		int v2 = (int)(a2+0.5);
		return defHand[v1][v2];
	}

	public static double[] trans(String a) throws Exception{
		if("g".equals(a)){
			return new double[]{0, 1};
		}else if("c".equals(a)){
			return new double[]{1, 0};
		}else if("p".equals(a)){
			return new double[]{1, 1};
		}else{
			throw new Exception();
		}
	}
}
