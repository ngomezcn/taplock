import re
from rest_framework import serializers
from .models import UserProfile

class UserSerializer(serializers.ModelSerializer):
    name=serializers.CharField(max_length=60)
    phone=serializers.CharField(max_length=60)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
        
    class Meta:
        model=UserProfile
        fields='__all__'

class SignUpSerializer(serializers.ModelSerializer):
    name=serializers.CharField(max_length=60)
    phone=serializers.CharField(max_length=60)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
    terms_of_service = serializers.BooleanField()
        
    class Meta:
        model=UserProfile
        fields='__all__'

class SignInSerializer(serializers.ModelSerializer):
    name=serializers.CharField(required=False)
    phone=serializers.CharField(required=False)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
        
    class Meta:
        model=UserProfile
        fields='__all__'
        
        
class ClaimiTapSerializer(serializers.ModelSerializer):
    name=serializers.CharField(max_length=60)
    address=serializers.CharField(max_length=60)
    phone=serializers.CharField(required=False)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
    activation_key = serializers.CharField(max_length=7)

        
    class Meta:
        model=UserProfile
        fields='__all__'
    
class testSerializer(serializers.ModelSerializer):   
    name=serializers.CharField(required=False)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
        
    class Meta:
        model=UserProfile
        fields='__all__'
    



        
    
class CustomSerializer(serializers.ModelSerializer):   
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
    newPassword = serializers.CharField(max_length=60)
    name = serializers.CharField(required=False)
    
    class Meta:
        model=UserProfile
        fields='__all__'


class ResendEmailVerificationSerializer(serializers.ModelSerializer):   
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(required=False)
    name = serializers.CharField(required=False)
    
    class Meta:
        model=UserProfile
        fields='__all__'
        

class RequestRecoverPasswordSerializer(serializers.ModelSerializer):   
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(required=False)
    name = serializers.CharField(required=False)
    
    class Meta:
        model=UserProfile
        fields='__all__'
        
        
class RecoverPasswordSerializer(serializers.ModelSerializer):   
    email = serializers.CharField(max_length=60)
    code = serializers.CharField(max_length=60)
    newPassword = serializers.CharField(max_length=60)

    password = serializers.CharField(required=False)
    name = serializers.CharField(required=False)
    
    class Meta:
        model=UserProfile
        fields='__all__'
        

