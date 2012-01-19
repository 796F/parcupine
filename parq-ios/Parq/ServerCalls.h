//
//  ServerCalls.h
//  hello
//
//  Created by Michael Xia on 1/13/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserObject.h"
#import "RateObject.h"
#import "ResponseCode.h"
#import "ParkResponse.h"
#import <RestKit/RestKit.h>


@interface ServerCalls : NSObject <RKRequestDelegate>{
    
}

+ (UserObject*) authEmail:(NSString*)emailIn 
                 Password:(NSString*)passwordIn;

+ (BOOL) registerEmail:(NSString*) emailIn 
                       Password:(NSString*) passwordIn 
                     CreditCard:(NSString*) creditCardIn 
                      cscNumber:(NSString*)cscIn
                     HolderName:(NSString*)nameIn 
                 BillingAddress:(NSString*) addressIn
                       ExpMonth:(NSNumber*) expMonthIn
                        ExpYear:(NSNumber*) expYearIn
                        Zipcode:(NSString*) zipcodeIn;

+ (RateObject*) getRateLat:(NSNumber*)latIn 
                       Lon:(NSNumber*)lonIn 
                    spotId:(NSString*)spotIdIn;

+ (RateObject*) getRateLotId:(NSString*)lotIdIn 
                      spotId:(NSString*)spotIdIn;
+ (ParkResponse*) parkUserWithSpotId:(NSNumber*) spotId Duration:(NSNumber*) durationMinutes ChargeAmount:(NSNumber*)chargeAmount PaymentType:(NSNumber*) paymentType;

+(ParkResponse*) refillUserWithSpotId:(NSNumber*)spotId Duration:(NSNumber*) durationMinutes ChargeAmount:(NSNumber*)chargeAmount PaymentType:(NSNumber*)paymentType ParkRefNum:(NSString*) parkingReferenceNumber;
+(BOOL) unparkUserWithSpotId:(NSNumber*)spotId ParkRefNum:(NSString*) parkingReferenceNumberIn;

+(BOOL) editUserEmail:(NSString*)emailIn Password:(NSString*)passwordIn PhoneNumber:(NSString*)phoneIn;

+(BOOL) changeCreditCardWithName:(NSString*)holderName CreditCard:(NSString*)creditCard csc:(NSString*)cscNumber BillingAddress:(NSString*)address Zipcode:(NSString*)zipcode ExpYear:(NSNumber*)expYear ExpMonth:(NSNumber*)expMonth;

+(NSMutableArray* ) findSpotsWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn;
@end
