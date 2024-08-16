# Mobile UI SDKs for Asgardeo - Documentation Site

Documentation site for the Mobile UI SDKs for Asgardeo. This site was developed using [VitePress](https://vitepress.dev/) which is a Vue-powered static site generator and a spiritual successor to VuePress, built on top of Vite. 

## Prerequisite

Setup development environment for the Documentation Site for Mobile UI SDKs for Asgardeo.

- Node.js version 18 or higher.
- Text Editor with Markdown syntax support.
  - VSCode is recommended.

## File Structure

```
.
├─ website
│  ├─ .vitepress
│  │  ├─ theme
│  │  │  └─ index.ts
│  │  │  └─ style.css
│  │  └─ config.mts
│  ├─ android
│  │  └─ <relevant .md files for Asgardeo-Android SDK>
│  ├─ public
│  │  └─ <image assets>
│  ├─ index.md
└─ package.json
```

### website/.vitepress/theme/index.ts

This file contains the configuration for the VitePress theme used in the documentation site.

### website/.vitepress/theme/style.css

This file contains the CSS styles for the documentation site. For more information on how to customize the theme, see the [VitePress documentation](https://vitepress.dev/guide/extending-default-theme).

### website/.vitepress/config.mts

This file contains the main structure of the documentation site. For more information on how to configure the site, see the [VitePress documentation](https://vitepress.dev/reference/site-config).

### website/android

This directory contains the relevant .md files for the Asgardeo-Android SDK.

### website/public

This directory contains the image assets used in the documentation site.

### website/index.md

This file contains the content for the home page of the documentation site. For more information on how to customize the home page, see the [VitePress documentation](https://vitepress.dev/reference/default-theme-home-page#home-page).

## Build & Run

1. Clone the repository.
2. Navigate to the `docs` directory.
3. Run the following command to install the dependencies:

```bash
npm install
```
4. Run the following command to start the development server:

```bash
npm run docs:dev
```
5. Open the following URL in your browser:

```
http://localhost:5173/mobile-ui-sdks/
```

## Other Information

- For more information on how to improve the documentation, see the [VitePress documentation](https://vitepress.dev/).