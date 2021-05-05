import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordView.module.scss';

import { wordType } from './wordTypes';
import { EditableTranscription, EditableSemanticBlock, NULL_SEMANTIC_BLOCK } from './SubWordParts';
import toRoman from '../../../../utils/toRomanNumbers';
import { isEditingSelector } from '../../../../store/selectors';

const SEM_BLOCKS_PATH_SEGMENT = 'semBlocks';
const TRANSCRIPTION_PATH_SEGMENT = 'transcription';

const WordView = props => {
    const { index, word } = props;
    const isEditing = useSelector(isEditingSelector);

    const path = [`${index}`];

    const nullSemBlock = isEditing ? [NULL_SEMANTIC_BLOCK] : [];
    const semBlocks = word.semBlocks && (word.semBlocks.concat(nullSemBlock))
        .map((sb, i) => (
            <EditableSemanticBlock 
                path={[...path, SEM_BLOCKS_PATH_SEGMENT, `${i}`]} 
                key={sb.id} 
                semBlock={sb} 
                index={i}
            />
        ));

    const wrapperClasses = [];
    if (isEditing)
        wrapperClasses.push(classes.Editing);

    return (
        <div className={wrapperClasses.join(' ')}>
            <div className={classes.WordEnum}>{toRoman(index + 1)}</div>
            <EditableTranscription path={[...path, TRANSCRIPTION_PATH_SEGMENT]}>
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
