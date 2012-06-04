//
//  PQCarLocationMapViewController.m
//  Parq
//
//  Created by Mark Yen on 5/31/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkedCarMapViewController.h"
#import "PQParkedCarAnnotation.h"

// Span distance in meters
#define SPAN_DISTANCE 100

@interface PQParkedCarMapViewController ()

@end

@implementation PQParkedCarMapViewController
@synthesize map;
@synthesize parkedCarCoordinate;

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
    NSString *title = @"title";
    float latitude = self.parkedCarCoordinate.latitude;
    float longitude = self.parkedCarCoordinate.longitude;
    int zoom = 26;
    NSString *stringURL = [NSString stringWithFormat:@"http://maps.google.com/maps?q=%@@%1.6f,%1.6f&z=%d", title, latitude, longitude, zoom];
    NSURL *url = [NSURL URLWithString:stringURL];
    [[UIApplication sharedApplication] openURL:url];
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

    // Hardcode the location for now
    CLLocationCoordinate2D point = {42.365077, -71.110838};
    self.parkedCarCoordinate = point;

    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(self.parkedCarCoordinate, SPAN_DISTANCE, SPAN_DISTANCE);
    [map setRegion:[map regionThatFits:viewRegion] animated:NO];

	PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:self.parkedCarCoordinate addressDictionary:nil];
	annotation.title = @"1106";

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
