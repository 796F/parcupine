from django import forms
from django.core.exceptions import ObjectDoesNotExist
from django.utils.translation import ugettext_lazy as _

from django.contrib.auth import authenticate
from django.contrib.auth.models import User

from datetime import datetime

from parqweb.main.models import Paymentaccount
from parqweb.actions.backend import ParqBackend

from django.contrib.auth.forms import PasswordChangeForm as DjangoPasswordChangeForm

class PaymentAccountForm(forms.ModelForm):
    class Meta:
        model = Paymentaccount
        fields = ('is_default_payment',)

class AddPaymentForm(forms.Form):
    name_on_card = forms.CharField(label=_("Name on Card"))
    type = forms.CharField(label=_("Type"))
    card_number = forms.CharField(label=_("Card Number"))
    month_expiration = forms.CharField(widget=forms.TextInput(attrs={'class':"span2"}))
    year_expiration = forms.CharField(widget=forms.TextInput(attrs={'class':"span2"}))
    security_code = forms.CharField(label=_("3-digit Security Code"),widget=forms.TextInput(attrs={'class':"span2"}))
    is_default_payment= forms.BooleanField(label=_("Set as default"))

    def __init__(self, user, *args, **kwargs):
        self.user = user
        super(AddPaymentForm, self).__init__(*args, **kwargs)

    def save(self):
        #need to authenticate payment type here. use external call
        if self.cleaned_data['is_default_payment']:
            for account in Paymentaccount.objects.filter(user=self.user.parq_user):
                account.is_default_payment = False
                account.save()
        pa = Paymentaccount(user=self.user.parq_user,
                            cc_stub = self.cleaned_data['card_number'][-4:],
                            is_default_payment = self.cleaned_data['is_default_payment'],
                            is_deleted = 0,
                            lastupdatedatetime = datetime.now())
        pa.save()
        return pa

'''Taken from django UserCreationForm. Changed so that it takes an email instead of a username'''

class UserCreationForm(forms.ModelForm):
    """
    A form that creates a user, with no privileges, from the given username and password.
    """
    username = forms.EmailField(label=_("Email"))
    password1 = forms.CharField(label=_("Password"), widget=forms.PasswordInput)
    password2 = forms.CharField(label=_("Password confirmation"), widget=forms.PasswordInput,
                                help_text = _("Enter the same password as above, for verification."))

    class Meta:
        model = User
        fields = ("username",)

    def clean_username(self):
        username = self.cleaned_data["username"]
        try:
            User.objects.get(username=username)
        except ObjectDoesNotExist:
            return username
        raise forms.ValidationError(_("A user with that username already exists."))

    def clean_password2(self):
        password1 = self.cleaned_data.get("password1", "")
        password2 = self.cleaned_data["password2"]
        if password1 != password2:
            raise forms.ValidationError(_("The two password fields didn't match."))
        return password2

    def save(self, commit=True):
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data["password1"])
        if commit:
            user.save()
        return user

class AuthenticationForm(forms.Form):
    """
    Base class for authenticating users. Extend this to get a form that accepts
    username/password logins.
    """
    username = forms.CharField(label=_("Email"), max_length=30)
    password = forms.CharField(label=_("Password"), widget=forms.PasswordInput)

    def __init__(self, request=None, *args, **kwargs):
        """
        If request is passed in, the form will validate that cookies are
        enabled. Note that the request (a HttpRequest object) must have set a
        cookie with the key TEST_COOKIE_NAME and value TEST_COOKIE_VALUE before
        running this validation.
        """
        self.request = request
        self.user_cache = None
        super(AuthenticationForm, self).__init__(*args, **kwargs)

    def clean(self):
        username = self.cleaned_data.get('username')
        password = self.cleaned_data.get('password')

        if username and password:
            self.user_cache = authenticate(username=username, password=password)
            if self.user_cache is None:
                raise forms.ValidationError(_("Please enter a correct username and password. Note that both fields are case-sensitive."))
            elif not self.user_cache.is_active:
                raise forms.ValidationError(_("This account is inactive."))
        self.check_for_test_cookie()
        return self.cleaned_data

    def check_for_test_cookie(self):
        if self.request and not self.request.session.test_cookie_worked():
            raise forms.ValidationError(
                _("Your Web browser doesn't appear to have cookies enabled. "
                  "Cookies are required for logging in."))

    def get_user_id(self):
        if self.user_cache:
            return self.user_cache.id
        return None

    def get_user(self):
        return self.user_cache

class PasswordChangeForm(DjangoPasswordChangeForm):
    def save(self):
        self.user.parq_user.password = self.cleaned_data['new_password1']
        self.user.parq_user.save()
        return super(PasswordChangeForm,self).save()
        
