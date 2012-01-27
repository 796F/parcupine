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

+ (NSDictionary*) getUserInfo{
    NSArray* authKeys = [NSArray arrayWithObjects:@"email" ,@"password", nil];
    //build userInfo from saved information.  
    NSString* email = @"miguel@parqme.com";
    NSString* password = @"a";
    NSArray* authValues = [NSArray arrayWithObjects:email,password , nil];
    return [NSDictionary dictionaryWithObjects:authValues forKeys:authKeys];
}

+(NSNumber*) getStoredUid{
    //load from saved information.  
    return [NSNumber numberWithLong:1];
}
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"lat", @"lon",@"spot",@"userInfo", nil];
    NSNumber* uid = [ServerCalls getStoredUid];
    //we're passed in the rest of the info    
    NSArray* values = [NSArray arrayWithObjects:uid, latIn, lonIn,spotIdIn, userInfo, nil];
//    NSArray* values = [NSArray arrayWithObjects:uid, [NSNumber numberWithDouble:42.358], [NSNumber numberWithDouble:-71.097983],spotIdIn, userInfo, nil];
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"lot",@"spot",@"userInfo", nil];
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


+(ParkInstanceObject*) parkUserWithRateObj:(RateObject*)rateObjIn duration:(NSNumber*)durationIn{
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSArray* keys = [NSArray arrayWithObjects:@"uid",@"spotId", @"durationMinutes", @"chargeAmount", @"paymentType", @"userInfo",   nil];
    NSNumber* uid = [ServerCalls getStoredUid];
    NSNumber* mZero = [NSNumber numberWithInt:0];
    NSNumber* chargeAmount = [NSNumber numberWithInt:(durationIn.intValue/rateObjIn.minuteInterval.intValue)*rateObjIn.rateCents.intValue];
    //we're passed in the rest of the info    
    NSArray* values = [NSArray arrayWithObjects:uid, rateObjIn.spotNumber, durationIn, chargeAmount, mZero, userInfo, nil];
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

+ (ParkResponse*) parkUserWithSpotId:(NSNumber*) spotId Duration:(NSNumber*) durationMinutes ChargeAmount:(NSNumber*)chargeAmount PaymentType:(NSNumber*) paymentType{
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSArray* keys = [NSArray arrayWithObjects:@"uid",@"spotId", @"durationMinutes", @"chargeAmount", @"paymentType", @"userInfo",   nil];
   NSNumber* uid = [ServerCalls getStoredUid];
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
    
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
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
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
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

+(NSMutableArray* ) findSpotsWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn{
    NSDictionary* userInfo = [ServerCalls getUserInfo];
    NSNumber* uid = [ServerCalls getStoredUid];
    NSArray* keys = [NSArray arrayWithObjects:@"uid", @"lat", @"lon", @"userInfo", nil];
    NSArray* values = [NSArray arrayWithObjects:uid, latIn, lonIn, userInfo, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:values forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.maps/find" delegate:self];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* result = [request sendSynchronously];
    return [Parser parseLocationList:[result bodyAsString]];
}

@end
