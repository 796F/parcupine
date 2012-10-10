//
//  PQMapViewController.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQMapViewController.h"
#import "PQParkingViewController.h"
#import "PQSettingsViewController.h"
#import "PQSpotAnnotation.h"
#import "NetworkLayer.h"
#import "MBProgressHUD.h"

//calculation constants
#define METERS_PER_MILE 1609.344
#define MAX_CALLOUTS 8
//maximum number that gets loaded until we start erasing.
#define MAX_GRIDS 100
#define MAX_STREETS 100
#define MAX_SPOTS 200

#define GREY_CIRCLE_R 12
#define CALLOUT_LINE_LENGTH 0.00000018
#define CALLOUT_WIDTH 0.00016
#define CALLOUT_HEIGHT 0.0003

/* 
 grid to street  dlon 0.009102
 street to spot  dlon 0.003932
 */

//map level span control constnats.  
#define GRID_SPAN_UPPER_LIMIT 0.035748
#define GRID_TO_STREET_SPAN_LAT 0.014040
#define STREET_TO_SPOT_SPAN_LAT 0.003 
//2387
#define SPOT_LEVEL_SPAN 0.001649

#define ACCURACY_LIMIT 100
#define USER_DISTANCE_FROM_SPOT_THRESHOLD 0.0001
#define GRID_LEVEL_REGION_METERS 1150
#define STREET_LEVEL_REGION_METERS 500
#define SPOT_LEVEL_REGION_METERS 100

//alert view tags
#define GPS_LAUNCH_ALERT 0
#define SPOT_LOOKS_TAKEN_ALERT 1

typedef enum {
    kClearZoomLevel,
    kGridZoomLevel,
    kStreetZoomLevel,
    kSpotZoomLevel
} ZoomLevel;

typedef enum {
    kAvailabilityData,
    kPriceData,
    kNoneData
} DataType;

typedef struct{
    CLLocationCoordinate2D A;
    CLLocationCoordinate2D B;
} SegTwo;

@interface PQMapViewController ()
@property (strong, nonatomic) UIView *disableViewOverlay;
@property (strong, nonatomic) UIBarButtonItem *leftBarButton;
@property (nonatomic) ZoomLevel zoomState;
@property (nonatomic) DataType displayedData;
@end

@implementation PQMapViewController
@synthesize bookmarkPin;
@synthesize gCircle;
@synthesize callouts;
@synthesize calloutLines;
@synthesize map;
@synthesize topSearchBar;
@synthesize availabilitySelectionView;
@synthesize justParkSelectionView;
@synthesize bottomSpotSelectionView;
@synthesize topSpotSelectionView;
@synthesize gradientIcon;
@synthesize navigationBar;
@synthesize bottomSpotSelectionBar;

@synthesize topSpotSelectionBar;
@synthesize geocoder;
@synthesize disableViewOverlay;
@synthesize leftBarButton;
@synthesize parkMeButton;
@synthesize dropPinButton;
@synthesize findMeButton;
@synthesize zoomState;
@synthesize displayedData;
@synthesize cancelDropPinButton;
@synthesize dropPinSelectionView;

@synthesize managedObjectContext;
@synthesize user_loc;
@synthesize user_loc_isGood;
@synthesize desired_spot;
@synthesize availabilitySelectionBar;
@synthesize oldStreetLevelRegion;
@synthesize shouldNotClearOverlays;
@synthesize gridMicroBlockMap;
@synthesize streetMicroBlockMap;
@synthesize spotMicroBlockMap;
@synthesize mockDataButton;
@synthesize currentMicroBlockIds;
//@synthesize currentGridMBIDs;
//@synthesize currentSpotMBIDs;

@synthesize spotInfo;
@synthesize allInsideCircle;
@synthesize doubleTapAlreadyCalled;
@synthesize isDroppingPin;

@synthesize popOutSpotNumberView;
@synthesize popOutSpotNumberField;

-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    NSLog(@"\n <<<<PQMapViewController:tableView did select row index path says hiiii>>>>\n\n");
}

-(NSArray*)loadCambridgeBlockData{
        NSArray* data = [NSArray arrayWithObjects:
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.377235,-71.11056;42.37505,-71.11112", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.377625,-71.109436", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.37763,-71.10812", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375122,-71.11163;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37439,-71.105896;42.374706,-71.10835", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37738,-71.11063;42.3779,-71.10813;42.377975,-71.10781", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376755,-71.10657;42.377975,-71.10781", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.378284,-71.10638;42.378235,-71.106316;42.377125,-71.10495", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.377125,-71.10495;42.374928,-71.10229", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375214,-71.10415;42.37417,-71.103035", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37439,-71.105896;42.37624,-71.105446;42.376278,-71.10542", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372902,-71.108215;42.374527,-71.10706", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37268,-71.107506;42.37441,-71.10612", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37718,-71.104866;42.377632,-71.10418;42.3776,-71.104225", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376625,-71.10436;42.377186,-71.10354", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376747,-71.102776;42.376114,-71.10372", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37323,-71.10557;42.373863,-71.105", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373417,-71.103935;42.374065,-71.103424", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372925,-71.106316;42.37326,-71.10702", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372986,-71.10499;42.37371,-71.10665", @"line", [NSNumber numberWithInt:3], @"color", nil],                   
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.373196,-71.110825", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3779,-71.10813;42.37738,-71.11063", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37738,-71.11063;42.37695,-71.112656", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37695,-71.112656;42.376312,-71.11561", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3791,-71.11478;42.37669,-71.11379", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37637,-71.11573;42.378574,-71.11655", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.378315,-71.113106;42.37695,-71.112656", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.377625,-71.109436", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37505,-71.11112;42.377235,-71.11056", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.375263,-71.11274;42.377007,-71.11236", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374928,-71.11007;42.37536,-71.11342", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.374928,-71.11007", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372894,-71.11496;42.374832,-71.11446", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372692,-71.11407;42.374348,-71.1136", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37384,-71.11245;42.37282,-71.112946", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.374706,-71.10835", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37397,-71.11278;42.37451,-71.11239;42.375202,-71.11227;42.3752,-71.11224", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374348,-71.1136;42.37536,-71.11342", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.376625,-71.11409;42.37585,-71.11429", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374775,-71.10885;42.37763,-71.10812", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.37416,-71.11327", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373196,-71.110825;42.371445,-71.11201", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.371117,-71.11081", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.370808,-71.10961", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372772,-71.109726;42.372055,-71.10795", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372055,-71.10795;42.37119,-71.105736", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371445,-71.11201;42.37005,-71.10692", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369576,-71.11186;42.371117,-71.11081", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374706,-71.10835;42.372772,-71.109726", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374527,-71.10706;42.37232,-71.10861", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37441,-71.10612;42.373062,-71.10718;42.37268,-71.107506;42.372055,-71.10795", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371613,-71.106834;42.37162,-71.10683;42.37323,-71.10557;42.373863,-71.105;42.374233,-71.10471", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37119,-71.105736;42.373417,-71.103935;42.374065,-71.103424", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37381,-71.10213;42.37133,-71.10429;42.370815,-71.10479", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37151,-71.109024;42.370678,-71.10649", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372055,-71.10795;42.37133,-71.108444", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37184,-71.1074;42.37115,-71.107895", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371613,-71.106834;42.37162,-71.10683;42.37094,-71.10728", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3704,-71.103745;42.373707,-71.10126", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37371,-71.10665;42.37323,-71.10557;42.373276,-71.10552", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369995,-71.110176;42.370808,-71.10961", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369205,-71.10783;42.37005,-71.10692", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36975,-71.10586;42.369164,-71.1042", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.3694,-71.1034;42.369865,-71.10471", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374706,-71.10835;42.372772,-71.109726", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.370808,-71.10961", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370808,-71.10961;42.369175,-71.11073", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371117,-71.11081;42.372772,-71.109726", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.373196,-71.110825;42.371445,-71.11201;42.371517,-71.11195", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371834,-71.11337;42.37384,-71.11245", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372074,-71.11425;42.374348,-71.1136", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372894,-71.11496;42.374832,-71.11446", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372074,-71.11425;42.3715,-71.112206;42.370808,-71.10961", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37232,-71.10861;42.373196,-71.110825", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370808,-71.10961;42.37053,-71.108665", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371086,-71.1161;42.369957,-71.11677", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.371414,-71.11706;42.370266,-71.11775", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370495,-71.11448;42.37165,-71.1177", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369366,-71.11465;42.370495,-71.11448", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372604,-71.11723", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.372524,-71.11727;42.37165,-71.1177", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37159,-71.11692;42.372326,-71.11656", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370266,-71.11775;42.3704,-71.11826;42.370632,-71.119286", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369583,-71.11812;42.370266,-71.11775", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370266,-71.11775;42.369957,-71.11677", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.37013,-71.11558;42.370754,-71.11518", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370495,-71.11448;42.3701,-71.11335;42.36986,-71.11258;42.369045,-71.110374", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374348,-71.1136;42.37536,-71.11342", @"line", [NSNumber numberWithInt:5], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.374832,-71.11446;42.37547,-71.114296", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:2], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:4], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360075,-71.094794;42.359591,-71.094086", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.359591,-71.094086;42.358925,-71.093528", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.358925,-71.093528;42.357253,-71.092616", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.370024,-71.113324;42.365998,-71.113539", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365998,-71.113539;42.364587,-71.113882", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.369263,-71.113389;42.367788,-71.109439", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367788,-71.109439;42.365838,-71.106027", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.368533,-71.113473;42.36759,-71.110651", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36759,-71.110651;42.365402,-71.106639", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367915,-71.113527;42.367035,-71.110898", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.367035,-71.110898;42.366163,-71.109171", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366163,-71.109171;42.363896,-71.108828", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366623,-71.110158;42.363722,-71.109707", @"line", [NSNumber numberWithInt:3], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.368938,-71.110308;42.367241,-71.111392", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366805,-71.110383;42.368375,-71.108806", @"line", [NSNumber numberWithInt:1], @"color", nil],
                         [[NSDictionary alloc] initWithObjectsAndKeys:@"42.357714,-71.094522;42.357888,-71.093986", @"line", [NSNumber numberWithInt:3], @"color", nil]
                         ,nil];

    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:data.count];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        for(i=0; i<raw_waypoints.count-1; i++){
            Segment* x =[[Segment alloc] initWithPointsA:&waypoints[i] andB:&waypoints[i+1] andColor:[[line objectForKey:@"color"] intValue]];
            [segList addObject:x];
        }        
    }
    return segList;
}

