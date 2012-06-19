//
//  PQBookmarksViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQBookmarksViewController.h"

@interface PQBookmarksViewController ()

@end

@implementation PQBookmarksViewController
@synthesize bookmarkSelectionBar;


- (IBAction)bottomBarTapped {
    NSLog(@"TAPPED %d\n", self.bookmarkSelectionBar.selectedSegmentIndex);
    switch (self.bookmarkSelectionBar.selectedSegmentIndex) {
        case 0:
            break;
        case 1:
            break;
        case 2:
            break;
    }
}


-(IBAction)editButtonPressed:(id)sender{
    NSLog(@"edit pressed");
    //show red minuses, and the blue plus for current location.  
    //also make accessory views appear.  
}

-(IBAction)doneButtonPressed:(id)sender{
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

@end
