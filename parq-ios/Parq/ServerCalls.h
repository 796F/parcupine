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


@interface ServerCalls : NSObject

+ (UserObject*) authEmail:(NSString*)emailIn Password:(NSString*)passwordIn;
+ (RateObject*) getRateLat:(NSNumber*)latIn Lon:(NSNumber*)lonIn spotId:(NSString*)spotIdIn;
+ (RateObject*) getRateLotId:(NSString*)lotIdIn spotId:(NSString*)spotIdIn;

@end
