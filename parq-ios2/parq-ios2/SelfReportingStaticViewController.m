//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingStaticViewController.h"

#import "PQParkingViewController.h"
#define ALERTVIEW_THANKS 1
#define FIRST_SPOT_INDEX 101

@implementation SelfReportingStaticViewController
@synthesize bottomImage;
@synthesize leftButton;
@synthesize rightButton;
@synthesize networkLayer;
@synthesize parent;

@synthesize userLabel;
@synthesize spot101;
@synthesize spot102;
@synthesize spot103;
@synthesize spot104;
@synthesize spot105;
@synthesize spot106;

-(IBAction)backButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}

-(IBAction)submitButtonPressed:(id)sender{
    NSArray* switchObjects = [NSArray arrayWithObjects:spot101, spot102, spot103, spot104, spot105, spot106, nil];

    NSMutableArray* orderedAvailability = [[NSMutableArray alloc] initWithCapacity:6];
    for(UISwitch* spot in switchObjects){
        // 1 is open, 0 is taken
        if (spot.on) {
            [orderedAvailability addObject:[NSNumber numberWithInt:1]];
        } else {
            [orderedAvailability addObject:[NSNumber numberWithInt:0]];
        }
    }

    BOOL reportOutcome = [networkLayer submitAvailablilityInformation:orderedAvailability];
    if(reportOutcome){
        //server got report
        DataLayer* dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
        [dataLayer setLastReportTime:[NSDate date]];
        UIAlertView* thanksAlert = [[UIAlertView alloc] initWithTitle:@"Thanks for your help" message:@"Users like you help keep our data up-to-date. For that, you've earned 60 Parcupine Points!" delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil ];
        thanksAlert.tag = ALERTVIEW_THANKS;
        [thanksAlert show];
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == ALERTVIEW_THANKS) {
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
    self.userLabel.hidden = YES;
    
    NSArray* switchObjects = [NSArray arrayWithObjects:spot101, spot102, spot103, spot104, spot105, spot106, nil];
    
    if (self.spotNumber >= 1 && self.spotNumber <= 106) {
        // Set label at spot location
        CGPoint oldOrigin = self.userLabel.frame.origin;
        CGSize oldSize = self.userLabel.frame.size;
        
        // shift label "You are here -->" label down this much per spot
        int shift = (self.spotNumber - FIRST_SPOT_INDEX) * 35;
        
        CGRect newRect = CGRectMake(oldOrigin.x, oldOrigin.y + shift, oldSize.width, oldSize.height);
        self.userLabel.frame = newRect;
        self.userLabel.hidden = NO;
        
        // User parked at spot: Forced to mark as taken.
        int spotIndex = self.spotNumber - FIRST_SPOT_INDEX;
        if (spotIndex < 6) {
            UISwitch *userSwitch = [switchObjects objectAtIndex:spotIndex];
            userSwitch.on = YES;
            userSwitch.userInteractionEnabled = NO;            
        }
    }
    
    self.bottomImage.userInteractionEnabled = YES;
    self.bottomImage.multipleTouchEnabled = YES;
    // Ensure network connection 
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
    }
    
    // Set up map view: Single taps to turn on/off reporting
    UITapGestureRecognizer* singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.bottomImage addGestureRecognizer:singleTap];
    
    // TAP COORDINATES GO FROM TOP LEFT. 
    
    // @TODO(PILOT) Static (no zoom/scroll) map at certain point
    CLLocationCoordinate2D point = {42.357820, -71.094310};
    float left = -71.094310 - 0.000429 / 2;
    float bot = 42.357820 - 0.000277 / 2;
    float pixelToLatScale = 0.000277 / 285; // 285 height
    float pixelToLonScale = 0.000429 / 320;
    
    // map view region span lat: 0.000277, lon: 0.000429 (possibly maximum zoom:
    // See http://stackoverflow.com/questions/12599565/how-to-match-ios5-max-zoomlevel-mkmapview-in-ios6
    
    // Load spots
    for(NSString* string in [self loadSpots]){
        NSArray* components = [string componentsSeparatedByString:@","];
        double lat = [[components objectAtIndex:0] floatValue];
        double lon = [[components objectAtIndex:1] floatValue];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(lat, lon);
        
        double x = (lon - left) / pixelToLonScale;
        double y = (lat - bot) / pixelToLatScale;
        
        
        // Scale it to pixel numbers here:
        //CGPoint = {
        CGRect pinrect = CGRectMake(x, y, 37, 37); // size of largest spot
        
        // Add the annotation onto imageView
        
        
        
        // CGContextDrawImage(<#CGContextRef c#>, <#CGRect rect#>, <#CGImageRef image#>)
        // self.bottomImage
        PQParkedCarAnnotation *annotation = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil];
        annotation.title = [components objectAtIndex:2];
        //  [self.mapView addAnnotation:annotation];
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
