//
//  PQBookmarksViewController.m
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQBookmarksViewController.h"
#import "BookmarkCell.h"



@interface PQBookmarksViewController ()

@end

@implementation PQBookmarksViewController
@synthesize table;
@synthesize bookmarkSelectionBar;
@synthesize bookmarks = _bookmarks;
@synthesize recentSearches = _recentSearches;
@synthesize contacts = _contacts;
@synthesize leftButton;
@synthesize midButton;
@synthesize userIsEditing;
@synthesize rightButton;

@synthesize parent;


#pragma mark - ACTION SHEET
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if(buttonIndex==0){
        //clear the recents
        [self.recentSearches removeAllObjects];
        [table reloadData];
    }
    //else cancel.  
}
#pragma mark - TABLE VIEW FUNCTIONS

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;
    if(displayType == 0){
        return 1;
    }else if (displayType == 1){
        return 1;
        //split time of search?
    }else{
        //split contacts up into alphabetical sections ???
        return 1;
    }
}
-(UITableViewCellEditingStyle) tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row == 0 && bookmarkSelectionBar.selectedSegmentIndex == 0){
        return UITableViewCellEditingStyleNone;
    }else{
        return UITableViewCellEditingStyleDelete;
    }
}

-(void) tableView:(UITableView *)tableView willBeginEditingRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
}

-(void) tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    NSInteger row = [indexPath row];    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        if(bookmarkSelectionBar.selectedSegmentIndex ==0){
            row-=1;
        }
        [self.bookmarks removeObjectAtIndex:row];
        [UIView animateWithDuration:.7 animations:^{
            [table deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationTop];
        }];

    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    // Return the number of rows in the section.
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;
    if(displayType == 0){
        if (self.bookmarks==nil){
            self.bookmarks = [self loadBookmarks];
        }
        return [self.bookmarks count] + 1;
    }else if(displayType == 1){
        if(self.recentSearches==nil){
            self.recentSearches = [self loadRecent];
        }
        return [self.recentSearches count];
    }else{
        if(self.contacts==nil){
            self.contacts = [self loadContacts];
        }
        return [self.contacts count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    static NSString *CellIdentifier;
    int displayType = bookmarkSelectionBar.selectedSegmentIndex;

    if(indexPath.row==0 && displayType == 0){
        UITableViewCell *cell = [tableView
                                 dequeueReusableCellWithIdentifier:@"currLocCell"];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]
                    initWithStyle:UITableViewCellStyleDefault 
                    reuseIdentifier:CellIdentifier];
        }
        return cell;
    }else{
        BookmarkCell *cell = [tableView
                                 dequeueReusableCellWithIdentifier:@"bookmarkCell"];
        if (cell == nil) {
            cell = [[BookmarkCell alloc]
                    initWithStyle:UITableViewCellStyleDefault 
                    reuseIdentifier:CellIdentifier];
        }
        if (self.bookmarks==nil){
            self.bookmarks = [self loadBookmarks];
        }
        if(displayType==0){
            //current location offsets the index.  
            cell.bookmarkName.text = [self.bookmarks objectAtIndex:[indexPath row]-1];
        }else if(displayType==1){
            //recents
            cell.bookmarkName.text = [self.recentSearches objectAtIndex:[indexPath row]];
        }else{
            //contacts
            cell.bookmarkName.text = [self.contacts objectAtIndex:[indexPath row]];
        }
        //initialize the cell.  
        [cell setRegion:CLLocationCoordinate2DMake(0, 0)];
        [cell setParent:self];
        return cell;
    }
    
    
}

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"BOOKMARK index tapped %d\n", indexPath.row);
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    //find which bookmark that index corresponds to.  
//    BookmarkCell* cell = (BookmarkCell*) [table cellForRowAtIndexPath:indexPath];
    
    CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(42.365102, -71.110841);
    //get rid of this view, and then zoom the map.  
    [self dismissModalViewControllerAnimated:YES];
    [self.parent showBookmarkWithLocation:&coord AndAnnotation:nil];    
}

#pragma mark - segment bar function
- (IBAction)bottomBarTapped {
    switch (self.bookmarkSelectionBar.selectedSegmentIndex) {
        case 0:
            [self hideRedMinus:self.contacts];
            leftButton.title = @"Edit";
            midButton.title = @"Bookmarks";
            [table reloadData];
            break;
        case 1:
            
            leftButton.title = @"Clear";
            midButton.title = @"All Recent";
            [table reloadData];
            break;
        case 2:
            [self hideRedMinus:self.bookmarks];
            leftButton.title = @"Edit";
            midButton.title = @"Contacts";
            [table reloadData];
            break;
    }
}

