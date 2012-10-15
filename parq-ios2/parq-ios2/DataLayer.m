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

-(void) setSpotInfo:(SpotInfo*) spotInfo{
    NSData* objData = [NSKeyedArchiver archivedDataWithRootObject:spotInfo];
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:objData forKey:@"spotInfo"];
    [data writeToFile:path atomically:YES];
}

-(SpotInfo*) getSpotInfo{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [NSKeyedUnarchiver unarchiveObjectWithData:[data objectForKey:@"spotInfo"]];
}

-(void) setParkingReference:(NSString*) ref{
    NSLog(@"setting ref = %@\n", ref);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:ref forKey:@"parkingReferenceNumber"];
    [data writeToFile:path atomically:YES];
}
-(NSString*) getParkingReference{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"parkingReferenceNumber"];
}

-(void) setSpotId:(NSNumber*) spotId{
    //set the currelyt parked spot's id for restore use.
    NSLog(@"setting spotId = %lu\n", [spotId longValue]);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:spotId forKey:@"spotId"];
    [data writeToFile:path atomically:YES];
}
-(NSNumber*) getSpotId{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"spotId"];
}

-(void) setLastReportTime:(NSDate*) startTime{
    NSLog(@"setting lastrepTime = %f\n", [startTime timeIntervalSince1970]);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:startTime forKey:@"lastReportTime"];
    [data writeToFile:path atomically:YES];
}
-(NSDate*) getLastReportTime{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"lastReportTime"];
}

-(void) setStartTime:(NSDate*) startTime{
    NSLog(@"setting lastrepTime = %f\n", [startTime timeIntervalSince1970]);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:startTime forKey:@"startTime"];
    [data writeToFile:path atomically:YES];
}
-(NSDate*) getStartTime{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"startTime"];
}

-(void) setEndTime:(NSDate*) endTime{
    NSLog(@"setting endTime = %f\n", [endTime timeIntervalSince1970]);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:endTime forKey:@"endTime"];
    [data writeToFile:path atomically:YES];
}
-(NSDate*) getEndTime{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    NSDate* endDate = [data objectForKey:@"endTime"];
    if (endDate == nil) {
        return [NSDate dateWithTimeIntervalSinceNow:-9000];
    }else{
        return endDate;
    }
}

-(void) logString:(NSString*) string{
    CLLocationManager* locationManager = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).locationManager;
    NSArray *documentPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDir = [documentPaths objectAtIndex:0];
    NSString *logPath = [documentsDir stringByAppendingPathComponent:@"log.txt"];
    NSFileHandle *fileHandler = [NSFileHandle fileHandleForUpdatingAtPath:logPath];
    NSString* loc = [NSString stringWithFormat:@"<%f,%f>", locationManager.location.coordinate.longitude, locationManager.location.coordinate.latitude];
    [fileHandler seekToEndOfFile];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM-dd HH:mm"];
    NSString *formattedDateString = [dateFormatter stringFromDate:[NSDate date]];
    [fileHandler writeData:[[NSString stringWithFormat:@"%@ %@ %@\n", formattedDateString, loc, string] dataUsingEncoding:NSUTF8StringEncoding]];
    [fileHandler closeFile];
    
}

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
        
        
        [grid setGridId:[NSNumber numberWithLong:gridid]];
        gridid++;
        NSNumber* lon = [f numberFromString:[innerArray objectAtIndex:1]];
        [grid setLon:lon];
        NSNumber* lat = [f numberFromString:[innerArray objectAtIndex:0]];
        [grid setLat:lat];
        NSNumber* mbid = [f numberFromString:[innerArray objectAtIndex:3]];
        if(mbid.longValue == 462 && gridid == 4878){
            //4870 4878 5390 5378
            [grid setStatus:[NSNumber numberWithInt:5]];
        }else{
            [grid setStatus:[NSNumber numberWithInt:-1]];
        }
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
-(int) UIType{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [[data objectForKey:@"uiType"] intValue];
}

-(BOOL) isLoggedIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    if([[data objectForKey:@"isLoggedIn"] boolValue]){
        //isLoggedIn 
        return YES;        
    }else{
        //flag was raised, this isn't first launch.  
        return NO;
    }
}
-(void) setLoggedIn:(BOOL) yesORno{
    NSLog(@"setting loggedIn = %d\n", yesORno);
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:[NSNumber numberWithBool:yesORno] forKey:@"isLoggedIn"];        
    [data writeToFile: path atomically:YES];
}
-(void) setUIType:(int) type{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:[NSNumber numberWithInt:type] forKey:@"uiType"];        
    [data writeToFile: path atomically:YES];
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
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@",  @"microblock", sortKey];
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
        //error fetching.
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
    
    NSString* sortKey = [NSString stringWithFormat:@"%lld",obj_id.longLongValue];

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

-(BOOL) userAddPoints:(NSNumber*) earnedPoints{
    User* user = [self getUser];
    [user setBalance:[NSNumber numberWithInt:user.balance.integerValue + earnedPoints.integerValue]];
    NSError* error;
    if(![managedObjectContext save:&error]){
        //ERROR
        return NO;
    }else{
        return YES;
    }
    
}
-(BOOL) userDecPoints:(NSNumber*) decreasePoints{
    User* user = [self getUser];

    [user setBalance:[NSNumber numberWithInt:user.balance.integerValue - decreasePoints.integerValue]];
    NSError* error;
    if(![managedObjectContext save:&error]){
        //ERROR
        return NO;
    }else{
        return YES;
    }
}

-(User*) getUser{
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"User" inManagedObjectContext:managedObjectContext];
    [request setEntity:entity];
    NSError *error = nil;
    NSArray* returnedObjects = [managedObjectContext executeFetchRequest:request error:&error];
    return (User*) [returnedObjects lastObject];
}

-(User*) saveUserWithEmail:(NSString*)email Pass:(NSString*)pass License:(NSString*)license UID:(NSNumber*) uid Balance:(NSNumber*) balance{
    User* user = (User*)[NSEntityDescription insertNewObjectForEntityForName:@"User" inManagedObjectContext:managedObjectContext];
    
    [user setAddress:@"michaelxia.com"];
    [user setName:@"@mikeyxia"];
    [user setEmail:email];  //this should be returned by server.
    [user setPassword:pass];
    [user setLicense:license];
    [user setCity:@"Cambridge"];
    [user setPayment:[NSNumber numberWithInt:0]];
    [user setUid:uid];
    [user setBalance:balance];
    NSError* error;
    if(![managedObjectContext save:&error]){
        //logged in, but something wrong wtih core data. cannot store user.
        [managedObjectContext deleteObject:user];
        return nil;
    }else{
        return user;
    }

}

@end
