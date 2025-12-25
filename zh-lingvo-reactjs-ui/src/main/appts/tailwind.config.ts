import type { Config } from "tailwindcss";

export default {
  content: ["./index.html", "./src/**/*.{ts,tsx,js,jsx}"],

  theme: {
    container: {
      center: true,

      padding: {
        DEFAULT: "1rem",
        sm: "1.25rem",
        md: "1.5rem",
        lg: "2rm",
      },

      screens: {
        lg: "1024px",
        xl: "1200px",
      },
    },

    extend: {
      colors: {
        brand: {
          // main CTAs, primary buttons, active navigation items, key highlights
          primary: "#2f5d8a",

          // subtle backgrounds, hover states, accents, badges, charts, Not for body text (low contrast)
          primarySoft: "#a8d8e0",

          // secondary CTAs, destructive or attention-grabbing actions, emphasis where primary would be too strong
          secondary: "#e76f51",

          // hover states, secondary backgrounds, decorative emphasis, Not for body text
          secondarySoft: "#f4a261",

          // inline links, navigation links, interactive text elements
          link: "#1d6fd6",
        },

        bg: {
          // page background, app shell
          canvas: "#f9fafb",

          // sidebars, alternating sections, subtle contrast areas
          light: "#eceff1",
        },

        surface: {
          // cards, modals, dropdowns
          1: "#ffffff",

          //  nested cards, panels inside pages, grouped content areas
          2: "#f3f4f6",
        },

        text: {
          // body text, headings, default readable content
          primary: "#1f2933",

          // labels, metadata, less important content
          secondary: "#4b5563",

          // placeholders, hints, disabled or de-emphasised text
          muted: "#6b7280",

          // text on dark backgrounds, buttons using brand.primary or state colours
          inverse: "#ffffff",
        },

        border: {
          // card outlines, inputs, separators
          DEFAULT: "#d1d5db",

          // dividers, very low-emphasis separation
          subtle: "#e5e7eb",
        },

        state: {
          // Success state: confirmations, success messages, positive status indicators
          success: "#2e7d32",

          // Warning state: non-blocking alerts, cautionary messages
          warning: "#ed6c02",

          // Error state: validation errors, destructive actions, failure states
          error: "#d32f2f",

          // Info state: informational banners, neutral notifications
          info: "#0288d1",
        },
      },
    },
  },
} satisfies Config;