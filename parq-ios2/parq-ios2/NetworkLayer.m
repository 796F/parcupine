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
#define GRID_MICROBLOCK_REF_LAT 42.245802 //42.245802
#define GRID_MICROBLOCK_REF_LON -71.321869 
#define GRID_MICROBLOCK_COLUMNS 40
#define GRID_MICROBLOCK_ROWS 40
#define GRID_MICROBLOCK_LENGTH_DEGREES 0.01
#define STREET_MICROBLOCK_LENGTH_DEGREES 0.0025
#define SPOT_MICROBLOCK_LENGTH_DEGREES 0.000625
#define MID_BOUNDING_ERROR -1

@implementation NetworkLayer

@synthesize dataLayer;
@synthesize mapController;


-(void) insertTestData{
    NSArray* grids = [[NSArray alloc] initWithObjects:  
                      //insert stuff here.  
                      
                      nil];
    for(id dic in grids){
        //create the grid object
        Grid* grid = (Grid*)[NSEntityDescription insertNewObjectForEntityForName:@"Grid" inManagedObjectContext:dataLayer.managedObjectContext];
        NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
        [f setNumberStyle:NSNumberFormatterNoStyle];
        [grid setGridId:[f numberFromString:[dic objectForKey:@"gridId"]]];
        NSNumber* lon = [f numberFromString:[dic objectForKey:@"lon"]];
        [grid setLon:lon];
        NSNumber* lat = [f numberFromString:[dic objectForKey:@"lat"]];
        [grid setLat:lat];
        [grid setStatus:[f numberFromString:[dic objectForKey:@"fillRate"]]];
        NSNumber* mbid = [f numberFromString:[dic objectForKey:@"mbid"]];
        [grid setMicroblock:mbid];
        
        //store the grid objects into core data if it doesn't exist yet.  
        if (![dataLayer objExistsInCoreData:grid entityType:kGridEntity]){
            //if grid isn't yet inside core data, leave it there.  
            //create the mk polygon and return it.  
        }else{
            //since grid already exists, delete reference in MOC
            [dataLayer.managedObjectContext deleteObject:grid];
            NSLog(@"exists@");
        }
    }
    //return the entire list regardless.  
    NSError* error= nil;
    if(![dataLayer.managedObjectContext save:&error]){
        //oh noes, cant' store this grid.  wtf to do.
        NSLog(@"error saving!!!\n");
    }

}

#pragma mark - RESTKIT callbacks
//ALL SERVER REQUESTS WILL GET HANDLED BY THIS METHOD ASYNCHRONOUSLY.  
-(void) request:(RKRequest *)request didLoadResponse:(RKResponse *)response{    
    //handle background data download to improve the user experience.  
    NSLog(@"\nRESULT >>> %@", [response bodyAsString]);
    
    //add overlays returned using
    [mapController addNewOverlays:nil];
    //update overlays returned using this.  
    [mapController updateOverlays:nil];
    
    //this method will be saving the server responses
    [dataLayer storeGridData:nil];
}

-(void) request:(RKRequest *)request didFailLoadWithError:(NSError *)error{
    
}

-(void) requestDidTimeout:(RKRequest *)request{
    
}

-(void) testAsync{
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
    [abs setBody:jsonData];
    [abs setContentType:@"application/json"];
    [[RKClient sharedClient] post:@"/parkservice.auth" params:abs delegate:self];
    NSLog(@"\nREQUEST >>> %@", [info description]);
    
}

-(void) requestGridsWithIDs:(NSArray*) newIDs{
    //request from the server overlay information like lat/long etc.  
    NSArray* boundingBoxes = [self convertGridLevelIDs:newIDs];
    NSNumber* lastUpdateTimeLong = [NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970]];
    NSDictionary* requestDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:boundingBoxes, @"boxes",lastUpdateTimeLong, @"lastUpdateTime", nil];

    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:requestDictionary options:0 error:&error];
    AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
    [abs setBody:jsonData];
    [abs setContentType:@"application/json"];
    [[RKClient sharedClient] post:@"/parkservice.auth" params:abs delegate:self];
}

-(void) addGridsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
    NSMutableArray* IDsToRequest = [[NSMutableArray alloc] init];
    NSMutableArray* tempNewIDs = [[NSMutableArray alloc] initWithArray:newIDs];
    //go through the newIDs and check if any aren't in Core Data.  
    for(NSNumber* mbid in newIDs){
        if (![dataLayer mbIdExistsInCoreData:mbid entityType:kGridEntity]){

            //if doesn't exist in core data, make a note to request from server
            [IDsToRequest addObject:mbid];
            [tempNewIDs removeObject:mbid];
        }
    }
    //tell the data layer to provide map with overlays it has.  
    [dataLayer fetchGridsForIDs:tempNewIDs];

    //request for those that aren't to fill in the holes.  
    [self requestGridsWithIDs:IDsToRequest];

    //call update on whole map
    CLLocationCoordinate2D NE = [mapController topRightOfMap];
    CLLocationCoordinate2D SW = [mapController botLeftOfMap];
    
    [self updateGridsWithNE:&NE SW:&SW];
}
-(void) addStreetsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
 
}
-(void) addSpotsToMapForIDs:(NSArray*) newIDs UpdateForIDs:(NSArray*) updateIDs{
 
}

-(void) updateGridsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    //send request to server to get updated colors
    [mapController updateOverlays:nil];
}
-(void) updateStreetsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{

}
-(void) updateSpotsWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{

}



-(NSArray*) callGridWithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{    
    //if were missing some, get grids json from server
    NSArray* grids = [NSArray arrayWithObjects:                      
                      
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
        if (![dataLayer objExistsInCoreData:grid entityType:kGridEntity]){
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
