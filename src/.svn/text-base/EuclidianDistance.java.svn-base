
public class EuclidianDistance {

	public double getDistance(ClusterObject obj1, ClusterObject obj2) {
		
		double dDistance = 0;
		double dTemp = 0;
		int nSize = obj1.getSize();
		
		for (int i = 0; i < nSize; i++) {
			double dDiff = obj1.getAttribute(i) - obj2.getAttribute(i);
			dTemp = dTemp + Math.pow(dDiff, 2); 
		}
		
		dDistance = Math.sqrt(dTemp);
		
		return dDistance;
	}
	
}
