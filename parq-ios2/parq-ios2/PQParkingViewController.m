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
#import <QuartzCore/QuartzCore.h>

#define ACTIONSHEET_EXTEND 1
#define ACTIONSHEET_UNPARK 2
#define ALERTVIEW_INFO 1
#define COUNT_DOWN 4
#define COUNT_UP 5
#define REFILL 6

typedef enum {
    kParkingParkState=0,
    kParkedParkState,
    kExtendingParkState,
    kReturningToAppState,
} ParkState;

@interface PQParkingViewController ()
@property (nonatomic) ParkState parkState;
@property (strong, nonatomic) NSTimer *timer;
@property (strong, nonatomic) NSTimer *expiresAtTimer;
@property (strong, nonatomic) NSDate *prepaidEndTime;
@property (strong, nonatomic) NSDate *paygStartTime;
@property (strong, nonatomic) NSDateFormatter *dateFormatter;
@end

@implementation PQParkingViewController
@dynamic rate;
@dynamic address;
@synthesize rateNumeratorCents;
@synthesize rateDenominatorMinutes;
@synthesize limit;
@synthesize limitUnit;
@synthesize addressLabel;
@synthesize expiresAtTime;
@synthesize cancelButton;
@synthesize doneButton;
@synthesize startButton;
@synthesize unparkButton;
@synthesize extendButton;
@synthesize meter;
@synthesize paygFlag;
@synthesize prepaidFlag;
@synthesize hours;
@synthesize minutes;
@synthesize colon;
@synthesize paygCheck;
@synthesize prepaidCheck;
@synthesize prepaidAmount;
@synthesize remainingAmount;
@synthesize extendAmount;
@synthesize paygCellView;
@synthesize addressCellView;
@synthesize prepaidCellView;
@synthesize seeMapCellView;
@synthesize timeRemainingCellView;
@synthesize timeToAddCellView;
@synthesize datePicker;
@synthesize rateNumerator;
@synthesize rateDenominator;
@synthesize limitValue;
@synthesize parkState;
@synthesize timer;
@synthesize expiresAtTimer;
@synthesize prepaidEndTime;
@synthesize paygStartTime;
@synthesize dateFormatter;
@synthesize parent;
@synthesize spotInfo;
@synthesize notificationTag;
@synthesize totalParkedSeconds;
#pragma mark - Park state transitions
- (void)parkedAfterParking {
    parkState = kParkedParkState;
    addressCellView.hidden = NO;
    seeMapCellView.hidden = NO;
    paygCellView.hidden = YES;
    prepaidCellView.hidden = YES;
    startButton.hidden = YES;
    unparkButton.hidden = NO;
    extendButton.hidden = prepaidFlag.hidden;
    self.navigationItem.leftBarButtonItem = nil;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0]].userInteractionEnabled = NO;
    [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0]].accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    [self.tableView reloadData];
    datePicker.countDownDuration = 0;
}

- (void)extendingAfterParked {
    //check limit before allowing extend.  
    
    //or just hide the button if limit is hit.  
    
    parkState = kExtendingParkState;
    timeRemainingCellView.hidden = NO;
    timeToAddCellView.hidden = NO;
    addressCellView.hidden = YES;
    seeMapCellView.hidden = YES;
    UITableViewCell *secondCell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0]];
    secondCell.userInteractionEnabled = NO;
    secondCell.accessoryType = UITableViewCellAccessoryNone;
    remainingAmount.text = [NSString stringWithFormat:@"%@:%@", hours.text, minutes.text, nil];
    [self.tableView reloadData];
    [self activatePicker];
}

- (void)parkedAfterExtending {
    parkState = kParkedParkState;
    addressCellView.hidden = NO;
    seeMapCellView.hidden = NO;
    timeRemainingCellView.hidden = YES;
    timeToAddCellView.hidden = YES;
    UITableViewCell *secondCell = [self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0]];
    secondCell.userInteractionEnabled = YES;
    secondCell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    [self.tableView reloadData];
    [self resignPicker];
    datePicker.countDownDuration = 0;
}

#pragma mark - Main button actions
- (IBAction)startTimer:(id)sender {
    if (!prepaidFlag.hidden) { // Prepaid
        //set up notifications.  
        notificationTag = COUNT_DOWN;
        [expiresAtTimer invalidate];
        prepaidEndTime = [NSDate dateWithTimeIntervalSinceNow:datePicker.countDownDuration];
        [self scheduleLocalNotification:prepaidEndTime];
        totalParkedSeconds = datePicker.countDownDuration;
        timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updatePrepaidTimer) userInfo:nil repeats:YES];

        if(totalParkedSeconds >= limit*60){
            //you have extended to or beyond the limit.  hide extend button or somthing.  
            extendButton.enabled = NO;
        }
    } else { // Pay as you go
        paygStartTime = [NSDate date];
        timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updatePaygTimer) userInfo:nil repeats:YES];
        totalParkedSeconds = 0;
    }
    [self parkedAfterParking];
}

