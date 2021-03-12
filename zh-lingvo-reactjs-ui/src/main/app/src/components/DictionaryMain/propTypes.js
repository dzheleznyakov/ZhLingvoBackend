import PropTypes from 'prop-types';

export const dictionaryType = PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
    language: PropTypes.shape({
        id: PropTypes.number,
        name: PropTypes.string,
        code: PropTypes.string,
    }).isRequired,
});
