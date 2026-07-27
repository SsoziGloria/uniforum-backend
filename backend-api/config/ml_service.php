<?php

return [
    'base_url' => env('ML_SERVICE_URL', 'http://localhost:8000'),
    'api_key'  => env('ML_SERVICE_API_KEY'),
    'timeout'  => env('ML_SERVICE_TIMEOUT', 5), // seconds
];