- (IBAction)unparkNow:(id)sender {
    UIActionSheet *unparkActionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:@"Unpark" otherButtonTitles:nil];
    unparkActionSheet.actionSheetStyle = UIActionSheetStyleBlackOpaque;
    unparkActionSheet.tag = ACTIONSHEET_UNPARK;
    [self scheduleLocalNotification:nil];   //remove all notifications
    totalParkedSeconds = 0;
    [unparkActionSheet showInView:self.tableView];
}

- (IBAction)extend:(id)sender {
    [self extendingAfterParked];
}

- (void)infoButtonPressed {
    UIAlertView *infoAlert = [[UIAlertView alloc] initWithTitle:@"Parking payment methods" message:@"Choose Prepaid if you would like to select how long you will be parking for.\nChoose Pay as you go if you would like to start the timer and pay for whatever you use." delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    infoAlert.tag = ALERTVIEW_INFO;
    [infoAlert show];
}

#pragma mark - UIActionSheetDelegate
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (actionSheet.tag == ACTIONSHEET_UNPARK && buttonIndex == actionSheet.destructiveButtonIndex) {
        [timer invalidate];
        totalParkedSeconds = 0;
        [self dismissModalViewControllerAnimated:YES];
    } else if (actionSheet.tag == ACTIONSHEET_EXTEND && buttonIndex == actionSheet.firstOtherButtonIndex) {
        prepaidEndTime = [NSDate dateWithTimeInterval:datePicker.countDownDuration sinceDate:prepaidEndTime];
        totalParkedSeconds += datePicker.countDownDuration;
        
        int totalMinutes = ([prepaidEndTime timeIntervalSinceNow]+59)/60;
        hours.text = [NSString stringWithFormat:@"%02d", totalMinutes/60];
        minutes.text = [NSString stringWithFormat:@"%02d", totalMinutes%60];
        expiresAtTime.text = [dateFormatter stringFromDate:prepaidEndTime];
        [self scheduleLocalNotification:prepaidEndTime]; //reset the notifications.  
        [self parkedAfterExtending];
        if(totalParkedSeconds >= limit*60){
            //you have extended to or beyond the limit.  hide extend button or somthing.  
            extendButton.enabled = NO;
        }
    }
}

#pragma mark - UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (alertView.tag == ALERTVIEW_INFO) {
        [alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
    }
}

#pragma mark - Timer update methods
//prepare a 5 minute warning and a out of time warning.  
- (void) scheduleLocalNotification:(NSDate*) endDate{
    //clears old notifications
    UIApplication* myApp = [UIApplication sharedApplication];
    if(endDate == nil){ //counting up
        [myApp cancelAllLocalNotifications];
    }else{ //counting down
        //set a 5 minute time from endDate, regardless of refill/parking
        [myApp cancelAllLocalNotifications];
        UILocalNotification* lowTimeAlert = [[UILocalNotification alloc] init];
        
        lowTimeAlert.applicationIconBadgeNumber=1; //mark the app with red 1
        //this method uses seconds.
        NSTimeInterval fiveMinutes = -300;
        lowTimeAlert.fireDate = [NSDate dateWithTimeInterval:fiveMinutes sinceDate:endDate];
        lowTimeAlert.timeZone = [NSTimeZone defaultTimeZone];
        lowTimeAlert.alertBody = @"You have 5 minutes remaining!";
        
        UILocalNotification* endingAlert = [[UILocalNotification alloc] init];        
        endingAlert.applicationIconBadgeNumber=1; //mark the app with red 1
        endingAlert.fireDate = endDate;
        endingAlert.timeZone = [NSTimeZone defaultTimeZone];
        endingAlert.alertBody = @"You have run out of time!";
        
        lowTimeAlert.soundName = UILocalNotificationDefaultSoundName;
        endingAlert.soundName = UILocalNotificationDefaultSoundName;
        
        [myApp scheduleLocalNotification:endingAlert];
        [myApp scheduleLocalNotification:lowTimeAlert];
    }
}
- (void)updatePrepaidTimer {
    int remainingSeconds = round([prepaidEndTime timeIntervalSinceNow]);
    
    
    if (remainingSeconds < 1) {
        //out of time.  
        [timer invalidate];
        [self dismissModalViewControllerAnimated:YES];
    }
    
    if (parkState == kParkedParkState) {
        // Adding 59 seconds rounds 00:04:59 to 00:05 but keeps 00:05:00 as 00:05
        int totalMinutes = ([prepaidEndTime timeIntervalSinceNow]+59)/60;
        hours.text = [NSString stringWithFormat:@"%02d", totalMinutes/60];
        minutes.text = [NSString stringWithFormat:@"%02d", totalMinutes%60];
        colon.hidden = !colon.hidden;
    } else { // parkState == kExtendingParkState
        int totalSeconds = round([prepaidEndTime timeIntervalSinceNow]);
        int totalMinutes = (totalSeconds+59)/60;
        //even odd thing makes it tick.  
        if (totalSeconds%2 == 0) {
            remainingAmount.text = [NSString stringWithFormat:@"%02d:%02d", totalMinutes/60, totalMinutes%60];
        } else {
            remainingAmount.text = [NSString stringWithFormat:@"%02d %02d", totalMinutes/60, totalMinutes%60];
        }
    }
}

