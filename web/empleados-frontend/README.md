# Empleados Frontend

CRUD web application for managing employees, built with vanilla HTML/CSS/JavaScript.

## Features

- Create, Read, Update, Delete employees
- Real-time API status indicator
- Responsive design
- Form validation
- Toast notifications

## Tech Stack

- HTML5
- CSS3 (Custom Properties, Flexbox, Grid)
- Vanilla JavaScript (ES6+)
- Font Awesome icons
- Inter font (Google Fonts)

## Project Structure

```
empleados-frontend/
├── index.html          # Main HTML file
├── styles.css          # Stylesheet
├── app.js              # JavaScript logic
├── nginx.conf          # Nginx configuration
├── package.json        # Test dependencies
├── cypress.config.js   # Cypress configuration
└── cypress/
    ├── e2e/
    │   └── employees.cy.js  # E2E tests
    └── support/
        ├── commands.js       # Custom commands
        └── e2e.js           # Support file
```

## Local Development

1. Start the API server:
   ```bash
   cd ../../api/Empleados
   ./gradlew bootRun
   ```

2. Open `index.html` in your browser or use a local server:
   ```bash
   npx serve .
   ```

## Running Tests

```bash
# Install dependencies
npm install

# Run Cypress tests
npm test

# Open Cypress Test Runner
npm run cy:open
```

## Deployment

This frontend is designed to be deployed on **GitHub Pages**.

The `API_URL` in `app.js` automatically detects the environment:
- **Local**: `http://localhost:8081`
- **Production**: `http://ec2-3-83-67-172.compute-1.amazonaws.com:8081`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /employees | List all employees |
| POST | /employees | Create employee |
| GET | /employees/{id} | Get employee by ID |
| PUT | /employees/{id} | Update employee |
| DELETE | /employees/{id} | Delete employee |

## Author

Juan Francisco Builes Montoya - [juanfranciscobumo@gmail.com](mailto:juanfranciscobumo@gmail.com)
