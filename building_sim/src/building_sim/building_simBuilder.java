package building_sim;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;

public class building_simBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("building_sim");
		ContinuousSpaceFactory spaceFactory =ContinuousSpaceFactoryFinder . createContinuousSpaceFactory ( null );
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context ,
				new RandomCartesianAdder <Object >() ,
				//new SimpleCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(),
				30, 30);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory ( null );
		Grid <Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
				new SimpleGridAdder<Object>(), true, 30, 30));
		
		int humanCount = 10;
		for (int i = 0; i < humanCount ; i++) {
			int energy = RandomHelper.nextIntFromTo(64,100);
			context .add (new Human (space , grid , energy ));
		}
		
		int roomCount = 6;
		int xpos=0,ypos=0;
		int xinit=8;
		for (int i = 0; i < roomCount ; i++) {
			double temperature = RandomHelper.nextDoubleFromTo(15, 25);
			Room r=new Room (space , grid , temperature );
			context .add (r);
			
			if(i>=roomCount/2){
				xpos=xinit+(i-roomCount/2)*8;
				ypos=18;
			}
			else{
				xpos=xinit+i*8;
				ypos=10;
			}
			space.moveTo(r, xpos, ypos);
		}
		
		for ( Object obj : context ) {
			
			NdPoint pt = space . getLocation (obj );
			grid . moveTo (obj , (int )pt. getX (), (int )pt. getY ());
		}
		return context;
	}

}
