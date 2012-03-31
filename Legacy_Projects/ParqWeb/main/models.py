from django.db import models
from django.contrib.auth.models import User as DjangoUser

from datetime import datetime


class ParqModel(models.Model):
    def get_primary_field(self):
        pass

class Admin(models.Model):
    admin_id = models.AutoField(primary_key=True,db_column="admin_id")
    django_user = models.OneToOneField(DjangoUser,related_name="admin_user",null=True)
    #username = models.TextField()
    password = models.TextField()
    email = models.TextField()
    is_deleted = models.IntegerField(null=True, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'admin'
    
    def __unicode__(self):
        return self.email

class Client(models.Model):
    client_id = models.AutoField(primary_key=True)
    name = models.TextField()
    address = models.TextField(blank=True)
    client_desc = models.TextField(blank=True)
    is_deleted = models.IntegerField(null=True, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'client'

    def __unicode__(self):
        return self.name

class Adminrole(models.Model):
    adminrole_id = models.AutoField(primary_key=True)
    role_name = models.TextField()
    role_desc = models.TextField(blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'adminrole'

    def __unicode__(self):
        return self.role_name

class Adminclientrelationship(models.Model):
    ac_rel_id = models.AutoField(primary_key=True)
    admin = models.ForeignKey(Admin,related_name="client_relationships")
    client = models.ForeignKey(Client,related_name="admin_relationships")
    adminrole = models.ForeignKey(Adminrole,related_name="relationships")
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'adminclientrelationship'

    def __unicode__(self):
        return "%s-%s" % (self.admin.email,self.client.name)


class Parkinglocation(models.Model):
    location_id = models.AutoField(primary_key=True)
    location_identifier = models.TextField()
    client = models.ForeignKey(Client,related_name="locations")
    location_name = models.TextField(blank=True)
    is_deleted = models.IntegerField(null=True, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'parkinglocation'

    def __unicode__(self):
        return "%s-%s" % (self.client.name,self.location_name)

    def save(self, *args, **kwargs):
        if not self.location_id:
            if Parkinglocation.objects.all().count()==0:
                self.location_id=1
            else:
                i = Parkinglocation.objects.all().order_by('-location_id')[0]
                self.location_id = i.location_id+1
        super(Parkinglocation, self).save(*args, **kwargs) 


class Geolocation(models.Model):
    geolocation_id = models.AutoField(primary_key=True,default=0)
    location = models.OneToOneField(Parkinglocation,related_name="geolocation")
    latitude = models.FloatField()
    longitude = models.FloatField()
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'geolocation'

    def __unicode__(self):
        return "%s - (%s,%s)" % (self.location.location_name,self.latitude,self.longitude)

class User(models.Model):
    user_id = models.AutoField(primary_key=True)
    django_user = models.OneToOneField(DjangoUser,related_name="parq_user")
    password = models.TextField()
    email = models.TextField()
    phone_number = models.TextField(blank=True)
    is_deleted = models.IntegerField(null=True, blank=True,default=False)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'user'

    def __unicode__(self):
        return self.email

    def is_parked(self):
        return Parkinginstance.objects.filter(user=self,park_end_time__gt=datetime.now()).exists()

    def current_parking_spaces(self):
        spaces = Parkinginstance.objects.filter(user=self,park_end_time__gt=datetime.now())
        if spaces.exists():
            return spaces
        else:
            return None

class Parkingspace(models.Model):
    space_id = models.AutoField(primary_key=True)
    space_identifier = models.TextField()
    location = models.ForeignKey(Parkinglocation,related_name="parking_spaces")
    space_name = models.TextField(blank=True)
    parking_level = models.TextField(blank=True)
    is_deleted = models.IntegerField(null=True, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'parkingspace'

    def is_occupied(self):
        return self.space_history.filter(park_end_time__gt=datetime.now()).exists()
        
    def __unicode__(self):
        return "%s-%s" % (self.location.location_name,self.space_name)


class Parkinginstance(models.Model):
    parkinginst_id = models.AutoField(primary_key=True,default=0)
    user = models.ForeignKey(User,related_name="parking_history")
    space = models.ForeignKey(Parkingspace,related_name="space_history")
    parkingrefnumber = models.TextField()
    park_began_time = models.DateTimeField()
    park_end_time = models.DateTimeField()
    is_paid_parking = models.IntegerField()
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'parkinginstance'
        
    def __unicode__(self):
        return "%s" % (self.space.space_name)
    
    def save(self, *args, **kwargs):
        if not self.parkinginst_id:
            if Parkinginstance.objects.all().count()==0:
                self.parkinginst_id=1
            else:
                i = Parkinginstance.objects.all().order_by('-parkinginst_id')[0]
                self.parkinginst_id = i.parkinginst_id+1
        super(Parkinginstance, self).save(*args, **kwargs) 


class Parkingrate(models.Model):
    rate_id = models.AutoField(primary_key=True)
    location = models.ForeignKey(Parkinglocation)
    parking_rate_cents = models.IntegerField()
    time_increment_mins = models.IntegerField()
    priority = models.IntegerField()
    min_park_mins = models.IntegerField(null=True, blank=True)
    max_park_mins = models.IntegerField(null=True, blank=True)
    space = models.ForeignKey(Parkingspace,related_name="rate")
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'parkingrate'

    def __unicode__(self):
        return "%s - %s" % (self.space.space_name,self.parking_rate_cents)

class Paymentaccount(models.Model):
    account_id = models.AutoField(primary_key=True)
    user = models.ForeignKey(User,related_name="accounts")
    customer_id = models.TextField(blank=True)
    payment_method_id = models.TextField(blank=True)
    cc_stub = models.TextField(blank=True)
    is_default_payment = models.IntegerField(null=True, blank=True)
    is_deleted = models.IntegerField(null=True, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'paymentaccount'

    def __unicode__(self):
        return "%s - %s" % (self.user.email,self.account_id)

    def save(self, *args, **kwargs):
        if not self.account_id:
            if Paymentaccount.objects.all().count()==0:
                self.account_id=1
            else:
                i = Paymentaccount.objects.all().order_by('-account_id')[0]
                self.account_id = i.account_id+1
        super(Paymentaccount, self).save(*args, **kwargs) 

class Payment(models.Model):
    payment_id = models.AutoField(primary_key=True)
    parkinginst = models.OneToOneField(Parkinginstance,related_name="payment")
    payment_type = models.TextField()
    account = models.ForeignKey(Paymentaccount,related_name="payments")
    payment_ref_num = models.TextField()
    payment_datetime = models.DateTimeField()
    amount_paid_cents = models.IntegerField()
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'payment'

    def __unicode__(self):
        return "%s - %s" % (self.account.user.email,self.payment_id)


class Userrole(models.Model):
    userrole_id = models.AutoField(primary_key=True)
    role_name = models.TextField()
    role_desc = models.TextField(blank=True)
    parking_rate = models.DecimalField(null=True, max_digits=7, decimal_places=3, blank=True)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'userrole'
        
    def __unicode__(self):
        return self.role_name

class Userclientrelationship(models.Model):
    uc_rel_id = models.AutoField(primary_key=True)
    user = models.ForeignKey(User,related_name="client_relationships")
    client = models.ForeignKey(Client,related_name="user_relationships")
    userrole = models.ForeignKey(Userrole)
    priority = models.IntegerField()
    location = models.ForeignKey(Parkinglocation)
    space = models.ForeignKey(Parkingspace)
    lastupdatedatetime = models.DateTimeField()
    class Meta:
        db_table = u'userclientrelationship'

    def __unicode__(self):
        return "%s - %s" % (self.user.email,self.client.name)


