//
//  PQParkingViewController.m
//  Parq
//
//  Created by Mark Yen on 4/26/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkingViewController.h"
#import "UIColor+Parq.h"

@interface PQParkingViewController ()
@property (nonatomic) BOOL timerStarted;
@end

@implementation PQParkingViewController
@synthesize startButton;
@synthesize unparkButton;
@synthesize paygFlag;
@synthesize prepaidFlag;
@synthesize hours;
@synthesize minutes;
@synthesize paygCheck;
@synthesize prepaidCheck;
@synthesize prepaidAmount;
@synthesize paygView;
@synthesize addressView;
@synthesize prepaidView;
@synthesize seeMapView;
@synthesize timerStarted;

- (IBAction)startTimer:(id)sender {
    timerStarted = YES;
    paygView.hidden = YES;
    addressView.hidden = NO;
    prepaidView.hidden = YES;
    seeMapView.hidden = NO;
    ((UIButton *)sender).hidden = YES;
    unparkButton.hidden = NO;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0]].userInteractionEnabled = NO;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0]].accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    [self.tableView reloadData];
}

- (IBAction)unparkNow:(id)sender {
    timerStarted = NO;
    paygView.hidden = NO;
    addressView.hidden = YES;
    prepaidView.hidden = NO;
    seeMapView.hidden = YES;
    startButton.hidden = NO;
    ((UIButton *)sender).hidden = YES;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0]].userInteractionEnabled = YES;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0]].accessoryType = UITableViewCellAccessoryNone;
    [self.tableView reloadData];
}

#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (!timerStarted) {
        if (indexPath.section == 0) {
            if (indexPath.row == 0) {
                paygCheck.image = [UIImage imageNamed:@"check.png"];
                prepaidCheck.image = [UIImage imageNamed:@"check_empty.png"];
                paygFlag.hidden = NO;
                prepaidFlag.hidden = YES;
                prepaidAmount.text = @"Enter Amount";
                prepaidAmount.textColor = [UIColor disabledTextColor];
                hours.text = @"00";
                minutes.text = @"00";
            } else if (indexPath.row == 1) {
                paygCheck.image = [UIImage imageNamed:@"check_empty.png"];
                prepaidCheck.image = [UIImage imageNamed:@"check.png"];
                paygFlag.hidden = YES;
                prepaidFlag.hidden = NO;
                prepaidAmount.text = @"1h 30m ($2.50)";
                prepaidAmount.textColor = [UIColor activeTextColor];
                hours.text = @"01";
                minutes.text = @"30";
            }
        }
    }
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    if (!timerStarted) {
        return @"Select payment method";
    } else {
        return @"Your car\u2019s parking location";
    }
}

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

    if (!timerStarted) {
        UIButton *abutton = [UIButton buttonWithType: UIButtonTypeInfoDark];
        abutton.frame = CGRectMake(288, 12, 14, 14);
        //    [abutton addTarget: self action: @selector(addPage:)
        //      forControlEvents: UIControlEventTouchUpInside];
        [containerView addSubview:abutton];
    }
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

    timerStarted = NO;

    [startButton setBackgroundImage:[[UIImage imageNamed:@"green_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];
    [unparkButton setBackgroundImage:[[UIImage imageNamed:@"red_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];

    hours.font = [UIFont fontWithName:@"OCR B Std" size:60];
    minutes.font = [UIFont fontWithName:@"OCR B Std" size:60];
}

- (void)viewDidUnload
{
    [self setPaygFlag:nil];
    [self setHours:nil];
    [self setMinutes:nil];
    [self setStartButton:nil];
    [self setPrepaidFlag:nil];
    [self setPaygCheck:nil];
    [self setPrepaidCheck:nil];
    [self setPrepaidAmount:nil];
    [self setPaygView:nil];
    [self setAddressView:nil];
    [self setPrepaidView:nil];
    [self setSeeMapView:nil];
    [self setUnparkButton:nil];
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