- (void)updatePaygTimer {
    int totalMinutes = (-[paygStartTime timeIntervalSinceNow]+59)/60;
    hours.text = [NSString stringWithFormat:@"%02d", totalMinutes/60];
    minutes.text = [NSString stringWithFormat:@"%02d", totalMinutes%60];
    colon.hidden = !colon.hidden;
}

- (void)updateExpiresAtTime {
    // Adding 30 seconds to the timer because sometimes the timer fires a few milliseconds before the target time, causing the Expire At time to be a minute off
    expiresAtTime.text = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSinceReferenceDate:[[NSDate date] timeIntervalSinceReferenceDate]+datePicker.countDownDuration+30.0]];
}

#pragma mark - Date Picker control
- (IBAction)durationChanged:(id)sender {
    
    int totalMinutes = datePicker.countDownDuration/60;
    if (totalMinutes > limit || totalMinutes + totalParkedSeconds/60 > limit) {
        datePicker.countDownDuration = limit*60 - totalParkedSeconds;
        totalMinutes = limit - totalParkedSeconds/60;
    }
    int hoursPart = totalMinutes/60;
    int minutesPart = totalMinutes%60;

    int totalCents = ceil(self.rate * totalMinutes);
    int dollarsPart = totalCents/100;
    int centsPart = totalCents%100;

    if (parkState == kParkingParkState) {
        hours.text = [NSString stringWithFormat:@"%02d", hoursPart];
        minutes.text = [NSString stringWithFormat:@"%02d", minutesPart];
        expiresAtTime.text = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSinceNow:datePicker.countDownDuration]];
        if (hoursPart == 0) {
            prepaidAmount.text = [NSString stringWithFormat:@"%dm ($%d.%02d)", minutesPart, dollarsPart, centsPart];
        } else {
            prepaidAmount.text = [NSString stringWithFormat:@"%dh %02dm ($%d.%02d)", hoursPart, minutesPart, dollarsPart, centsPart];
        }
    } else { // parkState == kExtendingParkState
        if (hoursPart == 0) {
            extendAmount.text = [NSString stringWithFormat:@"%dm ($%d.%02d)", minutesPart, dollarsPart, centsPart];
        } else {
            extendAmount.text = [NSString stringWithFormat:@"%dh %02dm ($%d.%02d)", hoursPart, minutesPart, dollarsPart, centsPart];
        }
    }
}

- (void)activatePicker {
    [UIView animateWithDuration:.25 animations:^{
        self.tableView.frame = CGRectMake(0, -131, 320, 611);
        datePicker.frame = CGRectMake(0, 2, 320, 216);
    }];

    datePicker.minuteInterval = spotInfo.minuteInterval.intValue;
    datePicker.countDownDuration = spotInfo.minuteInterval.intValue;

    self.navigationItem.title = @"Enter Amount";
    self.navigationItem.leftBarButtonItem = cancelButton;
    self.navigationItem.rightBarButtonItem = doneButton;
    [self durationChanged:nil];
}

- (void)resignPicker {
    [UIView animateWithDuration:.25 animations:^{
        self.tableView.frame = CGRectMake(0, 0, 320, 416);
        datePicker.frame = CGRectMake(0, 89, 320, 216);
    }];
    NSString* title = [NSString stringWithFormat:@"%d, %s.\n", spotInfo.spotNumber.intValue, spotInfo.streetName.UTF8String];
    
    self.navigationItem.title = title;
    if (parkState == kParkedParkState) {
        self.navigationItem.leftBarButtonItem = nil;
    }
    self.navigationItem.rightBarButtonItem = nil;
}

