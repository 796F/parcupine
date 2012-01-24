//
//  Parser.m
//  hello
//
//  Created by Michael Xia on 1/12/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//
/* obj-c uses its annoying typing system replacing primitives.  
 null = NSNull
 bool = NSNumber
 int/long = NSNumber
 String = NSString
 Array = NSArray
 Object = NSDictionary   */

#import "Parser.h"
#import "JSONKit.h"
#import "SingleSpot.h"


@implementation Parser

+ (UserObject*) parseUserObjectString:(NSString*)jsonString{
    NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* ccStub = [results objectForKey:@"creditCardStub"];
    NSNumber* parkState = [results objectForKey:@"parkState"];
    NSNumber* uid = [results objectForKey:@"uid"];
    
    if(uid.longValue > 0) return [[UserObject alloc] initWithUid:uid parkState:parkState creditCardStub:ccStub];
    else return nil;
    
}
+ (BOOL) parseResponseCode:(NSString*) jsonString{
        NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* responseCode = [results objectForKey:@"resp"];
    return [responseCode isEqualToString:@"OK"];
}
+ (RateObject *) parseRateObject:(NSString*) jsonString{
        NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* responseCode = [results objectForKey:@"resp"];
    if([responseCode isEqualToString:@"OK"]){
        NSDictionary* rateObj = [results objectForKey:@"rateObject"];
        return [[RateObject alloc] initWithLat:[rateObj objectForKey:@"lat"] 
                                           Lon:[rateObj objectForKey:@"lon"] 
                                          Spot:[rateObj objectForKey:@"spotId"] 
                                           Min:[rateObj objectForKey:@"minTime"] 
                                           Max:[rateObj objectForKey:@"maxTime"] 
                                       DefRate:[rateObj objectForKey:@"defaultRate"] 
                                        minInc:[rateObj objectForKey:@"minIncrement"] 
                                          desc:[rateObj objectForKey:@"location"]];
    }else{
        return nil;
    }
}

+ (ParkResponse*) parseParkingResponse:(NSString*) jsonString{
        NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* responseCode = [results objectForKey:@"resp"];
    return [[ParkResponse alloc] initWithResp:responseCode 
                                      EndTime:[results objectForKey:@"endTime"] 
                                   ParkRefNum:[results objectForKey:@"parkingReferenceNumber"]];
}

+(ParkSync*) parseSync:(NSString*)jsonString{
    NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];
    return [[ParkSync alloc] initWithLat:[results objectForKey:@"lat"] Lon:[results objectForKey:@"lon"] EndTime:[results objectForKey:@"endTime"] MinTime:[results objectForKey:@"minTime"] MaxTime:[results objectForKey:@"maxTime"] DefRate:[results objectForKey:@"defaultRate"] SpotId:[results objectForKey:@"spot"] MinInc:[results objectForKey:@"minIncrement"] Desc:[results objectForKey:@"description"] SpotNum:[results objectForKey:@"spotNumber"] ParkRefNum:[results objectForKey:@"parkingReferenceNumber"]];
}
+(ParkInstanceObject*) parseParkingInstance:(NSString*)jsonString{
    NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary* results = [jsonString objectFromJSONString];    
    ParkSync* parsedSyncObject = [Parser parseSync:[results objectForKey:@"sync"]];
    return [[ParkInstanceObject alloc] initWithNum:[results objectForKey:@"parkingReferenceNumber"] End:[results objectForKey:@"endTime"] Sync:parsedSyncObject];
    
}

+ (NSMutableArray*) parseLocationList:(NSString*)jsonString{   
        NSLog(@"RESPONSE >>> %@", jsonString);
    NSDictionary * deserializedData = [jsonString objectFromJSONString];
    NSMutableArray* arrayOfSpots = [[NSMutableArray alloc] initWithCapacity:[deserializedData count]];
    for (NSDictionary * dataDict in deserializedData) {
        NSString * spot = [dataDict objectForKey:@"spot"];
        NSNumber* lat = [dataDict objectForKey:@"lat"];
        NSNumber* lon = [dataDict objectForKey:@"lon"];
        [arrayOfSpots addObject:[[SingleSpot alloc] initWithLat:lat Lon:lon Desc:spot]];
    }
    return arrayOfSpots;
}


@end
