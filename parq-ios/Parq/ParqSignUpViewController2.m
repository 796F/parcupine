//
//  ParqSignUpViewController2.m
//  Parq
//
//  Created by Mark Yen on 1/30/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqSignUpViewController2.h"
#import "ServerCalls.h"

@interface ExpMonthDelegate : NSObject <UIPickerViewDelegate, UIPickerViewDataSource>
@property (weak, nonatomic) ParqSignUpViewController2 *parent;
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView;
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component;
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component;
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component;
@end

@interface ExpYearDelegate : NSObject <UIPickerViewDelegate, UIPickerViewDataSource>
@property (weak, nonatomic) ParqSignUpViewController2 *parent;
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView;
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component;
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component;
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component;
@end

@interface ParqSignUpViewController2 ()
@property (strong, nonatomic) UIPickerView *expMonthPicker;
@property (strong, nonatomic) UIPickerView *expYearPicker;
@property (strong, nonatomic) ExpMonthDelegate *expMonthDelegate;
@property (strong, nonatomic) ExpYearDelegate *expYearDelegate;
@end

@implementation ParqSignUpViewController2
@synthesize ccNumField;
@synthesize cscField;
@synthesize addressField;
@synthesize zipField;
@synthesize expMonthField;
@synthesize expYearField;
@synthesize name;
@synthesize email;
@synthesize password;
@synthesize expMonth;
@synthesize expYear;
@synthesize parent;
@synthesize expMonthPicker;
@synthesize expYearPicker;
@synthesize expMonthDelegate;
@synthesize expYearDelegate;

/**
 * Returns the error string, or nil if there is no error
 */
- (NSString*)validate {
    return nil;
}

- (IBAction)doneButton:(id)sender {
    NSString *errorString = [self validate];
    if (errorString.length == 0) {
        const BOOL success = [ServerCalls registerEmail:email Password:password CreditCard:ccNumField.text cscNumber:cscField.text HolderName:name BillingAddress:addressField.text ExpMonth:[NSString stringWithFormat:@"%d", expMonth] ExpYear:[NSString stringWithFormat:@"%d", expYear] Zipcode:zipField.text];
        if (success) {
            [parent logInAfterSigningUp:email password:password];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't sign up"
                                                            message:@"Please try again."
                                                           delegate:nil 
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Check your input"
                                                        message:errorString
                                                       delegate:nil 
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
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

- (void)viewDidLoad
{
    [super viewDidLoad];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;

    expMonthPicker = [[UIPickerView alloc] init];
    expMonthDelegate = [[ExpMonthDelegate alloc] init];
    expMonthDelegate.parent = self;
    expMonthPicker.delegate = expMonthDelegate;
    expMonthPicker.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    expMonthPicker.showsSelectionIndicator = YES;
    expMonthField.inputView = expMonthPicker;

    expYearPicker = [[UIPickerView alloc] init];
    expYearDelegate = [[ExpYearDelegate alloc] init];
    expYearDelegate.parent = self;
    expYearPicker.delegate = expYearDelegate;
    expYearPicker.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    expYearPicker.showsSelectionIndicator = YES;
    expYearField.inputView = expYearPicker;
}

- (void)viewDidUnload
{
    [self setCcNumField:nil];
    [self setCscField:nil];
    [self setAddressField:nil];
    [self setZipField:nil];
    [self setExpMonthField:nil];
    [self setExpYearField:nil];
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

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end

@implementation ExpMonthDelegate
@synthesize parent;
+ (NSString *)monthFromRow:(int)row {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    return [[dateFormatter monthSymbols] objectAtIndex:row];
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return 12;
}
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    return [ExpMonthDelegate monthFromRow:row];
}
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    parent.expMonthField.text = [ExpMonthDelegate monthFromRow:row];
    parent.expMonth = row+1;
}
@end

@implementation ExpYearDelegate
@synthesize parent;
+ (int)yearFromRow:(int)row {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy"];
    const int currentYear = [[dateFormatter stringFromDate:[NSDate date]] intValue];
    return currentYear+row;
}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return 50;
}
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    return [NSString stringWithFormat:@"%d",[ExpYearDelegate yearFromRow:row]];
}
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    const int year = [ExpYearDelegate yearFromRow:row];
    parent.expYearField.text = [NSString stringWithFormat:@"%d",year];
    parent.expYear = year;
}
@end