#pragma mark - Table View actions
- (void)paygSelected {
    meter.image = [UIImage imageNamed:@"meter_payg.png"];
    [expiresAtTimer invalidate];
    expiresAtTime.hidden = YES;
    paygCheck.image = [UIImage imageNamed:@"check.png"];
    prepaidCheck.image = [UIImage imageNamed:@"check_empty.png"];
    if (paygFlag.hidden) {
        [self animateFlagIn:paygFlag];
        [self animateFlagOut:prepaidFlag];
    }
    prepaidAmount.text = @"Enter Amount";
    prepaidAmount.textColor = [UIColor disabledTextColor];
    unparkButton.frame = CGRectMake(10, 10, 300, 52);
    unparkButton.titleLabel.font = [UIFont boldSystemFontOfSize:21.5];
    hours.text = @"00";
    minutes.text = @"00";
    [self resignPicker];
}

- (void)prepaidSelected {
    
    meter.image = [UIImage imageNamed:@"meter_prepaid.png"];
    expiresAtTime.hidden = NO;
    paygCheck.image = [UIImage imageNamed:@"check_empty.png"];
    prepaidCheck.image = [UIImage imageNamed:@"check.png"];
    if (prepaidFlag.hidden) {
        [self animateFlagOut:paygFlag];
        [self animateFlagIn:prepaidFlag];
    }

    prepaidAmount.textColor = [UIColor whiteColor];
    unparkButton.frame = CGRectMake(165, 10, 145, 52);
    unparkButton.titleLabel.font = [UIFont boldSystemFontOfSize:19];
    NSTimeInterval nextMinute = ceil([[NSDate date] timeIntervalSinceReferenceDate]/60.0)*60.0;
    expiresAtTimer = [[NSTimer alloc] initWithFireDate:[NSDate dateWithTimeIntervalSinceReferenceDate:nextMinute] interval:60.0 target:self selector:@selector(updateExpiresAtTime) userInfo:nil repeats:YES];
    [[NSRunLoop currentRunLoop] addTimer:expiresAtTimer forMode:NSDefaultRunLoopMode];
    [self activatePicker];
}

- (void)showParkedCar {
    [self performSegueWithIdentifier:@"showParkedCar" sender:self];
}

#pragma mark - UIViewController
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"showParkedCar"]) {
//        PQParkedCarMapViewController *parkedCarVC = [segue destinationViewController];
    }
}

#pragma mark - Bar Button actions
- (IBAction)cancelButtonPressed:(id)sender {
    if (parkState == kParkingParkState) {
        NSIndexPath *paygIndexPath = [NSIndexPath indexPathForRow:0 inSection:0];
        if ([self.tableView indexPathForSelectedRow] != nil) { // Date picker is showing
            [self.tableView selectRowAtIndexPath:paygIndexPath animated:NO scrollPosition:UITableViewScrollPositionNone];
            [self paygSelected];
            [self.tableView deselectRowAtIndexPath:paygIndexPath animated:YES];
        } else { // Date picker is not showing
            [self dismissModalViewControllerAnimated:YES];
        }
    } else { // parkState == kExtendingParkState
        [self parkedAfterExtending];
    }
}

- (IBAction)doneWithPicker:(id)sender {
    if (parkState == kParkingParkState) {
        [self.tableView deselectRowAtIndexPath:[NSIndexPath indexPathForRow:1 inSection:0] animated:YES];
        prepaidAmount.textColor = [UIColor activeTextColor];
        [self resignPicker];
    } else {
        int totalCents = ceil(self.rate * (datePicker.countDownDuration/60));
        UIActionSheet *extendActionSheet = [[UIActionSheet alloc] initWithTitle:[NSString stringWithFormat:@"After extending, you will be charged $%d.%02d and your parking will expire at %@.", totalCents/100, totalCents%100, [dateFormatter stringFromDate:[NSDate dateWithTimeInterval:datePicker.countDownDuration sinceDate:prepaidEndTime]]] delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles:@"Extend", nil];
        extendActionSheet.actionSheetStyle = UIActionSheetStyleBlackOpaque;
        extendActionSheet.tag = ACTIONSHEET_EXTEND;
        [extendActionSheet showInView:self.tableView];

    }
}

