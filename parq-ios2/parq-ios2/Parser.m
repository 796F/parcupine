//
//  Parser.m
//  Parq
//
//  Created by Michael Xia on 6/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "Parser.h"
#import <RestKit/RestKit.h>
#import "JSONKit.h"
#import "SpotInfo.h"

@implementation Parser

+(NSDictionary*) parseGridResponse:(NSString*) jsonResponse{
    return nil;
}

+(BOOL) parseRegisterResponseString:(NSString*) resp{
    NSDictionary* results = [resp objectFromJSONString];
    if([[results objectForKey:@"resp"] isEqualToString:@"OK"]){
        return YES;
    }else{
        return NO;
    }
}

+(NSDictionary*) parseUserObjectString:(NSString *)jsonResponse{
    NSLog(@"RESPONSE >>> %@", jsonResponse);
    NSDictionary* results = [jsonResponse objectFromJSONString];
    if([[results objectForKey:@"autherized"] boolValue]){
        return results;
    }else{
        return nil;
    }
}

//rate response
+(SpotInfo*) parseSpotInfo:(NSString*) jsonResponse{
    NSDictionary* results = [jsonResponse objectFromJSONString];
    SpotInfo* spot = [[SpotInfo alloc] init];
    [spot setLatitude:[results objectForKey:@"lat"]];
    [spot setLongitude:[results objectForKey:@"lon"]];
    [spot setStreetName:[results objectForKey:@"location"]];
    [spot setSpotId:[results objectForKey:@"spotId"]];
    [spot setSpotNumber:[results objectForKey:@"spotNumber"]];
    [spot setMinTime:[results objectForKey:@"minTime"]];
    [spot setMaxTime:[results objectForKey:@"maxTime"]];
    [spot setMinuteInterval:[results objectForKey:@"minIncrement"]];
    [spot setRateCents:[results objectForKey:@"defaultRate"]];
    return spot;
}

+(NSDictionary*) parseUpdateSpotsResponse:(NSString*) jsonResponse{
    NSMutableDictionary* updateTheseSpots = [[NSMutableDictionary alloc] init];

    NSDictionary * deserializedData = [jsonResponse objectFromJSONString];
    NSNumberFormatter* f = [[NSNumberFormatter alloc] init];
    NSDictionary* test = [[deserializedData objectForKey:@"getUpdatedSpotLevelInfoResponse"] objectForKey:@"parkingSpace"];
    for (NSDictionary * dataDict in test) {
        NSString * spaceId = [dataDict objectForKey:@"spaceId"];
        NSNumber* status;
        if([[dataDict objectForKey:@"status"] isEqualToString:@"available"]){
            status = [NSNumber numberWithInt:1];
        }else{
            status = [NSNumber numberWithInt:0];
        }
        [updateTheseSpots setObject:status forKey:[f numberFromString:spaceId]];

    }
    NSLog(@"%@", updateTheseSpots.description);
    return updateTheseSpots;
}

//park response
+(NSDictionary*) parseParkResponse:(NSString*) jsonResponse{
    NSLog(@"RESPONSE >>>> %@\n", jsonResponse);
    NSDictionary* result = [jsonResponse objectFromJSONString];
    if([[result objectForKey:@"resp"] isEqualToString:@"OK"]){
        return result;
    }else{
        return nil;
    }
}

//unpark response
+(BOOL) parseUnparkResponse:(NSString*) jsonResponse{
    if([[[jsonResponse objectFromJSONString] objectForKey:@"resp"] isEqualToString:@"OK"]){
        return YES;
    }else{
        return NO;
    }
}
//edit setting response
+(BOOL) parseUpdateUserResponse:(NSString*) jsonResponse{
    if([[[jsonResponse objectFromJSONString] objectForKey:@"resp"] isEqualToString:@"OK"]){
        return YES;
    }else{
        return NO;
    }
}


@end
