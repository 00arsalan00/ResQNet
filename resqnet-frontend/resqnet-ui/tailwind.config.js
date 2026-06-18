/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        amaranth: {
          DEFAULT: '#e63746',
          dark: '#c52d3a',
          light: '#ed6672',
        },
        peppermint: {
          DEFAULT: '#f2faef',
          dark: '#e0f1da',
        },
        aqua: {
          island: '#a7dadc',
        },
        wedgewood: {
          DEFAULT: '#447a9c',
          dark: '#38637f',
        },
        cello: {
          DEFAULT: '#1d3658',
          dark: '#152842',
        },
      },
      animation: {
        'spin-slow': 'spin 60s linear infinite',
      }
    },
  },
  plugins: [],
}
