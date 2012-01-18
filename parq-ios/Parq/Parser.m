//
//  Parser.m
//  hello
//
//  Created by Michael Xia on 1/12/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "Parser.h"
#import "UserObject.h"
#import "JSONKit.h"
/* obj-c uses its annoying typing system replacing primitives.  
    null = NSNull
    bool = NSNumber
    int/long = NSNumber
    String = NSString
    Array = NSArray
    Object = NSDictionary   */

@implementation Parser

+ (UserObject*) parseUserObjectString:(NSString*)jsonString{
    NSDictionary* results = [jsonString objectFromJSONString];
    NSString* ccStub = [results objectForKey:@"creditCardStub"];
    NSNumber* parkState = [results objectForKey:@"parkState"];
    NSNumber* uid = [results objectForKey:@"uid"];
    
    if(uid.longValue > 0) return [[UserObject alloc] initWithUid:uid parkState:parkState creditCardStub:ccStub];
    else return nil;
    
}

@end