- (NSArray*)loadBlockData {
    NSArray* data = [NSArray arrayWithObjects:
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364551,-71.113099;42.364753,-71.110776", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.357714,-71.094522;42.357888,-71.093986", @"line", [NSNumber numberWithInt:3], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36643,-71.111047;42.363285,-71.110543", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.364904,-71.112343", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.364904,-71.112343;42.364618,-71.112311", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365352,-71.112211;42.365294,-71.111857", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.365294,-71.111857;42.365383,-71.110889", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36532,-71.111565;42.366043,-71.111667", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366043,-71.111667;42.36622,-71.111839", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.36622,-71.111839;42.366392,-71.112826", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.366788,-71.11193;42.366412,-71.111031", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.360075,-71.094794;42.359591,-71.094086", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.359591,-71.094086;42.358925,-71.093528", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     [[NSDictionary alloc] initWithObjectsAndKeys:@"42.358925,-71.093528;42.357253,-71.092616", @"line", [NSNumber numberWithInt:1], @"color", nil],
                     nil];
    
    NSMutableArray* segList = [[NSMutableArray alloc] initWithCapacity:2];
    for(id line in data){
        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[raw_waypoints.count];
        int i=0;
        for (id raw_waypoint in raw_waypoints) {
            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
            waypoints[i++] = coordinate;
        }
        Segment* x =[[Segment alloc] initWithPointsA:&waypoints[0] andB:&waypoints[1] andColor:[[line objectForKey:@"color"] intValue]];
        [segList addObject:x];
    }
    return segList;
}
#pragma mark - callbacks

-(void) showBookmarkWithLocation:(CLLocationCoordinate2D*) coord AndAnnotation:(id <MKAnnotation>)annotation{
    DLog(@"");
    [self showSpotSelectionViews];
    if(annotation==nil){
        //just zoom to spot, there's no annotation to add.  
        MKCoordinateRegion viewRegion = [self.map regionThatFits:MKCoordinateRegionMakeWithDistance(*coord,SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS)];
        [self.map setRegion:viewRegion animated:YES];
    }else{
        PQPinAnnotation* pin = [[PQPinAnnotation alloc] initWithCoordinate:map.centerCoordinate title:@"title" subTitle:@"subtitle"];
        [self.map addAnnotation:pin];
    }
}

//this is used by the network and data layers to add overlay objects to the map
-(void) addNewOverlays:(NSDictionary*) overlayMap OfType:(EntityType) entityType{
    DLog(@"");
    if(entityType == kGridEntity){
        [gridMicroBlockMap addEntriesFromDictionary:overlayMap];
        for(NSDictionary* overlayDictionary in overlayMap.allValues){
            [self.map addOverlays:overlayDictionary.allValues];
        }
    }else if (entityType == kStreetEntity){
        [streetMicroBlockMap addEntriesFromDictionary:overlayMap];
        for(NSDictionary* overlayDictionary in overlayMap.allValues){
            [self.map addOverlays:overlayDictionary.allValues];
        }
    }else{
        [spotMicroBlockMap addEntriesFromDictionary:overlayMap];
        NSLog(@"%d\n", spotMicroBlockMap.count);
        for(NSDictionary* overlayDictionary in overlayMap.allValues){
            [self.map addAnnotations:overlayDictionary.allValues];
        }
    }
    
}

-(IBAction)justParkMeButtonPressed:(id)sender{
    DLog(@"");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    //show loading screen
    //request server for nearest open spot    
    //zoom user's map to that area
    CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(42.357835,-71.094333);
    [map setRegion:MKCoordinateRegionMakeWithDistance(coord, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS) animated:YES];
    [self showSpotSelectionViews];
    //make callouts appear.
    
}

-(IBAction)selfEnforcePressed:(id)sender{
    NSDate* lastReportTime = [dataLayer getLastReportTime];
    NSDate* anHourAgo = [NSDate dateWithTimeIntervalSinceNow:-3600]; //an hour ago.
    if([[lastReportTime earlierDate:anHourAgo] isEqualToDate:anHourAgo]){
        //reporting happened less than an hour ago
        NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar] ;
        NSDateComponents *components = [calendar components:NSMinuteCalendarUnit
                                                   fromDate:lastReportTime
                                                     toDate:[NSDate date]
                                                    options:0];
        NSString* waitString = [NSString stringWithFormat:@"Please try in %d minutes.",         60 - components.minute];
        UIAlertView* waitXMinutes = [[UIAlertView alloc] initWithTitle:@"We just recieved a report!" message:waitString delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [waitXMinutes show];
    }else{
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
        SelfReportingViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"selfReporting"];
        [vc setParent:self];
        [vc setIsNotParking:YES]; //user isn't parking, no need to trigger timer.
        [vc setUIType:[dataLayer UIType]];
        [vc setModalPresentationStyle:UIModalPresentationFullScreen];
        [self presentModalViewController:vc animated:YES];
    }
}


-(IBAction)dropPinButtonPressed:(id)sender{
    DLog(@"");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self showDropPinBar];
    [self hideAvailabilityBar];
}
-(IBAction)cancelDropPinButtonPressed:(id)sender{
    DLog(@"");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self showTopAndBottomControls];
    [self hideDropPinBar];
}

-(IBAction)findMeButtonPressed:(id)sender{
    DLog(@"");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    CLLocationCoordinate2D userLocation = [[map userLocation] coordinate];
    [map setCenterCoordinate:userLocation animated:YES];
//    [map setRegion:MKCoordinateRegionMakeWithDistance(userLocation, STREET_LEVEL_REGION_METERS, STREET_LEVEL_REGION_METERS) animated:YES];
}

-(void) mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view{
    
}

//this is used by the network layer to update the overlays on the map once server responds.  
-(void) updateOverlays:(NSDictionary*) updateMap OfType:(EntityType) entityType{
    DLog(@"");
    if(updateMap==nil){
        return;
    }
    
    for(NSDictionary* gridsDictionary in gridMicroBlockMap.allValues){
        for(MKPolygon* overlay in gridsDictionary.allValues){
            NSNumber* newAvailability = [updateMap objectForKey:[NSNumber numberWithLong: overlay.objId]];
            if(newAvailability==nil){
                NSLog(@"updated color data not found\n");
                continue;
            }
            MKPolygonView* view = (MKPolygonView*) [map viewForOverlay:overlay];
            switch (newAvailability.intValue) {
                case 0:
                    view.fillColor = [[UIColor veryLowAvailabilityColor] colorWithAlphaComponent:0.2];
                    break;
                case 1:
                    view.fillColor = [[UIColor lowAvailabilityColor] colorWithAlphaComponent:0.2];
                    break;
                case 2:
                    view.fillColor = [[UIColor mediumAvailabilityColor] colorWithAlphaComponent:0.2];
                    break;
                case 3:
                    view.fillColor = [[UIColor highAvailabilityColor] colorWithAlphaComponent:0.2];
                    break;
                case 4:
                    view.fillColor = [[UIColor veryHighAvailabilityColor] colorWithAlphaComponent:0.2];
                    break;
                default:
                    //error??
                    break;
            }
            
        }
    }

    
}

#pragma mark - ACTION SHEET AND ALERTS

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (actionSheet.tag == GPS_LAUNCH_ALERT) {
        switch (buttonIndex) {
//this one's kind of pointless when we have directions.  

//            case 0:
//                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maps.google.com/maps?q=Spot+%d@%f,%f", desired_spot.name, desired_spot.coordinate.latitude, desired_spot.coordinate.longitude]]];
//                break;
            case 0:
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://maps.google.com/maps?daddr=Spot+%d@%f,%f&saddr=Current+Location@%f,%f", desired_spot.name, desired_spot.coordinate.latitude, desired_spot.coordinate.longitude,user_loc.latitude, user_loc.longitude]]];
                break;
            case 1:
                [self parkNow];
                break;
        }
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag == SPOT_LOOKS_TAKEN_ALERT){
        //spot appears taken.  
        if(buttonIndex == 1){
            //yes im sure i wan tto park.  
            [self parkNow];
        }
    }
}

#pragma mark - CALLOUT PLACEMENT

-(bool)A:(double)a sameSignAsB:(double) b{
    if(a>0){
        return b>0 ? true : false;
    }else{
        return b<0 ? true : false;
    }
}

//separates points for a segment to left and right, uses first segment if multiple exist.  
-(NSArray*) newCalloutPlacementWithSegment:(Segment*)seg andSpots:(NSArray*)spotList{
    double left_coords[2] = {0,0};
    double right_coords[2] = {0,0};
    int left_count = 0;
    int right_count = 0;
    bool error = false;
    double reference_dy = 0;
    double reference_dx = 0;
    NSMutableArray* leftSide = [[NSMutableArray alloc] init];
    NSMutableArray* rightSide = [[NSMutableArray alloc] init];
    
    for(MKCircle* spot in spotList){
        bool isRightSide = false;        
        //for each spot inside the grey circle, calculate the projected point.  
        CLLocationCoordinate2D x = spot.coordinate;
        CLLocationCoordinate2D segment[2] = {[seg A], [seg B]};
        CLLocationCoordinate2D point = [self getProjectedPoint:&x A:&segment[0] B:&segment[1] error:&error];
        //don't care about errors in this case.  known to be localized.  
        //determine from that, what side of the street a point lays.  
        if(reference_dy == 0){
            //first one, assign to a side, keep it's sign. 
            reference_dy = x.latitude - point.latitude;
            reference_dx = x.longitude - point.longitude;
            isRightSide = true;
            //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
            [rightSide addObject:spot];
        }else{
            //do a check to see if it's the same side as ref_sign
            //compare latitudes, if useful, go on.  
            double spot_dy = x.latitude - point.latitude;
            double spot_dx = x.longitude - point.longitude;
            if([self A:reference_dx sameSignAsB:spot_dx] && [self A:reference_dy sameSignAsB:spot_dy]){
                //same side as reference.  
                //NSLog(@"%d is right side!\n", [[spotArray objectAtIndex:3] intValue]);
                isRightSide = true;
                [rightSide addObject:spot];
            }else{
                //NSLog(@"%d is left side!\n", [[spotArray objectAtIndex:3] intValue]);
                //different side as reference.  
                isRightSide = false;
                [leftSide addObject:spot];
            }
        }
        //add it to the left or right value depending
        if(isRightSide){
            right_coords[0] += point.latitude;
            right_coords[1] += point.longitude;
            right_count++;
        }else{
            left_coords[0] += point.latitude;
            left_coords[1] += point.longitude;
            left_count++;
        }
    }
    right_coords[0] /= right_count;
    right_coords[1] /= right_count;
    left_coords[0] /= left_count;
    left_coords[1] /= left_count;
    CLLocationCoordinate2D averages[2];
    averages[0] = CLLocationCoordinate2DMake(left_coords[0], left_coords[1]);
    averages[1] = CLLocationCoordinate2DMake(right_coords[0], right_coords[1]);
    
    //now have averages for both left and right, as well as arrays.  
    NSMutableArray* calloutArr = [[NSMutableArray alloc] initWithCapacity:right_count+left_count];
    [calloutArr addObjectsFromArray:[self oneSidedCallout:leftSide aLat:averages[0].latitude aLon:averages[0].longitude]];
    [ calloutArr addObjectsFromArray:[self oneSidedCallout:rightSide aLat:averages[1].latitude aLon:averages[1].longitude]];
    
    return calloutArr;
}

