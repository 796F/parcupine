//
//  AbstractRequestObject.m
//  Parq
//
//  Created by Michael Xia on 6/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "AbstractRequestObject.h"

@implementation AbstractRequestObject

@synthesize body;
@synthesize length;
@synthesize contentType;

- (NSUInteger)HTTPHeaderValueForContentLength {     
    return [self.body length]; 
} 

- (NSString *)HTTPHeaderValueForContentType{
    return self.contentType; 
}

- (NSData *)HTTPBody{
    return self.body;
}

@end
