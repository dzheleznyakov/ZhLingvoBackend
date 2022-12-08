import PropTypes from 'prop-types';

export const quizRecordTranslationType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    value: PropTypes.string.isRequired,
    elaboration: PropTypes.string,
});

export const quizRecordExampleType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    expression: PropTypes.string.isRequired,
    explanation: PropTypes.string.isRequired,
    remark: PropTypes.string,
});
