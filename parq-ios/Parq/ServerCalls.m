//
//  ServerCalls.m
//  hello
//
//  Created by Michael Xia on 1/13/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "ServerCalls.h"
#import "Parser.h"
#import "UserObject.h"
#import <RestKit/RestKit.h>

@implementation ServerCalls

//[NSNumber numberWithInteger:919]; cannot put normal int into NSArray.

+ (UserObject*) authEmail:(NSString*)emailIn Password:(NSString*)passwordIn{
    
    NSString* endpoint = @"http://75.101.132.219:8080/parkservice.auth/";
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:emailIn, passwordIn, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    
    RKClient* client = [RKClient clientWithBaseURL:@"http://75.101.132.219:80"];
    
    
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.auth" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseUserObjectString:[result bodyAsString]];
}

-(void) request:(RKRequest*) request didLoadResponse:(RKResponse*) response {
    NSLog(@"HEELELELELELEO");
}

-(void) request:(RKRequest*) request didFailLoadWithError:(NSError *)error {
    RKLogInfo(@"stringgggg %@", [error localizedDescription]   );
}

+ (RateObject*) getRateLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn spotId:(NSString*)spotIdIn{
    NSString* endpoint = @"http://75.101.132.219:8080/parkservice.rate/gps";
    NSNumber* uid = [NSNumber numberWithLong:13];
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    
    
}
+ (RateObject*) getRateLotId:(NSString*)LotIdIn spotId:(NSString*)spotIdIn{
    NSString* endpoint = @"http://75.101.132.219:8080/parkservice.rate/qrcode";
    
}

@end
