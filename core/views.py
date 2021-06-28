
# Django
from django.core.exceptions import *
from django.core.mail import send_mail
from django.core import mail
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.db.utils import Error, IntegrityError
from django.utils.html import strip_tags
from django.utils.crypto import get_random_string
from django.contrib.auth import authenticate
from django.shortcuts import render
from django.template.loader import render_to_string

# Django Rest Framework
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.views import APIView
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.response import Response
from rest_framework.response import Response
from rest_framework.response import Response
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.authentication import SessionAuthentication, BasicAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework.decorators import api_view

# Python Modules
import datetime
import hashlib

# Custom Modules
from .models import EmailVerification, UserProfile, UserProfile
from .serializers import UserSerializer
from .error_manager import codes


def activateAccount(request):
    key = request.GET.get('key')
    print("TOKEN", key)
    
    try:
        verification = EmailVerification.objects.get(token=key)
        
        user = UserProfile.objects.get(email=verification.email)
       
        user.email_verification = True
        user.save()
        
        verification.delete()

        return render(request, "email_success.html")
        
    except ObjectDoesNotExist as e: # If user doesn't exist.
        return render(request, "error.html")
        
        if 'EmailVerification matching query does not exist.' == e.args:
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



''' Sign Up API View'''
class signUp(APIView):
    def post(self, request, *args, **kwargs):
        serializer = UserSerializer(data=request.data)
        
        serializer.is_valid(raise_exception=True)        
            
        if serializer.is_valid():
        
            email    = serializer.validated_data['email']
            name     = serializer.validated_data['name']
            phone    = serializer.validated_data['phone']
            password = serializer.validated_data['password']

            try:       
                
                
                # Email validation
                try:
                    validate_email(email)
                except ValidationError as e:
                    return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)
                      
                # User creation validation                              
                try:
                    
                    UserProfile.objects.create_user(  
                    email    = email, 
                    name     = name, 
                    phone    = phone,
                    password = password
                    )                                  
                except Exception as e:
                    return Response(codes.signUp.user_already_exist(e.args), status.HTTP_400_BAD_REQUEST)
                    
                    
                # Send email verification
                try:                
                    
                    # IDEA: If a the random string has not enough entropy we can use a hash.
                    # hash_input = str(datetime.datetime.utcnow()) + name + phone + email + password
                    # hash_input.encode("utf-8")                        
                    # hash_token = hashlib.sha1(hash_input).hexdigest()
                    # email_token = hash_token
                
                    email_token = get_random_string(length=50, allowed_chars='1234567890-QWERTYUIOPASDFGHJKLZXCVBNM')
                    
                    EmailVerification.objects.create(
                    email=email, 
                    token=email_token
                    )
            
                    link = "http://172.10.10.10:8000/test/?key=" + email_token                        
                    context = {
                        'link':  link,
                        'name':  name,
                        'email': email
                    }
                    
                    subject = 'TapLock Admin - Activación'
                    html_message = render_to_string('mail_template.html', context)
                    plain_message = strip_tags(html_message)
                    from_email = 'cliente@taplock.es'
                    to = email
                    mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)
                                                
                    #user = authenticate(username=serializer.validated_data['email'], password=password)
                    #token, created = Token.objects.get_or_create(user=user)
                    
                    return Response({               
                        'message': "Se ha enviado el correo de activación",
                        'email': email,
                        'name':  name,    
                    })  
                except:
                    #TODO: Improve this
                    return("EMAIL FAILS")
                    
                                      
            except IntegrityError as e: 
                return(codes.signUp.undefined_error(e.args), status.HTTP_400_BAD_REQUEST)                    
                                              

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
      
      



