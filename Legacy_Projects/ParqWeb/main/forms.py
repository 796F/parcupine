from django import forms

class SearchParqForm(forms.Form):
    find_a_spot_near = forms.CharField(widget=forms.TextInput({"placeholder": "Find a spot near..."}))
