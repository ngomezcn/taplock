from datetime import date
from django.contrib.auth.models import User
from django.db.utils import Error, IntegrityError
from django.shortcuts import render
import argparse
import datetime

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
from django.utils import timezone
from .models import emailVerification
import hashlib

import hashlib

from django.core.mail import send_mail

from django.core import mail
from django.template.loader import render_to_string
from django.utils.html import strip_tags
from .models import emailVerification, UserProfile
from django.core.exceptions import FieldDoesNotExist, ObjectDoesNotExist


def activateAccount(request):
    key = request.GET.get('key')
    print("TOKEN", key)
    
    try:
        verification = emailVerification.objects.get(token=key)
        
        user = UserProfile.objects.get(email=verification.email)
       
        user.email_verification = True
        user.save()
        
        verification.delete()

        return render(request, "email_success.html")
        
    except ObjectDoesNotExist as e: # If user doesn't exist.
        return render(request, "error.html")
        
        if 'emailVerification matching query does not exist.' == e.args:
            print("HHHHHH",e.args)      
            return render(request, "error.html")
            
    # http://172.10.10.10:8000/test/?key=HOLA

class echoMail(APIView):
  
    def post(self, request, *args, **kwargs):
        # send and email

        subject = 'Subject'
        html_message = render_to_string('mail_template.html', {'context': 'values'})
        plain_message = strip_tags(html_message)
        from_email = 'From <from@example.com>'
        to = 'naimgomezcn@gmail.com'
        mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)
        
        #send_mail(subject, html_message,from_email,[to])
        
        return Response("HOLA")

from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from .error_manager import codes

value = "foo.bar@baz.qux"


import random
''' Sign Up API View'''
class signUp(APIView):
    def post(self, request, *args, **kwargs):
        serializer = UserSerializer(data=request.data)
        
        serializer.is_valid(raise_exception=True)
                
        email    = serializer.validated_data['email']
        name     = serializer.validated_data['name']
        phone    = serializer.validated_data['phone']
        password = serializer.validated_data['password']

        try:
            validate_email(email)
        except ValidationError as e:
            return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)
        else:
            print("good email")        
        
            if serializer.is_valid():
                try:
                    
                    UserProfile.objects.create_user(  
                    email    = email, 
                    name     = name, 
                    phone    = phone,
                    password = password)                
                    
                    random = str(datetime.datetime.utcnow())+serializer.validated_data['name']+serializer.validated_data['phone']+serializer.validated_data['email']
                    hash_object = hashlib.sha1(random.encode("utf-8"))
                    token_validation = hash_object.hexdigest()

                    
                    
                    
                    emailVerification.objects.create(email=serializer.validated_data['email'], token=token_validation)
                    send_mail(
                            "TapLock - Enlace de activación", 
                            "THIS IS YOUR LINK: http://172.10.10.10:8000/test/?key="+ token_validation,
                            "castelldeneim@gmail.com",
                            [email])
                            
            
                    user = authenticate(username=serializer.validated_data['email'], password=password)
                    token, created = Token.objects.get_or_create(user=user)
                    
                    return Response({               
                        'token': token.key,
                        'email': email,
                        'name': name,    
                        'password': password,                   
                    })
                    
            
                except IntegrityError as e: 
                    print("ERRROR", e.args)

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
        
        print(request)
        print(request.data)

        try:
            if user is not None:           
                if user.email_verification == False:
                    return Response(
                        codes.signIn.pending_email_verification(status=status.HTTP_400_BAD_REQUEST), status=status.HTTP_400_BAD_REQUEST)      

                else:                    
                    token, created = Token.objects.get_or_create(user=user)
                        
                    return Response({
                        'token': token.key,
                        'user_id': user.pk,
                        'email': user.email,
                        'active': user.is_active,
                        'name': user.name,
                        'email_verification': user.email_verification,
                    })
            else:
                return Response(codes.signIn.wrong_credentials())      


        except Exception as e:            
            return Response(codes.signIn.undefined_error(e.args))      

            
            


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
      
      



