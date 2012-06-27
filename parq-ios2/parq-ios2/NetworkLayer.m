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
#import "PQMapViewController.h"
#import "Parser.h"
#import "AbstractRequestObject.h"

//microblock constants
#define GRID_MICROBLOCK_REF_LAT 42.283405
#define GRID_MICROBLOCK_REF_LON -71.274834 
#define GRID_MICROBLOCK_COLUMNS 35
#define GRID_MICROBLOCK_ROWS 20
#define GRID_MICROBLOCK_LENGTH_DEGREES 0.01
#define STREET_MICROBLOCK_LENGTH_DEGREES 0.0025
#define SPOT_MICROBLOCK_LENGTH_DEGREES 0.000625
#define MID_BOUNDING_ERROR -1

@implementation NetworkLayer

@synthesize dataLayer;
@synthesize parent;
-(void) testAsync{
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
    [abs setBody:jsonData];
    [abs setContentType:@"application/json"];
    [[RKClient sharedClient] post:@"/parkservice.auth" params:abs delegate:parent];
    NSLog(@"\nREQUEST >>> %@", [info description]);
    
}

-(NSDictionary*) requestGridsWithIDs:(NSArray*) newIDs{
    //request from the server overlay information like lat/long etc.  
    NSArray* boundingBoxes = [self convertGridLevelIDs:newIDs];
    NSNumber* lastUpdateTimeLong = [NSNumber numberWithLong:[[NSDate date] longValue]];
    NSDictionary* requestDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:boundingBoxes, @"boxes",lastUpdateTimeLong, @"lastUpdateTime", nil];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:requestDictionary options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.maps"];
    //send boundingBoxes to server 
    [request setMethod:RKRequestMethodGET];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];    
    RKResponse* result = [request sendSynchronously];
    //create MKPolygons with the results, and then repackage and return.  
    return [Parser parseGridResponse:[result bodyAsString]];
}

-(NSDictionary*) addGridsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
    NSMutableArray* IDsToRequest = [[NSMutableArray alloc] init];
    NSMutableArray* temp = [[NSMutableArray alloc] initWithArray:newIDs];
    //go through the newIDs and check if any aren't in Core Data.  
    for(NSNumber* gridId in newIDs){
        if (![dataLayer existsInCoreData:gridId entityType:kGridEntity]){
            //move to requests array
            [IDsToRequest addObject:gridId];
            [temp removeObject:gridId];
        }
    }
    NSMutableDictionary* overlaysToAdd = [[NSMutableDictionary alloc] initWithDictionary:[dataLayer fetchGridsForIDs:temp]];
    //request for those that aren't to fill in the holes.  
    [overlaysToAdd addEntriesFromDictionary:[self requestGridsWithIDs:IDsToRequest]];
    //call update on whole map
    CLLocationCoordinate2D NE = [parent topRightOfMap];
    CLLocationCoordinate2D SW = [parent botLeftOfMap];
    NSDictionary* updatedAvailability = [self updateGridsWithNE:&NE SW:&SW];
    //nest and return both dictionaries.  
    NSMutableDictionary* results = [[NSMutableDictionary alloc] initWithObjectsAndKeys:overlaysToAdd , @"new", updatedAvailability, @"update", nil];
    return results;
}
-(NSDictionary*) addStreetsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
    return nil;
}
-(NSDictionary*) addSpotsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
    return nil;
}


-(NSDictionary*) updateGridsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    return nil;
}
-(NSDictionary*) updateStreetsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    return nil;    
}
-(NSDictionary*) updateSpotsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    return nil;
}



-(NSArray*) callGridWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{    
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
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"56", @"gridId", @"42.367981", @"lat", @"-71.132965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"57", @"gridId", @"42.367981", @"lat", @"-71.127965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"58", @"gridId", @"42.367981", @"lat", @"-71.122965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"59", @"gridId", @"42.367981", @"lat", @"-71.117965", @"lon", @"1", @"fillRate" , nil],
                      
                      [[NSDictionary alloc] initWithObjectsAndKeys:@"60", @"gridId", @"42.367981", @"lat", @"-71.112965", @"lon", @"4", @"fillRate" , nil],
                      
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
            //create the mk polygon and return it.  
        }else{
            //since grid already exists, delete reference in MOC
            //how can we keep the grid, but dont' save it.  
            //different object here?  perhaps using the same fields.  
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

-(NSArray*) callSpotWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D *) botLeft{
    return nil;
}

-(NSArray*) callStreetWithNE:(CLLocationCoordinate2D *)topRight SW:(CLLocationCoordinate2D *)botLeft{
    return nil;
}


#pragma mark - MICROBLOCK FUNCTIONS
-(long) gridLevelMicroBlockIDForPoint:(CLLocationCoordinate2D*) coord AndArray:(unsigned long*) RowCol{
    double dy = coord->latitude - GRID_MICROBLOCK_REF_LAT;
    double dx = coord->longitude - GRID_MICROBLOCK_REF_LON;
    if(dx < 0 || dy < 0 ){
        return MID_BOUNDING_ERROR;
    }
    long row = dy/GRID_MICROBLOCK_LENGTH_DEGREES;
    long col = dx/GRID_MICROBLOCK_LENGTH_DEGREES;
    RowCol[0] = row;
    RowCol[1] = col;
    if(row> GRID_MICROBLOCK_ROWS || col > GRID_MICROBLOCK_COLUMNS){
        return MID_BOUNDING_ERROR;
    }    
    long microblock_id = row * GRID_MICROBLOCK_COLUMNS + col;
    NSLog(@"row: %lu col %lu gives id: %lu\n", row, col, microblock_id);
    [self botLeftOfGridID:[NSNumber numberWithLong:microblock_id]];
    return microblock_id;
}

