//
//  SingleSpot.h
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SingleSpot : NSObject{
    NSNumber* lat;
    NSNumber* lon;
    NSString* description;
}
@property (nonatomic, retain) NSNumber* lat;
@property (nonatomic, retain) NSNumber* lon;
@property (nonatomic, retain) NSString* description;

- (SingleSpot*) initWithLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn Desc:(NSString*) descriptionIn;
@end
