import datetime
from django.utils import timezone

# Rest Framework 
from rest_framework import status

'''
200 - OK
400 - Solicitud incorrecta (error del cliente): un json con error \ más detalles deben regresar al cliente.
401 - No autorizado
500 - Error interno del servidor: un json con un error debe regresar al cliente solo cuando no hay riesgo de seguridad al hacerlo.
'''
# If ALL fails it will save us ;)
INTERNAL_SERVER_ERROR = "internal_error(exception=None) -> Failed  "

class codes:
    """
    A class used to manage all error responses from the Api Rest, 
    this class is maked to be called from views.py also there is no problem is we use this on another context.
    
    TODO: Make a log system to save all data on the disk
    
    """

    def serializer_error(exception, status=status.HTTP_500_INTERNAL_SERVER_ERROR):
            try:
                return {
                        "status" : str(status),
                        "code" : "internal-2000",
                        "timestamp":str(datetime.datetime.utcnow()),
                        "message" : "Error de serializador",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
  
    class server:   
        """
        A class used to manage internal server errors.
        
        ...

        Methods
        -------
        internalError(exception=e.args) : dict
            If server have and undefined internal error we call this,             
            Returns a response in json format with useful information.  
        """                     
        
        
        def internal_error(exception=None, status=status.HTTP_500_INTERNAL_SERVER_ERROR):    
            try:        
                context = {
                        "status" : str(status),
                        "code" : "internal-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error interno desconocido.",
                        "exception:": str(exception)}
                
                return context
            except Exception as e:
                return {INTERNAL_SERVER_ERROR}
              
            
    class signIn:      
        """
        A class used to manage sign in user errors.
        
        ...

        Methods
        -------
        undefinedError(exception=e.args) : dict
            If we have and undefined error while login we call this,
            Returns a response in json format with useful information.            
        wrongCredentials(exception=e.args) : dict
            Returns a response in json format with useful information.    
        """  
        
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "signIn-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def wrong_credentials(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {    
                        "status" : str(status),
                        "code" : "signIn-2002",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El usuario y la contraseña no coinciden.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                codes.server.internal_error(e.args)
                return -1
            
        def pending_email_verification(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signIn-2003",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Debe activar su cuenta mediante el enlace de email",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)

    class signUp:
        
        
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "signUp-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def bad_email_format(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signUp-2003",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email introducido no tiene un formato correcto.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def user_already_exist(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signUp-2003",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Este usuario ya existe.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
        
            
        



