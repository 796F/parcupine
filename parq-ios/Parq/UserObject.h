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
@property (nonatomic, retain) NSNumber* uid;
@property (nonatomic, retain) NSNumber* parkState;
@property (nonatomic, retain) NSString * creditCardStub;
- (UserObject*) initWithUid:(NSNumber*)uidIn parkState:(NSNumber*)parkStateIn creditCardStub:(NSString*)stub;
@end
