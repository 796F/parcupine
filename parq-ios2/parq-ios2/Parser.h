//
//  Parser.h
//  Parq
//
//  Created by Michael Xia on 6/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Parser : NSObject

+(NSDictionary*) parseGridResponse:(NSString*) jsonResponse;

@end
