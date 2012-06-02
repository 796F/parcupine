//
//  Street.h
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Spot;

@interface Street : NSManagedObject

@property (nonatomic, retain) NSNumber * streetId;
@property (nonatomic, retain) id waypoints;
@property (nonatomic, retain) Spot *spot;

@end
