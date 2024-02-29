
from django.contrib import admin
from django.urls import path, include

from woocommerceapi.fetchorders import callll, get_csrf_token


urlpatterns = [
   path('hello', callll),
   path('get-csrf-token/', get_csrf_token, name='get_csrf_token'),
]
