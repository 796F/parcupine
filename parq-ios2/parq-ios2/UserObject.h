//
//  UserObject.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UserObject : NSObject{
    
}
@property (weak, nonatomic) NSNumber* uid; //used to identify a user
@property (weak, nonatomic) NSNumber* city; //used to find reference lat/lon

@property (weak, nonatomic) NSString* name;
@property (weak, nonatomic) NSString* address;
@property (weak, nonatomic) NSString* plate;
@property (weak, nonatomic) NSNumber* ssn;
@property (weak, nonatomic) NSNumber* balance;

@property (weak, nonatomic) NSNumber* soundEnabled;
@property (weak, nonatomic) NSNumber* vibrateEnabled;

-(UserObject*) initWithUid:(NSNumber*) uidIn 
                      City:(NSNumber*) cityIn 
                       SSN:(NSNumber*) ssnIn 
                   Balance:(NSNumber*)balanceIn 
                     Sound:(NSNumber*) soundIn 
                   Vibrate:(NSNumber*)vibIn 
                      Name:(NSString*)nameIn 
                   Address:(NSString*) addr 
                     Plate:(NSString*)plateIn;

@end
