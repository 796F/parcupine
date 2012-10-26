//
//  PQLoginViewController.m
//  Parq
//
//  Created by Michael Xia on 7/22/12.
//  Copyright (c) 2012 PARQ LLC. All rights reserved.
//

#import "PQLoginViewController.h"
#import "PQAppDelegate.h"
#import "MBProgressHUD.h"

#define TOS_ALERT_VIEW 0
#define REGISTER_SURE_ALERT 1

@implementation PQLoginViewController
@synthesize parent;
@synthesize passwordField;
@synthesize emailField;
@synthesize entireScreen;
@synthesize navBar;
@synthesize whiteButton;
@synthesize registerButton;
@synthesize submitButton;
@synthesize confirmPasswordField;
@synthesize licensePlateField;



-(IBAction)submitButtonPressed:(id)sender{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    BOOL loginResp = [self tryLoggingIn];
    if(loginResp){
        [dataLayer setLoggedIn:YES];
        parent.view.hidden = NO;
        [self dismissModalViewControllerAnimated:YES];
    }else{
        //unhide email/password box.
        emailField.hidden = NO;
        [dataLayer setLoggedIn:NO];
    }
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

-(BOOL) tryLoggingIn{
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    NSDate* studyEnd = [NSDate dateWithTimeIntervalSince1970:1353490425];
    if([[NSDate date] earlierDate:studyEnd] == studyEnd){
        UIAlertView* studyOver = [[UIAlertView alloc] initWithTitle:@"Thank You" message:@"The study has concluded and systems shut down" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [studyOver show];
        return NO;
    }
    User* user;
    //call network layer to login.
    if(confirmPasswordField.hidden == NO){
        //registering
        user = [networkLayer registerEmail:emailField.text AndPassword:@"a" AndPlate:confirmPasswordField.text];
    }else{
        user = [networkLayer loginEmail:emailField.text AndPassword:@"a"];
    }
    if(user!=nil){
        //correct!
        return YES;
    }else{
        //launch alert.  
        UIAlertView* spotTakenAlert = [[UIAlertView alloc] initWithTitle:@"Could not log in!" message:@"Please check input" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [spotTakenAlert show];
        return NO;
    }
}

-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    if (textField.tag==0) {
        //this is email field.  simply go next
        if(passwordField.hidden == YES){
            //logging in
            passwordField.hidden = YES;
            emailField.hidden = YES;
            //display a LOADING CIRCLE thing.
            
            
            BOOL loginResp = [self tryLoggingIn];
            if(loginResp){
                [dataLayer setLoggedIn:YES];
                parent.view.hidden = NO;
                [self dismissModalViewControllerAnimated:YES];
            }else{
                //unhide email/password box.
                emailField.hidden = NO;
                [dataLayer setLoggedIn:NO];
            }

        }else{
            //in register mode.
            
//            [confirmPasswordField becomeFirstResponder];
            [passwordField becomeFirstResponder];
        }
        
    }else if (textField.tag==1){
        //hide the email/password box.
        [confirmPasswordField becomeFirstResponder];
    }
//    else if (textField.tag ==2) {
//        //go next
//        [licensePlateField becomeFirstResponder];
//    }
    else {
        //register the user.  
//        NSString* password = [passwordField text];
//        NSString* email = [emailField text];
//        NSString* license = licensePlateField.text;
        [self submitButtonPressed:self];
        [licensePlateField resignFirstResponder];
        [self removeKeyboard:nil];
    }
    return YES;
}

-(void) textFieldDidBeginEditing:(UITextField *)textField{
    //started editing, shift the view upwards
    if(passwordField.returnKeyType == UIReturnKeyGo){
        //in submit mode, simply shift up  a bit.  
        [UIView animateWithDuration:.25 animations:^{
            //x y width height
            self.entireScreen.frame = CGRectMake(0, -95, 320, 460);
            
        }];
    }else{
        //in register mode, shift to keep fields visible  
        [UIView animateWithDuration:.25 animations:^{
            //x y width height
            double originy = textField.frame.origin.y;
            //255, 283, 313, 344  make these -95
            self.entireScreen.frame = CGRectMake(0, 178 - originy , 320, 460);
            
        }];
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag == TOS_ALERT_VIEW && buttonIndex == 1){
        //agree to tos.  allow them to register.  
        
        //expand the button to cover more space. 140 height
        [UIView animateWithDuration:0.25 animations:^{
            whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 97);
//            self.entireScreen.center = CGPointMake(entireScreen.center.x, entireScreen.center.y-80);
            [self textFieldDidBeginEditing:self.emailField];
            registerButton.hidden = YES;
            self.emailField.returnKeyType = UIReturnKeyNext;
            self.passwordField.returnKeyType = UIReturnKeyNext;
            submitButton.hidden = NO;
            passwordField.hidden = NO;
            confirmPasswordField.hidden = NO;
            emailField.text = @"";
            passwordField.text = @"";
        }];
//        [emailField becomeFirstResponder];

//        [UIView animateWithDuration:0.5 animations:^{
//            self.navBar.center = CGPointMake(navBar.center.x, navBar.center.y + 44);
//        }];
//        [self performSegueWithIdentifier:@"showRegisterView" sender:self];
    }else if (alertView.tag == REGISTER_SURE_ALERT && buttonIndex == 1){
        //user is sure they want to oparticipate in study.  
        
    }
}

- (void)removeKeyboard:(UIGestureRecognizer *)sender {
    [self.passwordField resignFirstResponder];
    [self.emailField resignFirstResponder];
    [self.confirmPasswordField resignFirstResponder];
    [self.licensePlateField resignFirstResponder];
    [UIView animateWithDuration:.25 animations:^{
        //x y width height
        self.entireScreen.frame = CGRectMake(0, 0, 320, 460);
        whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 39);
        registerButton.hidden = NO;
        submitButton.hidden = YES;
        passwordField.hidden = YES;
        confirmPasswordField.hidden = YES;
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
    //upon login screen appearing, hide the nav bar.  
    navBar.hidden = YES;
    [UIView animateWithDuration:0.5 animations:^{
        navBar.center = CGPointMake(navBar.center.x, navBar.center.y - 44);
        //adjust the screen to make up for the nav bar disappearing.  
        self.entireScreen.frame = CGRectMake(0, 0, 320, 460);
    }];    
}
-(void) viewDidAppear:(BOOL)animated{
    
    if(![dataLayer hasMockData]){
        //if mock data hasn't been loaded yet.
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.labelText = @"Loading Data...";
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            [dataLayer loadMockData];
            [networkLayer loadSpotData];
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideHUDForView:self.view animated:YES];
            });
        });
    }
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
    [self.entireScreen addGestureRecognizer:singleTap];
    navBar = self.navigationController.navigationBar;
    parent = ((LoginNavigationController*) self.navigationController).parent; //share parent.
    
}

- (void)viewDidUnload
{
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
