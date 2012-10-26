//
//  HelpThreeViewController.m
//  Parq
//
//  Created by Eddie X on 10/26/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "HelpThreeViewController.h"

@interface HelpThreeViewController ()

@end

@implementation HelpThreeViewController

- (IBAction)startUsingApp:(id)sender {
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
