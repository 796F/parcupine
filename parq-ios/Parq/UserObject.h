//
//  UserObject.h
//  hello
//
//  Created by Michael Xia on 1/12/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface UserObject : NSObject{
    NSNumber* uid;
    NSNumber* parkState;
    NSString * creditCardStub;
}
- (id) initWithUid:(NSNumber*)uid parkState:(NSNumber*)parkState creditCardStub:(NSString*)stub;
- (NSNumber*) uid;
- (NSNumber*) parkState;
- (NSString*) creditCardStub;
- (void) setCCStub:(NSString*)input;
@end
