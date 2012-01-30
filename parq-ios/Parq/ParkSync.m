//
//  ParkSync.m
//  Parq
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParkSync.h"

@implementation ParkSync

@synthesize defaultRate;
@synthesize  desc;
@synthesize endTime;
@synthesize lat;
@synthesize lon;
@synthesize maxTime;
@synthesize minInc;
@synthesize minTime;
@synthesize parkingReferenceNumber;
@synthesize spotId;
@synthesize spotNumber;

-(ParkSync*) initWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn EndTime:(NSNumber*)endTimeIn MinTime:(NSNumber*) minTimeIn MaxTime:(NSNumber*) maxTimeIn DefRate:(NSNumber*)defRateIn SpotId:(NSNumber*) spotIdIn MinInc:(NSNumber*) minIncIn Desc:(NSString*)descIn SpotNum:(NSString*) spotNumIn ParkRefNum:(NSString*) parkRefIn{
    defaultRate = defRateIn;
    desc = descIn;
    endTime = endTimeIn;
    lat = latIn;
    lon = lonIn;
    maxTime = maxTimeIn;
    minInc = minIncIn;
    minTime = minTimeIn;
    parkingReferenceNumber = parkRefIn;
    spotId = spotIdIn;
    spotNumber = spotNumIn;
    return self;
}
@end
