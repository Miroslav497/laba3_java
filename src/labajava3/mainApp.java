package labajava3;


public class mainApp {
	public static void main(String[] args) {
		Double[] coefficients = new Double[5];
		int counter = 0;
		for(String arg: args) {
			coefficients[counter] = Double. parseDouble(arg);
			counter++;
		}
		/*for(Double d: coefficients) {
			System.out.println(d);
		}*/
		
		MainFrame table = new MainFrame(coefficients);
        table.setVisible(true);
    }
	
}
