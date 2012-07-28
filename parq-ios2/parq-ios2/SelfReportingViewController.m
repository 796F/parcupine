//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingViewController.h"

#import "PQParkingViewController.h"
@interface SelfReportingViewController ()

@end



@implementation SelfReportingViewController
@synthesize mapView;
@synthesize leftButton;
@synthesize rightButton;
@synthesize networkLayer;
@synthesize showTapMe;
@synthesize UIType;
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
        MKPinAnnotationView* customPinView = [[MKPinAnnotationView alloc]
                                              initWithAnnotation:annotation reuseIdentifier:parkedCarAnnotationIdentifier];
        //customPinView.pinColor = MKPinAnnotationColorPurple;
        NSLog(@"uitpe %d\n", UIType);
        if(UIType ==1 || UIType == 3){
            customPinView.image = [UIImage imageNamed:@"unknown.png"];
            customPinView.tag = 1;
        }else{
            customPinView.tag = 0;
            customPinView.image = [UIImage imageNamed:@"car.png"];
        }
        
        customPinView.animatesDrop = YES;
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
        view.image = [UIImage imageNamed:@"car.png"];
        tappedAnno.title = [NSString stringWithFormat:@"%sTaken", [tappedAnno.title substringToIndex:3].UTF8String ];
        view.tag = 1;
    }else{
        view.image = [UIImage imageNamed:@"open.png"];
        tappedAnno.title = [NSString stringWithFormat:@"%sOpen", [tappedAnno.title substringToIndex:3].UTF8String];
        view.tag = 0;
    }
    [mapView selectAnnotation:tappedAnno animated:YES];
}

-(IBAction)backButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}
-(IBAction)submitButtonPressed:(id)sender{
    //SUBMIT THE INFORMATION TO SERVER. 
    //[networkLayer submitAvailablilityInformation]
    [parent startTimerButtonAction];
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
    UITapGestureRecognizer* tgs = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.mapView addGestureRecognizer:tgs];
    CLLocationCoordinate2D point = {42.357820, -71.094310};
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, 15, 15);
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:NO];
    showTapMe = YES;
    for(NSString* string in [self loadSpots]){
        NSArray* components = [string componentsSeparatedByString:@","];
        double lat = [[components objectAtIndex:0] floatValue];
        double lon = [[components objectAtIndex:1] floatValue];
        CLLocationCoordinate2D coord =  CLLocationCoordinate2DMake(lat, lon);
        PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil]; 
        annotation.title = [components objectAtIndex:2];
        [self.mapView addAnnotation:annotation];
    }
	// Do any additional setup after loading the view.
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
            @"42.357767, -71.094471, 1:",  
            @"42.357789, -71.094409, 2:", 
            @"42.357812, -71.094344, 3:", 
            @"42.357838, -71.094275, 4:", 
            @"42.357855, -71.094215, 5:", 
            @"42.357881, -71.094151, 6:",
            nil];
}

@end