package jo.pichler;

public class Servus {

	public static void main(String[] args) {
		System.out.println("Servus!");
	}
	
	public boolean f(int a) {
		if (a < 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void g(Map<String, String> m){
		for(String e : m.keySet()){
			String v = m.get(e);
			System.out.println(v);
		}
	}
	
	
}
