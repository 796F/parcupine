from django.conf.urls.defaults import patterns, include, url

urlpatterns = patterns('parqweb.actions.views',
                       url(r'login/$','login'),
                       url(r'login_page/$','login_page'),
                       url(r'logout/$','logout'),
                       url(r'filter_spaces/$','filter_spaces'),
                       url(r'add_payment_account/$','add_payment_account'),
                       url(r'delete_payment_account/$','delete_payment_account'),
                       url(r'set_default_payment_account/$','set_default_payment_account'),
                       url(r'autocomplete_location/$','autocomplete_location'),
                       url(r'sign_up/$','sign_up'),
                       url(r'change_password/$','change_password'),

                       )

