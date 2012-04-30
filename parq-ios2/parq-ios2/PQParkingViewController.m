//
//  PQParkingViewController.m
//  Parq
//
//  Created by Mark Yen on 4/26/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkingViewController.h"

@implementation PQParkingViewController
@synthesize startButton;
@synthesize paygFlag;
@synthesize minutes;
@synthesize seconds;

#pragma mark - UITableViewDelegate
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
	UIView *containerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 40)];
	containerView.backgroundColor = [UIColor groupTableViewBackgroundColor];
	CGRect labelFrame = CGRectMake(20, 2, 320, 30);
	UILabel *label = [[UILabel alloc] initWithFrame:labelFrame];
	label.backgroundColor = [UIColor clearColor];
	label.font = [UIFont boldSystemFontOfSize:17];
	label.shadowColor = [UIColor colorWithWhite:1.0 alpha:1];
	label.shadowOffset = CGSizeMake(0, 1);
	label.textColor = [UIColor colorWithRed:0.265 green:0.294 blue:0.367 alpha:1.000];
	label.text = [self tableView:tableView titleForHeaderInSection:section];
	[containerView addSubview:label];

    UIButton *abutton = [UIButton buttonWithType: UIButtonTypeInfoDark];
    abutton.frame = CGRectMake(288, 12, 14, 14);
//    [abutton addTarget: self action: @selector(addPage:)
//      forControlEvents: UIControlEventTouchUpInside];
    [containerView addSubview:abutton];
	return containerView;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 36;
}

#pragma mark - Memory

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad{
    [super viewDidLoad];

    [startButton setBackgroundImage:[[UIImage imageNamed:@"green_button.png"] stretchableImageWithLeftCapWidth:14.0 topCapHeight:0.0] forState:UIControlStateNormal];
//    [startButton addTarget:self action:@selector(buttonPressed) forControlEvents:UIControlEventTouchUpInside];

    minutes.font = [UIFont fontWithName:@"OCR A Std" size:60];
    seconds.font = [UIFont fontWithName:@"OCR A Std" size:60];
}

- (void)viewDidUnload
{
    [self setPaygFlag:nil];
    [self setMinutes:nil];
    [self setSeconds:nil];
    [self setStartButton:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
@end
