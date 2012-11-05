//
//  SelfReportingViewController.m
//  Parq
//
//  Created by Michael Xia on 7/23/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "SelfReportingStaticViewController.h"

#import "PQParkingViewController.h"
#define FIRST_SPOT_INDEX 101

@implementation SelfReportingStaticViewController
@synthesize leftButton;
@synthesize rightButton;
@synthesize networkLayer;
@synthesize parent;

@synthesize userLabel;

@synthesize spot0;
@synthesize spot1;
@synthesize spot2;
@synthesize spot3;
@synthesize spot4;
@synthesize spot5;

-(IBAction)backButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}

-(IBAction)submitButtonPressed:(id)sender{
    NSDictionary *switchObjects = @{ @"1" : spot0, @"2" : spot1, @"3" : spot2, @"4" : spot3, @"5" : spot4, @"6" : spot5 };

    NSMutableDictionary* availability = [[NSMutableDictionary alloc] initWithCapacity:6];
    for (NSString *spot in switchObjects) {
        // 1 is open, 0 is taken
        availability[spot] = ((UIButton *)switchObjects[spot]).selected ? @(0) : @(1);
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

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
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

- (IBAction) toggleButton:(id)sender {
    if ([sender isSelected]) {
        [sender setSelected:NO];
    } else {
        [sender setSelected:YES];
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.userLabel.hidden = YES;
    
    NSArray* spotButtons = [NSArray arrayWithObjects:spot0, spot1, spot2, spot3, spot4, spot5, nil];
    
    // If user has parked, set their spot number as taken and do not allow to change
    // Also label that spot for ease of use.
    if (self.spotNumber >= 1 && self.spotNumber <= 106) {
        // Set label at spot location
        CGPoint oldOrigin = self.userLabel.frame.origin;
        CGSize oldSize = self.userLabel.frame.size;
        
        // shift label "You are here -->" label down this much per spot
        int shift = (self.spotNumber - FIRST_SPOT_INDEX) * 44;
        
        CGRect newRect = CGRectMake(oldOrigin.x, oldOrigin.y + shift, oldSize.width, oldSize.height);
        self.userLabel.frame = newRect;
        self.userLabel.hidden = NO;
        
        // User parked at spot: Forced to mark as taken.
        int spotIndex = self.spotNumber - FIRST_SPOT_INDEX;
        if (spotIndex < 6) {
            UIButton *userControl = [spotButtons objectAtIndex:spotIndex];
            userControl.selected = YES;
            userControl.userInteractionEnabled = NO;
        }
    }
    
    // Ensure network connection 
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
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
