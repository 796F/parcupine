//
//  ParqLoginViewController.m
//  Parq
//
//  Created by Mark Yen on 1/6/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqLoginViewController.h"
#import "SFHFKeychainUtils.h"
#import "ServerCalls.h"
#import "SavedInfo.h"
#import "ParqSignUpViewController1.h"

@implementation ParqLoginViewController
@synthesize emailControl;
@synthesize passwordControl;
@synthesize parent;

-(BOOL)textFieldShouldReturn:(UITextField*)textField{
    NSInteger nextTag = textField.tag+1;
    UIResponder* nextResponder = [textField.superview viewWithTag:nextTag];
    if(nextResponder){
        [nextResponder becomeFirstResponder];
    }else{
        [textField resignFirstResponder];
    }
    return NO;
}

- (void)saveUserInfoWithEmail:(NSString*)email andPassword:(NSString*)password andUserObj:(UserObject*)user
{
    NSError *error = nil;
    [SFHFKeychainUtils storeUsername:email andPassword:password forServiceName:@"com.parqme" updateExisting:YES error:&error];
    [SavedInfo logIn:user.parkState Email:email UID:user.uid ccStub:user.creditCardStub];
}
- (IBAction)goPressed {
    [self logUserIn];
}

- (void)logInAfterSigningUp:(NSString*)email password:(NSString*)password {
    UserObject *user = [ServerCalls authEmail:email Password:password];
    if (user != nil) {
        [self saveUserInfoWithEmail:email andPassword:password andUserObj:user];
        [self dismissModalViewControllerAnimated:YES];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't log in"
                                                        message:@"Please check your email and password"
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
    [passwordControl addTarget:self action:@selector(logUserIn:) forControlEvents:UIControlEventEditingDidEndOnExit];
    self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"backgroundlayer.png"]];
}

- (void)viewDidUnload
{
  [self setEmailControl:nil];
  [self setPasswordControl:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    passwordControl.secureTextEntry = YES;
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

-(void)logUserIn{
    UserObject *user = [ServerCalls authEmail:emailControl.text Password:passwordControl.text];
    if (user != nil) {
        [self saveUserInfoWithEmail:emailControl.text andPassword:passwordControl.text andUserObj:user];
        [self dismissModalViewControllerAnimated:YES];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't log in"
                                                        message:@"Please check your email and password"
                                                       delegate:nil 
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }

}
#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    if (indexPath.section == 1) {
      [tableView deselectRowAtIndexPath:indexPath animated:YES];
      if (indexPath.row == 0) {
          [self logUserIn];
      } else if (indexPath.row == 1) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
        UINavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"RegisterController"];
        [vc setModalPresentationStyle:UIModalPresentationFullScreen];
        ParqSignUpViewController1 *vcTop = [[vc viewControllers] objectAtIndex:0];
        vcTop.parent = self;

        [self presentModalViewController:vc animated:YES];
      }
    }
}

@end
