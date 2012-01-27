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

//instance variables for gps.  
@property double userLat;
@property double userLon;
@property (nonatomic, retain) CLLocationManager* locationManager;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UITextField *spotNumField;
@property BOOL goodLocation;
@property (strong, nonatomic) RateObject *rateObj;

-(IBAction)parqButton;
-(IBAction)scanButton;

@end
