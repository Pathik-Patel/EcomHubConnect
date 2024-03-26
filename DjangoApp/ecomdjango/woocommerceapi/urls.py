
from django.contrib import admin
from django.urls import path, include

from woocommerceapi.fetchorders import callll


urlpatterns = [
   path('hello', callll),
]
