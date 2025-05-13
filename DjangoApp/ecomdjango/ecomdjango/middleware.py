# middleware.py
import logging

logger = logging.getLogger(__name__)

class TraceLogMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        traceparent = request.headers.get('traceparent')
        if traceparent:
            # Example traceparent format: "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01"
            parts = traceparent.split('-')
            if len(parts) == 4:
                trace_id = parts[1]
                span_id = parts[2]
                logger.info(f"Received request - traceId={trace_id}, spanId={span_id}")
            else:
                logger.warning(f"Invalid traceparent format: {traceparent}")
        else:
            logger.info("No traceparent header found")

        return self.get_response(request)
