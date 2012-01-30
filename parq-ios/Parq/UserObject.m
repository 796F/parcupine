//
//  UserObject.m
//  hello
//
//  Created by Michael Xia on 1/12/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "UserObject.h"

@implementation UserObject

- (UserObject *) initWithUid:(NSNumber*)uidIn 
         parkState:(NSNumber*)parkStateIn 
    creditCardStub:(NSString*)stub {
    parkState=parkStateIn;
    uid = uidIn;
    creditCardStub = stub;
    return self;
}

@synthesize parkState;
@synthesize uid;
@synthesize creditCardStub;

@end
