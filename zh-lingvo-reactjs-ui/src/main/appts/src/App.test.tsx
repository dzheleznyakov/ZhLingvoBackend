import { render } from '@testing-library/react';
import { describe, test } from 'vitest';

import App from './App';

describe("<App> component", () => {
    test("is rendered on start", () => {
        // Act
        render(<App />);
        
    });
});
