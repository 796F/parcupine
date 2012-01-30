//
//  ParkInstanceObject.h
//  Parq
//
//  Created by Michael Xia on 1/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ParkSync.h"

@interface ParkInstanceObject : NSObject{
    ParkSync* sync;
}

@property (nonatomic, retain) NSString* parkingReferenceNumber;
@property (nonatomic, retain) NSNumber* endTime;

-(ParkInstanceObject*) initWithNum:(NSString*)parkRefNum End:(NSNumber*)endTimeIn Sync:(ParkSync*)syncObj;
@end
