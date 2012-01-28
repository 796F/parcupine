//
//  SavedInfo.m
//  hello
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "SavedInfo.h"

@implementation SavedInfo
+(NSNumber*) getUid{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"uid"];
}
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
    NSNumber* result = [savedStock objectForKey:@"uid"];
    if(result ==nil) return NO;
    return [result intValue]!=-1;
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
    [data setObject:parkRefNumIn forKey:@"parkRef"];        
    [data writeToFile: path atomically:YES];
}
+(NSString*) getParkRefNum{
    NSMutableDictionary* savedStock = [[NSMutableDictionary alloc] initWithContentsOfFile:[self getPlistPath]];
    return [savedStock objectForKey:@"parkRef"];
}
+(void) unpark{
    NSArray* keys = [NSArray arrayWithObjects:@"endTime", @"lat", @"lon", @"spot", @"minTime", @"maxTime", @"defaultRate", @"minIncrement", @"description", @"spotNumber", @"code", nil];
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data removeObjectsForKeys:keys];
    [data writeToFile:path atomically:YES];
}
+(void) park:(ParkInstanceObject*)parkInstObjIn Rate:(RateObject*) rateObjIn{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:parkInstObjIn.parkingReferenceNumber forKey:@"parkRef"];
    [data setObject:parkInstObjIn.endTime forKey:@"endTime"];
    [data setObject:rateObjIn.spotNumber forKey:@"spot"];
    [data setObject:rateObjIn.minTime forKey:@"minTime"];
    [data setObject:rateObjIn.rateCents forKey:@"defaultRate"];
    [data setObject:rateObjIn.minuteInterval forKey:@"minIncrement"];
    [data setObject:rateObjIn.description forKey:@"description"];
    [data writeToFile: path atomically:YES];
}
+(RateObject*) getRate{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    return [[RateObject alloc] initWithLat:[data objectForKey:@"lat"] lon:[data objectForKey:@"lon"] spot:[data objectForKey:@"spot"] min:[data objectForKey:@"minTime"] max:[data objectForKey:@"maxTime"] defRate:[data objectForKey:@"defaultRate"] minInc:[data objectForKey:@"minIncrement"] desc:[data objectForKey:@"description"]];
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
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data removeAllObjects];
    [data writeToFile:path atomically:YES];
}
+(void) syncParkingSession:(ParkSync*)sync{
    NSString* path = [self getPlistPath];
    NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile: path];
    [data setObject:sync.defaultRate forKey:@"defaultRate"];        
    [data setObject:sync.desc forKey:@"description"];        
    [data setObject:sync.endTime forKey:@"endTime"];        
    [data setObject:sync.lat forKey:@"lat"];           
    [data setObject:sync.lon forKey:@"lon"];       
    [data setObject:sync.maxTime forKey:@"maxTime"];       
    [data setObject:sync.minTime forKey:@"minTime"];       
    [data setObject:sync.minInc forKey:@"minIncrement"];       
    [data setObject:sync.parkingReferenceNumber forKey:@"parkRef"];       
    [data setObject:sync.spotId forKey:@"spot"];       
    [data setObject:sync.spotNumber forKey:@"spotNumber"];    
    [data writeToFile: path atomically:YES];
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
        [data setObject:[NSNumber numberWithBool:NO] forKey:@"remember"];
    }else{
        [data setObject:[NSNumber numberWithBool:YES] forKey:@"remember"];
    }
    [data writeToFile:path atomically:YES];
}

@end
