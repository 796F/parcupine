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
    
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:emailIn, passwordIn, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.auth" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseUserObjectString:[result bodyAsString]];
}

+ (BOOL) registerEmail:(NSString*) emailIn 
              Password:(NSString*) passwordIn 
            CreditCard:(NSString*) creditCardIn 
             cscNumber:(NSString*)cscIn
            HolderName:(NSString*)nameIn 
        BillingAddress:(NSString*) addressIn
              ExpMonth:(NSNumber*) expMonthIn
               ExpYear:(NSNumber*) expYearIn
               Zipcode:(NSString*) zipcodeIn{
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password",@"creditCard",@"cscNumber",@"address",@"holderName",@"expMonth", @"expYear", @"zipcode", nil];
    NSArray* values = [NSArray arrayWithObjects:emailIn, passwordIn, creditCardIn, cscIn, addressIn,nameIn,expMonthIn, expYearIn, zipcodeIn, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.user/register" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseResponseCode:[result bodyAsString]];
}
+ (RateObject*) getRateLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn spotId:(NSString*)spotIdIn{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"lat", @"lon",@"spot",@"userInfo", nil];
    //get uid from memory, saved on login.  
    NSNumber* uid = [NSNumber numberWithLong:13];
    //we're passed in the rest of the info    
    NSArray* values = [NSArray arrayWithObjects:uid, latIn, lonIn,spotIdIn, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.rate/gps" delegate:self];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseRateObject:[result bodyAsString]];
    
}
+ (RateObject*) getRateLotId:(NSString*)LotIdIn spotId:(NSString*)spotIdIn{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"lot",@"spot",@"userInfo", nil];
    //get uid from memory, saved on login.  
    NSNumber* uid = [NSNumber numberWithLong:13];
    //we're passed in the rest of the info    
    NSArray* values = [NSArray arrayWithObjects:uid, LotIdIn ,spotIdIn, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.rate/qrcode" delegate:self];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseRateObject:[result bodyAsString]];
}

@end