- (NSArray *)calloutBubblePlacement:(CLLocationCoordinate2D *)selectionCenter withR:(CLLocationDistance) radius{
    //using the street information, snap to the street via geometric projection 
    
    CLLocation* center = [[CLLocation alloc] initWithLatitude:(*selectionCenter).latitude longitude:(*selectionCenter).longitude];
    
    //look through list of points, check spot distanceFromLocation (coord) vs radius.  
    //keep track of some stuff
    NSMutableArray* insideCircle = [[NSMutableArray alloc] init];
    if (allInsideCircle==nil){
        allInsideCircle = [[NSMutableArray alloc] init];
    }else{
        [allInsideCircle removeAllObjects];
    }
    
    float avgLat=0, avgLon=0, count=0;
    for(NSNumber* MBID in spotMicroBlockMap){
        
        NSDictionary* spotMap = [spotMicroBlockMap objectForKey:MBID];
        for(PQSpotAnnotation* spot in spotMap.allValues){
            CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:spot.coordinate.latitude longitude: spot.coordinate.longitude];
            CLLocationDistance dist = [spot_loc distanceFromLocation:center];
            if(dist<radius-2){
                //inside the circle
                if(spot.available == YES){
                    [insideCircle addObject:spot];
                    avgLat += spot_loc.coordinate.latitude;
                    avgLon += spot_loc.coordinate.longitude;   
                    count++;
                }
                [allInsideCircle addObject:spot];
                
            }

        }
    }
    
    //before updating, we should sort our circles by number.  
    [allInsideCircle sortUsingComparator:^NSComparisonResult(PQSpotAnnotation* obj1, PQSpotAnnotation* obj2) {
        if(obj1.name < obj2.name){
            return -1;
        }else{
            return 1;
        }
    }];

    [self updateSpotSegmentBar];
    
    /* CALCULATE AVERAGES FOR BOTH SIDES */
    NSArray* segData = [self loadBlockData];
    //1 is the pilot area, 2 is the demo area.
    
    if ([spotMicroBlockMap.allKeys containsObject:[NSNumber numberWithInt:114924]]) {
        return [self newCalloutPlacementWithSegment:[segData objectAtIndex:1] andSpots:insideCircle];
    }else{
        return [self newCalloutPlacementWithSegment:[segData objectAtIndex:2] andSpots:insideCircle];
    }
    
}

-(NSArray*) oneSidedCallout:(NSArray*)spotList aLat:(double)avgLat aLon:(double)avgLon{
    int totalSpotsOnMap = 0;
    for(NSDictionary* spotMap in spotMicroBlockMap.allValues){
        totalSpotsOnMap += spotMap.count;
    }
    NSMutableArray* results = [[NSMutableArray alloc] initWithCapacity:totalSpotsOnMap];
//    NSMutableArray* results = [[NSMutableArray alloc] initWithCapacity:spots.count];    
    //project bubbles using this average and the spot's coordinates.  
    for(PQSpotAnnotation* spot in spotList){
        CLLocation* spot_loc = [[CLLocation alloc] initWithLatitude:spot.coordinate.latitude longitude:spot.coordinate.longitude];
        //controls how far the callouts go.  
        double rsq = CALLOUT_LINE_LENGTH;
        
        //only make callout for those clearly in circle

        
        double mdx = avgLat - spot_loc.coordinate.latitude;
        double mdy = avgLon - spot_loc.coordinate.longitude;
        double dx = sqrt(rsq / (1 + (mdy * mdy)/(mdx * mdx)));
        double dy = sqrt(rsq / (1 + (mdx * mdx)/(mdy * mdy)));
        double callout_lat;
        double callout_lon;
        CalloutCorner corner;
        if(mdy>0){
            if(mdx>0){
                //bottom left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon - dy;
                corner = kTopRightCorner;
            }else{
                //bottom right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon - dy;
                corner = kBottomRightCorner;
            }
        }else{
            if(mdx>0){
                //top Left of avg
                callout_lat = avgLat - dx;
                callout_lon = avgLon + dy;
                corner = kTopLeftCorner;
            }else{
                //top right of avg
                callout_lat = avgLat + dx;
                callout_lon = avgLon + dy;
                corner = kBottomLeftCorner;
                
            }
        }
        NSString* title = [NSString stringWithFormat:@"%d", spot.name];
        CalloutMapAnnotation* myCallout = [[CalloutMapAnnotation alloc] initWithLatitude:callout_lat                                         andLongitude:callout_lon
                                                                                andTitle:title
                                                                               andCorner:corner
                                                                               andCircle:spot];

        
        NSDictionary* add = [[NSDictionary alloc] initWithObjectsAndKeys:myCallout
                             , @"callout",spot_loc, @"spot", nil];
        
        
        [results addObject:add];
    }
    return results;
}

-(bool) pointA:(CLLocationCoordinate2D*)a isCloseToB:(CLLocationCoordinate2D*)b{
    double dx = a->longitude - b->longitude;
    double dy = a->latitude - b->latitude;
    double hsq = dx*dx+dy*dy;
    if(hsq < USER_DISTANCE_FROM_SPOT_THRESHOLD ){
        return true;
    }else{
        return false;
    }
}

// Selected a gray callout bubble on map containing spot number - user wants to park
- (bool) tappedCalloutAtCoords:(CLLocationCoordinate2D*) coords{
    for(int i=0; i< callouts.count; i++){
        
        CalloutMapAnnotation* c = [callouts objectAtIndex:i];
        //c.coordinate is the coord on the corner.  must be corrected.  
        double cLat;
        double cLon;
        switch (c.corner) {
            case kBottomLeftCorner:
                //bot left, add to both
                cLat = c.latitude+ CALLOUT_WIDTH;
                cLon = c.longitude+ CALLOUT_HEIGHT;
                break;
            case kBottomRightCorner:
                cLat = c.latitude+ CALLOUT_WIDTH; //+
                cLon = c.longitude- CALLOUT_HEIGHT; //-
                break;
            case kTopLeftCorner:
                cLat = c.latitude- CALLOUT_WIDTH; // -
                cLon = c.longitude+ CALLOUT_HEIGHT; // +
                break;
            case kTopRightCorner:
                //top right, subtract from both.  
                cLat = c.latitude- CALLOUT_WIDTH;
                cLon = c.longitude- CALLOUT_HEIGHT;
                break;
            default:
                break;
        }
        double dx = cLat-(*coords).latitude; 
        double dy = cLon-(*coords).longitude;
//        //draw a box.  
//        int color = 1;
//        CLLocationCoordinate2D nw_point = CLLocationCoordinate2DMake(cLat, cLon);
//        CLLocationCoordinate2D se_point = CLLocationCoordinate2DMake(nw_point.latitude - CALLOUT_WIDTH, nw_point.longitude + CALLOUT_HEIGHT);
//        CLLocationCoordinate2D ne_point = CLLocationCoordinate2DMake(nw_point.latitude ,se_point.longitude);
//        CLLocationCoordinate2D sw_point = CLLocationCoordinate2DMake(se_point.latitude ,nw_point.longitude);
//        CLLocationCoordinate2D testLotCoords[5]={nw_point, ne_point, se_point, sw_point, nw_point};        
//        MKPolygon *gridPoly = [MKPolygon polygonWithCoordinates:testLotCoords count:5];
//        [gridPoly setColor:color];
//        [gridPoly setObjId:1];
//        [map addOverlay:gridPoly];
//        //end draw a box.  
//        
        
        if(fabs(dx) < CALLOUT_WIDTH && fabs(dy) < CALLOUT_HEIGHT){
            [dataLayer logString:[NSString stringWithFormat:@"%s %@", __PRETTY_FUNCTION__, @"tapped callout"]];
            //check where user's location is.  
            CLLocationCoordinate2D spot_loc = c.circle.coordinate;
            desired_spot = c.circle;
            
            /*request from the server the rate object based on the spotID and gps?  
             gps is just used to make sure ur not too far fromt he spot, but what about 
             urban canyons?  
            
            user distance from spot.  
            user's gps reported accuracy. */

            spotInfo = [networkLayer getSpotInfoForId:[NSNumber numberWithLong:c.circle.objId] SpotNumber:[NSNumber numberWithInt:[c.title intValue]] GPS:&user_loc];
            [spotInfo setLatitude:[NSNumber numberWithDouble:desired_spot.coordinate.latitude]];
            [spotInfo setLongitude:[NSNumber numberWithDouble:desired_spot.coordinate.longitude]];
            [spotInfo setSpotId:[NSNumber numberWithLong:desired_spot.objId]];
            
            if(user_loc_isGood && ![self pointA:&spot_loc isCloseToB:&user_loc]){
                UIActionSheet *directionsActionSheet = [[UIActionSheet alloc] initWithTitle:[NSString stringWithFormat:@"Spot %@", c.title] delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles: @"Get Directions", @"Park Now", nil];
                directionsActionSheet.tag = GPS_LAUNCH_ALERT;
                [directionsActionSheet showInView:bottomSpotSelectionView];
                return true;
            }else{
                [self parkNow];
                return true;
            }
        }
    }
    return false;
}

- (void)parkNow {
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UINavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"ParkingController"];
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    PQParkingViewController *vcTop = [[vc viewControllers] objectAtIndex:0];
    vcTop.spotInfo = spotInfo;
    vcTop.parent = (PQMapViewController*)self;
    [self presentModalViewController:vc animated:YES];
}

-(void) clearCallouts{
    [self.map removeAnnotations:callouts];
    [self.map removeOverlays:calloutLines];   
    [calloutLines removeAllObjects];
    [callouts removeAllObjects];   
}

- (void)clearMap {
    DLog(@"");
    [map removeOverlays:map.overlays];
    [gridMicroBlockMap removeAllObjects];
    [streetMicroBlockMap removeAllObjects];
    for(NSNumber* MBID in spotMicroBlockMap){
        NSDictionary* spots = [spotMicroBlockMap objectForKey:MBID];
        [map removeAnnotations:spots.allValues];
    }
    [spotMicroBlockMap removeAllObjects];
    [map removeOverlays:calloutLines];
    
    
//    [currentSpotMBIDs removeAllObjects];
//    [currentGridMBIDs removeAllObjects];
    [currentMicroBlockIds removeAllObjects];
    
//    [map removeAnnotations:callouts];
    [calloutLines removeAllObjects];
    [callouts removeAllObjects];
    
}

- (void)clearGrids {
    DLog(@"");
    // For some reason, clearing just self.grids
    // is leaving residual stuff behind on the map.
    // A hack to get around this is to clear all
    // overlays and then add back the ones we want.
    
    /* this method mutates the dictionary while its being enumrated.  causes crash.
     go to grid level, once grids load, click justParkMe and zoom to spot level.  */
    NSMutableArray* toRemoveLater = [[NSMutableArray alloc] initWithCapacity:gridMicroBlockMap.count];
    for(NSNumber* MBID in gridMicroBlockMap){
        NSDictionary* gridMap = [gridMicroBlockMap objectForKey:MBID];
        [self.map removeOverlays:gridMap.allValues];
        [toRemoveLater addObject:MBID];
    }
    [gridMicroBlockMap removeObjectsForKeys:toRemoveLater];
    
//    [self.map removeOverlays:self.grids];
//    [grids removeAllObjects];
//    [self.map removeOverlays:self.map.overlays];
//    [grids removeAllObjects];
//    [self.map addOverlays:calloutLines];
//    if (gCircle != nil) {
//        [self.map addOverlay:gCircle];
//    }
}

