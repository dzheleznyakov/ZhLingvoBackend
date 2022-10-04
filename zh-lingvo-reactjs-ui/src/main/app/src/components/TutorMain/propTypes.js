import PropTypes from 'prop-types';

export const quizType = PropTypes.shape({
    id: PropTypes.number,
    name: PropTypes.string,
    targetLanguage: PropTypes.shape({
        id: PropTypes.number,
        name: PropTypes.string,
        code: PropTypes.string,
    }).isRequired,
});
