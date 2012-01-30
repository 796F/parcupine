//
//  ParkResponse.h
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ParkResponse : NSObject{
    NSString* resp;
    NSNumber* endTime;
    NSString* parkingReferenceNumber;
}
@property (nonatomic, retain) NSString* resp;
@property (nonatomic, retain) NSString* parkingReferenceNumber;
@property (nonatomic, retain) NSNumber* endTime;
-(ParkResponse*) initWithResp:(NSString*)responseCode EndTime:(NSNumber*)endTimeIn ParkRefNum:(NSString*)parkingReferenceNumberIn;

@end
