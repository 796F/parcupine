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


-(NSDictionary*) fetchGridsForIDs:(NSArray*) microBlockIDs{
    return nil;
}
-(NSDictionary*) fetchStreetsForIDs:(NSArray*) microBlockIDs{
    return nil;    
}
-(NSDictionary*) fetchSpotsForIDs:(NSArray*) microBlockIDs{
    return nil;
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

-(BOOL) existsInCoreData:(NSObject*)object entityType:(EntityType) entityType{
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

-(NSArray*) retrieveGridsForCoordinate:(CLLocationCoordinate2D*)coord{
    //extract them from core data
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"Grid" inManagedObjectContext:managedObjectContext];
    [request setEntity:entity];
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"gridId" ascending:NO];
    
    //ADD search parameters for coordinate.  
    
    NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortDescriptor, nil];
    [request setSortDescriptors:sortDescriptors];
    NSError* error  = nil;
    return [[managedObjectContext executeFetchRequest:request error:&error] mutableCopy];
}

@end