-(NSMutableArray*) getGridLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{    
    NSMutableArray* microBlockIds = [[NSMutableArray alloc] init];
    
    unsigned long topRightRowCol[2]; //ind 0 is row and 1 is col
    long topRightId = [self gridLevelMicroBlockIDForPoint:topRight AndArray:topRightRowCol];
    unsigned long botLeftRowCol[2];
    long botLeftId = [self gridLevelMicroBlockIDForPoint:botLeft AndArray:botLeftRowCol];

    for(int j= botLeftRowCol[0]; j<=topRightRowCol[0]; j++){
        long tempId = botLeftId;
        for(int i = botLeftRowCol[1]; i<=topRightRowCol[1]; i++){
            if(tempId>topRightId){
                //error, went beyond our limit.  
                NSLog(@"MICROBLOCK GEN ERROR\n");
            }
            [microBlockIds addObject:[NSNumber numberWithLong:tempId]];
            tempId++;
        }
        botLeftId+=GRID_MICROBLOCK_COLUMNS;
    }
    //by generating blocks in increasing order, the array is inherently sorted.  
    return microBlockIds;
}
-(long) streetLevelMicroBlockIDForPoint:(CLLocationCoordinate2D*) coord AndArray:(unsigned long*) RowCol{
    return 0;
}
-(NSArray*) getStreetLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{    
    
    return nil;
}

-(long) spotLevelMicroBlockIDForPoint:(CLLocationCoordinate2D*) coord AndArray:(unsigned long*) RowCol{
    return 0;
}
-(NSArray*) getSpotLevelMicroBlockIDListWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{    
    
    return nil;
}

//return minimal number of bounding boxes to represent the given microblocks.  
-(NSArray*) convertGridLevelIDs:(NSArray*) microBlockIds{
    //this list is pre-sorted, smallest to largest.  
    NSMutableArray* results = [[NSMutableArray alloc] init];
    NSMutableArray* temp = [[NSMutableArray alloc] initWithArray:microBlockIds copyItems:YES];
    //start from the lowest id number, and detect if +1 exists, and if +COLUMNS exists.  
    while(temp.count>0){
        //assume index 0 is start,
        NSNumber* startId = [microBlockIds objectAtIndex:0];
        NSNumber* nextID = [NSNumber numberWithLong: startId.longValue+1];
        int rightCount = 1, upCount = 1;
        while([temp containsObject:nextID]){
            rightCount++;
            //try to proceed to right
            nextID = [NSNumber numberWithLong:nextID.longValue+1];
        }
        //return to index 0 as start
        nextID = [NSNumber numberWithLong:startId.longValue+GRID_MICROBLOCK_COLUMNS];
        while([microBlockIds containsObject:nextID]){
            upCount++;
            //try to proceed upwards
            nextID = [NSNumber numberWithLong:nextID.longValue+GRID_MICROBLOCK_COLUMNS];
        }
        BoundingBox* bb = [[BoundingBox alloc] init];
        bb.botLeft = [self botLeftOfGridID:startId];
        if(rightCount > upCount){
            //create bonding box going right
            bb.topRight = [self topRightOfGridId:[NSNumber numberWithLong:startId.longValue+rightCount]];
            //add th bounding box to output
            [results addObject:bb];
            //remove indexes 0, 1, 2, ...
            NSIndexSet* toRemoveList = [[NSIndexSet alloc] initWithIndexesInRange:NSMakeRange(0, rightCount)];
            [temp removeObjectsAtIndexes:toRemoveList];
        }else{
            //create bounding box going up
            bb.topRight = [self topRightOfGridId:[NSNumber numberWithLong:startId.longValue+upCount*GRID_MICROBLOCK_COLUMNS]];
            [results addObject:bb];
            for(int i=0; i<upCount; i++){
                //removes objects 0, 35, 70, ...
                [temp removeObject:[NSNumber numberWithLong:startId.longValue+i*GRID_MICROBLOCK_COLUMNS]];
            }
        }
    }
    //keep track of which side is longer, go with the larger box.  
    
    //remove the id's that were consumed, repeat until no more ID's are in the list.  
    
    return nil;
}
//given a microblock ID for grid, return the bottom left lat/lon
-(CLLocationCoordinate2D) botLeftOfGridID:(NSNumber*) gridId{
    int column = gridId.longValue % GRID_MICROBLOCK_COLUMNS;
    int row = gridId.longValue / GRID_MICROBLOCK_COLUMNS;
    double lat = GRID_MICROBLOCK_REF_LAT + row * GRID_MICROBLOCK_LENGTH_DEGREES;
    double lon = GRID_MICROBLOCK_REF_LON + column * GRID_MICROBLOCK_LENGTH_DEGREES;
    return CLLocationCoordinate2DMake(lat, lon);
}
//given a microblock ID for grid, return the top right lat/lon
-(CLLocationCoordinate2D) topRightOfGridId:(NSNumber*) gridId{
    int column = gridId.longValue % GRID_MICROBLOCK_COLUMNS;
    int row = gridId.longValue / GRID_MICROBLOCK_COLUMNS;
    double lat = GRID_MICROBLOCK_REF_LAT + (row+1) * GRID_MICROBLOCK_LENGTH_DEGREES;
    double lon = GRID_MICROBLOCK_REF_LON + (column+1) * GRID_MICROBLOCK_LENGTH_DEGREES;
    return CLLocationCoordinate2DMake(lat, lon);
}

@end
