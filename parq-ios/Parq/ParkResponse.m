//
//  ParkResponse.m
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParkResponse.h"

@implementation ParkResponse

-(ParkResponse*) initWithResp:(NSString*)responseCode EndTime:(NSNumber*)endTimeIn ParkRefNum:(NSString*)parkingReferenceNumberIn{
    resp = responseCode;
    endTime = endTimeIn;
    parkingReferenceNumber = parkingReferenceNumberIn;
    return self;
}
@synthesize resp;
@synthesize endTime;
@synthesize parkingReferenceNumber;
@end
