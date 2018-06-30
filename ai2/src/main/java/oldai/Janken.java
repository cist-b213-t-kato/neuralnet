package oldai;

public class Janken {
	public static void main(String[] args){
		Janken janken = new Janken();

		double[] a = new double[]{
			0.5, 0.0
		};

//		Janken.hand(a[0],a[1]);
		System.out.println(Janken.hand(a[0], a[1]));
	}

	public static int hand(double a1, double a2){

		int[][] defHand = new int[2][2];
		defHand[0][0] = -1; //未定値
		defHand[0][1] = 0; //グー
		defHand[1][0] = 1; //チョキ
		defHand[1][1] = 2; //パー

		int v1 = (int)(a1+0.5);
		int v2 = (int)(a2+0.5);
		return defHand[v1][v2];
	}

	public static int compare(int a, int b){
		if(a == b){
			return 0;
		}else if((a+1)%3==b){
			return 1;
		}else{
			return -1;
		}
	}

	public static String strHand(int a){
		switch(a){
		case 0:
			return "グー";
		case 1:
			return "チョキ";
		case 2:
			return "パー";
		default:
			return "";
		}
	}

	public static double[] toBinary(String a) throws Exception{
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
