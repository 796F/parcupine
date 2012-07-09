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
#import "PQMapViewController.h"
#import "MKShape+Color.h"
#import "UIColor+Parq.h"



@implementation DataLayer
@synthesize managedObjectContext;
@synthesize mapController;

-(void) testFetch:(EntityType)entityType Microblocks:(NSArray*) mbids{
    for(NSNumber* mbid in mbids){
        NSFetchRequest *request = [[NSFetchRequest alloc] init];
       
        NSString* entityName;
        if(entityType==kGridEntity){
            entityName = @"Grid";
        }else if(entityType == kStreetEntity){
            entityName = @"Street";
        }else{
            entityName = @"Spot";
        }
        
        NSString* sortKey = [NSString stringWithFormat:@"%ld",mbid.longValue];
        
        //set which entity type you want to search
        NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:managedObjectContext];
        [request setEntity:entity];
        //set the sorting params
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
        [request setPredicate:predicate];
        
        NSError *error = nil;
        NSArray* returnedObjects = [managedObjectContext executeFetchRequest:request error:&error];
//        NSUInteger count = [managedObjectContext countForFetchRequest:request error:&error];
        for(Grid* grid in returnedObjects){
            //for each returned grid
            NSLog(@"%f %f\n", grid.lat.doubleValue, grid.lon.doubleValue);
        }
    }
}


-(void) fetchGridsForIDs:(NSArray*) microBlockIDs{
    //go through list of microblock ids.  
    for(NSNumber* mbid in microBlockIDs){
        //extract them from core data
        NSFetchRequest *request = [[NSFetchRequest alloc] init];
        NSEntityDescription *entity = [NSEntityDescription entityForName:@"Grid" inManagedObjectContext:managedObjectContext];
        [request setEntity:entity];
        NSString* sortKey = [NSString stringWithFormat:@"%ld",mbid.longValue];
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
        [request setPredicate:predicate];
        NSError *error = nil;
        NSArray* returnedObjects = [managedObjectContext executeFetchRequest:request error:&error];
        NSMutableDictionary* gridMap = [[NSMutableDictionary alloc] initWithCapacity:returnedObjects.count];
        for(Grid* grid in returnedObjects){
            //these all belong to one microblock id.  
            //NSLog(@"%lu = %f %f\n", grid.gridId.longValue, grid.lat.doubleValue, grid.lon.doubleValue);
            [gridMap addEntriesFromDictionary:[[NSDictionary alloc] initWithObjectsAndKeys:[grid generateOverlay], grid.gridId, nil]];
        }    
        NSDictionary* mbidToGridMap = [[NSDictionary alloc] initWithObjectsAndKeys:gridMap,mbid, nil];
        [mapController addNewOverlays:mbidToGridMap];
    }
}



-(void) fetchStreetsForIDs:(NSArray*) microBlockIDs{
   
}
-(void) fetchSpotsForIDs:(NSArray*) microBlockIDs{
    
}
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
-(BOOL) mbIdExistsInCoreData:(NSNumber*)objectId entityType:(EntityType) entityType{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSString* entityName;
    if(entityType==kGridEntity){
        entityName = @"Grid";
    }else if(entityType == kStreetEntity){
        entityName = @"Street";
    }else{
        entityName = @"Spot";
    }
    NSString* sortKey = [NSString stringWithFormat:@"%ld",objectId.longValue];
    
    
    
    //set which entity type you want to search
    NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:managedObjectContext];
    [request setEntity:entity];
    //set the sorting params
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
    [request setPredicate:predicate];
    
    NSError *error = nil;
    NSUInteger count = [managedObjectContext countForFetchRequest:request error:&error];
    if (count==0) {
        //NSLog(@"%s == %s did not occur\n", sortField.UTF8String, sortKey.UTF8String);
        return false;
    }else{
        //NSLog(@"%s == %s exists!!\n", sortField.UTF8String, sortKey.UTF8String);
        return true;        
    }
}

-(BOOL) objExistsInCoreData:(NSObject*)object entityType:(EntityType) entityType{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSString* entityName;
    NSString* sortField;
    NSNumber* obj_id;
    if(entityType==kGridEntity){
        entityName = @"Grid";
        sortField = @"gridId";
        obj_id = ((Grid*)object).gridId;
    }else if(entityType == kStreetEntity){
        entityName = @"Street";
        sortField = @"streetId";
        obj_id =  ((Street*)object).streetId;
    }else{
        entityName = @"Spot";
        sortField = @"spotNumber";
        obj_id = ((Spot*)object).spotNumber;
    }
    
    NSString* sortKey = [NSString stringWithFormat:@"%ld",obj_id.longLongValue];

    //set which entity type you want to search
    NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:managedObjectContext];
    [request setEntity:entity];
    //set the sorting params
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@",sortField, sortKey];
    [request setPredicate:predicate];
    
    NSError *error = nil;
    NSUInteger count = [managedObjectContext countForFetchRequest:request error:&error];
    if (count==0) {
        //NSLog(@"%s == %s did not occur\n", sortField.UTF8String, sortKey.UTF8String);
        return false;
    }else{
        //NSLog(@"%s == %s exists!!\n", sortField.UTF8String, sortKey.UTF8String);
        return true;        
    }
}

-(void) storeSpotData:(NSArray*)spotList{
    //for each spot returned by server
    for(id spot in spotList){
        if([self objExistsInCoreData:spot entityType:kSpotEntity]){
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
        if([self objExistsInCoreData:street entityType:kStreetEntity]){
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
        if([self objExistsInCoreData:grid entityType:kGridEntity]){
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


@end
