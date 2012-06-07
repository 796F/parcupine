//
//  Spot.h
//  Parq
//
//  Created by Michael Xia on 6/5/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Street;

@interface Spot : NSManagedObject

@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * segNumber;
@property (nonatomic, retain) NSNumber * spotId;
@property (nonatomic, retain) NSNumber * spotNumber;
@property (nonatomic, retain) NSNumber * status;
@property (nonatomic, retain) Street *parentStreet;

@end
