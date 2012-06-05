#import "CalloutMapAnnotation.h"

@interface CalloutMapAnnotation()


@end

@implementation CalloutMapAnnotation

@synthesize latitude = _latitude;
@synthesize longitude = _longitude;
@synthesize title = _title;
@synthesize corner = _corner;
@synthesize circle = _circle;

- (id)initWithLatitude:(CLLocationDegrees)latitude
		  andLongitude:(CLLocationDegrees)longitude
              andTitle:(NSString *)title
             andCorner:(CalloutCorner)corner 
             andCircle:(MKCircle *)circle{
	if (self = [super init]) {
		self.latitude = latitude;
		self.longitude = longitude;
        self.title = title;
        self.corner = corner;
        self.circle = circle;
	}
	return self;
}

- (CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coordinate;
	coordinate.latitude = self.latitude;
	coordinate.longitude = self.longitude;
	return coordinate;
}

@end
