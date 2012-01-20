//
//  ParqSpotViewController.h
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZBarSDK.h"
#import "ServerCalls.h"
                                                       //added protocol for zbar
@interface ParqSpotViewController : UIViewController <ZBarReaderDelegate>

//instance variables for gps.  
@property (nonatomic, retain) NSNumber* userLat;
@property (nonatomic, retain) NSNumber* userLon;

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UITextField *spotNumField;

-(IBAction)parqButton;
-(IBAction)scanButton;

@end
