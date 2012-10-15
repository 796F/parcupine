//
//  Parser.h
//  Parq
//
//  Created by Michael Xia on 6/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "User.h"
@class SpotInfo;
@interface Parser : NSObject

+(NSDictionary*) parseGridResponse:(NSString*) jsonResponse;
+(NSDictionary*) parseUserObjectString:(NSString*) jsonResponse;
+(BOOL) parseRegisterResponseString:(NSString*) resp;
+(BOOL) parseUpdateUserResponse:(NSString*) jsonResponse;
+(BOOL) parseUnparkResponse:(NSString*) jsonResponse;
+(NSDictionary*) parseParkResponse:(NSString*) jsonResponse;
+(SpotInfo*) parseSpotInfo:(NSString*) jsonResponse;
+(NSDictionary*) parseUpdateSpotsResponse:(NSString*) jsonResponse;
@end
