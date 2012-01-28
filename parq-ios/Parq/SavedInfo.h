//
//  SavedInfo.h
//  hello
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RateObject.h"
#import "ParkInstanceObject.h"

@interface SavedInfo : NSObject

+(NSString*) getPlistPath;
+(BOOL) ringEnable;
+(BOOL) vibrateEnable;
+(BOOL) autoRefill;
+(BOOL) isParked;
+(BOOL) isLoggedIn;
+(void) setEmail:(NSString*)emailIn;
+(void) setLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn;
+(void) setGeneric:(NSString*) title boolInput:(BOOL)mBoolean;
+(NSNumber*) getEndTime;
+(NSString*) getEmail;
+(NSString*) getCardStub;
+(NSNumber*) getLat;
+(NSNumber*) getLon;
+(NSNumber*) getUid;
+(void) setParkingReferenceNumber:(NSString*) parkRefNumIn;
+(NSString*) getParkRefNum;
+(void) unpark;
+(void) park:(ParkInstanceObject*)parkInstObjIn Rate:(RateObject*) rateObjIn;
+(RateObject*) getRate;
+(void) logIn:(NSNumber*) parkState Email:(NSString*)emailIn UID:(NSNumber*) userId ccStub:(NSString*)ccStubIn;
+(void) logOut;
+(void) syncParkingSession:(ParkSync*)sync;
+(NSString*) getSpotNumber;
+(NSNumber*) getSpotId;
+(void) setSpotNumber:(NSString*)spotNumIn;
+(void) toggleVibrate;
+(void) toggleRing;
+(void) toggleRefill;
+(void) toggleRemember;

@end
