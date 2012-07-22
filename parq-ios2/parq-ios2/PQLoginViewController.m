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
    NSLog(@"submitting...\n");
    
}

-(IBAction)registerButtonPressed:(id)sender{
    //allow user to register and show waiver for study.  
    NSString* terms = @"Users must agree to not sue the shit out of us when they find out were tracking their movements, looking through their cameras, and conducting human experiments ahaahahahah.  how do you think clair works?  pixie dust and unicorn horns?  we need to predict your movements purely through your cell phone data, so give us a break and hand over the data.";
    UIAlertView* tosAlert = [[UIAlertView alloc] initWithTitle:@"Terms of Service" message:terms delegate:self cancelButtonTitle:@"Quit" otherButtonTitles:@"I Agree", nil];
    tosAlert.tag = TOS_ALERT_VIEW;
    [tosAlert show];
}

-(BOOL) tryLoggingIn{
    //call network layer to login.  
    User* user = [networkLayer loginEmail:emailField.text AndPassword:passwordField.text];
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
        [passwordField becomeFirstResponder];
    }else if (textField.tag==1){
        //hide the email/password box. 
        if(passwordField.returnKeyType == UIReturnKeyGo){
            //currently trying to log in.  
            
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
                passwordField.hidden = NO;
                emailField.hidden = NO;
                [dataLayer setLoggedIn:NO];
            }
        }else{
            //in register mode.  
            [confirmPasswordField becomeFirstResponder];
        }
    }else if (textField.tag ==2) {
        //go next
        [licensePlateField becomeFirstResponder];
    }else {
        //register the user.  
//        NSString* password = [passwordField text];
//        NSString* email = [emailField text];
//        NSString* license = licensePlateField.text;
        [self removeKeyboard:nil];
        [licensePlateField resignFirstResponder];
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
            NSLog(@"%f\n", originy);
            self.entireScreen.frame = CGRectMake(0, 178 - originy , 320, 460);
            
        }];
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    NSLog(@"buttonIndex %d\n", buttonIndex);
    if(alertView.tag == TOS_ALERT_VIEW && buttonIndex == 1){
        //agree to tos.  allow them to register.  
        
        //expand the button to cover more space. 140 height
        [UIView animateWithDuration:0.25 animations:^{
            whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 130);
            self.entireScreen.center = CGPointMake(entireScreen.center.x, entireScreen.center.y-80);
            registerButton.hidden = YES;
            self.passwordField.returnKeyType = UIReturnKeyNext;
            submitButton.hidden = NO;
            confirmPasswordField.hidden = NO;
            licensePlateField.hidden = NO;
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
        whiteButton.frame = CGRectMake(whiteButton.frame.origin.x ,whiteButton.frame.origin.y , 227, 68);
        self.passwordField.returnKeyType = UIReturnKeyGo;
        registerButton.hidden = NO;
        submitButton.hidden = YES;
        confirmPasswordField.hidden = YES;
        licensePlateField.hidden = YES;
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
