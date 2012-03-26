# encoding: utf-8
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models

class Migration(SchemaMigration):

    def forwards(self, orm):
        
        # Adding field 'Admin.django_user'
        db.add_column(u'admin', 'django_user', self.gf('django.db.models.fields.related.OneToOneField')(related_name='admin_user', unique=True, null=True, to=orm['auth.User']), keep_default=False)


    def backwards(self, orm):
        
        # Deleting field 'Admin.django_user'
        db.delete_column(u'admin', 'django_user_id')


    models = {
        'auth.group': {
            'Meta': {'object_name': 'Group'},
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '80'}),
            'permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'})
        },
        'auth.permission': {
            'Meta': {'ordering': "('content_type__app_label', 'content_type__model', 'codename')", 'unique_together': "(('content_type', 'codename'),)", 'object_name': 'Permission'},
            'codename': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'content_type': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['contenttypes.ContentType']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '50'})
        },
        'auth.user': {
            'Meta': {'object_name': 'User'},
            'date_joined': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'email': ('django.db.models.fields.EmailField', [], {'max_length': '75', 'blank': 'True'}),
            'first_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'groups': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Group']", 'symmetrical': 'False', 'blank': 'True'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'is_active': ('django.db.models.fields.BooleanField', [], {'default': 'True'}),
            'is_staff': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'is_superuser': ('django.db.models.fields.BooleanField', [], {'default': 'False'}),
            'last_login': ('django.db.models.fields.DateTimeField', [], {'default': 'datetime.datetime.now'}),
            'last_name': ('django.db.models.fields.CharField', [], {'max_length': '30', 'blank': 'True'}),
            'password': ('django.db.models.fields.CharField', [], {'max_length': '128'}),
            'user_permissions': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['auth.Permission']", 'symmetrical': 'False', 'blank': 'True'}),
            'username': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '30'})
        },
        'contenttypes.contenttype': {
            'Meta': {'ordering': "('name',)", 'unique_together': "(('app_label', 'model'),)", 'object_name': 'ContentType', 'db_table': "'django_content_type'"},
            'app_label': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'model': ('django.db.models.fields.CharField', [], {'max_length': '100'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '100'})
        },
        'main.admin': {
            'Meta': {'object_name': 'Admin', 'db_table': "u'admin'"},
            'admin_id': ('django.db.models.fields.IntegerField', [], {'primary_key': 'True', 'db_column': "'admin_id'"}),
            'django_user': ('django.db.models.fields.related.OneToOneField', [], {'related_name': "'admin_user'", 'unique': 'True', 'null': 'True', 'to': "orm['auth.User']"}),
            'email': ('django.db.models.fields.TextField', [], {}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'password': ('django.db.models.fields.TextField', [], {}),
            'username': ('django.db.models.fields.TextField', [], {})
        },
        'main.adminclientrelationship': {
            'Meta': {'object_name': 'Adminclientrelationship', 'db_table': "u'adminclientrelationship'"},
            'ac_rel_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'admin': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'relationships'", 'to': "orm['main.Admin']"}),
            'adminrole': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'relationships'", 'to': "orm['main.Adminrole']"}),
            'client': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'relationships'", 'to': "orm['main.Client']"}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {})
        },
        'main.adminrole': {
            'Meta': {'object_name': 'Adminrole', 'db_table': "u'adminrole'"},
            'adminrole_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'role_desc': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'role_name': ('django.db.models.fields.TextField', [], {})
        },
        'main.client': {
            'Meta': {'object_name': 'Client', 'db_table': "u'client'"},
            'address': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'client_desc': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'client_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'name': ('django.db.models.fields.TextField', [], {})
        },
        'main.geolocation': {
            'Meta': {'object_name': 'Geolocation', 'db_table': "u'geolocation'"},
            'geolocation_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'latitude': ('django.db.models.fields.FloatField', [], {}),
            'location': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['main.Parkinglocation']"}),
            'longitude': ('django.db.models.fields.FloatField', [], {})
        },
        'main.parkinginstance': {
            'Meta': {'object_name': 'Parkinginstance', 'db_table': "u'parkinginstance'"},
            'is_paid_parking': ('django.db.models.fields.IntegerField', [], {}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'park_began_time': ('django.db.models.fields.DateTimeField', [], {}),
            'park_end_time': ('django.db.models.fields.DateTimeField', [], {}),
            'parkinginst_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'parkingrefnumber': ('django.db.models.fields.TextField', [], {}),
            'space': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'space_history'", 'to': "orm['main.Parkingspace']"}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'parking_history'", 'to': "orm['main.User']"})
        },
        'main.parkinglocation': {
            'Meta': {'object_name': 'Parkinglocation', 'db_table': "u'parkinglocation'"},
            'client': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'locations'", 'to': "orm['main.Client']"}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'location_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'location_identifier': ('django.db.models.fields.TextField', [], {}),
            'location_name': ('django.db.models.fields.TextField', [], {'blank': 'True'})
        },
        'main.parkingrate': {
            'Meta': {'object_name': 'Parkingrate', 'db_table': "u'parkingrate'"},
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'location': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['main.Parkinglocation']"}),
            'max_park_mins': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'min_park_mins': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'parking_rate_cents': ('django.db.models.fields.IntegerField', [], {}),
            'priority': ('django.db.models.fields.IntegerField', [], {}),
            'rate_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'space': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'spaces'", 'to': "orm['main.Parkingspace']"}),
            'time_increment_mins': ('django.db.models.fields.IntegerField', [], {})
        },
        'main.parkingspace': {
            'Meta': {'object_name': 'Parkingspace', 'db_table': "u'parkingspace'"},
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'location': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'parking_spaces'", 'to': "orm['main.Parkinglocation']"}),
            'parking_level': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'space_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'space_identifier': ('django.db.models.fields.TextField', [], {}),
            'space_name': ('django.db.models.fields.TextField', [], {'blank': 'True'})
        },
        'main.payment': {
            'Meta': {'object_name': 'Payment', 'db_table': "u'payment'"},
            'account': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'payments'", 'to': "orm['main.Paymentaccount']"}),
            'amount_paid_cents': ('django.db.models.fields.IntegerField', [], {}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'parkinginst': ('django.db.models.fields.related.OneToOneField', [], {'related_name': "'payment'", 'unique': 'True', 'to': "orm['main.Parkinginstance']"}),
            'payment_datetime': ('django.db.models.fields.DateTimeField', [], {}),
            'payment_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'payment_ref_num': ('django.db.models.fields.TextField', [], {}),
            'payment_type': ('django.db.models.fields.TextField', [], {})
        },
        'main.paymentaccount': {
            'Meta': {'object_name': 'Paymentaccount', 'db_table': "u'paymentaccount'"},
            'account_id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'cc_stub': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'customer_id': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'is_default_payment': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'payment_method_id': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'accounts'", 'to': "orm['main.User']"})
        },
        'main.user': {
            'Meta': {'object_name': 'User', 'db_table': "u'user'"},
            'django_user': ('django.db.models.fields.related.OneToOneField', [], {'related_name': "'parq_user'", 'unique': 'True', 'to': "orm['auth.User']"}),
            'email': ('django.db.models.fields.TextField', [], {}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'default': 'False', 'null': 'True', 'blank': 'True'}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'password': ('django.db.models.fields.TextField', [], {}),
            'phone_number': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'user_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'})
        },
        'main.userclientrelationship': {
            'Meta': {'object_name': 'Userclientrelationship', 'db_table': "u'userclientrelationship'"},
            'client': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'user_relationships'", 'to': "orm['main.Client']"}),
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'location': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['main.Parkinglocation']"}),
            'priority': ('django.db.models.fields.IntegerField', [], {}),
            'space': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['main.Parkingspace']"}),
            'uc_rel_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'client_relationships'", 'to': "orm['main.User']"}),
            'userrole': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['main.Userrole']"})
        },
        'main.userrole': {
            'Meta': {'object_name': 'Userrole', 'db_table': "u'userrole'"},
            'lastupdatedatetime': ('django.db.models.fields.DateTimeField', [], {}),
            'parking_rate': ('django.db.models.fields.DecimalField', [], {'null': 'True', 'max_digits': '7', 'decimal_places': '3', 'blank': 'True'}),
            'role_desc': ('django.db.models.fields.TextField', [], {'blank': 'True'}),
            'role_name': ('django.db.models.fields.TextField', [], {}),
            'userrole_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'})
        }
    }

    complete_apps = ['main']
