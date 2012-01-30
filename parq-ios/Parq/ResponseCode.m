//
//  ResponseCode.m
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ResponseCode.h"

@implementation ResponseCode


-(ResponseCode*) initWithResp:(NSString*)respCode{
    resp = respCode;
    return self;
}
@synthesize resp;

@end
