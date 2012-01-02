# encoding: utf-8
import datetime
from south.db import db
from south.v2 import SchemaMigration
from django.db import models

class Migration(SchemaMigration):

    def forwards(self, orm):
        
        # Adding model 'Admin'
        db.create_table(u'admin', (
            ('admin_id', self.gf('django.db.models.fields.IntegerField')(primary_key=True, db_column='admin_id')),
            ('username', self.gf('django.db.models.fields.TextField')()),
            ('password', self.gf('django.db.models.fields.TextField')()),
            ('email', self.gf('django.db.models.fields.TextField')()),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Admin'])

        # Adding model 'Client'
        db.create_table(u'client', (
            ('client_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('name', self.gf('django.db.models.fields.TextField')()),
            ('address', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('client_desc', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Client'])

        # Adding model 'Adminrole'
        db.create_table(u'adminrole', (
            ('adminrole_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('role_name', self.gf('django.db.models.fields.TextField')()),
            ('role_desc', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Adminrole'])

        # Adding model 'Adminclientrelationship'
        db.create_table(u'adminclientrelationship', (
            ('ac_rel_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('admin', self.gf('django.db.models.fields.related.ForeignKey')(related_name='relationships', to=orm['main.Admin'])),
            ('client', self.gf('django.db.models.fields.related.ForeignKey')(related_name='relationships', to=orm['main.Client'])),
            ('adminrole', self.gf('django.db.models.fields.related.ForeignKey')(related_name='relationships', to=orm['main.Adminrole'])),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Adminclientrelationship'])

        # Adding model 'Parkinglocation'
        db.create_table(u'parkinglocation', (
            ('location_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('location_identifier', self.gf('django.db.models.fields.TextField')()),
            ('client', self.gf('django.db.models.fields.related.ForeignKey')(related_name='locations', to=orm['main.Client'])),
            ('location_name', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Parkinglocation'])

        # Adding model 'Geolocation'
        db.create_table(u'geolocation', (
            ('geolocation_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('location', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['main.Parkinglocation'])),
            ('latitude', self.gf('django.db.models.fields.FloatField')()),
            ('longitude', self.gf('django.db.models.fields.FloatField')()),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Geolocation'])

        # Adding model 'User'
        db.create_table(u'user', (
            ('user_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('django_user', self.gf('django.db.models.fields.related.ForeignKey')(related_name='parq_user', to=orm['auth.User'])),
            ('password', self.gf('django.db.models.fields.TextField')()),
            ('email', self.gf('django.db.models.fields.TextField')()),
            ('phone_number', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['User'])

        # Adding model 'Parkingspace'
        db.create_table(u'parkingspace', (
            ('space_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('space_identifier', self.gf('django.db.models.fields.TextField')()),
            ('location', self.gf('django.db.models.fields.related.ForeignKey')(related_name='parking_spaces', to=orm['main.Parkinglocation'])),
            ('space_name', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('parking_level', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Parkingspace'])

        # Adding model 'Parkinginstance'
        db.create_table(u'parkinginstance', (
            ('parkinginst_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(related_name='parking_history', to=orm['main.User'])),
            ('space', self.gf('django.db.models.fields.related.ForeignKey')(related_name='space_history', to=orm['main.Parkingspace'])),
            ('parkingrefnumber', self.gf('django.db.models.fields.TextField')()),
            ('park_began_time', self.gf('django.db.models.fields.DateTimeField')()),
            ('park_end_time', self.gf('django.db.models.fields.DateTimeField')()),
            ('is_paid_parking', self.gf('django.db.models.fields.IntegerField')()),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Parkinginstance'])

        # Adding model 'Parkingrate'
        db.create_table(u'parkingrate', (
            ('rate_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('location', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['main.Parkinglocation'])),
            ('parking_rate_cents', self.gf('django.db.models.fields.IntegerField')()),
            ('time_increment_mins', self.gf('django.db.models.fields.IntegerField')()),
            ('priority', self.gf('django.db.models.fields.IntegerField')()),
            ('min_park_mins', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('max_park_mins', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('space', self.gf('django.db.models.fields.related.ForeignKey')(related_name='spaces', to=orm['main.Parkingspace'])),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Parkingrate'])

        # Adding model 'Paymentaccount'
        db.create_table(u'paymentaccount', (
            ('account_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(related_name='accounts', to=orm['main.User'])),
            ('customer_id', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('payment_method_id', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('cc_stub', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('is_default_payment', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('is_deleted', self.gf('django.db.models.fields.IntegerField')(null=True, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Paymentaccount'])

        # Adding model 'Payment'
        db.create_table(u'payment', (
            ('payment_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('parkinginst', self.gf('django.db.models.fields.related.ForeignKey')(related_name='payment', to=orm['main.Parkinginstance'])),
            ('payment_type', self.gf('django.db.models.fields.TextField')()),
            ('account', self.gf('django.db.models.fields.related.ForeignKey')(related_name='payments', to=orm['main.Paymentaccount'])),
            ('payment_ref_num', self.gf('django.db.models.fields.TextField')()),
            ('payment_datetime', self.gf('django.db.models.fields.DateTimeField')()),
            ('amount_paid_cents', self.gf('django.db.models.fields.IntegerField')()),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Payment'])

        # Adding model 'Userrole'
        db.create_table(u'userrole', (
            ('userrole_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('role_name', self.gf('django.db.models.fields.TextField')()),
            ('role_desc', self.gf('django.db.models.fields.TextField')(blank=True)),
            ('parking_rate', self.gf('django.db.models.fields.DecimalField')(null=True, max_digits=7, decimal_places=3, blank=True)),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Userrole'])

        # Adding model 'Userclientrelationship'
        db.create_table(u'userclientrelationship', (
            ('uc_rel_id', self.gf('django.db.models.fields.BigIntegerField')(primary_key=True)),
            ('user', self.gf('django.db.models.fields.related.ForeignKey')(related_name='client_relationships', to=orm['main.User'])),
            ('client', self.gf('django.db.models.fields.related.ForeignKey')(related_name='user_relationships', to=orm['main.Client'])),
            ('userrole', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['main.Userrole'])),
            ('priority', self.gf('django.db.models.fields.IntegerField')()),
            ('location', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['main.Parkinglocation'])),
            ('space', self.gf('django.db.models.fields.related.ForeignKey')(to=orm['main.Parkingspace'])),
            ('lastupdatedatetime', self.gf('django.db.models.fields.DateTimeField')()),
        ))
        db.send_create_signal('main', ['Userclientrelationship'])


    def backwards(self, orm):
        
        # Deleting model 'Admin'
        db.delete_table(u'admin')

        # Deleting model 'Client'
        db.delete_table(u'client')

        # Deleting model 'Adminrole'
        db.delete_table(u'adminrole')

        # Deleting model 'Adminclientrelationship'
        db.delete_table(u'adminclientrelationship')

        # Deleting model 'Parkinglocation'
        db.delete_table(u'parkinglocation')

        # Deleting model 'Geolocation'
        db.delete_table(u'geolocation')

        # Deleting model 'User'
        db.delete_table(u'user')

        # Deleting model 'Parkingspace'
        db.delete_table(u'parkingspace')

        # Deleting model 'Parkinginstance'
        db.delete_table(u'parkinginstance')

        # Deleting model 'Parkingrate'
        db.delete_table(u'parkingrate')

        # Deleting model 'Paymentaccount'
        db.delete_table(u'paymentaccount')

        # Deleting model 'Payment'
        db.delete_table(u'payment')

        # Deleting model 'Userrole'
        db.delete_table(u'userrole')

        # Deleting model 'Userclientrelationship'
        db.delete_table(u'userclientrelationship')


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
            'parkinginst': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'payment'", 'to': "orm['main.Parkinginstance']"}),
            'payment_datetime': ('django.db.models.fields.DateTimeField', [], {}),
            'payment_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
            'payment_ref_num': ('django.db.models.fields.TextField', [], {}),
            'payment_type': ('django.db.models.fields.TextField', [], {})
        },
        'main.paymentaccount': {
            'Meta': {'object_name': 'Paymentaccount', 'db_table': "u'paymentaccount'"},
            'account_id': ('django.db.models.fields.BigIntegerField', [], {'primary_key': 'True'}),
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
            'django_user': ('django.db.models.fields.related.ForeignKey', [], {'related_name': "'parq_user'", 'to': "orm['auth.User']"}),
            'email': ('django.db.models.fields.TextField', [], {}),
            'is_deleted': ('django.db.models.fields.IntegerField', [], {'null': 'True', 'blank': 'True'}),
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
