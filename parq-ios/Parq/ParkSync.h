//
//  ParkSync.h
//  Parq
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ParkSync : NSObject

@property (nonatomic, retain) NSNumber* defaultRate;
@property (nonatomic, retain) NSNumber* lat;
@property (nonatomic, retain) NSNumber* lon;
@property (nonatomic, retain) NSNumber* endTime;
@property (nonatomic, retain) NSNumber* minTime;
@property (nonatomic, retain) NSNumber* maxTime;
@property (nonatomic, retain) NSNumber* spotId;
@property (nonatomic, retain) NSNumber* minInc;
@property (nonatomic, retain) NSString* desc;
@property (nonatomic, retain) NSString* spotNumber;
@property (nonatomic, retain) NSString* parkingReferenceNumber;
-(ParkSync*) initWithLat:(NSNumber*)lat Lon:(NSNumber*)lon EndTime:(NSNumber*)endTimeIn MinTime:(NSNumber*) minTimeIn MaxTime:(NSNumber*) maxTimeIn DefRate:(NSNumber*)defRateIn SpotId:(NSNumber*) spotIdIn MinInc:(NSNumber*) minIncIn Desc:(NSString*)descIn SpotNum:(NSString*) spotNumIn ParkRefNum:(NSString*) parkRefIn;

@end
