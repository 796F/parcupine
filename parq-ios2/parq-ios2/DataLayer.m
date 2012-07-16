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

//

@implementation DataLayer
@synthesize managedObjectContext;
@synthesize mapController;

#pragma mark - plist calls

-(void) loadMockData{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSError *error;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:@"mockdata.plist"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath: path]){
        //if the file doesn't exist yet, create it and do write.  
        NSString *bundle = [[NSBundle mainBundle] pathForResource:@"mockdata" ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath: path error:&error]; 
    }
    NSDictionary *data = [[NSDictionary alloc] initWithContentsOfFile:path];
    
    
    //store data into core data.  
    int gridid = 0;
    for(NSString* innerString in data.allValues){
        NSArray* innerArray = [innerString componentsSeparatedByString:@","];
        //create the grid object
        Grid* grid = (Grid*)[NSEntityDescription insertNewObjectForEntityForName:@"Grid" inManagedObjectContext:managedObjectContext];
        NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
        [f setNumberStyle:NSNumberFormatterNoStyle];
        
        
        [grid setGridId:[NSNumber numberWithInt:gridid]];
        gridid++;
        NSNumber* lon = [f numberFromString:[innerArray objectAtIndex:1]];
        [grid setLon:lon];
        NSNumber* lat = [f numberFromString:[innerArray objectAtIndex:0]];
        [grid setLat:lat];
        [grid setStatus:[f numberFromString:[innerArray objectAtIndex:2]]];
        NSNumber* mbid = [f numberFromString:[innerArray objectAtIndex:3]];
        [grid setMicroblock:mbid];
        NSLog(@"remaining...%d\n", 6400 - gridid);
    }
    
    if(![managedObjectContext save:&error]){
        //oh noes, cant' store this grid.  wtf to do.
        NSLog(@"error saving!!!\n");
    }
}


-(NSString*) getPlistPath{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSError *error;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:@"parq.plist"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath: path]){
        //if the file doesn't exist yet, create it and do write.  
        NSString *bundle = [[NSBundle mainBundle] pathForResource:@"parq" ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath: path error:&error]; 
    }
    return path;
}
-(BOOL) isFirstLaunch{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    if(![data objectForKey:@"previouslyLaunched"]){
        //now mark the app as launched.  
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"previouslyLaunched"];        
        [data writeToFile: path atomically:YES];
        return YES;        
    }else{
        //flag was raised, this isn't first launch.  
        return NO;
    }
}

#pragma mark - CORE DATA CALLS

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


-(void) fetch:(EntityType) entityType ForIDs:(NSArray*) microBlockIDs{
    NSString* entityName;
    if(entityType==kGridEntity){
        entityName = @"Grid";
    }else if(entityType == kStreetEntity){
        entityName = @"Street";
    }else{
        entityName = @"Spot";
    }
    //go through list of microblock ids.  
    for(NSNumber* mbid in microBlockIDs){
        //extract them from core data
        NSFetchRequest *request = [[NSFetchRequest alloc] init];
        
        NSEntityDescription *entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:managedObjectContext];
        [request setEntity:entity];
        NSString* sortKey = [NSString stringWithFormat:@"%ld",mbid.longValue];
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
        [request setPredicate:predicate];
        NSError *error = nil;
        NSArray* returnedObjects = [managedObjectContext executeFetchRequest:request error:&error];
        NSMutableDictionary* overlayMap = [[NSMutableDictionary alloc] initWithCapacity:returnedObjects.count];
        for(id object in returnedObjects){
            //these all belong to one microblock id.  
            if(entityType == kGridEntity){
                [overlayMap addEntriesFromDictionary:[[NSDictionary alloc] initWithObjectsAndKeys:[object generateOverlay], [(Grid*)object gridId], nil]];
            }else if (entityType == kStreetEntity){
                [overlayMap addEntriesFromDictionary:[[NSDictionary alloc] initWithObjectsAndKeys:[object generateOverlay], [(Street*)object streetId], nil]];
            }else{
                [overlayMap addEntriesFromDictionary:[[NSDictionary alloc] initWithObjectsAndKeys:[object generateOverlay], [(Spot*)object spotId], nil]];
            }
        }    
        NSDictionary* mbidToGridMap = [[NSDictionary alloc] initWithObjectsAndKeys:overlayMap,mbid, nil];
        [mapController addNewOverlays:mbidToGridMap OfType:entityType];
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


-(BOOL) mbIdExistsInCoreData:(NSNumber*)objectId EntityType:(EntityType) entityType{
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

-(BOOL) objExistsInCoreData:(NSObject*)object EntityType:(EntityType) entityType{
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
-(void) store:(EntityType)entityType WithData:(NSArray*)overlayList{
    for(id overlay in overlayList){
        if([self objExistsInCoreData:overlay EntityType:entityType]){
            continue;
        }else{
            [managedObjectContext insertObject:overlay];
        }
    }
    NSError *error = nil;
    [managedObjectContext save:&error];
    if (error!=nil) {
        NSLog(@"error saving to core data: %s\n", error.description.UTF8String);
    }    

}


@end
