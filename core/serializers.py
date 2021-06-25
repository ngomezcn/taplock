from rest_framework import serializers
from .models import UserProfile

class UserSerializer(serializers.ModelSerializer):
    name=serializers.CharField(required=False)
    email = serializers.CharField(max_length=60)
    password = serializers.CharField(max_length=60)
    
    class Meta:
        model=UserProfile
        fields='__all__'
    