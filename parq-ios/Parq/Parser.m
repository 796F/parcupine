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
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* ccStub = [results objectForKey:@"creditCardStub"];
    NSNumber* parkState = [results objectForKey:@"parkState"];
    NSNumber* uid = [results objectForKey:@"uid"];
    
    if(uid.longValue > 0) return [[UserObject alloc] initWithUid:uid parkState:parkState creditCardStub:ccStub];
    else return nil;
    
}
+ (BOOL) parseResponseCode:(NSString*) jsonString{
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* responseCode = [results objectForKey:@"resp"];
    return [responseCode isEqualToString:@"OK"];
}
+ (RateObject *) parseRateObject:(NSString*) jsonString{
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
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* responseCode = [results objectForKey:@"resp"];
    return [[ParkResponse alloc] initWithResp:responseCode 
                                      EndTime:[results objectForKey:@"endTime"] 
                                   ParkRefNum:[results objectForKey:@"parkingReferenceNumber"]];
}

+ (NSMutableArray*) parseLocationList:(NSString*)jsonString{    
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
