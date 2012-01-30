//
//  Park.h
//  Parq
//
//  Created by Mark Yen on 1/20/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Park : NSManagedObject

@property (nonatomic, retain) NSNumber * endTime;
@property (nonatomic, retain) NSString * parkingReferenceCode;

@end
