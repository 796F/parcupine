//
//  ParqSpotViewController.m
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "ParqMapViewController.h"
#import "ParqParkViewController.h"
#import "SavedInfo.h"
#define LOCATION_ACCURACY 30.0  //this double is meters, we should be fine within 30 meters.  

@interface ParqSpotViewController ()
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (nonatomic) double userLat;
@property (nonatomic) double userLon;
@property (nonatomic) BOOL goodLocation;
@end

@implementation ParqSpotViewController
@synthesize scrollView = _scrollView;
@synthesize locationManager;
@synthesize spotNumField = _spotNumField;
@synthesize userLat;
@synthesize userLon;
@synthesize goodLocation;
@synthesize rateObj = _rateObj;

-(void) gpsParkFunction{
    //submit gps coordinates and spot to server.   
    if(goodLocation){
        //    if (YES) {
        //check response from server before allowing next view. 
        _rateObj = [ServerCalls getRateLat:[NSNumber numberWithDouble:self.userLat] Lon: [NSNumber numberWithDouble:self.userLon] spotId:_spotNumField.text];
        if(_rateObj !=nil){
            [self performSegueWithIdentifier:@"showParkTimePicker" sender:self];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't find spot"
                                                            message:@"There may not be a spot near you"
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
    }else{
        //SHOW "GETTING GPS LOCATION" dialog like android app.  
    }

}

-(IBAction)parqButton{
    [self gpsParkFunction];
}
-(IBAction)scanButton{
    //launch scanner and grab results.  
    //create new view for scanning
    ZBarReaderViewController * reader = [ZBarReaderViewController new];
    
    //set the delegate to receive results
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    //disable all barcode types
    [reader.scanner setSymbology:ZBAR_NONE config:ZBAR_CFG_ENABLE to:0];
    //re-enable qrcode, so we only scan for qr codes.  
    [reader.scanner setSymbology:ZBAR_QRCODE config:ZBAR_CFG_ENABLE to:1];
    
    //present the scanner
    [self presentModalViewController:reader animated:YES];
    //check resposne from serve before showing next view.  
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    ParqParkViewController *vc = segue.destinationViewController;
    vc.spotNumber = _spotNumField.text.intValue;
    vc.rateObj = _rateObj;
}

//this method is essentially onActivityResult()
-(void) imagePickerController:(UIImagePickerController *)reader didFinishPickingMediaWithInfo:(NSDictionary *)info{
    id<NSFastEnumeration> allResults = [info objectForKey:ZBarReaderControllerResults];
    ZBarSymbol* firstResult;
    //apparently, this scanner can return multiple results.  i know lol.  
    for(firstResult in allResults)
        break; //this just grabs the first one, sigh weird stuff.  

    NSArray* splitUrl = [firstResult.data componentsSeparatedByString:@"/"];
    NSString* lotId = [splitUrl objectAtIndex:3];
    NSString* spotId = [splitUrl objectAtIndex:4];
    _rateObj = [ServerCalls getRateLotId:lotId spotId:spotId];

    //once we display the result string, dismiss the scanner.
    [reader dismissModalViewControllerAnimated:YES];

    if(_rateObj!=nil){
        //stop getting gps, user successfully scanned a qr code.  
        [locationManager stopUpdatingLocation];
        [self performSegueWithIdentifier:@"showParkTimePicker" sender:self];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Couldn't find spot"
                                                        message:@"There was a problem looking up your QR code. Please try again."
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    }
}

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation
{
    //if accuracy isn't close enough, don't allow park, keep getting location.  display dialog. 
    //these numbers represent radius, so higher = less accurate.  
    
    double newAccuracy = (newLocation.verticalAccuracy)+(newLocation.horizontalAccuracy);
    //these numbers are in meters.  
    if (newAccuracy < LOCATION_ACCURACY){
        //if accuracy is acceptable, location is good.  
        goodLocation = YES;
        [locationManager stopUpdatingLocation];
    }
    userLat = newLocation.coordinate.latitude;
    userLon = newLocation.coordinate.longitude;
    
}

-(void)startGettingLocation{
    if (nil == locationManager)
        locationManager = [[CLLocationManager alloc] init];  //if doesn't exist make new.  
    locationManager.delegate = self;
    //setting accuracy to be 10 meters.  more powerful but uses battery more.  
    locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
    [locationManager startUpdatingLocation];
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
    return [SavedInfo isLoggedIn];
}
-(IBAction)spotNumGo:(id)sender{
        [self gpsParkFunction];
}


#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self registerForKeyboardNotifications];
    goodLocation=NO;  //when view first loads, set location to false.  
    [self startGettingLocation];   //start getting gps coords
    [_spotNumField addTarget:self action:@selector(spotNumGo:) forControlEvents:UIControlEventEditingDidEndOnExit];
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
    if(!goodLocation){
        //if gps didn't get a good location
        [self startGettingLocation];
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
