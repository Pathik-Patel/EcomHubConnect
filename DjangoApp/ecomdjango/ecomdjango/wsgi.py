"""
WSGI config for ecomdjango project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/5.0/howto/deployment/wsgi/
"""

import os

from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor, ConsoleSpanExporter
from opentelemetry.instrumentation.django import DjangoInstrumentor

# ✅ Set the Django settings module
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'ecomdjango.settings')

# ✅ Set up OpenTelemetry BEFORE Django starts
trace.set_tracer_provider(TracerProvider())
trace.get_tracer_provider().add_span_processor(BatchSpanProcessor(ConsoleSpanExporter()))
DjangoInstrumentor().instrument()

# ✅ Now get the WSGI application
from django.core.wsgi import get_wsgi_application
application = get_wsgi_application()

