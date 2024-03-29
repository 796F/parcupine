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
@synthesize mapController;

#pragma mark - plist calls


-(void) setAppVersion:(NSNumber*)appVersion{
    //set the currelyt parked spot's id for restore use.
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:appVersion forKey:@"appVersion"];
    [data writeToFile:path atomically:YES];
}
-(NSNumber*) getAppVersion{
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"appVersion"];
}

-(void) setSpotId:(NSNumber*) spotId{
    //set the currelyt parked spot's id for restore use.
    NSLog(@"setting spotId = %lu\n", [spotId longValue]);
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:spotId forKey:@"spotId"];
    [data writeToFile:path atomically:YES];
}
-(NSNumber*) getSpotId{
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"spotId"];
}

-(void) setLastReportTime:(NSDate*) startTime{
    NSLog(@"setting lastrepTime = %f\n", [startTime timeIntervalSince1970]);
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:startTime forKey:@"lastReportTime"];
    [data writeToFile:path atomically:YES];
}
-(NSDate*) getLastReportTime{
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"lastReportTime"];
}

+ (void)setEndTime:(NSDate *)endTime {
    NSLog(@"setting endTime = %f\n", [endTime timeIntervalSince1970]);
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:endTime forKey:@"endTime"];
    [data writeToFile:path atomically:YES];
}
+ (NSDate *)endTime {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    NSDate* endDate = [data objectForKey:@"endTime"];
    if (endDate == nil) {
        return [NSDate distantPast];
    } else {
        return endDate;
    }
}

+ (void)setStartTime:(NSDate *)startTime {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:startTime forKey:@"startTime"];
    [data writeToFile:path atomically:YES];
}
+ (NSDate *)startTime {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"startTime"];
}

+ (void)setParkingMode:(ParkMode)parkingMode {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    data[@"parkingMode"] = @(parkingMode);
    [data writeToFile:path atomically:YES];
}

+ (ParkMode)parkingMode {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data[@"parkingMode"] intValue];
}

+ (void)setSpotInfo:(SpotInfo *)spotInfo {
    NSData *objData = [NSKeyedArchiver archivedDataWithRootObject:spotInfo];
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:objData forKey:@"spotInfo"];
    [data writeToFile:path atomically:YES];
}

+ (SpotInfo *)spotInfo {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [NSKeyedUnarchiver unarchiveObjectWithData:[data objectForKey:@"spotInfo"]];
}

+ (NSString *)parkingReference {
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    return [data objectForKey:@"parkingReferenceNumber"];
}

+ (void)setParkingReference:(NSString*) ref{
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *plist = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [plist setObject:ref forKey:@"parkingReferenceNumber"];
    [plist writeToFile:path atomically:YES];
}

+ (void)clearSavedParkingSession {
    NSString *path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data removeObjectsForKeys:@[@"endTime", @"startTime", @"parkingMode", @"spotInfo", @"parkingReferenceNumber"]];
    [data writeToFile:path atomically:YES];
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
    
//    379,
//    380,
//    381,
//    382,
//    383,
//    384,
//    385,
    
//    419,
//    420,
//    421,
//    422,
//    423,
//    424,
//    425,
    
//    459,
//    460,
//    461,
//    462,
//    463,
//    464,
//    465,
    
//    499,
//    500,
//    501,
//    502,
//    503,
//    504,
//    505,
    
//    539,
//    540,
//    541,
//    542,
//    543,
//    544,
//    545
    
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
        Grid* grid = (Grid*)[NSEntityDescription insertNewObjectForEntityForName:@"Grid" inManagedObjectContext:[[self class] managedObjectContext]];
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
    }
    
    if(![[[self class] managedObjectContext] save:&error]){
        //oh noes, cant' store this grid.  wtf to do.
        NSLog(@"error saving!!!\n");
    }
}

-(BOOL) hasMockData{
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    if(![data objectForKey:@"hasMockData"]){
        //doesn't have mock data. tell app to load.
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"hasMockData"];
        [data writeToFile: path atomically:YES];
        return NO;
    }else{
        //flag was raised, already has mock data.
        return YES;
    }
}