-(void) clearStreets{

    [self.map removeOverlays:streetMicroBlockMap.allValues];
//    for(NSNumber* MBID in streetMicroBlockMap){
//        NSDictionary* streetMap = [streetMicroBlockMap objectForKey:MBID];
//        [self.map removeOverlays:streetMap.allValues];
//        [streetMicroBlockMap removeObjectForKey:MBID];
//    }
}
-(void) clearSpots{

    NSMutableArray* toRemoveLater = [[NSMutableArray alloc] initWithCapacity:spotMicroBlockMap.count];
    for(NSNumber* MBID in spotMicroBlockMap){
        NSDictionary* spotMap = [spotMicroBlockMap objectForKey:MBID];
        [self.map removeAnnotations:spotMap.allValues];
        [toRemoveLater addObject:MBID];

    }
    [spotMicroBlockMap removeObjectsForKeys:toRemoveLater];
    
}

// After tapping map: Called by handleSingleTapGesture, determine location for gray circle,
// snaps to grid, determines spots in radius and creates gray callout bubbles - shows
// top and bottom availability by spot number views
- (void)showSelectionCircle:(CLLocationCoordinate2D *)coord {
    NSLog(@"showSelectionCircle after tapping map");
    
    int radius = GREY_CIRCLE_R;
    
    NSArray *placement = [self calloutBubblePlacement:coord withR:radius];

    if(placement.count > 0){
        [self showSpotSelectionViews];
        [self.map setCenterCoordinate:*coord animated:YES];
    }else{
        [self showAvailabilitySelectionView];
    }
    
    MKCircle *greyCircle= [MKCircle circleWithCenterCoordinate:*coord radius:radius];
    [greyCircle setColor:-1];
    [self.map addOverlay:greyCircle];
    
    if(calloutLines==NULL || callouts == NULL){
        calloutLines = [[NSMutableArray alloc]initWithCapacity:MAX_CALLOUTS];
        callouts = [[NSMutableArray alloc] initWithCapacity:MAX_CALLOUTS];
        
    }else{
        [self clearCallouts];
        [self.map removeOverlay:gCircle];
    }
    gCircle = greyCircle;
    
    for (NSDictionary *bubble in placement) {
        CLLocationCoordinate2D endpoints[2];
        CalloutMapAnnotation *callout = [bubble objectForKey:@"callout"];
        double corner_lat = callout.coordinate.latitude;
        double corner_lon = callout.coordinate.longitude;
        
        endpoints[0] = CLLocationCoordinate2DMake(corner_lat, corner_lon);
        endpoints[1] = ((CLLocation *)[bubble objectForKey:@"spot"]).coordinate;
        MKPolyline *calloutLine = [MKPolyline polylineWithCoordinates:endpoints count:2];
        [calloutLine setColor:-1];
        [callouts addObject:callout];
        [calloutLines addObject:calloutLine];
        
        [self.map addAnnotation:callout];
        [self.map addOverlay:calloutLine];
    }
}

-(CLLocationCoordinate2D)topRightOfMap{
    return CLLocationCoordinate2DMake(map.centerCoordinate.latitude + map.region.span.latitudeDelta/2,
                                      map.centerCoordinate.longitude + map.region.span.longitudeDelta/2);
}

-(CLLocationCoordinate2D)botLeftOfMap{
    return CLLocationCoordinate2DMake(map.centerCoordinate.latitude - map.region.span.latitudeDelta/2,
                                      map.centerCoordinate.longitude - map.region.span.longitudeDelta/2);
}

- (void)showGridLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    DLog(@"");
    [self clearSpots];
    [self clearStreets];
    
    CLLocationCoordinate2D NE = [self topRightOfMap];
    CLLocationCoordinate2D SW = [self botLeftOfMap];
    NSMutableArray* newMicroBlockIds = [networkLayer getMBIDsWithType:kGridEntity NE:&NE SW:&SW];
    NSMutableArray* updateMicroBlockIds = [[NSMutableArray alloc] init];
    //see header file for structure of maps.  
    
    
    
    if (currentMicroBlockIds==nil || currentMicroBlockIds.count==0){
    //if(currentGridMBIDs == nil || currentGridMBIDs.count==0){
        //everything needs to be loaded again.
    }else{
        //go through both arrays, from left to right.  
        int i=0, j=0;
        while(i<currentMicroBlockIds.count && j< newMicroBlockIds.count){
            //NSLog(@"currCount: %d, newCount: %d\n", currentMicroBlockIds.count, newMicroBlockIds.count);
            long oldId = [[currentMicroBlockIds objectAtIndex:i] longValue];
            long newId = [[newMicroBlockIds objectAtIndex:j] longValue];
            if(oldId < newId){
                i++;
            }else if(newId < oldId){
                j++;
            }else{
                
                //remove both.  
                [currentMicroBlockIds removeObjectAtIndex:i];
                [newMicroBlockIds removeObjectAtIndex:j];
                //add to update list.  
                [updateMicroBlockIds addObject:[NSNumber numberWithLong:oldId]];
            }
        }        
    }
//    NSLog(@"current:%s\n", currentMicroBlockIds.description.UTF8String);
//    NSLog(@"new:%s\n", newMicroBlockIds.description.UTF8String);
//    NSLog(@"update %s\n", updateMicroBlockIds.description.UTF8String);

    //remove overlays no longer on map.  
    for(NSNumber* old in currentMicroBlockIds){
//    NSLog(@"removing...%lu\n", old.longValue);
        //get the dictionary for the microblock id
        NSDictionary* gridsForMicroBlock = [gridMicroBlockMap objectForKey:old];
        //remove all objects associated with that micro block
        [map removeOverlays:[gridsForMicroBlock allValues]];
        //remove the microblock from the dictionary
        [gridMicroBlockMap removeObjectForKey:old];
    }
    //add the grids that are missing, and update the rest.  
    [networkLayer addOverlayOfType:kGridEntity ToMapForIDs:newMicroBlockIds AndUpdateForIDs:updateMicroBlockIds];
    
    //assign old to be a combination of new and update.  
    [newMicroBlockIds addObjectsFromArray:updateMicroBlockIds];
    [newMicroBlockIds sortUsingComparator:^NSComparisonResult(NSNumber* obj1, NSNumber* obj2) {
        if(obj1.longValue < obj2.longValue){
            return -1;
        }else{
            return 1;
        }
    }];
    currentMicroBlockIds = newMicroBlockIds;
}

- (void)showStreetLevelWithCoordinates:(CLLocationCoordinate2D *)coord {
    DLog(@"");
    //THIS METHOD CAUSES A ZOOMING HICCUP WHEN THE USER ZOOMS OUT OF SPOT TO STREET LEVEL.  
    //MOST NOTICEABLE WHEN YOU DOUBLE TAP AT SPOT LEVEL TO ZOOM OUT TO STREET LEVEL.  
    [self clearMap];
    /* 
     
     ALERT THIS FUNCTION DOESN'T USE MICROBLOCKS YET.  NEED TO IMPLEMENT.  
     
     */
    NSArray* data = [self loadCambridgeBlockData];
    if(streetMicroBlockMap==NULL){
        streetMicroBlockMap = [[NSMutableDictionary alloc] init];
    }
    int streetid = 0;
    for (id line in data) {
        //        NSArray *raw_waypoints = [[line objectForKey:@"line"] componentsSeparatedByString:@";"];
        CLLocationCoordinate2D waypoints[2] = {[line A], [line B]};
        
        //        int i=0;
        //        for (id raw_waypoint in raw_waypoints) {
        //            NSArray *coordinates = [raw_waypoint componentsSeparatedByString:@","];
        //            CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
        //            waypoints[i++] = coordinate;
        //        }
        //        
        MKPolyline *routeLine = [MKPolyline polylineWithCoordinates:waypoints count:2];
        
        int color = [((Segment*)line) color];
        [routeLine setColor:color];
        [self.map addOverlay:routeLine];        
        NSDictionary* streetMap = [[NSDictionary alloc] initWithObjectsAndKeys:routeLine,[NSNumber numberWithInt:streetid], nil];
        [streetMicroBlockMap addEntriesFromDictionary:streetMap];
        streetid++;
    }


}


- (void)showSpotLevelWithCoordinates:(CLLocationCoordinate2D *)coord { 
    DLog(@"");
    [self clearStreets];
    [self clearGrids];
    
    CLLocationCoordinate2D NE = [self topRightOfMap];
    CLLocationCoordinate2D SW = [self botLeftOfMap];
    NSMutableArray* newMicroBlockIds = [networkLayer getMBIDsWithType:kSpotEntity NE:&NE SW:&SW];

    NSMutableArray* updateMicroBlockIds = [[NSMutableArray alloc] init];
    //see header file for structure of maps.  
    
    if (currentMicroBlockIds ==nil || currentMicroBlockIds.count==0){
        //everything needs to be loaded again.
    }else{
        //go through both arrays, from left to right.  
        int i=0, j=0;
        while(i<currentMicroBlockIds.count && j< newMicroBlockIds.count){
            //NSLog(@"currCount: %d, newCount: %d\n", currentMicroBlockIds.count, newMicroBlockIds.count);
            long oldId = [[currentMicroBlockIds objectAtIndex:i] longValue];
            long newId = [[newMicroBlockIds objectAtIndex:j] longValue];
            if(oldId < newId){
                i++;
            }else if(newId < oldId){
                j++;
            }else{
                
                //remove both.  
                [currentMicroBlockIds removeObjectAtIndex:i];
                [newMicroBlockIds removeObjectAtIndex:j];
                //add to update list.  
                [updateMicroBlockIds addObject:[NSNumber numberWithLong:oldId]];
            }
        }        
    }
        //NSLog(@"current:%s\n", currentMicroBlockIds.description.UTF8String);
        //NSLog(@"new:%s\n", newMicroBlockIds.description.UTF8String);
        //NSLog(@"update %s\n", updateMicroBlockIds.description.UTF8String);

    
    
    for(NSNumber* old in currentMicroBlockIds){
        NSDictionary* spotForMicroBlock = [spotMicroBlockMap objectForKey:old];
        if(spotForMicroBlock!=nil){
            //removed something??
            NSLog(@"%lu %s\n >>>>", old.longValue, spotForMicroBlock.description.UTF8String);
        }
        [map removeAnnotations:[spotForMicroBlock allValues]];
        [spotMicroBlockMap removeObjectForKey:old];
    }
    //add the grids that are missing, and update the rest.  
    [networkLayer addOverlayOfType:kSpotEntity ToMapForIDs:newMicroBlockIds AndUpdateForIDs:updateMicroBlockIds];
    
    //assign old to be a combination of new and update.  
    [newMicroBlockIds addObjectsFromArray:updateMicroBlockIds];
    [newMicroBlockIds sortUsingComparator:^NSComparisonResult(NSNumber* obj1, NSNumber* obj2) {
        if(obj1.longValue < obj2.longValue){
            return -1;
        }else{
            return 1;
        }
    }];
    currentMicroBlockIds = [NSMutableArray arrayWithArray:newMicroBlockIds];
}

