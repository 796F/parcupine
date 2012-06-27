//
//  AbstractRequestObject.h
//  Parq
//
//  Created by Michael Xia on 6/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@interface AbstractRequestObject : NSObject <RKRequestSerializable>{
    NSData *body; 
    NSUInteger length; 
    NSString *contentType; 
}

@property(nonatomic, retain) NSData *body; 
@property(nonatomic, assign) NSUInteger length; 
@property(nonatomic, retain) NSString *contentType; 

@end