#pragma mark - UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (parkState == kParkingParkState) {
        if (indexPath.section == 0) {
            if (indexPath.row == 0) {
                [self paygSelected];
                [tableView deselectRowAtIndexPath:indexPath animated:YES];
            } else { // indexPath.row == 1
                [self prepaidSelected];
            }
        }
    }
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    if (parkState == kParkingParkState) {
        return @"Select payment method";
    } else if (parkState == kParkedParkState) {
        return @"Your car\u2019s parking location";
    } else { // parkState == kExtendingParkState
        return @"Specify amount of additional time";
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
	label.textColor = [UIColor colorWithRed:0.265 green:0.294 blue:0.367 alpha:1.0];
	label.text = [self tableView:tableView titleForHeaderInSection:section];
	[containerView addSubview:label];

    if (parkState == kParkingParkState) {
        UIButton *infoButton = [UIButton buttonWithType: UIButtonTypeInfoDark];
        infoButton.frame = CGRectMake(288, 12, 14, 14);
        [infoButton addTarget:self action:@selector(infoButtonPressed)
          forControlEvents: UIControlEventTouchUpInside];
        [containerView addSubview:infoButton];
    }
	return containerView;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 36;
}

#pragma mark - Animations
- (void)animateFlagIn:(UIView *)flag {
    flag.hidden = NO;
    [UIView animateWithDuration:0.5 delay:0.15 options:UIViewAnimationCurveEaseOut animations:^{
        flag.transform = CGAffineTransformRotate(CGAffineTransformIdentity, -M_PI/24);
    } completion:^(BOOL s){
        [UIView animateWithDuration:0.4 animations:^{
            flag.transform = CGAffineTransformRotate(CGAffineTransformIdentity, M_PI/64);
        } completion:^(BOOL t) {
            [UIView animateWithDuration:0.3 animations:^{
                flag.transform = CGAffineTransformIdentity;
            }];
        }];
    }];
}

- (void)animateFlagOut:(UIView *)flag {
    [UIView animateWithDuration:0.5 delay:0.2 options:UIViewAnimationCurveEaseIn animations:^{
        flag.transform = CGAffineTransformRotate(CGAffineTransformIdentity, M_PI/4);
    } completion:^(BOOL s) {
        flag.hidden = YES;
    }];
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
    self.navigationItem.title =[NSString stringWithFormat:@"%d, %s.\n", spotInfo.spotNumber.intValue, spotInfo.streetName.UTF8String];
    addressLabel.text = spotInfo.streetName;
    
    rateNumeratorCents = [spotInfo.rateCents intValue];
    rateDenominatorMinutes = [spotInfo.minuteInterval intValue];
    limit = [spotInfo.maxTime intValue];

    // Initialize variables
    parkState = kParkingParkState;
    dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"h:mma"];

    // Set values not settable in IB (resizeable backgrounds, fonts, etc)
    [startButton setBackgroundImage:[[UIImage imageNamed:@"green_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];
    [unparkButton setBackgroundImage:[[UIImage imageNamed:@"red_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];
    [extendButton setBackgroundImage:[[UIImage imageNamed:@"green_button.png"] resizableImageWithCapInsets:UIEdgeInsetsMake(0, 14, 0, 14)] forState:UIControlStateNormal];

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
    [seeMapCellView addGestureRecognizer:seeMapRecognizer];

    // Set anchor points for flag animations and begin first animation
    paygFlag.layer.anchorPoint = CGPointMake(0.0,1.0);
    paygFlag.center = CGPointMake(0, 99);
    prepaidFlag.layer.anchorPoint = CGPointMake(0.0,1.0);
    prepaidFlag.center = CGPointMake(0, 102);
    paygFlag.transform = CGAffineTransformRotate(CGAffineTransformIdentity, M_PI/4);
    [self animateFlagIn:paygFlag];
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
    [self setPaygCellView:nil];
    [self setAddressCellView:nil];
    [self setPrepaidCellView:nil];
    [self setSeeMapCellView:nil];
    [self setUnparkButton:nil];
    [self setExtendButton:nil];
    [self setDatePicker:nil];
    [self setRateNumerator:nil];
    [self setRateDenominator:nil];
    [self setLimitValue:nil];
    [self setLimitUnit:nil];
    [self setAddressLabel:nil];
    [self setTimeRemainingCellView:nil];
    [self setTimeToAddCellView:nil];
    [self setExtendAmount:nil];
    [self setRemainingAmount:nil];
    [self setColon:nil];
    [self setCancelButton:nil];
    [self setDoneButton:nil];
    [self setExpiresAtTime:nil];
    [self setMeter:nil];
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
