//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingViewController.h"
#import "PQParkedCarAnnotation.h"
@interface SelfReportingViewController ()

@end



@implementation SelfReportingViewController
@synthesize mapView;
@synthesize leftButton;
@synthesize rightButton;
@synthesize networkLayer;
@synthesize showTapMe;
@synthesize UIType;

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
        [mapView dequeueReusableAnnotationViewWithIdentifier:parkedCarAnnotationIdentifier];
        
        if (!pinView)
        {
            // if an existing pin view was not available, create one
            MKPinAnnotationView* customPinView = [[MKPinAnnotationView alloc]
                                                  initWithAnnotation:annotation reuseIdentifier:parkedCarAnnotationIdentifier];
            //customPinView.pinColor = MKPinAnnotationColorPurple;
            customPinView.image = [UIImage imageNamed:@"unknown.png"];
            customPinView.animatesDrop = YES;
            customPinView.canShowCallout = YES;
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
-(void) mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view{
    if(view.tag ==1){
        view.image = [UIImage imageNamed:@"unknown.png"];
    }else if(view.tag == 2){
        view.image = [UIImage imageNamed:@"car.png"];
    }else{
        view.image = [UIImage imageNamed:@"open.png"];
    }

}
-(void) mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view{
    if(view.tag ==0){
        //unknown
        view.image = [UIImage imageNamed:@"unknown.png"];
        view.tag = 1;
    }else if(view.tag == 1){
        //taken
        view.image = [UIImage imageNamed:@"car.png"];
        view.tag = 2;
    }else{
        //open
        view.image = [UIImage imageNamed:@"open.png"];
        view.tag = 0;
    }
    //deselect annotationf or next try.  
    NSArray *selectedAnnotations = self.mapView.selectedAnnotations;
    for(id annotation in selectedAnnotations) {
        if(showTapMe){
            [self.mapView selectAnnotation:annotation animated:YES];
            showTapMe = NO;
        }else{
            [self.mapView deselectAnnotation:annotation animated:NO];
        }
    
    }
}

-(void) handleSingleTap:(UIGestureRecognizer*) gestureRecognizer{
    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    CGPoint touchPoint = [gestureRecognizer locationInView:self.mapView];
    CLLocationCoordinate2D coord = [self.mapView convertPoint:touchPoint toCoordinateFromView:self.mapView];
    NSLog(@"%f %f\n", coord.latitude, coord.longitude);
    //self.parentViewController 
    
    //PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil]; 
    //[self.mapView addAnnotation:annotation];
}

-(IBAction)backButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}
-(IBAction)submitButtonPressed:(id)sender{
    //SUBMIT THE INFORMATION TO SERVER. 
    //[networkLayer submitAvailablilityInformation]
    
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
    showTapMe = YES;
    
    //[self.mapView addGestureRecognizer:tgs];
    CLLocationCoordinate2D point = {42.357802, -71.094285};
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, 15, 15);
    [mapView setRegion:[mapView regionThatFits:viewRegion] animated:NO];

    for(NSString* string in [self loadSpots]){
        NSArray* components = [string componentsSeparatedByString:@","];
        double lat = [[components objectAtIndex:0] floatValue];
        double lon = [[components objectAtIndex:1] floatValue];
        CLLocationCoordinate2D coord =  CLLocationCoordinate2DMake(lat, lon);
        PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil]; 
        annotation.title = @"Tap Me!";
        [self.mapView addAnnotation:annotation];
        [self.mapView selectAnnotation:annotation animated:NO];
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
            @"42.357767, -71.094471",  
            @"42.357789, -71.094409", 
            @"42.357812, -71.094344", 
            @"42.357838, -71.094275", 
            @"42.357855, -71.094215", 
            @"42.357881, -71.094151",
            nil];
}

@end
