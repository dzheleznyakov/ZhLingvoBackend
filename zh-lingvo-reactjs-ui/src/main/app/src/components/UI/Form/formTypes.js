import PropTypes from 'prop-types';

import * as inputTypes from './inputTypes';

const inputTypesArray = Object.keys(inputTypes).map(key => inputTypes[key]);

export const groupType = PropTypes.shape({
    key: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
});

export const validationType = PropTypes.shape({
    validate: PropTypes.func.isRequired,
    failureMessage: PropTypes.string.isRequired,
});

export const fieldType = PropTypes.shape({
    key: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    type: PropTypes.oneOf(inputTypesArray).isRequired,
    defaultValue: PropTypes.string.isRequired,
    forwardRef: PropTypes.object.isRequired,
    values: PropTypes.arrayOf(PropTypes.string),
    disabled: PropTypes.bool,
    groupKey: PropTypes.string,
    validation: PropTypes.arrayOf(validationType),
});
