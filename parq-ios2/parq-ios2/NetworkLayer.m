//
//  Server.m
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

/* 
 The network layer handles all interactions with the server.  these methods can be accessed by
 retrieving the data layer object from the app delegate, which can be reached by all classes.
 All pushes to, and fetches from, will go thorugh this layer.  There is a single instance.  
 */

#import "NetworkLayer.h"
#import "Grid.h"

@implementation NetworkLayer

@synthesize dataLayer;

#define GRID_MICROBLOCK_REF_LAT 42.412079
#define GRID_MICROBLOCK_REF_LON -71.168098 
#define GRID_MICROBLOCK_COLUMNS 20
#define GRID_MICROBLOCK_ROWS 20
#define GRID_MICROBLOCK_LENGTH_DEGREES 0.1

-(NSArray*) getGridLevelMicroBlockIdListWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{    
    double dy = topLeft->latitude - GRID_MICROBLOCK_REF_LAT;
    double dx = topLeft->longitude - GRID_MICROBLOCK_REF_LON;
    if(dx < 0 || dy > 0 ){
        NSLog(@"error, past reference point\n");
        return nil;
    }
    double row = fabs(dy/GRID_MICROBLOCK_LENGTH_DEGREES);
    double col = dx/GRID_MICROBLOCK_LENGTH_DEGREES;
    if(row> GRID_MICROBLOCK_ROWS || col > GRID_MICROBLOCK_COLUMNS){
        NSLog(@"error, past bounded area\n");
        return nil;
    }
    double microblock_id = row * GRID_MICROBLOCK_COLUMNS + col;
    NSLog(@"block id = %d\n", (int)microblock_id);
    return nil;
}
-(NSArray*) getStreetLevelMicroBlockIdListWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{    
 
    return nil;
}
-(NSArray*) getSpotLevelMicroBlockIdListWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{    
    
    return nil;
}

