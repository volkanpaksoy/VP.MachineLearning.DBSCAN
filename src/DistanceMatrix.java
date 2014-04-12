
public class DistanceMatrix {
	
	private double[][] m_dArrMatrix; 

	private int m_nSize = 0;
	
	public DistanceMatrix(int nSize) {
		
		m_nSize = nSize;
		m_dArrMatrix = new double[nSize][nSize];
	}
	
	
	public void calculateDistanceMatrix(InputData data) {
		
		EuclidianDistance distanceMetric = new EuclidianDistance(); 
		double dDistance = 0;
		
		for (int i = 0; i < m_nSize; i++) {
		
			for (int j = 0; j < m_nSize; j++) {
				dDistance = distanceMetric.getDistance(data.getPoint(i), data.getPoint(j));
				m_dArrMatrix[i][j] = dDistance;
			}
		}
	}
	
	
	public double getDistance(ClusterObject obj1, ClusterObject obj2) {
		return m_dArrMatrix[obj1.getObjectId()][obj2.getObjectId()];
	}
	
	public double getDistance(int id1, int id2) {
		return m_dArrMatrix[id1][id2];
	}
	
}