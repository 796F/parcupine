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
    
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.rate/qrcode" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseRateObject:[result bodyAsString]];
}

+ (ParkResponse*) parkUserWithSpotId:(NSNumber*) spotId Duration:(NSNumber*) durationMinutes ChargeAmount:(NSNumber*)chargeAmount PaymentType:(NSNumber*) paymentType{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSArray* keys = [NSArray arrayWithObjects:@"uid",@"spotId", @"durationMinutes", @"chargeAmount", @"paymentType", @"userInfo",   nil];
    //get uid from memory, saved on login.  
    NSNumber* uid = [NSNumber numberWithLong:13];
    //we're passed in the rest of the info    
    NSArray* values = [NSArray arrayWithObjects:uid, spotId, durationMinutes, chargeAmount,paymentType, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/park" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseParkingResponse:[result bodyAsString]];
}

+(ParkResponse*) refillUserWithSpotId:(NSNumber*)spotId Duration:(NSNumber*) durationMinutes ChargeAmount:(NSNumber*)chargeAmount PaymentType:(NSNumber*)paymentType ParkRefNum:(NSString*) parkingReferenceNumber{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSNumber* uid = [NSNumber numberWithLong:13];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"spotId", @"durationMinutes", @"chargeAmount", @"paymentType", @"parkingReferenceNumber", @"userInfo", nil];
    NSArray* values = [NSArray arrayWithObjects:uid, spotId, durationMinutes, chargeAmount, paymentType, parkingReferenceNumber, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/refill" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseParkingResponse:[result bodyAsString]];
}


+(BOOL) unparkUserWithSpotId:(NSNumber*)spotId ParkRefNum:(NSString*) parkingReferenceNumberIn{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSNumber* uid = [NSNumber numberWithLong:13];
    
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"spotId", @"parkingReferenceNumber", @"userInfo", nil];
    NSArray* values = [NSArray arrayWithObjects:uid, spotId, parkingReferenceNumberIn, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/unpark" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseResponseCode:[result bodyAsString]];
}


+(BOOL) editUserEmail:(NSString*)emailIn Password:(NSString*)passwordIn PhoneNumber:(NSString*)phoneIn{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSNumber* uid = [NSNumber numberWithLong:13];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"email", @"password", @"phone", @"userInfo", nil];
    NSArray* values = [NSArray arrayWithObjects:uid, emailIn,passwordIn, phoneIn, userInfo, nil];
    
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.user/update" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseResponseCode:[result bodyAsString]];
}

+(BOOL) changeCreditCardWithName:(NSString*)holderName CreditCard:(NSString*)creditCard csc:(NSString*)cscNumber BillingAddress:(NSString*)address Zipcode:(NSString*)zipcode ExpYear:(NSNumber*)expYear ExpMonth:(NSNumber*)expMonth{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from memory, saved on login.  
    NSArray* authValues = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* userInfo = [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
    NSNumber* uid = [NSNumber numberWithLong:13];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"holderName", @"creditCard", @"cscNumber",@"address",@"zipcode",@"expMonth", @"expYear", @"userInfo", nil];
    NSArray* values = [NSArray arrayWithObjects:uid, holderName, creditCard, cscNumber, address, zipcode, expMonth, expYear, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.user/changeCC" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    NSLog(@"\n\n%@\n\n", [result bodyAsString] );
    return [Parser parseResponseCode:[result bodyAsString]];

}

@end
