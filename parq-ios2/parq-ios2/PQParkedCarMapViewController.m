//
//  PQCarLocationMapViewController.m
//  Parq
//
//  Created by Mark Yen on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkedCarMapViewController.h"
#import "PQParkedCarAnnotation.h"
#import "PQMapViewController.h"
// Span distance in meters
#define SPAN_DISTANCE 100

@interface PQParkedCarMapViewController ()

@end

@implementation PQParkedCarMapViewController
@synthesize parent;
@synthesize map;
@synthesize spotInfo;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)launchMaps
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maps.apple.com/maps?daddr=%@,%@&saddr=%f,%f", spotInfo.latitude, spotInfo.longitude, parent.user_loc.latitude, parent.user_loc.longitude]]];

    [self.navigationController popToRootViewControllerAnimated:NO];
}

#pragma mark - MKMapViewDelegate

- (MKAnnotationView *)mapView:(MKMapView *)theMapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    // if it's the user location, just return nil.
    if ([annotation isKindOfClass:[MKUserLocation class]])
        return nil;

    if ([annotation isKindOfClass:[PQParkedCarAnnotation class]])
    {
        // try to dequeue an existing pin view first
        static NSString* parkedCarAnnotationIdentifier = @"parkedCarAnnotationIdentifier";
        MKPinAnnotationView* pinView = (MKPinAnnotationView *)
        [map dequeueReusableAnnotationViewWithIdentifier:parkedCarAnnotationIdentifier];
        if (!pinView)
        {
            // if an existing pin view was not available, create one
            MKPinAnnotationView* customPinView = [[MKPinAnnotationView alloc]
                                                   initWithAnnotation:annotation reuseIdentifier:parkedCarAnnotationIdentifier];
            customPinView.pinColor = MKPinAnnotationColorPurple;
            customPinView.animatesDrop = YES;
            customPinView.canShowCallout = YES;

            UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            [rightButton addTarget:self
                            action:@selector(launchMaps)
                  forControlEvents:UIControlEventTouchUpInside];
            customPinView.rightCalloutAccessoryView = rightButton;

            return customPinView;
        }
        else
        {
            pinView.annotation = annotation;
        }
        return pinView;
    }

    return nil;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    CLLocationCoordinate2D point = CLLocationCoordinate2DMake(spotInfo.latitude.doubleValue, spotInfo.longitude.doubleValue);

    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, SPAN_DISTANCE, SPAN_DISTANCE);
    [map setRegion:[map regionThatFits:viewRegion] animated:NO];

	PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:point addressDictionary:nil];
	annotation.title = spotInfo.spotNumber.stringValue;

    [self.map addAnnotation:annotation];
    [self.map selectAnnotation:annotation animated:YES];
}

- (void)viewDidUnload
{
    [self setMap:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
