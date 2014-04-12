import java.util.ArrayList;
import java.util.Random;


public class DBSCAN {
	
	private String m_strConnectionString;
	public void setConnectionString(String m_strConnectionString) {
		this.m_strConnectionString = m_strConnectionString;
	}
	public String getConnectionString() {
		return m_strConnectionString;
	}
	
	private double m_dEpsilon;
	public void setEpsilon(double dEpsilon) {
		this.m_dEpsilon = dEpsilon;
	}
	public double getEpsilon() {
		return m_dEpsilon;
	}	
	
	private int m_nMinPts;
	public void setMinPts(int nMinPts) {
		this.m_nMinPts = nMinPts;
	}
	public double getMinPts() {
		return m_nMinPts;
	}	
	
	private InputData input;
	private DistanceMatrix distMatrix;
	
	private ArrayList<Boolean> m_lstVisitedPoints; 
	private int m_nVisitedPointCount = 0;
	
	
	public DBSCAN() {
		
		m_lstVisitedPoints = new ArrayList<Boolean>();
		
	}
	
	public void initializeVisitedList() {
		
		m_lstVisitedPoints.clear(); 
		for (int i = 0; i < input.getSize(); i++) {
			m_lstVisitedPoints.add(false);
		}
	}
	
	public void run() {
		
		Logger.logMessage("DBSCAN started.");
		
		Database db = new TextFileDatabase();
		input = db.loadData(getConnectionString());
		
		initializeVisitedList();
		
		distMatrix = new DistanceMatrix(input.getSize());
		distMatrix.calculateDistanceMatrix(input);
		
		printDistanceMatrix();
		
		// Keep running until all points are visited at least once.
		int nClusterCount = 0;
		while (m_nVisitedPointCount < input.getSize()) {
			
			ClusterObject objStart = findUnvisitedPoint();
			Logger.logMessage(String.format("Started with object: %s", objStart.toString()));
			
			markVisited(objStart);
			
			ArrayList<ClusterObject> lstNeighbors = getNeighbors(objStart);
			if (lstNeighbors.size() < getMinPts()) {
				objStart.setClusterId(-1); // Cluster ID -1: Noise. It doesn't belong to any cluster.
				Logger.logMessage(String.format("Noise point. Cluster ID will be -1"));
				continue;
				
			} else {
				expandCluster(objStart, nClusterCount, lstNeighbors);
				nClusterCount++;
			}
			
		}
		
		Logger.logMessage("DBSCAN completed.");
		Logger.logMessage("-----------------------");
		for (ClusterObject obj : input) {
			Logger.logMessage(String.format("Object [%d] is in cluster [%d]", obj.getObjectId(), obj.getClusterId()));
		}
		Logger.logMessage("-----------------------");
		
	}
	
	private void expandCluster(ClusterObject objStart, int nClusterId, ArrayList<ClusterObject> lstNeighbors) {
		
		objStart.setClusterId(nClusterId);
		Logger.logMessage(String.format("It is a core point. Start cluster : %d", nClusterId));
		
		Logger.logMessage(String.format("Examining neighbors. Neighbor count: %d", lstNeighbors.size()));
		for (ClusterObject neighbor : lstNeighbors) {
			
			Logger.logMessage(String.format("Current neighbor: %s", neighbor.toString()));
			
			if (!isVisited(neighbor)) {
				markVisited(neighbor);
				
				ArrayList<ClusterObject> lstNeighbors2 = getNeighbors(neighbor);
				Logger.logMessage(String.format("Neighbors of the current neighbor count: %d", lstNeighbors2.size()));
				
				if (lstNeighbors.size() >= getMinPts()) {
					
					Logger.logMessage(String.format("Neighbor is core"));
					
					for (ClusterObject neighbor2 : lstNeighbors2) {
						neighbor2.setClusterId(nClusterId);
						Logger.logMessage(String.format("Expand cluster %d with object %s", nClusterId, neighbor2.toString()));
					}
				}
			} else {
				Logger.logMessage(String.format("Neighbor is already visited."));
			}
			
			neighbor.setClusterId(nClusterId);
			Logger.logMessage(String.format("Set neighbor's (%s) cluster to %d too", neighbor, nClusterId));
		}
		
	}
	
	// Randomly selects an object from the database
	private ClusterObject findUnvisitedPoint() {
		
		ClusterObject objUnvisitedPoint = null;
		ClusterObject objTemp = null;
		
		Random rand = new Random();
		
		while (objUnvisitedPoint == null) {
		
			int nObjectId = Math.abs(rand.nextInt()) % input.getSize();
			
			objTemp = input.getPoint(nObjectId);
			if (!isVisited(objTemp))
				objUnvisitedPoint = objTemp;
		}
		
		return objUnvisitedPoint;
	}
	
	private void markVisited(ClusterObject obj) {
	
		m_lstVisitedPoints.set(obj.getObjectId(), true);
		m_nVisitedPointCount++;
	}
	
	private boolean isVisited(ClusterObject obj) {
		return m_lstVisitedPoints.get(obj.getObjectId());
	}
	
	
	private ArrayList<ClusterObject> getNeighbors(ClusterObject objStartPoint) {
		
		ArrayList<ClusterObject> lstNeighbors = new ArrayList<ClusterObject>();
		
		for (ClusterObject objTemp : input) {
			
			double dDistance = distMatrix.getDistance(objStartPoint, objTemp);
			if (objTemp.getObjectId() != objStartPoint.getObjectId() && dDistance <= getEpsilon()) {
				lstNeighbors.add(objTemp);
			}
		}
		
		return lstNeighbors;	
		
	}

	private void printDistanceMatrix() {
		Logger.logMessage("-------- Distance Matrix ----------");
		
		for (int i = 0; i < input.getSize(); i++) {
			for (int j = 0; j < input.getSize(); j++) {
				Logger.logMessage(String.format("%.4f\t", distMatrix.getDistance(i, j)), false);
			}
			Logger.logMessage("", true);
		}
		
		Logger.logMessage("-------- --------------- ----------");
	}

	
}
