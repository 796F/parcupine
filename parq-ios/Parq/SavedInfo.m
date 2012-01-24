//
//  SavedInfo.m
//  hello
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "SavedInfo.h"

@implementation SavedInfo

+(NSString*) getPlistPath{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSError *error;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:@"ParqInfo.plist"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath: path]){
        //if the file doesn't exist yet, create it and do write.  
        NSString *bundle = [[NSBundle mainBundle] pathForResource:@"ParqInfo" ofType:@"plist"];
        [fileManager copyItemAtPath:bundle toPath: path error:&error]; 
    }
    return path;
}

+(BOOL) ringEnable{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [[savedStock objectForKey:@"ringEnable"] boolValue];
}
+(BOOL) vibrateEnable{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [[savedStock objectForKey:@"vibrateEnable"] boolValue];
}
+(BOOL) autoRefill{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [[savedStock objectForKey:@"autoRefill"] boolValue];
}
+(BOOL) isParked{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    NSNumber* endTime = [savedStock objectForKey:@"endTime"];
    return [endTime doubleValue]>CACurrentMediaTime();
}
+(BOOL) isLoggedIn{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [[savedStock objectForKey:@"uid"] intValue]!=-1;
}
+(void) setEmail:(NSString*)emailIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:emailIn forKey:@"email"];        
    [data writeToFile: path atomically:YES];
}
+(void) setLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:latIn forKey:@"lat"];
    [data setObject:lonIn forKey:@"lon"];
    [data writeToFile: path atomically:YES];
}
+(void) setGeneric:(NSString*) title boolInput:(BOOL)mBoolean{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:[NSNumber numberWithBool:mBoolean ] forKey:title];        
    [data writeToFile: path atomically:YES];
}
+(NSNumber*) getEndTime{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"endTime"];
}
+(NSString*) getEmail{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"email"];
}
+(NSString*) getCardStub{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"ccStub"];
}
+(NSNumber*) getLat{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"lat"];
}
+(NSNumber*) getLon{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"lon"];
}
+(void) setParkingReferenceNumber:(NSString*) parkRefNumIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:parkRefNumIn forKey:@"PARKID"];        
    [data writeToFile: path atomically:YES];
}
+(NSString*) getParkRefNum{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"PARKID"];
}
+(void) unpark{
    //erase PARKID, endTime, lat, lon, spot, minTime, maxTime, defaultRate, minIncrement, description, spotNumber, code
    //from plist.  ERASE.  
}
+(void) park:(ParkInstanceObject*)parkInstObjIn Rate:(RateObject*) rateObjIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:parkInstObjIn.parkingReferenceNumber forKey:@"PARKID"];   
    [data setObject:parkInstObjIn.endTime forKey:@"endTime"]; 
    [data setObject:rateObjIn.lat forKey:@"lat"]; 
    [data setObject:rateObjIn.lon forKey:@"lon"]; 
    [data setObject:rateObjIn.spot forKey:@"spot"];
    [data setObject:rateObjIn.minTime forKey:@"minTime"];
    [data setObject:rateObjIn.maxTime forKey:@"maxTime"]; 
    [data setObject:rateObjIn.defaultRate forKey:@"defaultRate"]; 
    [data setObject:rateObjIn.minIncrement forKey:@"minIncrement"]; 
    [data setObject:rateObjIn.description forKey:@"description"]; 
    [data writeToFile: path atomically:YES];
}
+(RateObject*) getRate{
    //initialize rate object using info stored.  
    //remember to alloc.  
}
+(void) logIn:(NSNumber*) parkState Email:(NSString*)emailIn UID:(NSNumber*) userId ccStub:(NSString*)ccStubIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:parkState forKey:@"parkState"];        
    [data setObject:emailIn forKey:@"email"];        
    [data setObject:userId forKey:@"uid"];        
    [data setObject:ccStubIn forKey:@"ccStub"];        
    [data writeToFile: path atomically:YES];
}
+(void) logOut{
    //CLEAR OUT ALL INFORMATION.
}
+(void) syncParkingSession{
    //STICK ALL INFO FROM SYNC OBJECT INTO PLIST.
}
+(NSString*) getSpotNumber{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"spotNumber"];
}
+(NSNumber*) getSpotId{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"spot"];
}
+(void) setSpotNumber:(NSString*)spotNumIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:spotNumIn forKey:@"spot"];        
    [data writeToFile: path atomically:YES];
}
+(void) toggleVibrate{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    if([[data objectForKey:@"vibrateEnable"] boolValue]){
        //if vibrate is enabled, set false.    
        [data setObject:[NSNumber numberWithBool:NO] forKey:@"vibrateEnable"];
    }else{
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"vibrateEnable"];
    }
    [data writeToFile:path atomically:YES];
}
+(void) toggleRing{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    if([[data objectForKey:@"ringEnable"] boolValue]){
        //if vibrate is enabled, set false.    
        [data setObject:[NSNumber numberWithBool:NO] forKey:@"ringEnable"];
    }else{
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"ringEnable"];
    }
    [data writeToFile:path atomically:YES];
    
}
+(void) toggleRefill{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    if([[data objectForKey:@"autoRefill"] boolValue]){
        //if vibrate is enabled, set false.    
        [data setObject:[NSNumber numberWithBool:NO] forKey:@"autoRefill"];
    }else{
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"autoRefill"];
    }
    [data writeToFile:path atomically:YES];
}
+(void) toggleRemember{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    if([[data objectForKey:@"remember"] boolValue]){
        //if vibrate is enabled, set false.    
        [data setObject:[NSNumber numberWithBool:NO] forKey:@"remember"];
    }else{
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"remember"];
    }
    [data writeToFile:path atomically:YES];
}

@end
