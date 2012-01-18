//
//  ResponseCode.h
//  Parq
//
//  Created by Michael Xia on 1/18/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ResponseCode : NSObject{
    NSString* resp;
}
@property (nonatomic, retain) NSString* resp;
-(ResponseCode*) initWithResp:(NSString*)respCode;
@end
