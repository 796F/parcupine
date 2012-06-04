//
//  PQParkingViewController.m
//  Parq
//
//  Created by Mark Yen on 4/26/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQParkingViewController.h"
#import "PQParkedCarMapViewController.h"
#import "UIColor+Parq.h"

@interface PQParkingViewController ()
@property (nonatomic) BOOL timerStarted;
@property (strong, nonatomic) UIBarButtonItem *cancelButton;
@property (strong, nonatomic) UIBarButtonItem *doneButton;
@end

@implementation PQParkingViewController
@dynamic rate;
@dynamic address;
@synthesize rateNumeratorCents;
@synthesize rateDenominatorMinutes;
@synthesize limit;
@synthesize limitUnit;
@synthesize addressLabel;
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
@synthesize datePicker;
@synthesize rateNumerator;
@synthesize rateDenominator;
@synthesize limitValue;
@synthesize cancelButton;
@synthesize doneButton;
@synthesize timerStarted;
@synthesize parent;
#pragma mark - Main button actions
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
    /*
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
    */
    [self dismissModalViewControllerAnimated:YES];
}

#pragma mark - Date Picker control
- (void)durationChanged {
    int totalMinutes = (datePicker.countDownDuration-1)/60+1;
    if (totalMinutes > limit) {
        datePicker.countDownDuration = limit*60;
        totalMinutes = limit;
    }
    int hoursPart = totalMinutes/60;
    int minutesPart = totalMinutes%60;
    hours.text = [NSString stringWithFormat:@"%02d", hoursPart];
    minutes.text = [NSString stringWithFormat:@"%02d", minutesPart];

    int totalCents = self.rate * totalMinutes;
    int dollarsPart = totalCents/100;
    int centsPart = totalCents%100;

    if (hoursPart == 0) {
        prepaidAmount.text = [NSString stringWithFormat:@"%dm ($%d.%02d)", minutesPart, dollarsPart, centsPart];
    } else {
        prepaidAmount.text = [NSString stringWithFormat:@"%dh %02dm ($%d.%02d)", hoursPart, minutesPart, dollarsPart, centsPart];
    }
}

- (void)activatePicker {
    [UIView animateWithDuration:.25 animations:^{
        self.tableView.frame = CGRectMake(0, -131, 320, 611);
        datePicker.frame = CGRectMake(0, 2, 320, 216);
    }];
    self.navigationItem.title = @"Enter Amount";
    if (cancelButton == nil) {
        cancelButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(cancelPicker)];
    }
    if (doneButton == nil) {
        doneButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(doneWithPicker)];
    }
    self.navigationItem.leftBarButtonItem = cancelButton;
    self.navigationItem.rightBarButtonItem = doneButton;
    [self durationChanged];
}

- (void)resignPicker {
    [UIView animateWithDuration:.25 animations:^{
        self.tableView.frame = CGRectMake(0, 0, 320, 416);
        datePicker.frame = CGRectMake(0, 89, 320, 216);
    }];
    self.navigationItem.title = @"1106, Cambridge, MA";
    self.navigationItem.leftBarButtonItem = nil;
    self.navigationItem.rightBarButtonItem = nil;
}

#pragma mark - Table View actions
- (void)paygSelected {
    paygCheck.image = [UIImage imageNamed:@"check.png"];
    prepaidCheck.image = [UIImage imageNamed:@"check_empty.png"];
    paygFlag.hidden = NO;
    prepaidFlag.hidden = YES;
    prepaidAmount.text = @"Enter Amount";
    prepaidAmount.textColor = [UIColor disabledTextColor];
    hours.text = @"00";
    minutes.text = @"00";
    [self resignPicker];
}

- (void)prepaidSelected {
    paygCheck.image = [UIImage imageNamed:@"check_empty.png"];
    prepaidCheck.image = [UIImage imageNamed:@"check.png"];
    paygFlag.hidden = YES;
    prepaidFlag.hidden = NO;
    prepaidAmount.textColor = [UIColor whiteColor];
    [self activatePicker];
}

- (void)showParkedCar {
    [self performSegueWithIdentifier:@"showParkedCar" sender:self];
}

