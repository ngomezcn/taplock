from django.contrib.auth.models import User
from django.shortcuts import render

# Create your views here.
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.authentication import SessionAuthentication, BasicAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.response import Response
from rest_framework import status
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.decorators import api_view
from .models import UserProfile
from django.contrib.auth import authenticate
from core.serializers import UserSerializer

from django.db import IntegrityError


class signUp(APIView):
    def post(self, request, *args, **kwargs):
        serializer = UserSerializer(data=request.data)
        
        serializer.is_valid(raise_exception=True)

        if serializer.is_valid():
            try:
                UserProfile.objects.create_user(                
                serializer.validated_data['email'],
                serializer.validated_data['name'],
                serializer.validated_data['phone'],
                serializer.validated_data['password'],
                )
                
                # Generate token and login user after SingUp
                user = authenticate(username=serializer.validated_data['email'], password=serializer.validated_data['password'])
                token, created = Token.objects.get_or_create(user=user)
                
                return Response({               
                    'token': token.key,
                    'user_id': user.pk,
                    'email': user.email,
                    'active': user.is_active,
                    'name': user.name,    
                    'password': user.password,                    
                
                })
                return Response(serializer.validated_data, status=status.HTTP_201_CREATED)       
                
         
            except IntegrityError as e: 
                print(e.args)

                if 'UNIQUE constraint failed: core_userprofile.email' in e.args:
                    return Response({
                        'exception': "Este usuario ya existe.",                        
                    })
                else:
                    return Response({
                        'exception': "Ha ocurrido un error indefinido.",                        
                    })                    
        else:
            return Response(serializer._errors, status=status.HTTP_400_BAD_REQUEST)



class signIn(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        
        serializer = UserSerializer(data=request.data)        
        serializer.is_valid(raise_exception=True)
        
        user = authenticate(username=serializer.validated_data['email'], password=serializer.validated_data['password'])
        
        if user is not None:           
        
            token, created = Token.objects.get_or_create(user=user)
                
            return Response({
                'token': token.key,
                'user_id': user.pk,
                'email': user.email,
                'active': user.is_active,
                'name': user.name,
            })
        else:
            return Response({
                        'exception': "El email o la contraseña no son correctos.",                        
                    })  
            

class signOff(APIView):
    def get(self, request, format=None):

        try:
            content = {'logout_user': str(request.user)}
            request.user.auth_token.delete()    
            return Response(content, status=status.HTTP_200_OK)    
            
        except AttributeError as e: 
                print(e.args)
                if "'AnonymousUser' object has no attribute 'auth_token'" in e.args:
                    return Response({
                        'exception': "El usuario no ha iniciado sesión."                 
                    }, status=status.HTTP_400_BAD_REQUEST)
                else:
                    return Response({
                        'exception': "Ha ocurrido un error indefinido.",                        
                    }, status=status.HTTP_400_BAD_REQUEST)
            
             
class HelloView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        content = {'message': 'Hello, World!'}
        return Response(content)
      
      



