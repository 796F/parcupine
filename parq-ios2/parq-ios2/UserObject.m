//
//  UserObject.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "UserObject.h"

@implementation UserObject

@synthesize city;
@synthesize uid;

@synthesize name;
@synthesize address;
@synthesize plate;
@synthesize ssn;
@synthesize balance;

@synthesize soundEnabled;
@synthesize vibrateEnabled;

-(UserObject*) initWithUid:(NSNumber*) uidIn 
                      City:(NSNumber*) cityIn 
                       SSN:(NSNumber*) ssnIn 
                   Balance:(NSNumber*)balanceIn 
                     Sound:(NSNumber*) soundIn 
                   Vibrate:(NSNumber*)vibIn 
                      Name:(NSString*)nameIn 
                   Address:(NSString*) addr 
                     Plate:(NSString*)plateIn{
    self.city = cityIn;
    self.uid =uidIn;
    self.name = nameIn;
    self.address = addr;
    self.plate = plateIn;
    self.ssn = ssnIn;
    self.balance = balanceIn;
    self.soundEnabled = soundIn;
    self.vibrateEnabled = vibIn;
    return self;
}


@end