#pragma mark - UIViewController
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"showParkedCar"]) {
        PQParkedCarMapViewController *parkedCarVC = [segue destinationViewController];
    }
}

#pragma mark - Bar Button actions
- (void)cancelPicker {
    NSIndexPath *paygIndexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    [self.tableView selectRowAtIndexPath:paygIndexPath animated:NO scrollPosition:UITableViewScrollPositionNone];
    [self paygSelected];
    [self.tableView deselectRowAtIndexPath:paygIndexPath animated:YES];
}

- (void)doneWithPicker {
    [self.tableView deselectRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0] animated:YES];
    prepaidAmount.textColor = [UIColor activeTextColor];
    [self resignPicker];
}

#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (!timerStarted) {
        if (indexPath.section == 0) {
            if (indexPath.row == 0) {
                [self paygSelected];
                [tableView deselectRowAtIndexPath:indexPath animated:YES];
            } else if (indexPath.row == 1) {
                [self prepaidSelected];
            }
        }
    } else {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
    }
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

#pragma mark - Dynamic properties
- (double)rate {
    return (double)rateNumeratorCents/rateDenominatorMinutes;
}

- (NSString *)address {
    return addressLabel.text;
}

- (void)setAddress:(NSString *)addressString {
    addressLabel.text = addressString;
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

    // Hardcoding the rate and limit for now
    rateNumeratorCents = 50;
    rateDenominatorMinutes = 30;
    limit = 600; // 10 hours * 60 min/hr

    timerStarted = NO;

    [startButton setBackgroundImage:[[UIImage imageNamed:@"green_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];
    [unparkButton setBackgroundImage:[[UIImage imageNamed:@"red_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];

    [datePicker addTarget:self action:@selector(durationChanged) forControlEvents:UIControlEventValueChanged];

    hours.font = [UIFont fontWithName:@"OCR B Std" size:60];
    minutes.font = [UIFont fontWithName:@"OCR B Std" size:60];

    // rateNumerator
    int dollarsPart = rateNumeratorCents/100;
    int centsPart = rateNumeratorCents%100;
    if (dollarsPart == 0) {
        rateNumerator.text = [NSString stringWithFormat:@"%d¢", centsPart];
    } else if (centsPart == 0) {
        rateNumerator.text = [NSString stringWithFormat:@"$%d", dollarsPart];
    } else {
        rateNumerator.text = [NSString stringWithFormat:@"$%d.%02d", dollarsPart, centsPart];
    }

    // rateDenominator
    int hoursPart = rateDenominatorMinutes/60;
    int minutesPart = rateDenominatorMinutes%60;
    if (hoursPart == 0) {
        rateDenominator.text = [NSString stringWithFormat:@"/%dmin", minutesPart];
    } else {
        if (minutesPart == 0) {
            if (hoursPart == 1) {
                rateDenominator.text = @"/hour";
            } else {
                rateDenominator.text = [NSString stringWithFormat:@"/%d hrs", hoursPart];
            }
        } else {
            rateDenominator.text = [NSString stringWithFormat:@"/%dh %02dm", hoursPart, minutesPart];
        }
    }

    // limitValue and limitUnit
    if (limit<60) {
        limitValue.text = [NSString stringWithFormat:@"%d", limit];
        limitUnit.text = @"mins";
    } else if (limit>60) {
        if (limit%60 == 0) {
            limitValue.text = [NSString stringWithFormat:@"%d", limit/60];
        } else {
            limitValue.text = [NSString stringWithFormat:@"%.1f", limit/60.0];
        }
        limitUnit.text = @"hours";
    } else {
        limitValue.text = @"1";
        limitUnit.text = @"hour";
    }

    // Gesture recognizer for "See map" table view cell
    UITapGestureRecognizer *seeMapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showParkedCar)];
    [self.seeMapView addGestureRecognizer:seeMapRecognizer];
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
    [self setDatePicker:nil];
    [self setRateNumerator:nil];
    [self setRateDenominator:nil];
    [self setLimitValue:nil];
    [self setLimitUnit:nil];
    [self setAddressLabel:nil];
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
