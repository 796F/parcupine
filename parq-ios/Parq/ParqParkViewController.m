//
//  ParqParkViewController.m
//  Parq
//
//  Created by Mark Yen on 1/25/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqParkViewController.h"
#import "ServerCalls.h"
#import "ParkResponse.h"

@implementation ParqParkViewController
@synthesize timePicker;
@synthesize rate;
@synthesize total;
@synthesize lotNameLabel;
@synthesize spotNumLabel;
@synthesize spotNumber;
@synthesize rateObj;

#define CONFIRM_PAYMENT_ALERT 1
#define PARKING_ERROR_ALERT 2

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

#pragma mark - Utility methods

+ (NSString*) centsToString:(int)cents
{
    return [NSString stringWithFormat:@"$%d.%02d", cents/100, cents%100];
}

#pragma mark - User selection

- (int)durationSelectedInMinutes
{
    return ((int)(timePicker.countDownDuration))/60;
}

- (int)costSelectedInCents
{
    return [self durationSelectedInMinutes]*rateObj.rateCents.intValue/rateObj.minuteInterval.intValue;
}

- (void)updateTotal {
    total.text = [ParqParkViewController centsToString:[self costSelectedInCents]];
}

#pragma mark -

- (IBAction)parkButton:(id)sender {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Confirm payment"
                                                    message:[NSString stringWithFormat:@"Parking for %d minutes will cost %@. Is this okay?",[self durationSelectedInMinutes],[ParqParkViewController centsToString:[self costSelectedInCents]]]
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Park", nil];
    alertView.tag = CONFIRM_PAYMENT_ALERT;
    [alertView show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (alertView.tag == CONFIRM_PAYMENT_ALERT) {
        if (buttonIndex == alertView.firstOtherButtonIndex) {
            ParkResponse *response = [ServerCalls parkUserWithRateObj:rateObj duration:[self durationSelectedInMinutes] cost:[self costSelectedInCents]];
            if ([response.resp isEqualToString:@"OK"]) {
                [self performSegueWithIdentifier:@"showTimeRemaining" sender:self];
            } else {
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"There was an error while parking. Please try again." delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
                alertView.tag = PARKING_ERROR_ALERT;
                [alertView show];
            }
        } else {
            [alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
        }
    }
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
}
*/

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    timePicker.minuteInterval = rateObj.minuteInterval.intValue;
    rate.text = [NSString stringWithFormat:@"%@ per %d minutes", [ParqParkViewController centsToString:rateObj.rateCents.intValue], rateObj.minuteInterval.intValue];
    lotNameLabel.text = rateObj.lotName;
    spotNumLabel.text = [NSString stringWithFormat:@"Spot #%d", spotNumber];
    [timePicker addTarget:self action:@selector(updateTotal) forControlEvents:UIControlEventValueChanged];
    timePicker.countDownDuration = rateObj.minuteInterval.doubleValue;
    [self updateTotal];
}

- (void)viewDidUnload
{
    [self setRate:nil];
    [self setTimePicker:nil];
    [self setTotal:nil];
    [self setLotNameLabel:nil];
    [self setSpotNumLabel:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
