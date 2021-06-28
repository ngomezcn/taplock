from django.contrib import admin
from django import forms
from .models import UserProfile, iTapActivation, iTap, EmailVerification
# Register your models here.


#admin.site.register(UserProfile, ArduinoKey )

@admin.register(UserProfile)
class UserProfileRegister(admin.ModelAdmin):
    list_display = ("email", "name", "phone")
    pass

@admin.register(EmailVerification)
class EmailVerificationRegister(admin.ModelAdmin):
    list_display = ("email", "valid", "creation")
    pass


#myModels = [UserProfile, iTapActivation, iTap, EmailVerification]  # iterable list
#admin.site.register(myModels)
