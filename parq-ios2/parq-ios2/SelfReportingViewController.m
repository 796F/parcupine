//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingViewController.h"

#import "PQParkingViewController.h"

@implementation SelfReportingViewController
@synthesize mapView;
@synthesize leftButton;
@synthesize rightButton;
@synthesize networkLayer;
@synthesize parent;

- (MKAnnotationView *)mapView:(MKMapView *)theMapView viewForAnnotation:(id <MKAnnotation>)annotation
{    
    // if it's the user location, just return nil.
    if ([annotation isKindOfClass:[MKUserLocation class]])
        return nil;
    
    if ([annotation isKindOfClass:[PQParkedCarAnnotation class]])
    {
        // try to dequeue an existing pin view first
        static NSString* parkedCarAnnotationIdentifier = @"parkedCarAnnotationIdentifier";
        // if an existing pin view was not available, create one
        MKAnnotationView* customPinView = [[MKAnnotationView alloc]
                                              initWithAnnotation:annotation reuseIdentifier:parkedCarAnnotationIdentifier];
        customPinView.image = [UIImage imageNamed:@"spot_occupied.png"];
        customPinView.tag = 1;
        customPinView.canShowCallout = YES;
        return customPinView;
    }
    
    return nil;
}

-(void) handleSingleTap:(UIGestureRecognizer*) gestureRecognizer{
    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    [mapView deselectAnnotation:[mapView.selectedAnnotations objectAtIndex:0] animated:YES];
    CGPoint touchPoint = [gestureRecognizer locationInView:self.mapView];
    CLLocationCoordinate2D coord = [self.mapView convertPoint:touchPoint toCoordinateFromView:self.mapView];
    
    double dist = 0.4; //only works for * 10000
    PQParkedCarAnnotation* tappedAnno;
    for(PQParkedCarAnnotation* anno in mapView.annotations){
        //multiplied since it's too small.  
        double dLat = (coord.latitude - anno.coordinate.latitude) * 10000;
        double dLon = (coord.longitude - anno.coordinate.longitude) * 10000;
        double thisDist = dLat*dLat + dLon*dLon;
        if (thisDist < dist) {
            dist = thisDist;
            tappedAnno = anno;
        }
    }
    //after loop, we would have selected closest annotation.  
    
    MKAnnotationView* view = [mapView viewForAnnotation:tappedAnno];
    if(view.tag == 0){
        view.image = [UIImage imageNamed:@"spot_occupied.png"];
        tappedAnno.title = [NSString stringWithFormat:@"%sTaken", [tappedAnno.title substringToIndex:3].UTF8String ];
        view.tag = 1;
    }else{
        view.image = [UIImage imageNamed:@"spot_free_report.png"];
        tappedAnno.title = [NSString stringWithFormat:@"%sOpen", [tappedAnno.title substringToIndex:3].UTF8String];
        view.tag = 0;
    }
    [mapView selectAnnotation:tappedAnno animated:YES];
}

-(IBAction)backButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}
-(IBAction)submitButtonPressed:(id)sender{
    NSMutableDictionary *availability = [[NSMutableDictionary alloc] init];
    for (PQParkedCarAnnotation *annotation in mapView.annotations) {
        availability[[annotation.title substringToIndex:2]] = [annotation.title hasSuffix:@"Open"] ? @(1) : @(0);
    }
    [NetworkLayer reportAvailability:availability delegate:self];
}

- (void)afterReportingOnBackend:(NSInteger)statusCode {
    switch (statusCode) {
        case STATUSCODE_REPORTING_SUCCESS:
        {
            DataLayer* dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
            [dataLayer setLastReportTime:[NSDate date]];
            UIAlertView* thanksAlert = [[UIAlertView alloc] initWithTitle:@"Thanks for your help" message:@"Users like you help keep our data up-to-date. For that, you've earned 60 Parcupine Points!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [thanksAlert show];
            break;
        }
        case STATUSCODE_REPORTING_TWICE:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Thanks for your help" message:@"Unfortuately we can only give points for the first two reports per day. Come back tomorrow!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
            break;
        }
        case STATUSCODE_REPORTING_BAD_TIME:
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Thanks for your help" message:@"You can only earn points from 8am to 6pm. Come back tomorrow!" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alert show];
            break;
        }
        default:
        {
            [self dismissModalViewControllerAnimated:YES];
            break;
        }
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    [self dismissModalViewControllerAnimated:YES];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
    }
    
    // Set up map view: Single taps to turn on/off reporting
    UITapGestureRecognizer* singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];    
    [self.mapView addGestureRecognizer:singleTap];
    
    // @TODO(PILOT) Static (no zoom/scroll) map at certain point
    CLLocationCoordinate2D point = {42.357820, -71.094310};
    self.mapView.scrollEnabled = NO;
    self.mapView.zoomEnabled = NO;
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, 14, 14);
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:NO];
    
    // Load spots
    for(NSString* string in [self loadSpots]){
        NSArray* components = [string componentsSeparatedByString:@","];
        double lat = [[components objectAtIndex:0] floatValue];
        double lon = [[components objectAtIndex:1] floatValue];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(lat, lon);
        PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil]; 
        annotation.title = [components objectAtIndex:2];
        [self.mapView addAnnotation:annotation];
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

-(NSArray*) loadSpots{
    return [NSArray arrayWithObjects:
            @"42.357767, -71.094471, 1:Taken",
            @"42.357789, -71.094409, 2:Taken",
            @"42.357812, -71.094344, 3:Taken",
            @"42.357838, -71.094275, 4:Taken",
            @"42.357855, -71.094215, 5:Taken",
            @"42.357881, -71.094151, 6:Taken",
            nil];
}

@end
