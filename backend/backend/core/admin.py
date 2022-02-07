from django.contrib import admin
from django import forms
from .models import *
# Register your models here.


#admin.site.register(UserProfile, ArduinoKey )

@admin.register(UserProfile)
class UserProfileRegister(admin.ModelAdmin):
    list_display = ("email", "phone", "name")
    pass

@admin.register(EmailVerification)
class EmailVerificationRegister(admin.ModelAdmin):
    list_display = ("email", "valid", "creation")
    pass

@admin.register(iTap)
class iTap(admin.ModelAdmin):
    list_display = ("activation_key","owner", "name", "address", "is_active")
    readonly_fields=("activation_key","seed")
    pass


