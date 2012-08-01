package building_sim;
import java.util.List;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.*;
import repast.simphony.util.SimUtilities;



public class Human {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private int energy, startingenergy;
	private boolean trigger=false;
	private int tickcounter=0;
	final private int ticks_torecover=10;
	
	private GridPoint pointToGo;
	
	private int visiblerooms=0;
	
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, int energy){
		this.space=space;
		this.grid=grid;
		this.energy=startingenergy=energy;
		this.pointToGo=grid.getLocation(this);
	}
	
	public int getEnergy(){
		return energy;
	}
	
	public void setEnergy(int en){
		energy=en;
	}
	
	public int getVisiblerooms(){
		return visiblerooms;
	}
	
	//public int getPointToGoX(){
	//	return this.pointToGo.getX();
	//}
	
	//public int getPointToGoY(){
	//	return this.pointToGo.getY();
	//}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		GridPoint pt=grid.getLocation(this);
		GridCellNgh<Room> nghCreator = new GridCellNgh<Room>(grid,pt,Room.class,4,4);
		List<GridCell<Room>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells,RandomHelper.getUniform());
		pointToGo = null ;
		
		visiblerooms=0;
		for(GridCell<Room> cell:gridCells){
			visiblerooms+=cell.size();
			if(cell.size()>0)
				pointToGo=cell.getPoint();
		}
		
		if(energy>0){
						
			if(visiblerooms==0){ //if no visible room, random walk
				Random r=new Random();
				pointToGo=new GridPoint(pt.getX()-10+r.nextInt(21),pt.getY()-10+r.nextInt(21));
				
			}
			
			moveTowards(pointToGo);
			//else{
				
			//}
			//System.out.println(pointToGo.getX() + " " + pointToGo.getY());
			
			
		}
		else{
			//rest and get energy back..?
			//energy=this.startingenergy;
			tickcounter++;
			if(tickcounter==this.ticks_torecover){
				tickcounter=0;
				energy=this.startingenergy;
			}
			
			pointToGo=grid.getLocation(this);
		}
		trigger=true;
		
	}
	
	public void moveToDoor(GridPoint roomcenter){
		GridPoint roomdoor=new GridPoint(roomcenter.getX(),roomcenter.getY()-2);
		/*if(!roomdoor.equals(grid.getLocation(this))){
			NdPoint myPoint = space . getLocation ( this );
			NdPoint otherPoint = new NdPoint (pt. getX (), pt. getY ());
			double angle = SpatialMath.calcAngleFor2DMovement(space ,myPoint , otherPoint );
			space . moveByVector (this , 1, angle , 0);
			myPoint = space . getLocation ( this );
			grid.moveTo(this , (int ) myPoint . getX (), (int ) myPoint . getY ());
			energy--;
		}*/
	}
	
	public void moveTowards(GridPoint pt){
		if(!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space . getLocation ( this );
			NdPoint otherPoint = new NdPoint (pt. getX (), pt. getY ());
			double angle = SpatialMath.calcAngleFor2DMovement(space ,myPoint , otherPoint );
			space . moveByVector (this , 1, angle , 0);
			myPoint = space . getLocation ( this );
			grid.moveTo(this , (int ) myPoint . getX (), (int ) myPoint . getY ());
			energy--;
		}
		
	}
}
