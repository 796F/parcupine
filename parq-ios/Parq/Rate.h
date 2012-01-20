//
//  Rate.h
//  Parq
//
//  Created by Mark Yen on 1/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Rate : NSManagedObject

@property (nonatomic, retain) NSString * location;
@property (nonatomic, retain) NSNumber * lat;
@property (nonatomic, retain) NSNumber * lon;
@property (nonatomic, retain) NSNumber * spotId;
@property (nonatomic, retain) NSNumber * minTime;
@property (nonatomic, retain) NSNumber * maxTime;
@property (nonatomic, retain) NSNumber * defaultRate;
@property (nonatomic, retain) NSNumber * minIncrement;

@end
