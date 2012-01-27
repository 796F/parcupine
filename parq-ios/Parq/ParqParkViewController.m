//
//  ParqParkViewController.m
//  Parq
//
//  Created by Mark Yen on 1/25/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqParkViewController.h"

@implementation ParqParkViewController
@synthesize timePicker;
@synthesize rate;
@synthesize total;
@synthesize rateCents;
@synthesize minuteInterval;
@synthesize lotName;
@synthesize spotNum;
@synthesize lotNameLabel;
@synthesize spotNumLabel;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

+ (NSString*) centsToString:(int)cents
{
    return [NSString stringWithFormat:@"$%d.%02d", cents/100, cents%100];
}

- (int)durationSelectedInMinutes
{
    return ((int)(timePicker.countDownDuration))/60;
}

- (int)costSelectedInCents
{
    return [self durationSelectedInMinutes]*rateCents/minuteInterval;
}

- (IBAction)parkButton:(id)sender {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Confirm payment" message:[NSString stringWithFormat:@"Parking for %d minutes will cost %@. Is this okay?",[self durationSelectedInMinutes],[ParqParkViewController centsToString:[self costSelectedInCents]]] delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Park", nil];
    [alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == alertView.firstOtherButtonIndex) {
        [self performSegueWithIdentifier:@"showTimeRemaining" sender:self];
    } else {
        [alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
    }
}

- (void)updateTotal {
    total.text = [ParqParkViewController centsToString:[self costSelectedInCents]];
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
    timePicker.minuteInterval = minuteInterval;
    rate.text = [NSString stringWithFormat:@"%@ per %d minutes", [ParqParkViewController centsToString:rateCents], minuteInterval];
    lotNameLabel.text = lotName;
    spotNumLabel.text = [NSString stringWithFormat:@"Spot #%d", spotNum];
    [timePicker addTarget:self action:@selector(updateTotal) forControlEvents:UIControlEventValueChanged];
    timePicker.countDownDuration = minuteInterval;
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
