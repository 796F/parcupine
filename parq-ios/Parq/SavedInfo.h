//
//  SavedInfo.h
//  hello
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RateObject.h"
#import "ParkResponse.h"
#import "ParkSync.h"

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
+(NSNumber*) endTime;
+(NSString*) getEmail;
+(NSString*) getCardStub;
+(NSNumber*) getLat;
+(NSNumber*) getLon;
+(NSNumber*) getUid;
+(void) setParkingReferenceNumber:(NSString*) parkRefNumIn;
+(NSString*) getParkRefNum;
+(void) unpark;
+(void) park:(ParkResponse*)parkResponseIn rate:(RateObject*) rateObjIn spotNumber:(NSNumber*) spotNumberIn;
+(RateObject*) rate;
+(void) logIn:(NSNumber*) parkState Email:(NSString*)emailIn UID:(NSNumber*) userId ccStub:(NSString*)ccStubIn;
+(void) logOut;
+(void) syncParkingSession:(ParkSync*)sync;
+(NSNumber*) spotNumber;
+(NSNumber*) getSpotId;
+(void) setSpotNumber:(NSString*)spotNumIn;
+(void) toggleVibrate;
+(void) toggleRing;
+(void) toggleRefill;
+(void) toggleRemember;

@end
