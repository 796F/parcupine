//
//  ParqTimeRemainingViewController.m
//  Parq
//
//  Created by Mark Yen on 1/27/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqTimeRemainingViewController.h"

@implementation ParqTimeRemainingViewController
@synthesize lotNameLabel;
@synthesize spotNumLabel;
@synthesize hours;
@synthesize minutes;
@synthesize spotNumber;
@synthesize rateObj;

- (IBAction)refillButton:(id)sender {
}

- (void)backButton {
    NSLog(@"Back button pressed\n");
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
    lotNameLabel.text = rateObj.lotName;
    spotNumLabel.text = [NSString stringWithFormat:@"Spot #%d", spotNumber];
    UIBarButtonItem *back = self.navigationItem.backBarButtonItem;
    back.title = @"Unpark";
    back.target = self;
    back.action = @selector(backButton);
}

- (void)viewDidUnload
{
    [self setLotNameLabel:nil];
    [self setSpotNumLabel:nil];
    [self setHours:nil];
    [self setMinutes:nil];
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
