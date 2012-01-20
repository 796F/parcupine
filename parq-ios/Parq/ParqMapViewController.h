//
//  ParqMapViewController.h
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface ParqMapViewController : UIViewController <MKMapViewDelegate>{
    IBOutlet UITextField *addressField;
    IBOutlet MKMapView *mapView;
}

- (IBAction)goAddress;
-(IBAction)goUser;

@end
