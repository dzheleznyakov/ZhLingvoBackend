import PropTypes from 'prop-types';

import * as inputTypes from './inputTypes';
import { refType } from '../../../static/types/generalTypes';

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
    defaultValue: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
    forwardRef: refType,
    values: PropTypes.arrayOf(PropTypes.string),
    disabled: PropTypes.bool,
    autocomplete: PropTypes.bool,
    groupKey: PropTypes.string,
    validation: PropTypes.arrayOf(validationType),
    listeners: PropTypes.objectOf(PropTypes.func),
});
