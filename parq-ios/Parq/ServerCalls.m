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
