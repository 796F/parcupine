//
//  ParkInstanceObject.m
//  Parq
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParkInstanceObject.h"

@implementation ParkInstanceObject

@synthesize parkingReferenceNumber;
@synthesize endTime;

-(void) setSync:(ParkSync *)syncIn{
    sync = syncIn;
}
-(ParkInstanceObject*) initWithNum:(NSString*)parkRefNum End:(NSNumber*)endTimeIn Sync:(ParkSync*)syncObj{
    parkingReferenceNumber = parkRefNum;
    endTime = endTimeIn;
    sync = syncObj;
    return self;
}
@end
