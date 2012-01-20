//
//  ParqSpotViewController.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqSpotViewController.h"

@implementation ParqSpotViewController
@synthesize scrollView = _scrollView;
@synthesize spotNumField = _spotNumField;


-(IBAction)parqButton{
    //submit gps coordinates and spot to server.   
    RateObject* rateObj = [ServerCalls getRateLat:self.userLat Lon:self.userLon spotId:_spotNumField.text];
    //check response from server before allowing next view.  

}
-(IBAction)scanButton{
    //launch scanner and grab results.  
    //create new view for scanning
    ZBarReaderViewController * reader = [ZBarReaderViewController new];
    
    //set the delegate to receive results
    reader.readerDelegate = self;
    
    //configure reader for type of barcode
    [reader.scanner setSymbology:ZBAR_QRCODE config:ZBAR_CFG_ENABLE to:0];
    
    //present the scanner
    [self presentModalViewController:reader animated:YES];
    //check resposne from serve before showing next view.  
}

//this method is essentially onActivityResult()
-(void) imagePickerController:(UIImagePickerController *)reader didFinishPickingMediaWithInfo:(NSDictionary *)info{
    id<NSFastEnumeration> results = [info objectForKey:ZBarReaderControllerResults];
    ZBarSymbol* dunno;
    //apparently, this scanner can return multiple results.  i know lol.  
    for(dunno in results)
        break; //this just grabs the first one, sigh weird stuff.  

    NSArray* splitUrl = [dunno.data componentsSeparatedByString:@"/"];
    NSString* lotId = [splitUrl objectAtIndex:3];
    NSString* spotId = [splitUrl objectAtIndex:4];
    RateObject* rateObj = [ServerCalls getRateLotId:lotId spotId:spotId];
    //TODO: launch next screen using this rate object.      
    
    
    //LOGGING CODE STARTS
    NSLog(@"\nLOG>>> returned dictionary %@", info.description);
    NSLog(@"\nEXTRACTED DATA");
    NSLog(@"\nLOG>>> %@", dunno.data);
    NSLog(@"\nLOG>>> %@", dunno.description);
    NSLog(@"\nLOG>>> %@", dunno.typeName);
    NSLog(@"\nLOG>>> %@", dunno.typeName );
    //LOGGING CODE ENDS
    
    
    //once we display the result string, dismiss the scanner.  
    [reader dismissModalViewControllerAnimated:YES];
    
}


// Call this method somewhere in your view controller setup code.
- (void)registerForKeyboardNotifications
{
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(keyboardWasShown:)
                                               name:UIKeyboardDidShowNotification object:nil];
  
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(keyboardWillBeHidden:)
                                               name:UIKeyboardWillHideNotification object:nil];
  
}

// Called when the UIKeyboardDidShowNotification is sent.
- (void)keyboardWasShown:(NSNotification*)aNotification
{
  NSDictionary* info = [aNotification userInfo];
  CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
  
  UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
  self.scrollView.contentInset = contentInsets;
  self.scrollView.scrollIndicatorInsets = contentInsets;
  
  // If active text field is hidden by keyboard, scroll it so it's visible
  // Your application might not need or want this behavior.
  CGRect aRect = self.view.frame;
  const int rectHeight = aRect.size.height;
  aRect.size.height -= kbSize.height;
  if (!CGRectContainsPoint(aRect, self.spotNumField.frame.origin) ) {
    const int y = self.spotNumField.frame.origin.y;
    CGPoint scrollPoint = CGPointMake(0.0, kbSize.height-(rectHeight-y));
    [self.scrollView setContentOffset:scrollPoint animated:YES];
  }
}

// Called when the UIKeyboardWillHideNotification is sent
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
  UIEdgeInsets contentInsets = UIEdgeInsetsZero;
  self.scrollView.contentInset = contentInsets;
  self.scrollView.scrollIndicatorInsets = contentInsets;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (BOOL)isLoggedIn
{
  NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
  return [defaults stringForKey:@"email"] != nil;
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self registerForKeyboardNotifications];
}

- (void)viewDidUnload
{
  [self setScrollView:nil];
  [self setSpotNumField:nil];
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

    if (![self isLoggedIn]) {
      UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
      UIViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
      [vc setModalPresentationStyle:UIModalPresentationFullScreen];

      [self presentModalViewController:vc animated:YES];
    }
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
  return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

@end
