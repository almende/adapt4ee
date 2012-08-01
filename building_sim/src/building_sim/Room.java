package building_sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.CellAccessor;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class Room {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private double temperature;
	private int humancount=0;
	private boolean humanin=false;
	
	public void setTemperature(double temp){
		temperature=temp;
	}
	
	public double getTemperature(){
		return temperature;
	}
	
	public int getHumancount(){
		return humancount;
	}
	
	public Room(ContinuousSpace<Object> space, Grid<Object> grid, double temperature){
		this.space=space;
		this.grid=grid;
		this.temperature=temperature;
	}
	
	@Watch ( watcheeClassName = "building_sim.Human",
			watcheeFieldNames = "trigger",
		    query = "within_moore 2",
		    whenToTrigger = WatcherTriggerSchedule . IMMEDIATE )		
	public void humanIN(){
		humanin=true;
		GridPoint pt=grid.getLocation(this);
		//CellAccessor<Human> myaccess=grid.getCellAccessor();
		
		GridCellNgh<Human> nghCreator = new GridCellNgh<Human>(grid,pt,Human.class,2,2);
		List<GridCell<Human>> gridCells = nghCreator.getNeighborhood(true);
		humancount=0;
		for(GridCell<Human> cell:gridCells){
			humancount+=cell.size();
			if(temperature<36.5)
				temperature+=0.05*cell.size();
		}				
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step(){
		
		if(temperature>15){
			Random r=new Random();
			temperature-=r.nextDouble()/100;				
        }
		
		if(humanin){
			humanin=false;
		}
		else{
			humancount=0;
		}
			
		
	}
	
}
