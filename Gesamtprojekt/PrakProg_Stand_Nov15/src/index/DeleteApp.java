package index;

public class DeleteApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String S = "internOrdner";
		DeleteDir d = new DeleteDir(S);
		d.delete();

		System.out.println("Success");
	}

}
