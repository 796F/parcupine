//
//  DataLayer.m
//  Parq
//
//  Created by Michael Xia on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

/*
 Data layer handles all interactions with core Data.  these methods can be accessed by
 retrieving the data layer object from the app delegate, which can be reached by all classes.  
 a single instance of the data layer is initialized when application launches.  
 */

#import "DataLayer.h"
#import "Grid.h"
#import "Street.h"
#import "Spot.h"

@implementation DataLayer
@synthesize managedObjectContext;

- (NSSet *)fetchObjectsForEntityName:(NSString *)newEntityName
                       withPredicate:(id)stringOrPredicate {
    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:newEntityName];
    if (stringOrPredicate){
        NSPredicate *predicate;
        if ([stringOrPredicate isKindOfClass:[NSString class]]){            
            predicate = [NSPredicate predicateWithFormat:stringOrPredicate];
        }else{
            predicate = (NSPredicate *)stringOrPredicate;
        }
        [request setPredicate:predicate];
    }
    
    NSError *error = nil;
    NSArray *results = [managedObjectContext executeFetchRequest:request error:&error];
    if (error != nil){
        [NSException raise:NSGenericException format:[error description]];
    }    
    return [NSSet setWithArray:results];
}

-(BOOL) existsInCoreData:(NSObject*)object entityType:(EntityType) entityType{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSString* entityName;
    NSString* sortField;
    NSNumber* sortKey;
    if(entityType==kGridEntity){
        entityName = @"Grid";
        sortField = @"gridId";
        sortKey = ((Grid*)object).gridId;
    }else if(entityType == kStreetEntity){
        entityName = @"Street";
        sortField = @"streetId";
        sortKey =  ((Street*)object).streetId;
    }else{
        entityName = @"Spot";
        sortField = @"spotNumber";
        sortKey = ((Spot*)object).spotNumber;
    }
    
    //set which entity type you want to search
    NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:managedObjectContext];
    [request setEntity:entity];
    //set the sorting params
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%s == %ld",sortField, sortKey.longValue];
    [request setPredicate:predicate];
    
    NSError *error = nil;
    NSUInteger count = [managedObjectContext countForFetchRequest:request error:&error];
    if (count==0) {
        NSLog(@"does not exist\n");
        return false;
    }else{
        NSLog(@"exists!\n");
        return true;        
    }
}

-(void) storeSpotData:(NSArray*)spotList{
    //for each spot returned by server
    for(id spot in spotList){
        if([self existsInCoreData:spot entityType:kSpotEntity]){
            continue;
        }else{
            [managedObjectContext insertObject:spot];
        }
    }
    NSError *error = nil;
    [managedObjectContext save:&error];
    if (error!=nil) {
        NSLog(@"error saving to core data: %s\n", error.description.UTF8String);
    }    
}

-(void) storeStreetData:(NSArray*)streetList{
    for(id street in streetList){
        if([self existsInCoreData:street entityType:kStreetEntity]){
            continue;
        }else{
            [managedObjectContext insertObject:street];
        }
    }
    NSError *error = nil;
    [managedObjectContext save:&error];
    if (error!=nil) {
        NSLog(@"error saving to core data: %s\n", error.description.UTF8String);
    }
}
-(void) storeGridData:(NSArray*)gridList{
    for(id grid in gridList){
        if([self existsInCoreData:grid entityType:kGridEntity]){
            continue;
        }else{
            [managedObjectContext insertObject:grid];
        }
    }
    NSError *error = nil;
    [managedObjectContext save:&error];
    if (error!=nil) {
        NSLog(@"error saving to core data: %s\n", error.description.UTF8String);
    }
}

- (NSArray*)loadGridData {
    
    
    return [NSArray arrayWithObjects:
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.104159", @"nw_corner", @"42.360393,-71.114159", @"se_corner", [NSNumber numberWithInt:0], @"color", nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.104159", @"nw_corner", @"42.370393,-71.114159", @"se_corner", [NSNumber numberWithInt:4], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.104159", @"nw_corner", @"42.380393,-71.114159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.114159", @"nw_corner", @"42.360393,-71.124159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.114159", @"nw_corner", @"42.370393,-71.124159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.114159", @"nw_corner", @"42.380393,-71.124159", @"se_corner", [NSNumber numberWithInt:0], @"color",nil], 
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.350393,-71.124159", @"nw_corner", @"42.360393,-71.134159", @"se_corner", [NSNumber numberWithInt:1], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360393,-71.124159", @"nw_corner", @"42.370393,-71.134159", @"se_corner", [NSNumber numberWithInt:2], @"color",nil],
            [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370393,-71.124159", @"nw_corner", @"42.380393,-71.134159", @"se_corner", [NSNumber numberWithInt:3], @"color",nil], nil];
    
}

- (NSArray*)loadBlockData {
    NSArray* data = [NSArray arrayWithObjects:
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:2], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:0], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:4], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:0], @"color", nil],nil];
    
    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:2];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        Segment* x =[[Segment alloc] initWithPointsA:&waypoints[0] andB:&waypoints[1] andColor:[[line objectForKey:@"color"] intValue]];
        [segList addObject:x];
    }
    return segList;
}

+(NSArray*) loadSpotData {
    return [NSArray arrayWithObjects:
            @"42.365354,-71.110843,1,1410,0,1",
            @"42.365292,-71.110835,1,1412,0,2",
            @"42.365239,-71.110825,1,1414,0,3",
            @"42.365187,-71.110811,0,1416,0,4",
            @"42.365140,-71.110806,1,1418,0,5",
            @"42.365092,-71.110798,0,1420,0,6",
            @"42.365045,-71.110790,1,1422,0,7",
            @"42.364995,-71.110782,0,1424,0,8",
            @"42.364947,-71.110768,0,1426,0,9",
            @"42.364896,-71.110766,0,1428,0,10",
            @"42.364846,-71.110752,0,1430,0,11",
            @"42.364797,-71.110739,0,1432,0,12",
            
            @"42.365348,-71.110924,1,1411,0,13",
            @"42.365300,-71.110916,0,1413,0,14",
            @"42.365251,-71.110905,0,1415,0,15",
            @"42.365203,-71.110900,0,1417,0,16",
            @"42.365154,-71.110892,1,1419,0,17",
            @"42.365104,-71.110876,0,1421,0,18",
            @"42.365049,-71.110868,1,1423,0,19",
            @"42.364993,-71.110860,1,1425,0,20",
            @"42.364943,-71.110849,1,1427,0,21",
            @"42.364894,-71.110846,1,1429,0,22",
            @"42.364846,-71.110835,0,1431,0,23",
            @"42.364799,-71.110830,1,1433,0,24",
            nil];
    
}

@end