-(BOOL) isFirstLaunch{
    NSString* path = [[self class] plistPath];
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

-(BOOL) isLoggedIn{
    NSString* path = [[self class] plistPath];
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
    NSString* path = [[self class] plistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:path];
    [data setObject:[NSNumber numberWithBool:yesORno] forKey:@"isLoggedIn"];        
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
        NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:[[self class] managedObjectContext]];
        [request setEntity:entity];
        //set the sorting params
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
        [request setPredicate:predicate];
        
        NSError *error = nil;
        NSArray* returnedObjects = [[[self class] managedObjectContext] executeFetchRequest:request error:&error];
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
        
        NSEntityDescription *entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:[[self class] managedObjectContext]];
        [request setEntity:entity];
        NSString* sortKey = [NSString stringWithFormat:@"%ld",mbid.longValue];
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@",  @"microblock", sortKey];
        [request setPredicate:predicate];
        NSError *error = nil;
        NSArray* returnedObjects = [[[self class] managedObjectContext] executeFetchRequest:request error:&error];
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
    NSArray *results = [[[self class] managedObjectContext] executeFetchRequest:request error:&error];
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
    NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:[[self class] managedObjectContext]];
    [request setEntity:entity];
    //set the sorting params
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@", @"microblock", sortKey];
    [request setPredicate:predicate];
    
    NSError *error = nil;
    NSUInteger count = [[[self class] managedObjectContext] countForFetchRequest:request error:&error];
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
    NSEntityDescription* entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:[[self class] managedObjectContext]];
    [request setEntity:entity];
    //set the sorting params
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"%K == %@",sortField, sortKey];
    [request setPredicate:predicate];
    
    NSError *error = nil;
    NSUInteger count = [[[self class] managedObjectContext] countForFetchRequest:request error:&error];
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
            [[[self class] managedObjectContext] insertObject:overlay];
        }
    }
    NSError *error = nil;
    [[[self class] managedObjectContext] save:&error];
    if (error!=nil) {
        NSLog(@"error saving to core data: %s\n", error.description.UTF8String);
    }    

}

-(BOOL) userAddPoints:(NSNumber*) earnedPoints{
    User* user = [[self class] fetchUser];
    [user setBalance:[NSNumber numberWithInt:user.balance.integerValue + earnedPoints.integerValue]];
    NSError* error;
    if(![[[self class] managedObjectContext] save:&error]){
        //ERROR
        return NO;
    }else{
        return YES;
    }
    
}
-(BOOL) userDecPoints:(NSNumber*) decreasePoints{
    User* user = [[self class] fetchUser];

    [user setBalance:[NSNumber numberWithInt:user.balance.integerValue - decreasePoints.integerValue]];
    NSError* error;
    if(![[[self class] managedObjectContext] save:&error]){
        //ERROR
        return NO;
    }else{
        return YES;
    }
}

-(User*) saveUserWithEmail:(NSString*)email Pass:(NSString*)pass License:(NSString*)license UID:(NSNumber*) uid Balance:(NSNumber*) balance{
    User* user = (User*)[NSEntityDescription insertNewObjectForEntityForName:@"User" inManagedObjectContext:[[self class] managedObjectContext]];
    
    [user setAddress:@"Room 9-209"];
    [user setName:@"Peter Parker"];
    [user setEmail:email];  //this should be returned by server.
    [user setPassword:pass];
    [user setLicense:license];
    [user setCity:@"Cambridge"];
    [user setPayment:[NSNumber numberWithInt:0]];
    [user setUid:uid];
    [user setBalance:balance];
    NSError* error;
    if(![[[self class] managedObjectContext] save:&error]){
        //logged in, but something wrong wtih core data. cannot store user.
        [[[self class] managedObjectContext] deleteObject:user];
        return nil;
    }else{
        return user;
    }
}

+ (User *)fetchUser {
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"User" inManagedObjectContext:[[self class] managedObjectContext]];
    [request setEntity:entity];
    NSError *error = nil;
    NSArray* returnedObjects = [[[self class] managedObjectContext] executeFetchRequest:request error:&error];
    return (User *)[returnedObjects lastObject];
}

+ (NSManagedObjectContext *)managedObjectContext {
    static NSManagedObjectContext *moc;
    if (moc == nil) {
        moc = ((PQAppDelegate *)[UIApplication sharedApplication].delegate).managedObjectContext;
    }
    return moc;
}

+ (NSString *)plistPath {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSError *error;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:@"parq.plist"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath: path]){
        NSString *bundle = [[NSBundle mainBundle] pathForResource:@"parq" ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath: path error:&error];
    }
    return path;
}

@end
