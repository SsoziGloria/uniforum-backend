import defaultTheme from 'tailwindcss/defaultTheme';
import forms from '@tailwindcss/forms';


/** @type {import('tailwindcss').Config} */

export default {

    content: [
        './vendor/laravel/framework/src/Illuminate/Pagination/resources/views/*.blade.php',
        './storage/framework/views/*.php',
        './resources/**/*.blade.php',
        './resources/**/*.js',
        './resources/**/*.vue',
    ],


    theme: {

        extend: {

            colors: {

                primary: '#2563eb',

                background: '#ffffff',

                foreground: '#111827',

                muted: '#f3f4f6',

                border: '#e5e7eb',

                'input-background': '#ffffff',

            },


            fontFamily: {

                sans: [
                    'Inter',
                    ...defaultTheme.fontFamily.sans,
                ],

            },

        },

    },


    plugins: [

        forms,

    ],

};