- (void)showAvailabilitySelectionView {
    //Debug when we switch to number pad, then zoom out, and select search bar again.
    topSearchBar.keyboardType = UIKeyboardTypeDefault;
    topSearchBar.selectedScopeButtonIndex=0;
    //debug
    if(doubleTapAlreadyCalled){
        doubleTapAlreadyCalled = NO;
    }else{
        
        [UIView animateWithDuration:.7 animations:^{
            //x y width height
            self.topSpotSelectionView.frame = CGRectMake(320, 44, 320, 44);
            self.bottomSpotSelectionView.frame = CGRectMake(-320, 416, 320, 44);
            self.availabilitySelectionView.frame = CGRectMake(0, 44, 320, 44);
            self.justParkSelectionView.frame = CGRectMake(0, 416, 320, 44);
        }];
        
        self.availabilitySelectionView.hidden = NO;
        //self.topSpotSelectionView.hidden = YES;
        //self.bottomSpotSelectionView.hidden = YES;
    }
}

// Show the top and bottom bars to select individual numbered spots when most zoomed in
- (void)showSpotSelectionViews {
    [UIView animateWithDuration:.7 animations:^{
                                                    //x y width height
        self.topSpotSelectionView.frame = CGRectMake(0, 44, 320, 44);
        self.bottomSpotSelectionView.frame = CGRectMake(0, 416, 320, 44);
        self.availabilitySelectionView.frame = CGRectMake(-320, 44, 320, 44);
        self.justParkSelectionView.frame = CGRectMake(320, 416, 320, 44);
    }];
    self.topSpotSelectionView.hidden = NO;
    self.bottomSpotSelectionView.hidden = NO;
    //self.availabilitySelectionView.hidden = YES;
}

-(void)showDropPinBar{
    isDroppingPin = YES;
    map.zoomEnabled = NO;
    [UIView animateWithDuration:.7 animations:^{
        //x y width height
        self.bottomSpotSelectionView.frame = CGRectMake(-640, 416, 320, 44);
        self.justParkSelectionView.frame = CGRectMake(-320, 416, 320, 44);
        self.dropPinSelectionView.frame = CGRectMake(0, 416, 320, 44);
    }];
}
-(void) hideDropPinBar{
    isDroppingPin = NO;
    map.zoomEnabled = YES;
    if(bookmarkPin!=nil){
        [map removeAnnotation:bookmarkPin];
    }
    [UIView animateWithDuration:.7 animations:^{
        //x y width height
        self.bottomSpotSelectionView.frame = CGRectMake(-320, 416, 320, 44);
        self.justParkSelectionView.frame = CGRectMake(0, 416, 320, 44);
        self.dropPinSelectionView.frame = CGRectMake(320, 416, 320, 44);
    }];
}

-(void) hideAvailabilityBar{
    [UIView animateWithDuration:0.7 animations:^{
        self.availabilitySelectionView.frame = CGRectMake(availabilitySelectionView.frame.origin.x, availabilitySelectionView.frame.origin.y-44, availabilitySelectionView.frame.size.width, availabilitySelectionView.frame.size.height);
    }];

}
-(void) hideJustParkMeBar{
    [UIView animateWithDuration:0.7 animations:^{
        self.justParkSelectionView.frame = CGRectMake(justParkSelectionView.frame.origin.x, justParkSelectionView.frame.origin.y+44, justParkSelectionView.frame.size.width, justParkSelectionView.frame.size.height);
    }];

}
-(void) hideTopSpotSelectionBar{
    [UIView animateWithDuration:0.7 animations:^{
        self.topSpotSelectionView.frame = CGRectMake(topSpotSelectionView.frame.origin.x, topSpotSelectionView.frame.origin.y-44, topSpotSelectionView.frame.size.width, topSpotSelectionView.frame.size.height);
    }];
    
}
-(void) hideBottomSpotSelectionBar{
    [UIView animateWithDuration:0.7 animations:^{
        self.bottomSpotSelectionView.frame = CGRectMake(bottomSpotSelectionView.frame.origin.x, bottomSpotSelectionView.frame.origin.y+44, bottomSpotSelectionView.frame.size.width, bottomSpotSelectionView.frame.size.height);
    }];
    
}

-(void) hideTopAndBottomControls{
    [self hideJustParkMeBar];
    [self hideAvailabilityBar];
//    [self hideBottomSpotSelectionBar];
//    [self hideTopSpotSelectionBar];
}

-(void) showTopAndBottomControls{
    [UIView animateWithDuration:.7 animations:^{
        //x y width height
        self.availabilitySelectionView.frame = CGRectMake(0, 44, 320, 44);
        self.justParkSelectionView.frame = CGRectMake(0, 416, 320, 44);
    }];

//    [UIView animateWithDuration:0.7 animations:^{
//    self.availabilitySelectionView.frame = CGRectMake(availabilitySelectionView.frame.origin.x, availabilitySelectionView.frame.origin.y+44, availabilitySelectionView.frame.size.width, availabilitySelectionView.frame.size.height);
//    self.justParkSelectionView.frame = CGRectMake(justParkSelectionView.frame.origin.x, justParkSelectionView.frame.origin.y-44, justParkSelectionView.frame.size.width, justParkSelectionView.frame.size.height);
//    }];

}
- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    DLog(@"");
    oldStreetLevelRegion.center = map.centerCoordinate;    
    if (displayedData != kNoneData) {
        double currentSpan = mapView.region.span.latitudeDelta;
        CLLocationCoordinate2D center = mapView.centerCoordinate;
        if(currentSpan >= GRID_SPAN_UPPER_LIMIT){
            //above the limit, clear map.  
            zoomState = kClearZoomLevel;
            [self clearMap];
            if(user_loc_isGood)[self hideTopAndBottomControls];
        }else if (currentSpan >= GRID_TO_STREET_SPAN_LAT) {
            //within grid level, show grids.  
            if(zoomState == kStreetZoomLevel){
                //came in from street level
                [currentMicroBlockIds removeAllObjects];
                [self showAvailabilitySelectionView];
            }else if(zoomState==kSpotZoomLevel){
                //came in from spot level?
                [currentMicroBlockIds removeAllObjects];
                [self showAvailabilitySelectionView];
            }else if (zoomState == kClearZoomLevel){
                [self showTopAndBottomControls];
            }
            zoomState = kGridZoomLevel;
            [self showGridLevelWithCoordinates:&center];
        }else if(currentSpan >= STREET_TO_SPOT_SPAN_LAT){
            if(zoomState==kSpotZoomLevel){
                //changed INTO street level.  current ids useless.  
                //[currentMicroBlockIds removeAllObjects];
                [map removeAnnotations:callouts];
                [self showAvailabilitySelectionView];
            }else if(zoomState==kGridZoomLevel){
                //came in from grid level.  
                //[currentMicroBlockIds removeAllObjects];
            }else if(zoomState == kClearZoomLevel){
                [self showTopAndBottomControls];
            }
            
            //within street level, show streets.  
            zoomState = kStreetZoomLevel;
            [self showStreetLevelWithCoordinates:&center];
            
        }else if(currentSpan < STREET_TO_SPOT_SPAN_LAT){
            
            if(!shouldNotClearOverlays){
                //if we're allowed to clear the overlays, remove them.  
                [allInsideCircle removeAllObjects]; //update what's inside the circle.  
                [self updateSpotSegmentBar];
                if(callouts.count >0){
                    //remove overlays on pan
                    [self clearCallouts]; 
                }
                [self.map removeOverlay:gCircle];

            }
            shouldNotClearOverlays = false;
//            if(zoomState!=kSpotZoomLevel){
//                //changed INTO spot level.  current ids useless.  
//                NSLog(@"changed INTO street view\n");
//                [currentSpotMBIDs removeAllObjects];
//            }
            //below the street canyon, fall down into spot level.  
            zoomState = kSpotZoomLevel;
            [mapView setRegion:MKCoordinateRegionMake(center, MKCoordinateSpanMake(SPOT_LEVEL_SPAN, SPOT_LEVEL_SPAN)) animated:YES];
            [self showSpotLevelWithCoordinates:&center];
        }

    }
    [dataLayer logString:[NSString stringWithFormat:@"%s %d", __PRETTY_FUNCTION__, zoomState]];
}

