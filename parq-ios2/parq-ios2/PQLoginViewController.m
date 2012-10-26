//
//  PQLoginViewController.m
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "PQLoginViewController.h"
#import "PQAppDelegate.h"

#define TOS_ALERT_VIEW 0

@implementation PQLoginViewController
@synthesize parent;
@synthesize confirmEmailField;
@synthesize emailField;
@synthesize whiteButton;
@synthesize registerButton;
@synthesize goButton;
@synthesize submitButton;
@synthesize backLabel;
@synthesize licensePlateField;



-(IBAction)submitButtonPressed:(id)sender{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self tryLoggingIn];
}

-(IBAction)registerButtonPressed:(id)sender{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    NSDate* studyEnd = [NSDate dateWithTimeIntervalSince1970:1353490425];
    if([[NSDate date] earlierDate:studyEnd] == studyEnd){
        [dataLayer setLoggedIn:NO];
        UIAlertView* studyOver = [[UIAlertView alloc] initWithTitle:@"Thank You" message:@"The study has concluded and systems shut down" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [studyOver show];
    }else{
    //allow user to register and show waiver for study.  
    NSString* terms = @"Consent to participate in research study Parcupine, a real-time, crowdsourced parking management system You are asked to participate in a research study conducted by David Lee, MCP, from the Department of Urban Studies and Planning at the Massachusetts Institute of Technology (M.I.T.). The purpose of the study is to better understand how mobile applications can assist users in finding available parking spaces, and to collectively manage a database of parking availability. The results of this study will contribute to a Ph.D. research paper written by David Lee. You were selected as a possible participant in this study because you have visited the Parcupine website and indicated that you wish to download the mobile application to your phone. You should read the information below, and ask questions about anything you do not understand, before deciding whether or not to participate.  \n•	This study is voluntary. You have the right not to use any feature of the application, and to stop and delete the application at any time or for any reason.  \n•	You will be compensated for participating in this research in the following ways:  \n   o	Free use of designated parking spots reserved for use in the Parcupine study on the MIT campus, within the rules of use of the study itself;   \n   o	Monetary compensation for completing parking reports using the application. Each report will take up to two minutes to complete, and will involve answering questions about the occupancy of the designated parking spots within eyesight of you. The compensation amount for each report will vary between ten cents to one dollar, and each report will be posted on the mobile application for users to complete on a first-come-first-serve basis. These amounts will be recorded in our electronic database. You may visit our research office on the MIT campus at any time during the study or in the month after the study completes to claim your accrued compensation.  \n•	All the below data we collect from your use of the mobile application will be stored on secure servers on the MIT campus. This project will be completed by October 1, 2012 and all data will be stored until 2 years after that date:  \n    o	We will require your email address and, for drivers wishing to park in the designated parking spots, license plate number for the duration of the study. This information will be kept confidential and will not be used in the analysis of the data.\n   o	We will also collect location data at certain points when the application is running, as well as “tap” data detailing button presses when using the application. This information will be anonymized, and disassociated from any identifying information about yourself.\n\n    I understand the procedures described above. My questions have been answered to my satisfaction, and I agree to participate in this study. \n\n If you have any questions of concerns about the research, please feel free to contact David Lee (Principal Investigator, david733@mit.edu, 617-324-4474) or Professor Carlo Ratti (Faculty Sponsor, 617-324-4474). Both can be contacted at the following address: MIT Senseable City Laboratory, 77 Massachusetts Ave, Room 9-209, Cambridge, MA 02139.\n\n    If you feel you have been treated unfairly, or you have questions regarding your rights as a research subject, you may contact the Chairman of the Committee of the Use of Humans as Experimental Subjects, M.I.T., Room E25-143b, 77 Massachusetts Ave, Cambridge, MA 02139, phone 1-617-253-6787.";
    UIAlertView* tosAlert = [[UIAlertView alloc] initWithTitle:@"Terms of Service" message:terms delegate:self cancelButtonTitle:@"Disagree" otherButtonTitles:@"Agree", nil];

    tosAlert.tag = TOS_ALERT_VIEW;
    [tosAlert show];
    }
}

-(void) tryLoggingIn{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    NSDate* studyEnd = [NSDate dateWithTimeIntervalSince1970:1353490425];
    if([[NSDate date] earlierDate:studyEnd] == studyEnd){
        UIAlertView* studyOver = [[UIAlertView alloc] initWithTitle:@"Thank You" message:@"The study has concluded and systems shut down" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [studyOver show];
        //unhide email/password box.
        emailField.hidden = NO;
        [dataLayer setLoggedIn:NO];
    }
    User* user;
    //call network layer to login.
    if(confirmEmailField.hidden == NO){
        //registering
        user = [networkLayer registerEmail:emailField.text AndPassword:@"a" AndPlate:licensePlateField.text];
    }else{
        user = [networkLayer loginEmail:emailField.text AndPassword:@"a"];
    }
    if(user!=nil){
        //correct!
        [dataLayer setLoggedIn:YES];
        parent.view.hidden = NO;
        [self dismissModalViewControllerAnimated:YES];
    }else{
        //launch alert.  
        UIAlertView* spotTakenAlert = [[UIAlertView alloc] initWithTitle:@"Could not log in!" message:@"Please check input" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [spotTakenAlert show];
        //unhide email/password box.
        emailField.hidden = NO;
        [dataLayer setLoggedIn:NO];
    }
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    if (textField.tag==0) {
        //this is email field.  simply go next
        if(confirmEmailField.hidden == YES){
            emailField.hidden = YES;
            //display a LOADING CIRCLE thing.

            [self tryLoggingIn];

        }else{
            //in register mode.
            [confirmEmailField becomeFirstResponder];
        }
        
    }else if (textField.tag==1){
        //hide the email/password box.
        [licensePlateField becomeFirstResponder];
    }
    else {
        //register the user.
        [self submitButtonPressed:self];
    }
    return YES;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField{
    [UIView animateWithDuration:.25 animations:^{
        //255, 283, 313, 344  make these -95
        self.tableView.frame = CGRectMake(0, 178 - textField.frame.origin.y , 320, 460);
    }];
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag == TOS_ALERT_VIEW && buttonIndex == 1){
        //agree to tos.  allow them to register.  
        
        //expand the button to cover more space. 140 height
        [UIView animateWithDuration:0.25 animations:^{
            whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 97);
            [self textFieldDidBeginEditing:self.emailField];
            registerButton.hidden = YES;
            goButton.hidden = YES;
            self.emailField.returnKeyType = UIReturnKeyNext;
            self.confirmEmailField.returnKeyType = UIReturnKeyNext;
            submitButton.hidden = NO;
            backLabel.hidden = NO;
            confirmEmailField.hidden = NO;
            licensePlateField.hidden = NO;
            confirmEmailField.text = @"";
            licensePlateField.text = @"";
        }];
        [emailField becomeFirstResponder];
    }
}

- (void)removeKeyboard:(UIGestureRecognizer *)sender {
    [self.confirmEmailField resignFirstResponder];
    [self.emailField resignFirstResponder];
    [self.confirmEmailField resignFirstResponder];
    [self.licensePlateField resignFirstResponder];
    [UIView animateWithDuration:.25 animations:^{
        //x y width height
        self.tableView.frame = CGRectMake(0, 0, 320, 460);
        whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 39);
        registerButton.hidden = NO;
        goButton.hidden = NO;
        submitButton.hidden = YES;
        backLabel.hidden = YES;
        confirmEmailField.hidden = YES;
        licensePlateField.hidden = YES;
        emailField.returnKeyType = UIReturnKeyGo;
    }];
    
}

#pragma mark - VIEW LIFECYCLE

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void) viewWillAppear:(BOOL)animated{
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
    }
    if(!dataLayer){
        dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
    }
    UITapGestureRecognizer* singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(removeKeyboard:)];
    [self.tableView addGestureRecognizer:singleTap];
    parent = ((LoginNavigationController*) self.navigationController).parent; //share parent.
    
}

- (void)viewDidUnload
{
    [self setBackLabel:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{

    // Return the number of sections.
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{

    // Return the number of rows in the section.
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    // Configure the cell...
    
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

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
