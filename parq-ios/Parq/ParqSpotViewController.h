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
#import <MapKit/MapKit.h>
#import "ParqSpotViewController.h"
//added protocol for zbar
@interface ParqSpotViewController : UIViewController <ZBarReaderDelegate, CLLocationManagerDelegate>

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UITextField *spotNumField;
@property (strong, nonatomic) RateObject *rateObj;

-(IBAction)parqButton;
-(IBAction)scanButton;

@end