- (MKOverlayView *)mapView:(MKMapView *)myMapView viewForOverlay:(id<MKOverlay>)overlay {
    if ([overlay isKindOfClass:[MKPolygon class]]) {
        MKPolygon* polygon = (MKPolygon*) overlay;
        MKPolygonView *view = [[MKPolygonView alloc] initWithOverlay:polygon];
        view.lineWidth=1;
        view.strokeColor = [UIColor whiteColor];
        switch (displayedData) {
            case kAvailabilityData:
                switch (polygon.color) {
                    case -1:
                        view.fillColor = [[UIColor grayColor] colorWithAlphaComponent:0.2];
                        break;
                    case 0:
                        view.fillColor = [[UIColor veryLowAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 1:
                        view.fillColor = [[UIColor lowAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 2:
                        view.fillColor = [[UIColor mediumAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 3:
                        view.fillColor = [[UIColor highAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                    case 4:
                        view.fillColor = [[UIColor veryHighAvailabilityColor] colorWithAlphaComponent:0.2];
                        break;
                }
                break;
            case kPriceData:
                switch (polygon.color) {
                    case -1:
                        view.fillColor = [[UIColor grayColor] colorWithAlphaComponent:0.2];
                        break;
                    case 0:
                        view.fillColor = [[UIColor veryHighPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 1:
                        view.fillColor = [[UIColor highPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 2:
                        view.fillColor = [[UIColor mediumPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 3:
                        view.fillColor = [[UIColor lowPriceColor] colorWithAlphaComponent:0.2];
                        break;
                    case 4:
                        view.fillColor = [[UIColor veryLowPriceColor] colorWithAlphaComponent:0.2];
                        break;
                }
                break;
            case kNoneData:
                return nil;
        }
        return view;
    } else if ([overlay isKindOfClass:[MKPolyline class]]) {
        MKPolylineView *view = [[MKPolylineView alloc] initWithOverlay:overlay];
        
        //FOR DEVICE
        //view.lineWidth = 16;
        //for simulator
        view.lineWidth = 8;
        
        MKPolyline *polyline = (MKPolyline *)overlay;
        switch (displayedData) {
            case kAvailabilityData:
                switch (polyline.color) {
                    case -1:
                        view.strokeColor = [UIColor blackColor];
                        view.lineWidth = 1;
                        break;
                    case 0:
                        view.strokeColor = [UIColor veryLowAvailabilityColor];
                        break;
                    case 1:
                        view.strokeColor = [UIColor lowAvailabilityColor];
                        break;
                    case 2:
                        view.strokeColor = [UIColor mediumAvailabilityColor];
                        break;
                    case 3:
                        view.strokeColor = [UIColor highAvailabilityColor];
                        break;
                    case 4:
                        view.strokeColor = [UIColor veryHighAvailabilityColor];
                        break;
                }
                break;
            case kPriceData:
                switch (polyline.color) {
                    case -1:
                        view.strokeColor = [UIColor blackColor];
                        view.lineWidth = 1;
                        break;
                    case 0:
                        view.strokeColor = [UIColor veryHighPriceColor];
                        break;
                    case 1:
                        view.strokeColor = [UIColor highPriceColor];
                        break;
                    case 2:
                        view.strokeColor = [UIColor mediumPriceColor];
                        break;
                    case 3:
                        view.strokeColor = [UIColor lowPriceColor];
                        break;
                    case 4:
                        view.strokeColor = [UIColor veryLowPriceColor];
                        break;
                }
                break;
            case kNoneData:
                return nil;
        }
        return view;
    } else if ([overlay isKindOfClass:[MKCircle class]]) {
        MKCircleView *circleView = [[MKCircleView alloc] initWithCircle:(MKCircle *)overlay];
        // Grey selection circle
        circleView.fillColor = [[UIColor blackColor] colorWithAlphaComponent:0.3];
        circleView.strokeColor = [UIColor whiteColor];
        //for simulator
        circleView.lineWidth = 6;
        //for device
//        circleView.lineWidth = 12;
        return circleView;
    }
    return nil;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	if ([annotation isKindOfClass:[CalloutMapAnnotation class]]) {
        
        //reusing annotation views causes the displayed numberes to be incorrect 
        //ie.  1412's callout is 1422 or something.  temporarily removed, not too much slower.
		CalloutMapAnnotationView *calloutMapAnnotationView;// = (CalloutMapAnnotationView *)[self.map dequeueReusableAnnotationViewWithIdentifier:@"CalloutAnnotation"];
		if (!calloutMapAnnotationView) {
			calloutMapAnnotationView = [[CalloutMapAnnotationView alloc] initWithAnnotation:annotation 
                                                                            reuseIdentifier:@"CalloutAnnotation"];
            
		}
        calloutMapAnnotationView.enabled = NO;
		calloutMapAnnotationView.mapView = self.map;
        return calloutMapAnnotationView;
	} else if ([annotation isKindOfClass:[PQSpotAnnotation class]]) {
        static NSString *availableSpotIdentifier = @"AvailableSpotIdentifier";
        static NSString *occupiedSpotIdentifier = @"OccupiedSpotIdentifier";
        MKAnnotationView *annotationView;
        if(((PQSpotAnnotation *)annotation).available) {
            annotationView = [self.map dequeueReusableAnnotationViewWithIdentifier:availableSpotIdentifier];
            if (!annotationView) {
                annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:availableSpotIdentifier];
                annotationView.image = [UIImage imageNamed:@"spot_free.png"];
                annotationView.opaque = NO;
            }
        } else {
            annotationView = [self.map dequeueReusableAnnotationViewWithIdentifier:occupiedSpotIdentifier];
            if (!annotationView) {
                annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:occupiedSpotIdentifier];
                annotationView.image = [UIImage imageNamed:@"spot_occupied.png"];
                annotationView.opaque = NO;
            }
        }
        return annotationView;
    }else if ([annotation isKindOfClass:[PQPinAnnotation class]]){
        //drop a pin with some info.  
        MKPinAnnotationView* customPinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"test"];
        
        customPinView.pinColor = MKPinAnnotationColorPurple;
        customPinView.animatesDrop = YES;
        customPinView.canShowCallout = YES;
        
        // add a detail disclosure button to the callout which will open a new view controller page
        //
        // note: you can assign a specific call out accessory view, or as MKMapViewDelegate you can implement:
        //  - (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control;
        //
        
        UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton addTarget:self
                        action:@selector(showDetails:)
              forControlEvents:UIControlEventTouchUpInside];
        customPinView.rightCalloutAccessoryView = rightButton;
        return customPinView;
    }else if ([annotation isKindOfClass:[PQParkedCarAnnotation class]]){
        MKPinAnnotationView* customPinView = [[MKPinAnnotationView alloc]
                                              initWithAnnotation:annotation reuseIdentifier:@"parkedCarAnnotationIdentifier"];
        customPinView.pinColor = MKPinAnnotationColorPurple;
        customPinView.animatesDrop = YES;
        customPinView.canShowCallout = YES;
        bookmarkPin = (PQParkedCarAnnotation*) annotation;
        UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton addTarget:self
                        action:@selector(saveDroppedPin:)
              forControlEvents:UIControlEventTouchUpInside];
        customPinView.rightCalloutAccessoryView = rightButton;
        return customPinView;
    }
    
	return nil;
}

-(void) saveDroppedPin:(id)sender{
    //store the pin into bookmarks core data.  
    
    //remove it from the map.  
    
    //present edit subview for save bookmark.  
    
    //[map removeAnnotation:bookmarkPin];
    //[self hideDropPinBar];
    //show an alert saying we saved it fine.  
    UIAlertView* comingSoonAlert = [[UIAlertView alloc] initWithTitle:@"Feature coming Soon!" message:nil delegate:self cancelButtonTitle:@"Cool!" otherButtonTitles:nil];
    [comingSoonAlert show];
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
}
#pragma mark - LINE PROJECTION

//given a point p, and a segment a to b, find the orthogonally projected point on the segment.
-(CLLocationCoordinate2D) getProjectedPoint: (CLLocationCoordinate2D*) p A:(CLLocationCoordinate2D*)a B:(CLLocationCoordinate2D*) b error:(bool*)err{
    double x1 = b->latitude;
    double y1 = b->longitude;
    double x2 = a->latitude;
    double y2 = a->longitude;
    double dy = y1 - y2;
    double dx = x1- x2;
    double x3 = p->latitude;
    double y3 = p->longitude;
    
    double m = dy/dx; 
    double b1 = y1 - x1*m;
    
    double new_x = (m*y3 + x3 - m*y1 + m*m*x1) / (m*m+1);
    double new_y = m*new_x + b1;
    //check validity of projected point.  
    
    if((new_y > y1 && new_y > y2) || (new_y < y1 && new_y < y2)){
        *err = true;
    }
    return CLLocationCoordinate2DMake(new_x, new_y);
}

-(CLLocationCoordinate2D) snapFromCoord:(CLLocationCoordinate2D*) coord toSegments:(NSArray*) segments{
    // each segment is CLLocationCoordinate2D points[2];
    double min_dist = 180*180;
    CLLocationCoordinate2D ret_coord;
    
    for(id line in segments){
        bool error = false;
        CLLocationCoordinate2D seg[2] = {[line A], [line B]};
        CLLocationCoordinate2D snaploc = [self getProjectedPoint:coord A:&seg[0] B:&seg[1] error:&error];
        if(error){
            continue;
        }
        double d_lat = snaploc.latitude - (*coord).latitude;
        double d_lon = snaploc.longitude - (*coord).longitude;
        double dist = d_lat*d_lat + d_lon*d_lon;
        if(dist < min_dist){
            min_dist = dist;
            ret_coord = snaploc;
        }
    }
    return ret_coord;
}

#pragma mark - GESTURE HANDLERS


-(void)handlePanGesture:(UIGestureRecognizer*)gestureRecognizer{
    NSLog(@"panpanpan\n");
    if(gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;    
}


-(void)handleDoubleTapGesture:(UIGestureRecognizer *)gestureRecognizer{

    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;

    CGPoint touchPoint = [gestureRecognizer locationInView:self.map];
    CLLocationCoordinate2D coord = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];

    MKMapPoint mapPoint = MKMapPointForCoordinate(coord);
    if(isDroppingPin){
        //user is dropping pin.  do nothing on double tap.
        return;
    }
    
    if(zoomState == kClearZoomLevel){
        [map setRegion:MKCoordinateRegionMakeWithDistance(coord, GRID_LEVEL_REGION_METERS, GRID_LEVEL_REGION_METERS) animated:YES];
    }else if (zoomState==kGridZoomLevel) {

        for (MKPolygon* gridOverlay in self.map.overlays) {
                
            MKPolygonView *polyView = (MKPolygonView*)[self.map viewForOverlay:gridOverlay];
            CGPoint polygonViewPoint = [polyView pointForMapPoint:mapPoint];
            
            if (CGPathContainsPoint(polyView.path, NULL, polygonViewPoint, NO)) {

                MKCoordinateRegion reg = [self.map convertRect:polyView.bounds toRegionFromView:polyView];
                //save this for when user wants to zoom out of spot level.  
                oldStreetLevelRegion = [self.map regionThatFits:reg];
                
                [self.map setRegion:oldStreetLevelRegion animated:YES];
                [self showStreetLevelWithCoordinates:&(reg.center)];
            }
        }
    } else if (zoomState==kStreetZoomLevel) { 
        MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(coord,SPOT_LEVEL_REGION_METERS ,SPOT_LEVEL_REGION_METERS);
        [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
        [self showSpotLevelWithCoordinates:&coord];
        [self showSpotSelectionViews];
    } else if (zoomState==kSpotZoomLevel) {

        if(oldStreetLevelRegion.center.longitude==0){
            oldStreetLevelRegion.center = map.centerCoordinate;
        }
        if(oldStreetLevelRegion.span.longitudeDelta==0){
            oldStreetLevelRegion.span.latitudeDelta = 0.005000;
            oldStreetLevelRegion.span.longitudeDelta =  0.005000;
        }
        [self.map setRegion:[self.map regionThatFits:oldStreetLevelRegion] animated:YES];            
        //this below method causes animation hiccup.  see method for more info.

        /* apparently by removing annotations before/while a region animation, certain elements break int he map.  Maybe we should try removing things and then move the map.  */
        
        //[self showStreetLevelWithCoordinates:&(oldStreetLevelRegion.center)];

        [self showAvailabilitySelectionView];
        doubleTapAlreadyCalled = YES;
    }
    [networkLayer sendLogs];

}

// 
- (void)handleSingleTapGesture:(UIGestureRecognizer *)gestureRecognizer
{

    if (gestureRecognizer.state != UIGestureRecognizerStateEnded)
        return;
    
    CGPoint touchPoint = [gestureRecognizer locationInView:self.map];
    CLLocationCoordinate2D coord = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
    
    
    if(isDroppingPin){
        if(bookmarkPin!=nil){
//            [map removeAnnotations:self.map.annotations];
            [self.map removeAnnotation:bookmarkPin];
        }
        //a tap drops an annotation onto the map.  
        PQParkedCarAnnotation* pin = [[PQParkedCarAnnotation alloc] initWithCoordinate:coord addressDictionary:nil];
        pin.title = @"Bookmark Me?";
        [self.map addAnnotation:pin];
        [self.map selectAnnotation:pin animated:YES];
        return;
    }else if(map.selectedAnnotations.count!=0) {
        [map deselectAnnotation:[self.map.selectedAnnotations objectAtIndex:0]  animated:YES]; 
    }
    if(zoomState == kSpotZoomLevel){
        //snap the circle to the closest polyline.
        NSArray* segList = [self loadBlockData];
        CLLocationCoordinate2D snaploc = [self snapFromCoord:&coord toSegments:segList];
        /* end snap stuff */
        if(![self tappedCalloutAtCoords:&coord]){
            //if the result returned is valid
            [self showSelectionCircle:&snaploc];
            //since you just summoned them, tell region change handler they shouldn't be cleared.  
            shouldNotClearOverlays = true;
            
        }
    }else if(zoomState == kGridZoomLevel){
        
        
    }
    [networkLayer sendLogs];
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {   
    return YES;
}

#pragma mark - Search bar and UISearchBarDelegate methods

- (void)setSearchBar:(UISearchBar *)searchBar active:(BOOL)visible {
    for(UIView *subView in topSearchBar.subviews)
        if([subView isKindOfClass: [UITextField class]])
            [(UITextField *)subView setKeyboardAppearance:UIKeyboardAppearanceAlert];
    
    if (visible) { //hide stuff.  
        if (zoomState == kSpotZoomLevel) {
            self.disableViewOverlay.frame = CGRectMake(0.0f,88.0f,320.0f,372.0f);
        } else {
            self.disableViewOverlay.frame = CGRectMake(0.0f,44.0f,320.0f,416.0f);
        }
        [self.view addSubview:self.disableViewOverlay];
        
        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = nil;
            self.disableViewOverlay.alpha = 0.6;
            if (zoomState == kSpotZoomLevel) {
                searchBar.frame = CGRectMake(0, 0, 320, 88);
            } else {
                searchBar.frame = CGRectMake(0, 0, 320, 44);
            }
        }];
    } else { //show stuff.  
        [searchBar resignFirstResponder];
        
        [UIView animateWithDuration:0.25 animations:^{
            self.navigationBar.leftBarButtonItem = self.leftBarButton;
            self.disableViewOverlay.alpha = 0;
            searchBar.frame = CGRectMake(45, 0, 275, 44);
        } completion:^(BOOL s){
            [self.disableViewOverlay removeFromSuperview];
        }];
    }
    [searchBar setShowsScopeBar:(visible && zoomState==kSpotZoomLevel)];
    [searchBar setShowsCancelButton:visible animated:YES];
    [networkLayer sendLogs];
}


- (void)handleSingleTap:(UIGestureRecognizer *)sender {
    //close search bar on tap anywhere.
    [self setSearchBar:topSearchBar active:NO];
    [topSearchBar setText:@""];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self clearMap];
    
    [geocoder geocodeAddressString:searchBar.text inRegion:nil completionHandler:^(NSArray *placemarks, NSError * error){
        CLLocation* locationObject = [[placemarks objectAtIndex:0] location];
        MKCoordinateRegion viewRegion = [self.map regionThatFits:MKCoordinateRegionMakeWithDistance(locationObject.coordinate,SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS)];
        
        [self.map setRegion:viewRegion animated:YES];
        //[self.map setCenterCoordinate:locationObject.coordinate animated:YES];
    }];
    
    [self setSearchBar:searchBar active:NO];
}

- (void)searchBar:(UISearchBar *)searchBar selectedScopeButtonIndexDidChange:(NSInteger)selectedScope {
    if (selectedScope == 1) {
        searchBar.keyboardType = UIKeyboardTypeNumberPad;
    } else {
        searchBar.keyboardType = UIKeyboardTypeDefault;
    }
    [searchBar resignFirstResponder];
    [searchBar becomeFirstResponder];
}

- (void)searchBarBookmarkButtonClicked:(UISearchBar *)searchBar {
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    PQBookmarksViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"BookmarksController"];
    [vc setParent:self]; //assignt he parent to be self.  
    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    [self presentModalViewController:vc animated:YES];
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self setSearchBar:searchBar active:YES];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    if(searchText.length==4){
        //WE COULD ENABLE UPON 4 DIGITS
        
        //or just let done button do nothing unless 4.  
    }
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [self setSearchBar:searchBar active:NO];
}

#pragma mark - MAP TOOLBARS
- (IBAction)availabilityBarTapped {
    switch (self.availabilitySelectionBar.selectedSegmentIndex) {
        case 0:
            [dataLayer logString:[NSString stringWithFormat:@"%s %@", __PRETTY_FUNCTION__, @"avail"]];
            gradientIcon.image = [UIImage imageNamed:@"gradient_avail"];
            displayedData = kAvailabilityData;
            [self mapView:map regionDidChangeAnimated:NO];
            break;
        case 1:
            [dataLayer logString:[NSString stringWithFormat:@"%s %@", __PRETTY_FUNCTION__, @"price"]];
            gradientIcon.image = [UIImage imageNamed:@"gradient_price"];
            displayedData = kPriceData;
            [self mapView:map regionDidChangeAnimated:NO];
            break;
        case 2:
            [dataLayer logString:[NSString stringWithFormat:@"%s %@", __PRETTY_FUNCTION__, @"none"]];
            gradientIcon.image = [UIImage imageNamed:@"gradient_none"];
            displayedData = kNoneData;
            [self clearMap];
            break;
    }
    //hack fix.  
    if(zoomState == kGridZoomLevel){
        [self clearGrids];
        [currentMicroBlockIds removeAllObjects];
        CLLocationCoordinate2D coord = [map centerCoordinate];
        [self showGridLevelWithCoordinates:&coord];
    }
}

// User selects a spot number in top or bottom UISegmentedControl of numbers - which changes
// the selected index and calls this function to act: Let them Park or Get Directions
// sender: topSpotSelectionBar or bottomSpotSelectionBar: Both handlers combined
-(void)userSelectedSpotInSpotBar:(id)sender{
    UISegmentedControl *spotSelectBar = (UISegmentedControl *) sender;
    
    int index = spotSelectBar.selectedSegmentIndex;
    [dataLayer logString:[NSString stringWithFormat:@"%s %@", __PRETTY_FUNCTION__, @"seg bar"]];
    int name = [[spotSelectBar titleForSegmentAtIndex:index] intValue];
    for(PQSpotAnnotation* spot in allInsideCircle){
        //for each spot inside the circle, check match spot number.  
        if(spot.name == name){
            //found our target.  
            spotInfo = [networkLayer getSpotInfoForId:[NSNumber numberWithLong:spot.objId] SpotNumber:[NSNumber numberWithInt:spot.name] GPS:&user_loc];
            [spotInfo setLatitude:[NSNumber numberWithDouble:spot.coordinate.latitude]];
            [spotInfo setLongitude:[NSNumber numberWithDouble:spot.coordinate.longitude]];
            
            CLLocationCoordinate2D spot_loc = spot.coordinate;
            if(!spot.available){
                //not available.  ask are you sure?
                NSString* title = [NSString stringWithFormat:@"Spot %d appears taken", name];
                UIAlertView* spotTakenAlert = [[UIAlertView alloc] initWithTitle:title message:@"Are you sure you want to park?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Park", nil];
                spotTakenAlert.tag = SPOT_LOOKS_TAKEN_ALERT;
                [spotTakenAlert show];
            }else if(user_loc_isGood && ![self pointA:&spot_loc isCloseToB:&user_loc]){
                UIActionSheet *directionsActionSheet = [[UIActionSheet alloc] initWithTitle:[NSString stringWithFormat:@"Spot %d", name] delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:nil otherButtonTitles: @"Get Directions", @"Park Now", nil];
                directionsActionSheet.tag = GPS_LAUNCH_ALERT;
                [directionsActionSheet showInView:bottomSpotSelectionView];
                
            }else{
                [self parkNow];
            }
        
        }
    }
}

// When selecting area on map, update spot numbers at top or bottom
// TODO: IF NOT AVAILABLE, DARKEN THE BUTTON???? text red?
-(void) updateSpotSegmentBar{
    int segIndex = 0; //0 and 5 are arrows.  
    while(segIndex < 7){
        NSString* title;
        if(segIndex < allInsideCircle.count){
            //we have a number to show
            PQSpotAnnotation* spot = [allInsideCircle objectAtIndex:segIndex];
            title = [NSString stringWithFormat:@"%d", spot.name];
            if(segIndex < 4){
                int trueIndex = segIndex+1;
                [topSpotSelectionBar setTitle:title forSegmentAtIndex:trueIndex];
                [topSpotSelectionBar setEnabled:YES forSegmentAtIndex:trueIndex];
            }else{
                int trueIndex = segIndex - 3;  
                [bottomSpotSelectionBar setTitle:title forSegmentAtIndex:trueIndex];
                [bottomSpotSelectionBar setEnabled:YES forSegmentAtIndex:trueIndex];
            }
            
        }else{
           //we don't have a number to show
            
            if(segIndex < 4){
                //top bar
                int trueIndex = segIndex+1;
                [topSpotSelectionBar setTitle:@"-----" forSegmentAtIndex:trueIndex];
                [topSpotSelectionBar setEnabled:NO forSegmentAtIndex:trueIndex];
            }else{
                int trueIndex = segIndex - 3;  
                [bottomSpotSelectionBar setTitle:@"-----" forSegmentAtIndex:trueIndex];
                [bottomSpotSelectionBar setEnabled:NO forSegmentAtIndex:trueIndex];
            }
        }
        segIndex++;
    }
    
}


-(IBAction)settingsButtonPressed :(id)sender{
    DLog(@"");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
    UINavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"SettingsController"];

    [vc setModalPresentationStyle:UIModalPresentationFullScreen];
    [vc setModalTransitionStyle:UIModalTransitionStyleFlipHorizontal];
    PQSettingsViewController *vcTop = [[vc viewControllers] objectAtIndex:0];
    [vcTop setParent:self];
    //also pass rate information for the selected spot here.  
    [self presentModalViewController:vc animated:YES];
}

#pragma mark - Debug button actions

- (IBAction)gridButtonPressed:(id)sender {
    [self showMoreTextBox];
}

- (IBAction)streetButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.37369,-71.10976};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, STREET_LEVEL_REGION_METERS, STREET_LEVEL_REGION_METERS);
    [self.map setRegion:[self.map regionThatFits:viewRegion] animated:YES];
    [self showStreetLevelWithCoordinates:&point];
    [self showAvailabilitySelectionView];
}

- (IBAction)spotButtonPressed:(id)sender {
    CLLocationCoordinate2D point = {42.37369,-71.10976};
    
    MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(point, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS);
    [map setRegion:[map regionThatFits:viewRegion] animated:YES];
    [self showSpotLevelWithCoordinates:&point];
    [self showAvailabilitySelectionView];
    
}
-(void) hideMoreTextBox{
//    PopOutView* box = [[PopOutView alloc] initWithFrame:CGRectMake(112, 369, 141, 71)];
//    [box setParent:self];
//    [self.view addSubview:box];
//    [self.view bringSubviewToFront:box];
    [UIView animateWithDuration:0.35 animations:^{
        //hide

        self.popOutSpotNumberField.hidden = YES;
        self.popOutSpotNumberView.frame = CGRectMake(253, 436, 0, 0);

    }];
}
-(void) showMoreTextBox{
    [UIView animateWithDuration:.35 animations:^{
        //show

        self.popOutSpotNumberField.hidden = NO;
        self.popOutSpotNumberView.frame = CGRectMake(112, 369, 141, 71);
        self.popOutSpotNumberField.frame = CGRectMake(6.000000,20.000000,129.000000,31.000000);
    }];
}

- (IBAction)noneButtonPressed:(id)sender {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.labelText = @"Loading Data...";
    dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
        
        [dataLayer loadMockData];
        [networkLayer loadSpotData];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideHUDForView:self.view animated:YES];
        });
    });
    ((UIButton*)sender).hidden = YES;

    
    //[self hideMoreTextBox];