#pragma mark - red minus animations
-(void) hideRedMinus:(NSArray*) cellList{
    if(userIsEditing==NO){
        //wasn't editing .  do nothing.  
    }else{
        userIsEditing = NO;
        int i, row=0;
        if(bookmarkSelectionBar.selectedSegmentIndex==0){
            row = 1;
        }
        for(i=0; i<cellList.count; i++){
            BookmarkCell* cell = (BookmarkCell*)[table cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row+i inSection:0]];
            cell.selectionStyle = UITableViewCellSelectionStyleBlue;
            [cell endEditing];
        }

    }
}
-(void) showRedMinus:(NSArray*) cellList{
    if(userIsEditing){
        //already shown, do nothing.  
    }else{
        userIsEditing = YES;        
        int i, row=0;
        if(bookmarkSelectionBar.selectedSegmentIndex==0){
            row = 1;
        }
        for(i=0; i<cellList.count; i++){
            BookmarkCell* cell = (BookmarkCell*)[table cellForRowAtIndexPath:[NSIndexPath indexPathForRow:row+i inSection:0]];
            [cell beginEditing];
        }
    }
    
}

-(void) toggleShowRedMinus:(NSArray*) cellList {
    if(userIsEditing){
        //hide, currently editing.  
        [self hideRedMinus:cellList];
    }else{
        //show, not yet editing.  
        [self showRedMinus:cellList];
    }
}
#pragma mark - toolbar buttons
-(IBAction)editButtonPressed:(id)sender{
    
    switch(self.bookmarkSelectionBar.selectedSegmentIndex){
        case 0:
            //this is all a place holder really, best case is to immitate ios bookmarks.
            [self toggleShowRedMinus:self.bookmarks];
            break;
        case 1:{
            
            UIActionSheet *clearRecentActionSheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:@"Clear All Recents" otherButtonTitles: nil];
            clearRecentActionSheet.tag = 1;
            [clearRecentActionSheet showInView:self.view];
            //clear the recent searches
            
            
            }
            break;
        case 2:
            //edit contacts
            [self toggleShowRedMinus:self.contacts];
            break;
    }
    
    //shift all labels to the right a small bit.    
    
    //show red minus signs in the shited space
    
    //also make accessory view on right side appear.  
    
}

-(IBAction)doneButtonPressed:(id)sender{
    [self dismissModalViewControllerAnimated:YES];
}

#pragma mark - view lifecycle
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(NSMutableArray*) loadBookmarks{
    return [[NSMutableArray alloc] initWithObjects:@"Work",@"Gym",@"Lab",@"School", @"Girl",@"Bars", nil];
}

-(NSMutableArray*) loadRecent{
    return [[NSMutableArray alloc] initWithObjects:@"House of Blues", @"SENSEable Labs", @"Baltimore Aquarium", nil];
}
-(NSMutableArray*) loadContacts{
//    ABAddressBookRef addressBook = ABAddressBookCreate( );
//    CFArrayRef allPeople = ABAddressBookCopyArrayOfAllPeople( addressBook );
//    CFIndex nPeople = ABAddressBookGetPersonCount( addressBook );
//    
//    for ( int i = 0; i < nPeople; i++ )
//    {
//        ABRecordRef ref = CFArrayGetValueAtIndex( allPeople, i );
//        CFStringRef name = ABRecordCopyCompositeName(ref);
//        NSLog(@"%@\n", name);
//    }
    return [[NSMutableArray alloc] initWithObjects:@"Baczuk, Eric", @"Biderman, Assaf", @"Bukauskus, Aurimas", @"Long, Sunny", @"Ratti, Carlo", @"Xia, Michael", @"Yen, Mark", @"Zheng, Gordon", nil];

}
- (void)viewDidLoad
{
    [super viewDidLoad];
    //load the array needed from core data.  
    int arrayToLoad = bookmarkSelectionBar.selectedSegmentIndex;
    if(arrayToLoad==0){
        self.bookmarks = [self loadBookmarks];
    }else if(arrayToLoad==1){
        self.recentSearches = [self loadRecent];
        //screw descriptions.  
    }else{
        self.contacts = [self loadContacts];
    }
    userIsEditing = NO;
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
