import PropTypes from 'prop-types';

export const translationType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    value: PropTypes.string.isRequired,
    elaboration: PropTypes.string,
});

export const exampleType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    remark: PropTypes.string,
    expression: PropTypes.string.isRequired,
    explanation: PropTypes.string.isRequired,
});

export const meaningType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    remark: PropTypes.string,
    translations: PropTypes.arrayOf(translationType),
    examples: PropTypes.arrayOf(exampleType),
});

export const semBlockType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    pos: PropTypes.string.isRequired,
    gender: PropTypes.string,
    meanings: PropTypes.arrayOf(meaningType),
});

export const wordType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    mainForm: PropTypes.string.isRequired,
    transcription: PropTypes.string,
    semBlocks: PropTypes.arrayOf(semBlockType),
});