//    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
//    int olduitype = [dataLayer UIType];
//    if(olduitype==3) olduitype = -1;
//    [dataLayer setUIType:olduitype+1];
//    NSString* string = [NSString stringWithFormat:@"uitype = %d", olduitype+1];
//    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:nil message:string delegate:self cancelButtonTitle:@"ok" otherButtonTitles:nil];
//    [alert show];
    
    
//    self.map = [[MKMapView alloc] initWithFrame:CGRectMake(0, 44, 320, 416)];
    
//    [self clearMap];
//    [networkLayer testAsync];

//    int loop = 0;
//    NSLog(@"map annotation count%d\n", map.annotations.count);
//    for(PQSpotAnnotation* anno in map.annotations){
//        loop++;
//        NSLog(@"looped %d\n", loop);
//        MKAnnotationView* view = [map viewForAnnotation:anno];
//        view.image = [UIImage imageNamed:@"spot_occupied.png"];
//    }
}

- (void)keyboardWillShow:(NSNotification *)note { 

    if(topSearchBar.selectedScopeButtonIndex==1){
        // create custom button
        UIButton *doneButton = [UIButton buttonWithType:UIButtonTypeCustom];
        doneButton.frame = CGRectMake(0, 163, 106, 53);
        doneButton.adjustsImageWhenHighlighted = NO;
        [doneButton setImage:[UIImage imageNamed:@"doneup.png"] forState:UIControlStateNormal];
        [doneButton setImage:[UIImage imageNamed:@"donedown.png"] forState:UIControlStateHighlighted];
        [doneButton addTarget:self action:@selector(numPadSubmit:) forControlEvents:UIControlEventTouchUpInside];
        // locate keyboard view
        UIWindow* tempWindow = [[[UIApplication sharedApplication] windows] objectAtIndex:1];
        UIView* keyboard;

        for(int i=0; i<[tempWindow.subviews count]; i++) {
            keyboard = [tempWindow.subviews objectAtIndex:i];
            
            // keyboard view found; add the custom button to it
            if([[keyboard description] hasPrefix:@"<UIPeripheralHostView"] == YES){
                [keyboard addSubview:doneButton];
            }
        }
    }
    
}