-(NSArray*) callGridWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{    
    //first check core data for grids.  
    [self getGridLevelMicroBlockIdListWithNW:topLeft SE:botRight];
    
    //if were missing some, get grids json from server
    NSArray* grids = [NSArray arrayWithObjects:                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"12", @"gridId", @"42.387981", @"lat", @"-71.132965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"13", @"gridId", @"42.387981", @"lat", @"-71.127965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"14", @"gridId", @"42.387981", @"lat", @"-71.122965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"15", @"gridId", @"42.387981", @"lat", @"-71.117965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"16", @"gridId", @"42.387981", @"lat", @"-71.112965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"17", @"gridId", @"42.387981", @"lat", @"-71.107965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"18", @"gridId", @"42.387981", @"lat", @"-71.102965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"19", @"gridId", @"42.387981", @"lat", @"-71.097965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"20", @"gridId", @"42.387981", @"lat", @"-71.092965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"21", @"gridId", @"42.387981", @"lat", @"-71.087965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"22", @"gridId", @"42.387981", @"lat", @"-71.082965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"23", @"gridId", @"42.382981", @"lat", @"-71.132965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"24", @"gridId", @"42.382981", @"lat", @"-71.127965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"25", @"gridId", @"42.382981", @"lat", @"-71.122965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"26", @"gridId", @"42.382981", @"lat", @"-71.117965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"27", @"gridId", @"42.382981", @"lat", @"-71.112965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"28", @"gridId", @"42.382981", @"lat", @"-71.107965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"29", @"gridId", @"42.382981", @"lat", @"-71.102965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"30", @"gridId", @"42.382981", @"lat", @"-71.097965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"31", @"gridId", @"42.382981", @"lat", @"-71.092965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"32", @"gridId", @"42.382981", @"lat", @"-71.087965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"33", @"gridId", @"42.382981", @"lat", @"-71.082965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"34", @"gridId", @"42.377981", @"lat", @"-71.132965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"35", @"gridId", @"42.377981", @"lat", @"-71.127965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"36", @"gridId", @"42.377981", @"lat", @"-71.122965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"37", @"gridId", @"42.377981", @"lat", @"-71.117965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"38", @"gridId", @"42.377981", @"lat", @"-71.112965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"39", @"gridId", @"42.377981", @"lat", @"-71.107965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"40", @"gridId", @"42.377981", @"lat", @"-71.102965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"41", @"gridId", @"42.377981", @"lat", @"-71.097965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"42", @"gridId", @"42.377981", @"lat", @"-71.092965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"43", @"gridId", @"42.377981", @"lat", @"-71.087965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"44", @"gridId", @"42.377981", @"lat", @"-71.082965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"45", @"gridId", @"42.372981", @"lat", @"-71.132965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"46", @"gridId", @"42.372981", @"lat", @"-71.127965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"47", @"gridId", @"42.372981", @"lat", @"-71.122965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"48", @"gridId", @"42.372981", @"lat", @"-71.117965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"49", @"gridId", @"42.372981", @"lat", @"-71.112965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"50", @"gridId", @"42.372981", @"lat", @"-71.107965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"51", @"gridId", @"42.372981", @"lat", @"-71.102965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"52", @"gridId", @"42.372981", @"lat", @"-71.097965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"53", @"gridId", @"42.372981", @"lat", @"-71.092965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"54", @"gridId", @"42.372981", @"lat", @"-71.087965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"55", @"gridId", @"42.372981", @"lat", @"-71.082965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"56", @"gridId", @"42.367981", @"lat", @"-71.132965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"57", @"gridId", @"42.367981", @"lat", @"-71.127965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"58", @"gridId", @"42.367981", @"lat", @"-71.122965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"59", @"gridId", @"42.367981", @"lat", @"-71.117965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"60", @"gridId", @"42.367981", @"lat", @"-71.112965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"61", @"gridId", @"42.367981", @"lat", @"-71.107965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"62", @"gridId", @"42.367981", @"lat", @"-71.102965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"63", @"gridId", @"42.367981", @"lat", @"-71.097965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"64", @"gridId", @"42.367981", @"lat", @"-71.092965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"65", @"gridId", @"42.367981", @"lat", @"-71.087965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"66", @"gridId", @"42.367981", @"lat", @"-71.082965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"67", @"gridId", @"42.362981", @"lat", @"-71.132965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"68", @"gridId", @"42.362981", @"lat", @"-71.127965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"69", @"gridId", @"42.362981", @"lat", @"-71.122965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"70", @"gridId", @"42.362981", @"lat", @"-71.117965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"71", @"gridId", @"42.362981", @"lat", @"-71.112965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"72", @"gridId", @"42.362981", @"lat", @"-71.107965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"73", @"gridId", @"42.362981", @"lat", @"-71.102965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"74", @"gridId", @"42.362981", @"lat", @"-71.097965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"75", @"gridId", @"42.362981", @"lat", @"-71.092965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"76", @"gridId", @"42.362981", @"lat", @"-71.087965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"77", @"gridId", @"42.362981", @"lat", @"-71.082965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"78", @"gridId", @"42.357981", @"lat", @"-71.132965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"79", @"gridId", @"42.357981", @"lat", @"-71.127965", @"lon", @"0", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"80", @"gridId", @"42.357981", @"lat", @"-71.122965", @"lon", @"4", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"81", @"gridId", @"42.357981", @"lat", @"-71.117965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"82", @"gridId", @"42.357981", @"lat", @"-71.112965", @"lon", @"2", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"83", @"gridId", @"42.357981", @"lat", @"-71.107965", @"lon", @"3", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"84", @"gridId", @"42.357981", @"lat", @"-71.102965", @"lon", @"2", @"fillRate" , nil],
                      nil];
    NSMutableArray* gridArr = [[NSMutableArray alloc] initWithCapacity:grids.count];
    for(id dic in grids){
        //create the grid object
        Grid* grid = (Grid*)[NSEntityDescription insertNewObjectForEntityForName:@"Grid" inManagedObjectContext:dataLayer.managedObjectContext];
        NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
        [f setNumberStyle:NSNumberFormatterNoStyle];
        [grid setGridId:[f numberFromString:[dic objectForKey:@"gridId"]]];
        [grid setLon:[f numberFromString:[dic objectForKey:@"lon"]]];
        [grid setLat:[f numberFromString:[dic objectForKey:@"lat"]]];
        [grid setStatus:[f numberFromString:[dic objectForKey:@"fillRate"]]];
        //store the grid objects into core data if it doesn't exist yet.  
        if (![dataLayer existsInCoreData:grid entityType:kGridEntity]){
            //if grid isn't yet inside core data, leave it there.  
        }else{
            //since grid already exists, delete reference in MOC
            //how can we keep the grid, but dont' save it.  
        }
        [gridArr addObject:grid];
    }
    //return the entire list regardless.  
    NSError* error= nil;
    if(![dataLayer.managedObjectContext save:&error]){
        //oh noes, cant' store this grid.  wtf to do.
    }
    return gridArr; 
}

-(NSArray*) callSpotWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{
    return nil;
}

-(NSArray*) callStreetWithNW:(CLLocationCoordinate2D*) topLeft SE:(CLLocationCoordinate2D*) botRight{
    return nil;
}


@end
