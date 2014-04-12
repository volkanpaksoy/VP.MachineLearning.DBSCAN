import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DBSCANApp {

	public static void main(String args[]) {
		
		ConsoleLogListener consoleListener = new ConsoleLogListener(); 
		Logger.attachListener(consoleListener);
		
		Logger.logMessage("---------------------------------");
		Logger.logMessage("DBSCAN Algorithm Implementation");
		Logger.logMessage("---------------------------------");
		
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String strParam = null;
		DBSCAN dbscan = new DBSCAN();
		try {
			
			System.out.print("Enter the path of database: ");
			strParam = in.readLine();
			dbscan.setConnectionString(strParam);
			
			System.out.print("Enter value for epsilon: ");
			strParam = in.readLine();
			dbscan.setEpsilon(Double.parseDouble(strParam));
			
			System.out.print("Enter value for minPts: ");
			strParam = in.readLine();
			dbscan.setMinPts(Integer.parseInt(strParam));
			
		} catch (Exception ex) {
			Logger.logMessage("Error while reading parameters : " + ex.getMessage());
			return;
		} 
		
		dbscan.run();
		
	}
	
	
}