- (IBAction)numPadSubmit:(id)sender {
    if(topSearchBar.text.length==4){
        [self setSearchBar:topSearchBar active:NO];
        //set it as "SpotNumber:1234" so the user knows?  
        //[topSearchBar setText:[NSString stringWithFormat:@"Spot: %s", topSearchBar.text.UTF8String]];
    }else{
        NSLog(@"check your number, len not 4\n");
    }
}

-(void) checkLoggedIn{
    if([dataLayer isLoggedIn]){
        //already logged in.  dont' hsow login screen.
        NSDate* endTime = [dataLayer getEndTime];
        if([endTime laterDate:[NSDate date]] == endTime){
            //parked, keep map hidden, show timer.
            self.spotInfo = [dataLayer getSpotInfo];
            [self parkNow];
        }else{
            //not parked, show map.
            self.view.hidden = NO;
            NSLog(@"reveal the map\n");
        }
    }else{
        self.view.hidden = YES;
        NSLog(@"hide the map\n");
        
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
        LoginNavigationController *vc = [storyboard instantiateViewControllerWithIdentifier:@"LoginController"];
        vc.parent = self;
        [vc setModalPresentationStyle:UIModalPresentationFullScreen];
        
        [vc setModalTransitionStyle:UIModalTransitionStyleFlipHorizontal];
        [self presentModalViewController:vc animated:YES];
    }
}

#pragma mark - LOCATION
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    user_loc = newLocation.coordinate;
    
    if (MAX(newLocation.horizontalAccuracy, newLocation.verticalAccuracy) < ACCURACY_LIMIT) {
        if (!user_loc_isGood) {
            manager.distanceFilter = 30.0;
            MKCoordinateRegion viewRegion = MKCoordinateRegionMakeWithDistance(newLocation.coordinate, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS);
            [self showSpotSelectionViews];
            [self.map setRegion:[map regionThatFits:viewRegion] animated:YES];
            user_loc_isGood = true;
        }
    }
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
    

    if(!dataLayer){
        //set pointer to data layer.
        dataLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).dataLayer;
        [dataLayer setMapController:self];
    }
    if(!networkLayer){
        networkLayer = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).networkLayer;
        [networkLayer setMapController:self];
    }
    if(!locationManager){
        locationManager = ((PQAppDelegate*)[[UIApplication sharedApplication] delegate]).locationManager;
        locationManager.delegate = self;
    }
    
    //initially hide, incase app was purged or user not logged in.
    self.view.hidden = YES;
    if([dataLayer mbIdExistsInCoreData:[NSNumber numberWithInt:0] EntityType:kGridEntity]){
        //already loaded stuff.
        mockDataButton.hidden = YES;
    }
    
    
    if(gridMicroBlockMap==nil) gridMicroBlockMap = [[NSMutableDictionary alloc] init];
    if(spotMicroBlockMap==nil) spotMicroBlockMap = [[NSMutableDictionary alloc] init];
    if(streetMicroBlockMap==nil) streetMicroBlockMap = [[NSMutableDictionary alloc] init];
    if(allInsideCircle==nil) allInsideCircle = [[NSMutableArray alloc] init];
    if(callouts == nil) callouts = [[NSMutableArray alloc] init];
    [self updateSpotSegmentBar];
    
    //check the current zoom level to set the ZOOM_STATE integer.  
    if (self.map.bounds.size.width >= GRID_TO_STREET_SPAN_LAT) {
        zoomState = kGridZoomLevel;
        //above middle zoom level        
    } else if (self.map.bounds.size.width >= STREET_TO_SPOT_SPAN_LAT) {
        //above spot zoom level
        zoomState = kStreetZoomLevel;
    } else {
        //inside spot zoom level.  
        zoomState = kSpotZoomLevel;
    }
    self.disableViewOverlay = [[UIView alloc]
                               initWithFrame:CGRectMake(0.0f,88.0f,320.0f,372.0f)];
    self.disableViewOverlay.backgroundColor=[UIColor blackColor];
    self.disableViewOverlay.alpha = 0;
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.disableViewOverlay addGestureRecognizer:singleTap];
    
    
    ((UISearchBar *)self.topSearchBar).scopeButtonTitles = [[NSArray alloc] initWithObjects:@"Place name", @"Spot number", nil];
    
    self.leftBarButton = self.navigationBar.leftBarButtonItem;
    
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.topSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:0];
    [self.bottomSpotSelectionBar setWidth:36 forSegmentAtIndex:5];
    [self.bottomSpotSelectionBar setTitle:@"More" forSegmentAtIndex:4];
    [self.bottomSpotSelectionBar setEnabled:NO forSegmentAtIndex:4];
    //disable arrows
    [self.bottomSpotSelectionBar setEnabled:NO forSegmentAtIndex:0];
    [self.bottomSpotSelectionBar setEnabled:NO forSegmentAtIndex:5];    
    [self.topSpotSelectionBar setEnabled:NO forSegmentAtIndex:0];
    [self.topSpotSelectionBar setEnabled:NO forSegmentAtIndex:5];
    
    // Event delegate for acting on top and bottom selection bars of spot numbers
    [self.topSpotSelectionBar addTarget:self action:@selector(userSelectedSpotInSpotBar:) forControlEvents:UIControlEventValueChanged];
    [self.bottomSpotSelectionBar addTarget:self action:@selector(userSelectedSpotInSpotBar:) forControlEvents:UIControlEventValueChanged];

    // Do any additional setup after loading the view, typically from a nib.
    self.map.delegate=self;
    self.map.showsUserLocation = YES;
    //setup gesture recognizer for grids and blocks and spots.  
    UITapGestureRecognizer *doubleTapRecognizer = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self action:@selector(handleDoubleTapGesture:)];
    UITapGestureRecognizer * singleTapRecognizer = [[UITapGestureRecognizer alloc]
                                                    initWithTarget:self action:@selector(handleSingleTapGesture:)];

    [doubleTapRecognizer setNumberOfTapsRequired:2];
    [singleTapRecognizer setNumberOfTapsRequired:1];
    
    [singleTapRecognizer requireGestureRecognizerToFail:doubleTapRecognizer];

    [self.map addGestureRecognizer:singleTapRecognizer];
    [self.map addGestureRecognizer:doubleTapRecognizer];
    
    [[NSNotificationCenter defaultCenter] addObserver:self 
                                             selector:@selector(keyboardWillShow:) 
                                                 name:UIKeyboardDidShowNotification 
                                               object:nil];
    
//    [[NSNotificationCenter defaultCenter] addObserver:self 
//                                             selector:@selector(keyboardAppearing:) 
//                                                 name:UIKeyboardWillShowNotification
//                                               object:nil];
    
    
    // For pilot: start off in Amherst Street on most-zoomed in level: Change user_loc_isGood=false
    // and remove default location for actual app
    CLLocationCoordinate2D pilot_street = CLLocationCoordinate2DMake(42.357835,-71.094333);
    user_loc = pilot_street;
    user_loc_isGood = true;
    
    [map setRegion:MKCoordinateRegionMakeWithDistance(pilot_street, SPOT_LEVEL_REGION_METERS, SPOT_LEVEL_REGION_METERS) animated:YES];
    [self showAvailabilitySelectionView];
    
    isDroppingPin = false;
}

- (void)viewDidUnload
{
    [self setMap:nil];
    [self setNavigationBar:nil];
    [self setTopSearchBar:nil];
    [self setBottomSpotSelectionBar:nil];
    [self setTopSpotSelectionBar:nil];
    [self setAvailabilitySelectionView:nil];
    [self setBottomSpotSelectionView:nil];
    [self setTopSpotSelectionView:nil];
    [self setGradientIcon:nil];
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(![dataLayer isLoggedIn]){
        NSLog(@"hide the map\n");
        self.view.hidden = YES; //not logged in?  hide map.
    }
}

- (void)viewDidAppear:(BOOL)animated
{
    [self checkLoggedIn];
    [super viewDidAppear:animated];
    //prepare geocoder upon view load.  
    if(geocoder ==nil){
        geocoder = [[CLGeocoder alloc] init];
    }

//    locationManager=[[CLLocationManager alloc] init];
//    locationManager.delegate=self;
//    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    
//    [locationManager startUpdatingLocation];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
    
//    [locationManager stopUpdatingLocation];
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

#pragma mark - IDEA DUMP