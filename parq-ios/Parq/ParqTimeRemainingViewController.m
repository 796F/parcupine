//
//  ParqTimeRemainingViewController.m
//  Parq
//
//  Created by Mark Yen on 1/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqTimeRemainingViewController.h"
#import "SavedInfo.h"

@interface ParqTimeRemainingViewController ()
@property (strong, nonatomic) NSTimer *timer;
@property (strong, nonatomic) NSDate *endTime;
@end

@implementation ParqTimeRemainingViewController
@synthesize lotNameLabel;
@synthesize spotNumLabel;
@synthesize colonLabel;
@synthesize hours;
@synthesize minutes;
@synthesize timer;
@synthesize endTime;
@synthesize delegate;

- (IBAction)unparkButton:(id)sender {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Unpark" message:@"Are you sure you want to unpark? If you need to repark you will have to pay again." delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"Unpark", nil];
    [alertView show];
}

- (IBAction)refillButton:(id)sender {
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == alertView.firstOtherButtonIndex) {
        [timer invalidate];
        [delegate unpark];
    }
}

- (void)flashColon {
    colonLabel.hidden = !colonLabel.hidden;
}

- (void)updateTimer {
    NSTimeInterval secondsRemaining = [endTime timeIntervalSinceNow];
    if (secondsRemaining > 0) {
        int hoursRemaining = secondsRemaining/3600;
        int minutesRemaining = ((int)secondsRemaining%3600)/60;
        if (minutesRemaining < 59) {
            minutesRemaining++;
        } else {
            minutesRemaining = 0;
            hoursRemaining++;
        }

        hours.text = [NSString stringWithFormat:@"%d", hoursRemaining];
        minutes.text = [NSString stringWithFormat:@"%02d", minutesRemaining];

        [self flashColon];
    } else {
        [timer invalidate];
        [delegate timeUp];
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
    RateObject *rateObj = [SavedInfo rate];
    lotNameLabel.text = rateObj.lotName;
    NSNumber *spotNumber = [SavedInfo spotNumber];
    spotNumLabel.text = [NSString stringWithFormat:@"Spot #%d", [spotNumber intValue]];
    UIBarButtonItem *unpark = self.navigationItem.backBarButtonItem;
    unpark.title = @"Unpark";
    unpark.target = self;
    unpark.action = @selector(unparkButton);

    timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updateTimer) userInfo:nil repeats:YES];
    endTime = [NSDate dateWithTimeIntervalSince1970:[[SavedInfo endTime] doubleValue]/1000];
}

- (void)viewDidUnload
{
    [self setLotNameLabel:nil];
    [self setSpotNumLabel:nil];
    [self setHours:nil];
    [self setMinutes:nil];
    [self setLotNameLabel:nil];
    [self setSpotNumLabel:nil];
    [self setColonLabel:nil];
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
