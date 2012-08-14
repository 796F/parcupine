//
//  User.h
//  Parq
//
//  Created by Michael Xia on 8/14/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface User : NSManagedObject

@property (nonatomic, retain) NSString * address;
@property (nonatomic, retain) NSNumber * balance;
@property (nonatomic, retain) NSString * city;
@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSString * license;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * payment;
@property (nonatomic, retain) NSNumber * uid;
@property (nonatomic, retain) NSString * password;

@end
