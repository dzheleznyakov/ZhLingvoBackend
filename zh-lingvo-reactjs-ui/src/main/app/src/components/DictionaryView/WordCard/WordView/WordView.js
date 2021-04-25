import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordView.module.scss';

import { wordType } from './wordTypes';
import { EditableTranscription } from './SubWordParts';
import SemanticBlock from './SubWordParts/SemanticBlock';
import toRoman from '../../../../utils/toRomanNumbers';
import { isEditingSelector } from '../../../../store/selectors';

const WordView = props => {
    const { index, word } = props;
    const isEditing = useSelector(isEditingSelector);

    const path = [`${index}`];

    const semBlocks = word.semBlocks && word.semBlocks
        .map((sb, i) => <SemanticBlock path={[...path, 'semBlocks', `${i}`]} key={sb.id} semBlock={sb} index={i} />);

    const wrapperClasses = [];
    if (isEditing)
        wrapperClasses.push(classes.Editing);

    return (
        <div className={wrapperClasses.join(' ')}>
            <div className={classes.WordEnum}>{toRoman(index + 1)}</div>
            <EditableTranscription path={[...path, 'transcription']}>
                {word.transcription}
            </EditableTranscription>
            <div>
                {semBlocks}
            </div>
        </div>
    );
};

WordView.propTypes = {
    index: PropTypes.number,
    word: wordType.isRequired,
};

WordView.defaultProps = {};

export default WordView;
