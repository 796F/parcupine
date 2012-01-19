//
//  Parser.h
//  hello
//
//  Created by Michael Xia on 1/12/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserObject.h"
#import "ResponseCode.h"
#import "RateObject.h"
#import "ParkResponse.h"
@interface Parser : NSObject

+ (UserObject*) parseUserObjectString:(NSString*)jsonString;
+ (BOOL) parseResponseCode:(NSString*) jsonString;
+ (RateObject *) parseRateObject:(NSString*) jsonString;
+ (ParkResponse*) parseParkingResponse:(NSString*) jsonString;

@end
