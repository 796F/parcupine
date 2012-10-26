//
//  HelpViewController.m
//  Parq
//
//  Created by Eddie X on 10/26/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "HelpViewController.h"
#import "HelpThreeViewController.h"

@interface HelpViewController ()

@end

@implementation HelpViewController

- (IBAction)nextScreen:(id)sender {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    HelpThreeViewController *helpThree = [storyboard instantiateViewControllerWithIdentifier:@"helpThree"];
    
    [self.navigationController pushViewController:helpThree animated:YES];
}

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
