//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingViewController.h"

#import "PQParkingViewController.h"
#define THANKS_FOR_PLAYING_TAG 88
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
@synthesize isNotParking;

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
        //customPinView.pinColor = MKPinAnnotationColorPurple;
        NSLog(@"uitpe %d\n", UIType);
        if(UIType ==1 || UIType == 3){
            customPinView.image = [UIImage imageNamed:@"unknown.png"];
            customPinView.tag = 0;
        }else{
            //type 0, force report start on car.
            customPinView.image = [UIImage imageNamed:@"spot_occupied.png"];
            customPinView.tag = 1;
        }
        //customPinView.animatesDrop = YES;
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
    //SUBMIT THE INFORMATION TO SERVER.
    NSArray* sortedAnno = [[NSArray arrayWithArray:mapView.annotations] sortedArrayUsingComparator:^NSComparisonResult(PQParkedCarAnnotation* obj1, PQParkedCarAnnotation* obj2) {
        int id1 = [[[obj1.title componentsSeparatedByString:@":"] objectAtIndex:0] intValue];
        int id2 = [[[obj2.title componentsSeparatedByString:@":"] objectAtIndex:0] intValue];
        if(id1 < id2){
            return -1;
        } else {
            return 1;
        }
    }];
    BOOL badReport = NO;
    NSMutableArray* orderedAvailability = [[NSMutableArray alloc] initWithCapacity:6];
    for(PQParkedCarAnnotation* anno in sortedAnno){
        if([anno.title hasSuffix:@"Open"]){
            [orderedAvailability addObject:[NSNumber numberWithInt:1]];
        }else if([anno.title hasSuffix:@"Taken"]){
            [orderedAvailability addObject:[NSNumber numberWithInt:0]];
        }else{
            //not everything was tapped.
            badReport = YES;
        }
    }
    
    if(badReport){
        //alert that they need to fill everything in.
        UIAlertView* fillAllAlert = [[UIAlertView alloc] initWithTitle:@"Uh Oh!" message:@"You missed a spot" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [fillAllAlert show];
    }else{
        BOOL reportOutcome = [networkLayer submitAvailablilityInformation:orderedAvailability];
        if(reportOutcome){
            //server got report
            DataLayer* dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
            [dataLayer setLastReportTime:[NSDate date]];
            if(isNotParking){

            }else{
                //uh, doesn't need to be casted.  just change parent field type to PQParkingViewController, and add an @Class to the .h file.
                PQParkingViewController* castedParent = (PQParkingViewController*) parent;
                if([networkLayer parkUserWithSpotInfo:castedParent.spotInfo AndDuration:castedParent.datePicker.countDownDuration]){ //server accepted parking request
                    [castedParent startTimerButtonAction];
                }else{ //failed to park on server
                    UIAlertView* failedToPark = [[UIAlertView alloc] initWithTitle:@"Error Parking" message:@"Please try again" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [failedToPark show];
                }
            }
            UIAlertView* thanksAlert = [[UIAlertView alloc] initWithTitle:@"Thanks for Helping" message:@"You earned 60 parking points" delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil ];
            thanksAlert.tag = THANKS_FOR_PLAYING_TAG;
            [thanksAlert show];
            
        }else{
            //fail
        }
//        [parent startTimerButtonAction];
//        [self dismissModalViewControllerAnimated:YES];
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag == THANKS_FOR_PLAYING_TAG && buttonIndex == 0){
        [self dismissModalViewControllerAnimated:YES];
    }
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
    if(UIType == 3 || UIType == 1){
        return [NSArray arrayWithObjects:
                @"42.357767, -71.094471, 1:",
                @"42.357789, -71.094409, 2:",
                @"42.357812, -71.094344, 3:",
                @"42.357838, -71.094275, 4:",
                @"42.357855, -71.094215, 5:",
                @"42.357881, -71.094151, 6:",
                nil];
    }else{
        return [NSArray arrayWithObjects:
                @"42.357767, -71.094471, 1:Taken",
                @"42.357789, -71.094409, 2:Taken",
                @"42.357812, -71.094344, 3:Taken",
                @"42.357838, -71.094275, 4:Taken",
                @"42.357855, -71.094215, 5:Taken",
                @"42.357881, -71.094151, 6:Taken",
                nil];
    }
    
}

@end
